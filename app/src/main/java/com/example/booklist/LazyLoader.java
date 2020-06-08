package com.example.booklist;

import androidx.loader.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

//placeholder drawable while image downloads needs to be incorporated.

public class LazyLoader extends AsyncTask<String, Void, Bitmap>
{
    private final WeakReference<ImageView> imageViewReference;
    private final static String TAG = "LazyLoader";
    private String imageUrlString;
    private Bitmap bitmap;
    private int instance;

    public LazyLoader(ImageView imageView, String imageUrlString, int instance)
    {
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.imageUrlString = imageUrlString;
        this.instance = instance;
        Log.d(TAG, "LazyLoader number:  " + instance);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        bitmap = QueryUtils.downloadImage(imageUrlString);
        if(bitmap!=null)
        {
            Log.d(TAG, "bitmap not null!");
            return bitmap;
        }

        Log.d(TAG, "bitmap is null!");
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        ImageView imageView = imageViewReference.get();
        if(imageView!=null)
        {
            if (bitmap!=null)
                Log.d(TAG, "In onPostExecute, imageView.getTag(): " + imageView.getTag());

            imageView.getTag(instance);


//
//            if(imageView.getTag() == instance){
//                imageView.setImageBitmap(bitmap);
//            }
        }
    }
}

//public class LazyLoader extends AsyncTaskLoader {
//
//    private final WeakReference<ImageView> imageViewWeakReference;
//    private String imageUrlString;
//    private Bitmap bitmap;
//
//    public LazyLoader(Context context, ImageView imageView, String imageUrlString) {
//        super(context);
//        imageViewWeakReference = new WeakReference<ImageView>(imageView);
//        this.imageUrlString = imageUrlString;
//    }
//
//    @Nullable
//    @Override
//    public Bitmap loadInBackground() {
//        bitmap = QueryUtils.downloadImage(imageUrlString);
//        if (bitmap!=null)
//            return bitmap;
//        return null;
//    }
//}
