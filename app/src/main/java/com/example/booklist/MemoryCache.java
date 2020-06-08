package com.example.booklist;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryCache {

    private static final String TAG = "MemoryCache";

    //last argument true for LRU ordering
    private Map<String, Bitmap> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    //current allocated size
    private long size=0;

    //max memory cache folder used to download images in bytes
    private long limit=1000000;

    public MemoryCache(){
        //use 25%of available heap space.
        limit = Runtime.getRuntime().maxMemory()/4;
    }
}
