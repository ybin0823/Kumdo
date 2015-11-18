package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
 * 로컬 갤러리에 저장된 이미지를 리스트로 보여주는 Fragment
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */
public class MylistFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MylistFragment";
    private Context mContext;

    private GridView mGridView;
    private ImageAdapter mAdapter;

    private int mImageSize;

    private String userEmail;

    public String[] mImageUrls;
    private List<Writing> writings;

    private ProgressBar mProgressBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size);

        SharedPreferences data = getActivity().getSharedPreferences("userInfo", 0);
        userEmail = data.getString("userEmail", "");
        Log.d(TAG, "userEmail : " + data.getString("userEmail", ""));

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.mylist_view, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setOnItemClickListener(this);

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
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData(mAdapter);


        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mAdapter.getNumColumn() == 0) {
                            final int numColumns = (int) Math.floor(
                                    mGridView.getWidth() / mImageSize);
                            if (numColumns > 0) {
                                int columnWidth = mGridView.getWidth() / numColumns;
                                mAdapter.setNumColumns(numColumns);
                                mAdapter.setItemHeight(columnWidth);
                            }
                        }
                    }
                }
        );
        mProgressBar.setVisibility(View.GONE);
    }

    protected void requestData(final BaseAdapter imageAdapter) {
        // category num가 -1이면 전체 정보 가져오기
        // 그 외(0~3) 이면 해당되는 카테고리 정보만 가져온다
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                RequestUrl.GET_MY_LIST + userEmail,
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
                mGridView.setAdapter(imageAdapter);

                mProgressBar.setVisibility(View.GONE);
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
        intent.putExtra(DetailActivity.WRITING_DATA_EXTRA, writings.get(position));
        startActivity(intent);
    }

    private class ImageAdapter extends BaseAdapter {
        private static final String TAG = "ImageAdapter";
        private RelativeLayout.LayoutParams mImageViewLayoutParams;
        private int mNumColumns = 0;
        private int mItemHeight = 0;
        private ImageLoader mImageLoader;

        public ImageAdapter(ImageLoader imageLoader) {
            mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
            ViewHolder holder;
            Writing writing = writings.get(position);

            if (v == null) {
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                v = inflater.inflate(R.layout.mylist_tile, parent, false);
                holder = new ViewHolder(v);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            if (holder.image.getLayoutParams().height != mItemHeight) {
                holder.image.setLayoutParams(mImageViewLayoutParams);
            }
            holder.image.setImageUrl(writing.getImageUrl(), mImageLoader);
            holder.words.setText(writing.getWords());
            return v;
        }

        public int getNumColumn() {
            return mNumColumns;
        }

        public void setNumColumns(int numColumns) {
            this.mNumColumns = numColumns;
        }

        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
            notifyDataSetChanged();
        }

        private class ViewHolder {
            NetworkImageView image;
            TextView words;

            public ViewHolder(View v) {
                image = (NetworkImageView) v.findViewById(R.id.mylist_image);
                words = (TextView) v.findViewById(R.id.mylist_word);
            }
        }
    }
}