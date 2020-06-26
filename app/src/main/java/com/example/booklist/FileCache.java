package com.example.booklist;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class FileCache {

    //cache directory to store downloaded images
    private File cacheDir;

    private static final String TAG = "FileCache";

    public FileCache(Context context){
        //Find the directory at SD Card to save cached images
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //if SDCard is present on device and mounted
            cacheDir = new File(context.getExternalCacheDir(), "BookList");
        }
        else{
            cacheDir = context.getCacheDir();
        }
        if(!cacheDir.exists()) {
            //create cache in your application context
            cacheDir.mkdirs();
        }
    }

    //identify images by URL hashcode
    public File getFile(String url){
        String filename = url.valueOf(url.hashCode());
        File file = new File(cacheDir, filename);

        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch (IOException exception){
                Log.d(TAG, exception.getMessage());
            }
        }
        return file;
    }

    public void clear(){
        File[] files = cacheDir.listFiles();
        Log.d("MainActivityx", "In files");
        if(files == null)
            return;
        for(File f: files) {
            Log.d("MainActivityx", "Deldeldel");
            f.delete();
        }
    }

}