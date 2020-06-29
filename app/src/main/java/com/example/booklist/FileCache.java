package com.example.booklist;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

//create book list directory and files to write images on. These files are named using URL's hashcode.
public class FileCache {

    //cache directory to store downloaded images
    private File cacheDir;

    private static final String TAG = "FileCache";

    public FileCache(Context context){
        //Find the directory to save cached images
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //External storage can be a removable storage media (such as an SD card) or an internal (non-removable) storage.
            //MEDIA_MOUNTED: Storage state if the media is present and mounted at its mount point with read/write access
            cacheDir = new File(context.getExternalCacheDir(), "BookList");
            //File (File parent, String child).
        }
        else{
            cacheDir = context.getCacheDir();
        }

        if(!cacheDir.exists()) {
            //create cache directory in your application context (when running for the first time).
            cacheDir.mkdirs();
        }
    }

    //identify images by URL hashcode
    public File getFile(String url){
        String filename = url.valueOf(url.hashCode());
        File file = new File(cacheDir, filename);

        if(!file.exists()){
            try{
                file.createNewFile(); //new file named url's hashcode.
            }
            catch (IOException exception){
                Log.d(TAG, exception.getMessage());
            }
        }
        return file;
    }

    public void clear(){
        File[] files = cacheDir.listFiles();
        if(files == null)
            return;
        for(File f: files) {
            f.delete();
        }
    }

}