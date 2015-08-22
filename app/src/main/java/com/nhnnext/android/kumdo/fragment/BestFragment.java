package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.nhnnext.android.kumdo.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 서버에서 저장된 데이터 중 최신 데이터(or 추천수가 가장 높은 데이터)를 화면에 뿌려주는 Fragmet
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */
public class BestFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "BestFragment";
    private static final String SERVER_GET_BEST = "http://192.168.0.3:3000/best";

    private ImageAdapter mAdapter;
    public String[] mImageUrls;

    private Context mContext;

    private List<Writing> writings;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.best_view, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.best_list);
        mListView.setOnItemClickListener(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, SERVER_GET_BEST,
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
                        mImageUrls[i] = writings.get(i).getImageUrl();
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException : " + e);
                    }
                }
                mAdapter = new ImageAdapter(getActivity(), mImageUrls,
                        VolleySingleton.getInstance(mContext).getImageLoader());
                mListView.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "VolleyError : " + volleyError);
            }
        });

        VolleySingleton.getInstance(mContext).addTodRequestQueue(jsonArrayRequest);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        intent.putExtra(DetailActivity.IMAGE_DATA_EXTRA, mImageUrls[position]);
        startActivity(intent);
    }

    private class ImageAdapter extends ArrayAdapter {
        private final Context mContext;
        private String[] mImageUrls;
        private ImageLoader mImageLoader;

        public ImageAdapter(Context context, String[] param, ImageLoader imageLoader) {
            super(context, 0, param);
            this.mContext = context;
            mImageUrls = param;
            mImageLoader = imageLoader;
        }

        @Override
        public int getCount() {
            return mImageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return mImageUrls[position];
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
                LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.best_row, null);
            }

            ViewHolder holder = (ViewHolder) v.getTag(R.id.id_holder);

            if (holder == null) {
                holder = new ViewHolder(v);
                v.setTag(R.id.id_holder, holder);
            }

            holder.image.setImageUrl(mImageUrls[position], mImageLoader);
            holder.text.setText(writing.getSentence());
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
