package com.nhnnext.android.kumdo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "BitmapWorkerTask";
    private final WeakReference<ImageView> imageViewReference;
    private String url;

    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    //TODO Volley로 변경
    @Override
    protected Bitmap doInBackground(String... params) {
        Log.d(TAG, "doInBackground - starting work");

        try {
            HttpURLConnection conn = (HttpURLConnection )new URL(params[0]).openConnection();
            InputStream is = conn.getInputStream();
            return BitmapFactory.decodeStream(is);
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
}
