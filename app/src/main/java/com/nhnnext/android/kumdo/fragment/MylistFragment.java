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
import android.widget.ArrayAdapter;
import android.widget.GridView;

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
 * 로컬 갤러리에 저장된 이미지를 리스트로 보여주는 Fragment
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */
public class MylistFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MylistFragment";
    private Context mContext;
    private ImageAdapter mAdapter;

    private int mImageSize;

    private static final String SERVER_GET_MYLIST = "http://192.168.0.3:3000/mylist";
    public String[] mImageUrls;
    private String userEmail;

    private List<Writing> writings;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size);

        SharedPreferences data = getActivity().getSharedPreferences("userInfo", 0);
        userEmail = data.getString("userEmail", "");
        Log.d(TAG, "userEmail : " + data.getString("userEmail", ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylist_view, container, false);
        final GridView mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setOnItemClickListener(this);

        //TODO Refactoring JsonArray -> Gson : convert to Writng.class -> add List<Writing>
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                SERVER_GET_MYLIST + "?userEmail=" + userEmail,
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
                mGridView.setAdapter(mAdapter);

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
                                        Log.d(TAG, "numColumns : " + numColumns);
                                        Log.d(TAG, "width, height : " + columnWidth);
                                    }
                                }
                            }
                        }
                );
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
        private static final String TAG = "ImageAdapter";
        private final Context mContext;
        private GridView.LayoutParams mImageViewLayoutParams;
        private int mNumColumns = 0;
        private int mItemHeight = 0;
        private String[] imageUrls;
        private ImageLoader mImageLoader;

        public ImageAdapter(Context context, String[] param, ImageLoader imageLoader) {
            super(context, 0, param);
            this.mContext = context;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.imageUrls = param;
            mImageLoader = imageLoader;
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return imageUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.mylist_tile, null);
            }

            ViewHolder holder = (ViewHolder) v.getTag(R.id.id_holder);

            if (holder == null) {
                holder = new ViewHolder(v);
                v.setTag(R.id.id_holder, holder);
            }

            holder.image.setLayoutParams(mImageViewLayoutParams);

            if (holder.image.getLayoutParams().height != mItemHeight) {
                holder.image.setLayoutParams(mImageViewLayoutParams);
            }
            Log.d(TAG, mImageUrls[position]);
            holder.image.setImageUrl(mImageUrls[position], mImageLoader);
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
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
            notifyDataSetChanged();
        }

        private class ViewHolder {
            NetworkImageView image;

            public ViewHolder(View v) {
                image = (NetworkImageView) v.findViewById(R.id.best_row);
                v.setTag(this);
            }
        }
    }
}