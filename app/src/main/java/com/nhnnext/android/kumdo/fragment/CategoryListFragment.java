package com.nhnnext.android.kumdo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nhnnext.android.kumdo.DetailActivity;
import com.nhnnext.android.kumdo.R;
import com.nhnnext.android.kumdo.model.Writing;
import com.nhnnext.android.kumdo.volley.VolleySingleton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 카테고리 별 리스트를 볼 때, 상단의 AppBar로 인해 이미지의 상단부분이 덮이는 문제가 있다.
 * 따라서, AppBar만큼의 Empty View를 넣어주는 ImageAdapter를
 * 구현하고 나머지는 BestFragment를 상속받아서 사용한다.(`15.08.24 by jyb)
 */
public class CategoryListFragment extends BestFragment {
    private ImageAdapter mImageAdpater;

    private static final String TAG = "CategoryListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mImageAdpater = new ImageAdapter(getActivity(), writings,
                VolleySingleton.getInstance(getActivity()).getImageLoader());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.WRITING_DATA_EXTRA, writings.get(position - 1));
        startActivity(intent);
    }

    private class ImageAdapter extends ArrayAdapter {
        private final Context mContext;
        private List<Writing> mWritings;
        private ImageLoader mImageLoader;
        private int mActionBarHeight = 0;

        public ImageAdapter(Context context, List<Writing> param, ImageLoader imageLoader) {
            super(context, 0, param);
            this.mContext = context;
            mWritings = param;
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
                LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.best_row, null);
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
            holder.date.setText(sdf.format(new Date(Long.valueOf(writing.getDate()))));

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
