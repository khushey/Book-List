package com.example.booklist;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoryCache {

    private static final String TAG = "MemoryCache";

    //last argument true for LRU ordering: having the last value present at the end.

    private Map<String, Bitmap> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    //Sum of the sizes of individual images.
    private long size=0;

    //max memory cache folder used to download images in bytes
    private long limit=1000000;

    public MemoryCache(){
        //use 25%of available heap space.
        setLimit(Runtime.getRuntime().maxMemory()/4);
    }

    private void setLimit(long limit){
        this.limit = limit;
    }

    public Bitmap get(String idURL){
        try{
            if(!cache.containsKey(idURL))
                return null;
            Log.d("ImageLoader" , "memorycacheget: we have image's row bytes: "  + cache.get(idURL).getRowBytes());
            return cache.get(idURL);
        }
        catch (NullPointerException exception){
            exception.printStackTrace();
            return null;
        }
    }

    public void put(String idURL, Bitmap bitmap){
        //if the URL is already present in hashmap,
        if(cache.containsKey(idURL))
            size-=getSizeInBytes(bitmap); //decrease its size
        cache.put(idURL, bitmap); //putting same key in LinkedHashMap deletes older key + value;  and puts this one at last
        size+=getSizeInBytes(bitmap);
        checkSize();
    }

    private long getSizeInBytes(Bitmap bitmap){
        if(bitmap == null)
            return 0;
        else
            return bitmap.getHeight() * bitmap.getRowBytes();
    }


    private void checkSize(){
        Iterator<Map.Entry<String, Bitmap>> iterator =
                cache.entrySet().iterator();

        while(size>limit)
        {
            //delete the value that was first put in (oldest value)
            //HashMap is FIFO.
            Map.Entry<String, Bitmap> mapEntry = iterator.next();
            size -= getSizeInBytes(mapEntry.getValue());
            iterator.remove();
        }
    }

    public void clear(){
        try{
            //Clear cache
            cache.clear();
            size = 0;
        }
        catch (NullPointerException exception){
            exception.printStackTrace();
        }
    }


}
