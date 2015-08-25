package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nhnnext.android.kumdo.DetailActivity;
import com.nhnnext.android.kumdo.R;
import com.nhnnext.android.kumdo.db.WritingOpenHelper;
import com.nhnnext.android.kumdo.model.Writing;
import com.nhnnext.android.kumdo.volley.VolleySingleton;

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

    private List<Writing> writings;
    private ProgressBar mProgressBar;

    WritingOpenHelper mDbHelper;
    SQLiteDatabase db;

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

        mDbHelper = new WritingOpenHelper(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.mylist_view, container, false);
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setOnItemClickListener(this);

        return view;
    }

    private List<Writing> readFromDb() {
        List<Writing> writings = new ArrayList<>();
        // Get the data repository in read mode
        db = mDbHelper.getReadableDatabase();

        String[] projection = {
                "name",
                "email",
                "sentence",
                "words",
                "imageUrl",
                "category",
                "date"
        };
        String selection = WritingOpenHelper.KEY_EMAIL + "= ?";
        String[] selectionArgs = { userEmail };

        // Table, Column, WHERE, ARGUMENTS, GROUPING, HAVING, SORTING
        Cursor cursor = db.query("writings", projection, selection, selectionArgs, null, null, null);

        // AddView into the TableLayout using return value
        while (cursor.moveToNext()) {
            Writing writing = new Writing();
            writing.setName(cursor.getString(0));
            writing.setEmail(cursor.getString(1));
            writing.setSentence(cursor.getString(2));
            writing.setWords(cursor.getString(3));
            writing.setImageUrl(cursor.getString(4));
            writing.setCategory(cursor.getInt(5));
            writing.setDate(cursor.getString(6));
            writings.add(writing);
        }
        cursor.close();
        db.close();

        return writings;
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
        writings = readFromDb();

        mAdapter = new ImageAdapter(getActivity(), writings,
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
                            }
                        }
                    }
                }
        );
        mProgressBar.setVisibility(View.GONE);
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

    private class ImageAdapter<T> extends ArrayAdapter {
        private static final String TAG = "ImageAdapter";
        private final Context mContext;
        private RelativeLayout.LayoutParams mImageViewLayoutParams;
        private int mNumColumns = 0;
        private int mItemHeight = 0;
        private List<T> params;
        private ImageLoader mImageLoader;

        public ImageAdapter(Context context, List<T> params, ImageLoader imageLoader) {
            super(context, 0, params);
            this.mContext = context;
            mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.params = params;
            mImageLoader = imageLoader;
        }

        @Override
        public int getCount() {
            return params.size();
        }

        @Override
        public Object getItem(int position) {
            return params.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            Writing writing = (Writing) params.get(position);

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.mylist_tile, parent, false);
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