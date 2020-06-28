package com.example.booklist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookLoader bookLoader;
    private static final int BOOK_LOADER_ID = 0;
    private static final String TAG = "MainActivityx";
    private URL url = null;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUrl();//Async: listener
    }

    //dataLoader
    LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks<List<BookAttributes>>()
    {
        @NonNull
        @Override
        public Loader onCreateLoader(int id, @Nullable Bundle args) {
            bookLoader = new BookLoader(MainActivity.this, url);
            return bookLoader;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<BookAttributes>> loader, final List<BookAttributes> data) {
            //if the data is null, tell the user their query sucked adn they could please rephrase it.
            try{
                    ArrayAdapter arrayAdapter = new BookListAdapter(MainActivity.this, data);
                    ListView listView = (ListView) findViewById(R.id.listview);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity.this, BookListActivity.class);
                            intent.putExtra("readerLink", data.get(position).getWebReaderLink());
                            intent.putExtra("desc", data.get(position).getDescription());
                            startActivity(intent);
                        }
                    });
                    LoaderManager.getInstance(MainActivity.this).destroyLoader(BOOK_LOADER_ID);
            }
            catch (NullPointerException exception){
                Log.d(TAG, "NullPointer exception");
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {
        }
    };

        public void createUrl() {
            //Load activity and get query text to build URL at onStartLoading() in the BookLoader
            //object.
            //The mToCreateUrl object implements an interface in BookLoader's onStartLoading
            searchView = (SearchView) findViewById(R.id.search_view);
            searchView.setOnQueryTextListener(queryTextListener);

        }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            url = QueryUtils.buildQuery(query);
            if(url!=null){
                searchView.clearFocus();
                LoaderManager.getInstance(MainActivity.this)
                        .initLoader(BOOK_LOADER_ID, null, loaderCallbacks).forceLoad();
                return true; //true if the query has been handled by the listener.
            }
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        FileCache fileCache = new FileCache(this);
        fileCache.clear();
    }
}
