package com.example.booklist;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

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
    public ArrayList<BookAttributes> loadInBackground() throws NullPointerException{
        try{
            jsonResponse = QueryUtils.getJsonResponse(url);
            bookList = QueryUtils.extractBooks(jsonResponse);
            if(bookList == null)
            {
                return new ArrayList<BookAttributes>();
            }
            return bookList;

        }
        catch (NullPointerException exception){
            Log.d(TAG, exception.getMessage());
            return  null;
        }
    }
}
