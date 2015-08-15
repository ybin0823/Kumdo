package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ImageAdapter mAdapter;

    // local image server url for test
    public final static String[] imageUrls = {
            "http://192.168.2.109:3000/uploads/1.jpg",
            "http://192.168.2.109:3000/uploads/2.jpg",
            "http://192.168.2.109:3000/uploads/3.jpg",
            "http://192.168.2.109:3000/uploads/4.jpg",
            "http://192.168.2.109:3000/uploads/5.jpg",
            "http://192.168.2.109:3000/uploads/6.jpg",
            "http://192.168.2.109:3000/uploads/7.jpg",
            "http://192.168.2.109:3000/uploads/8.jpg"
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ImageAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylist_view, container, false);
        GridView gridview = (GridView) view.findViewById(R.id.grid_view);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(this);
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
        private final Context mContext;

        public ImageAdapter(Context context) {
            this.mContext = context;
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
            } else {
                imageView = (ImageView) convertView;
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
    }
}
