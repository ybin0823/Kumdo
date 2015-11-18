package com.nhnnext.android.kumdo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 카테고리 별 리스트를 볼 때, 상단의 AppBar로 인해 이미지의 상단부분이 덮이는 문제가 있다.
 * 따라서, AppBar만큼의 Empty View를 넣어주는 ImageAdapter를
 * 구현하고 나머지는 BestFragment를 상속받아서 사용한다.(`15.08.24 by jyb)
 */
public class CategoryListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "CategoryListFragment";

    private List<Writing> writings;
    public String[] mImageUrls;

    private Context mContext;
    private ImageAdapter mImageAdpater;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.best_view, container, false);
        mListView = (ListView) view.findViewById(R.id.best_list);
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        category = getArguments().getInt("category");
    }

    @Override
    public void onStart() {
        super.onStart();
        mImageAdpater = new ImageAdapter(getActivity(),
                VolleySingleton.getInstance(getActivity()).getImageLoader());

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(mImageAdpater);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData(mImageAdpater);
    }

    protected void requestData(final BaseAdapter imageAdapter) {
        // category num가 -1이면 전체 정보 가져오기
        // 그 외(0~3) 이면 해당되는 카테고리 정보만 가져온다
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                RequestUrl.GET_BEST_FROM + category,
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.WRITING_DATA_EXTRA, writings.get(position - 1));
        startActivity(intent);
    }

    private class ImageAdapter extends BaseAdapter {
        private final Context mContext;
        private ImageLoader mImageLoader;
        private int mActionBarHeight = 0;

        public ImageAdapter(Context context, ImageLoader imageLoader) {
            mContext = context;
            mImageLoader = imageLoader;

            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true)) {
                mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }
        }

        @Override
        public int getCount() {
            return writings.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return writings.get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return position - 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Writing writing;

            // First check if this is the top row
            if (position < 1) {
                if (convertView == null) {
                    convertView = new View(mContext);
                }
                // Set empty view with height of ActionBar
                convertView.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));

                android.util.Log.d(TAG, "mActionBarHeight : " + mActionBarHeight);
                return convertView;
            }

            writing = writings.get(position - 1);
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
            holder.text.setText(writing.getSentence());
            holder.words.setText(writing.getWords());
            holder.name.setText(writing.getName());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

            // iOS앱의 경우 date가 millisecond가 소수점까지 저장되기 때문에
            // Android에서 dot(.)를 기준으로 split해준다
            holder.date.setText(sdf.format(new Date(Long.valueOf(writing.getDate().split("\\.")[0]))));

            return v;
        }

        private class ViewHolder {
            NetworkImageView image;
            TextView text;
            TextView words;
            TextView name;
            TextView date;

            public ViewHolder(View v) {
                image = (NetworkImageView) v.findViewById(R.id.writing_image);
                text = (TextView) v.findViewById(R.id.writing_text);
                words = (TextView) v.findViewById(R.id.writing_words);
                name = (TextView) v.findViewById(R.id.writing_writer);
                date = (TextView) v.findViewById(R.id.writing_date);
                v.setTag(this);
            }
        }
    }
}