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

            return decodeSampledBitmapFromStream(is, 100, 100);
//            return BitmapFactory.decodeStream(is);
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

    public Bitmap decodeSampledBitmapFromStream(InputStream is, int reqWidth, int reqHeight) {
        Log.d(TAG, "decodeSampledBitmapFromStream - resizing bitmap");

        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(is, null, options);
//
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = 8;

        return BitmapFactory.decodeStream(is, null, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqHeight) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight &&
                    (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        Log.d(TAG, "inSampleSize - " + inSampleSize);
        return inSampleSize;
    }
}
