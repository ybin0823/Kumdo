package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.nhnnext.android.kumdo.DetailActivity;
import com.nhnnext.android.kumdo.R;
import com.nhnnext.android.kumdo.model.Writing;
import com.nhnnext.android.kumdo.util.RequestUrl;
import com.nhnnext.android.kumdo.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 서버에서 저장된 데이터 중 최신 데이터(or 추천수가 가장 높은 데이터)를 화면에 뿌려주는 Fragmet
 * Home화면(MenuActivity)에서 사용(`15.08.24 by jyb)
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */
public class BestFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "BestFragment";

    public String[] mImageUrls;
    protected Context mContext;

    protected List<Writing> writings;

    protected BaseAdapter mAdapter;
    protected ListView mListView;
    protected ProgressBar mProgressBar;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    public static BestFragment newInstance(int category) {
        BestFragment f = new BestFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("category", category);
        f.setArguments(args);

        return f;
    }

    public int getSelectedCategory() {
        if (getArguments() == null) {
            return -1;
        }
        return getArguments().getInt("category", -1);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.best_view, container, false);
        mListView = (ListView) view.findViewById(R.id.best_list);
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ImageAdapter(VolleySingleton.getInstance(mContext).getImageLoader());

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        requestData(mAdapter);
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData(mAdapter);
    }

    protected void requestData(final BaseAdapter imageAdapter) {
        // category num가 -1이면 전체 정보 가져오기
        // 그 외(0~3) 이면 해당되는 카테고리 정보만 가져온다
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                RequestUrl.GET_BEST_FROM + getSelectedCategory(),
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Gson gson = new Gson();
                writings = new ArrayList<Writing>();
                int size = jsonArray.length();
                mImageUrls = new String[size];
                for (int i = 0; i < size; i++) {
                    try {
                        writings.add(gson.fromJson(jsonArray.getString(i), Writing.class));
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException : " + e);
                    }
                }
                mListView.setAdapter(imageAdapter);

                mProgressBar.setVisibility(View.GONE);

                // Stop the refreshing indicator
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "VolleyError : " + volleyError);
            }
        });

        VolleySingleton.getInstance(mContext).addTodRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.WRITING_DATA_EXTRA, writings.get(position));
        startActivity(intent);
    }

    private class ImageAdapter extends BaseAdapter {
        private ImageLoader mImageLoader;

        public ImageAdapter(ImageLoader imageLoader) {
            mImageLoader = imageLoader;
        }

        @Override
        public int getCount() {
            return writings.size();
        }

        @Override
        public Object getItem(int position) {
            return writings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Writing writing = writings.get(position);
            if (v == null) {
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                v = inflater.inflate(R.layout.best_row, null);
            }

            ViewHolder holder = (ViewHolder) v.getTag(R.id.id_holder);

            if (holder == null) {
                holder = new ViewHolder(v);
                v.setTag(R.id.id_holder, holder);
            }

            holder.image.setImageUrl(writing.getImageUrl(), mImageLoader);
            if (writing.getSentence().length() > 100) {
                holder.text.setText(writing.getSentence().substring(0, 90) + "...");
            } else {
                holder.text.setText(writing.getSentence());
            }
            holder.words.setText(writing.getWords());

            return v;
        }

        private class ViewHolder {
            NetworkImageView image;
            TextView text;
            TextView words;

            public ViewHolder(View v) {
                image = (NetworkImageView) v.findViewById(R.id.writing_image);
                text = (TextView) v.findViewById(R.id.writing_text);
                words = (TextView) v.findViewById(R.id.writing_words);
                v.setTag(this);
            }
        }
    }
}
