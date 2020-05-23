package com.example.booklist;

import android.graphics.Bitmap;

import java.net.URL;

public class BitmapWithURL {
    private URL imgURL;
    private Bitmap bitmap;

    BitmapWithURL(URL imgURL, Bitmap bitmap){
        this.imgURL = imgURL;
        this.bitmap = bitmap;
    }

    public BitmapWithURL getBitmapWithUrl(){
        return this;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
