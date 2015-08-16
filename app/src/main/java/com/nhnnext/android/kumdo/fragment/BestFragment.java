package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhnnext.android.kumdo.DetailActivity;
import com.nhnnext.android.kumdo.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 서버에서 저장된 데이터 중 최신 데이터(or 추천수가 가장 높은 데이터)를 화면에 뿌려주는 Fragmet
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */
public class BestFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ImageAdapter mAdapter;
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
        mAdapter = new ImageAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.best_view, container, false);
        ListView mListView = (ListView) view.findViewById(R.id.best_list);
        mListView.setAdapter(mAdapter);
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
        intent.putExtra(DetailActivity.IMAGE_DATA_EXTRA, imageUrls[position]);
        startActivity(intent);
    }

    private class ImageAdapter extends BaseAdapter {
        private final Context mContext;
        private ListView.LayoutParams mImageViewLayoutParams;

        public ImageAdapter(Context context) {
            this.mContext = context;
            mImageViewLayoutParams = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    350);
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
            loadBitmap(imageUrls[position], imageView);
            return imageView;
        }

        private void loadBitmap(String imageUrl, ImageView imageView) {

            if (cancelPotentialWork(imageUrl, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(getResources(), null, task);
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

                return decodeSampledBitmapFromStream(is, 8);
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
