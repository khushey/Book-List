package com.example.booklist;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.net.URL;
import java.util.ArrayList;

public class BookLoader extends AsyncTaskLoader {
    private static final String TAG = "BookLoader";
    private Context context;
    private String jsonResponse;
    URL url;
    private ArrayList<BookAttributes> bookList;

    public BookLoader(Context context, URL url){
        super(context);
        this.url = url;
        this.context  = context;
        }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Nullable
    @Override
    public ArrayList<BookAttributes> loadInBackground() {
        jsonResponse = QueryUtils.getJsonResponse(url);
        Log.d(TAG, jsonResponse);
        bookList = QueryUtils.extractBooks(jsonResponse);

        //whenbooklist is empy, return null.
        return bookList;

    }
}
