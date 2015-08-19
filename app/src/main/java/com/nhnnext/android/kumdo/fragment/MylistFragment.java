package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhnnext.android.kumdo.DetailActivity;
import com.nhnnext.android.kumdo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 로컬 갤러리에 저장된 이미지를 리스트로 보여주는 Fragment
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */
public class MylistFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MylistFragment";
    private ImageAdapter mAdapter;

    private Bitmap mPlaceHolderBitmap;

    private int mImageSize;

    private static final String SERVER_GET_MYLIST = "http://10.64.192.61:3000/mylist";
    public String[] mImageUrls;
    private String userEmail;

    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
        mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty_photo);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d(TAG, "maxMemory : " + maxMemory);

        // Use 1/8 of the available memory for this memory cache
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        SharedPreferences data = getActivity().getSharedPreferences("userInfo", 0);
        userEmail = data.getString("userEmail", "");
        Log.d(TAG, "userEmail : " + data.getString("userEmail", ""));
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylist_view, container, false);
        final GridView mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setOnItemClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(SERVER_GET_MYLIST + "?userEmail=" + userEmail);
                    conn = (HttpURLConnection) url.openConnection();

                    InputStream is = conn.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    line = br.readLine();
                    Gson gson = new Gson();
                    mImageUrls = gson.fromJson(line, String[].class);

                    mGridView.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new ImageAdapter(getActivity(), mImageUrls);
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
                    });
                } catch (IOException e) {
                    Log.e(TAG, "IOException :" + e);
                }
            }
        }).start();
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

        public ImageAdapter(Context context, String[] param) {
            super(context, 0, param);
            this.mContext = context;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.imageUrls = param;
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

        private void loadBitmap(String imageUrl, ImageView imageView) {
            Bitmap bitmap = getBitmapFromMemCache(imageUrl);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else if (cancelPotentialWork(imageUrl, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);

                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    task.execute(imageUrl);
                } else {
                    //TODO change to dialog
                    Toast.makeText(getActivity(), "No network connection available!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean cancelPotentialWork(String imageUrl, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            if (!bitmapData.equals(imageUrl)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    private class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTasReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTasReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTasReference.get();
        }
    }

    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private static final String TAG = "BitmapWorkerTask";
        private final WeakReference<ImageView> imageViewReference;
        public String data = "";

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        //TODO Volley로 변경
        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d(TAG, "doInBackground - starting work");

            data = params[0];

            try {
                HttpURLConnection conn = (HttpURLConnection )new URL(data).openConnection();
                InputStream is = conn.getInputStream();
                Bitmap bitmap = decodeSampledBitmapFromStream(is, 8);
                addBitmapToMemoryCache(data, bitmap);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    Log.d(TAG, "onPostExecute - setting bitmap");
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public Bitmap decodeSampledBitmapFromStream(InputStream is, int inSampleSize) {
            Log.d(TAG, "decodeSampledBitmapFromStream - resizing bitmap");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeStream(is, null, options);
        }
    }
}
