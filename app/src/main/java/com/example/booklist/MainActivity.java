package com.example.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SearchView;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String queryURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setQuery();
    }

    void setQuery (){
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            //make URI from this query.
            StringBuilder urlBuilder = new StringBuilder("https://www.googleapis.com/books/v1/volumes?q=");
            urlBuilder = removeWhiteSpace(urlBuilder.length(), urlBuilder.append(query));
            URL url = createURL(urlBuilder.toString());

            if(query!=null)
                return true;
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private URL createURL (String urlString){
        URL url = null;

        try {
            url = new URL(urlString);
            return url;
        }
        catch (MalformedURLException exception)
        {
            Log.d(TAG, "Url malformed!");
        }
        return  null;
    }

    private static StringBuilder removeWhiteSpace(int len, StringBuilder urlBuilder){
        for (int i = len; i < urlBuilder.length(); i++){
            if(!Character.isWhitespace(urlBuilder.charAt(i)))//if not white space
                urlBuilder.setCharAt(len++, urlBuilder.charAt(i));
        }
        urlBuilder.delete(len, urlBuilder.length()); //delete superfluous characters
        return urlBuilder;
    }
}
