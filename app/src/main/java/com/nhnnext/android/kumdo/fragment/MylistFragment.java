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
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nhnnext.android.kumdo.DetailActivity;
import com.nhnnext.android.kumdo.R;
import com.nhnnext.android.kumdo.util.BitmapWorkerTask;

/**
 * 로컬 갤러리에 저장된 이미지를 리스트로 보여주는 Fragment
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */
public class MylistFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MylistFragment";
    private ImageAdapter mAdapter;

    private int mImageSize;

    private static final String LOCAL_SERVER_IP = "192.168.1.105:3000";

    // local image server url for test
    public final static String[] imageUrls = {
            "http://" + LOCAL_SERVER_IP +"/uploads/1.jpg",
            "http://" + LOCAL_SERVER_IP +"/uploads/2.jpg",
            "http://" + LOCAL_SERVER_IP +"/uploads/3.jpg",
            "http://" + LOCAL_SERVER_IP +"/uploads/4.jpg",
            "http://" + LOCAL_SERVER_IP +"/uploads/5.jpg",
            "http://" + LOCAL_SERVER_IP +"/uploads/6.jpg",
            "http://" + LOCAL_SERVER_IP +"/uploads/7.jpg",
            "http://" + LOCAL_SERVER_IP +"/uploads/8.jpg"
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
        mAdapter = new ImageAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylist_view, container, false);
        final GridView mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(mAdapter.getNumColumn() == 0) {
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
        intent.putExtra(DetailActivity.IMAGE_DATA_EXTRA, position);
        startActivity(intent);
    }

    private class ImageAdapter extends BaseAdapter {
        private static final String TAG = "ImageAdapter";
        private final Context mContext;
        private GridView.LayoutParams mImageViewLayoutParams;
        private int mNumColumns = 0;
        private int mItemHeight = 0;

        public ImageAdapter(Context context) {
            this.mContext = context;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else {
                imageView = (ImageView) convertView;
            }

            // Check the height matches our calculated column width
            if (imageView.getLayoutParams().height != mItemHeight) {
                imageView.setLayoutParams(mImageViewLayoutParams);
            }
            loadBitmap(imageUrls[position], imageView);
            return imageView;
        }

        private void loadBitmap(String imageUrl, ImageView imageView) {
            //TODO task 확인

            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);

            // TODO Drawable 추가

            task.execute(imageUrl);
        }

        public int getNumColumn() {
            return mNumColumns;
        }

        public void setNumColumns(int numColumns) {
            this.mNumColumns = numColumns;
        }

        public void setItemHeight(int height) {
            Log.d(TAG, "setItemHeight");
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
            notifyDataSetChanged();
        }
    }
}
