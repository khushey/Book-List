package com.example.booklist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    FileCache fileCache; //create directory / image file
    MemoryCache memoryCache = new MemoryCache(); //create a hashmap of URL against bitmap, to save a limited number of images

    ExecutorService executorService;
    //Maximum size of imageViewStringMap = num of times convertView in BookListAdapter was not null

    //this makes sure previous photos queued for download are not getting downloaded if the user has scrolled away.
    //key is imageView, since URL's can be very large.
    private Map<ImageView, String> imageViewStringMap = Collections.synchronizedMap(
            new WeakHashMap<ImageView, String>());

    //Handler to display images in the UI thread
    Handler handler = new Handler();

    public ImageLoader(Context context){
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    public void displayImage(String keyURL, ImageView imageView){
        //map URL again imageView.
        imageViewStringMap.put(imageView, keyURL);

        //See if the bitmap to this image has already been downloaded and stored in cache
        Bitmap bitmap = memoryCache.get(keyURL);

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
        else{
            //
//            PhotoToLoad photoToLoad = new PhotoToLoad(keyURL, imageView);
            queuePhoto(keyURL, imageView);
        }
    }

    private class PhotoToLoad{
        private String url;
        private ImageView imageView;

        public PhotoToLoad(String url, ImageView imageView){
            this.url = url;
            this.imageView = imageView;
        }
    }

    class PhotosLoader implements Runnable{

        PhotoToLoad photoToLoad;
        Bitmap bitmap;

        public PhotosLoader (PhotoToLoad photoToLoad){
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {

            //return if imageView has replaced: url has changed
            if (imageViewReused(photoToLoad))
                return;
            else
            {
                bitmap = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bitmap);

                if(imageViewReused(photoToLoad))
                    return;

                BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(bitmap, photoToLoad);
                handler.post(bitmapDisplayer);
            }
        }
    }

    private Bitmap getBitmap(String keyURL){

        File file = fileCache.getFile(keyURL);
        //DO SEE WHAT IS GOT HERE. IN CASE OF EMPTY. OR WHATVER.!

        try {
            Bitmap imgBitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            if(imgBitmap!=null)
                return imgBitmap;
            else {
                imgBitmap = QueryUtils.downloadImage(keyURL, file);
                return imgBitmap;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return  null;
        }
    }


    private void queuePhoto (String url, ImageView imageView){
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private boolean imageViewReused(PhotoToLoad photoToLoad){
        if(imageViewStringMap.get(photoToLoad.imageView) == null ||  //imageView was never mapped
                imageViewStringMap.get(photoToLoad.imageView) != photoToLoad.url) //imageView is mapped against a different URL now
            return true;
        else
            return false;
    }

    class BitmapDisplayer implements Runnable{
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer (Bitmap b, PhotoToLoad p){
            bitmap = b;
            photoToLoad = p;
        }

        @Override
        public void run(){
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                if(bitmap!=null)
                    photoToLoad.imageView.setImageBitmap(bitmap);
//                else
//                    photoToLoad.imageView.setImageResource(stubInt);
        }
    }

}
