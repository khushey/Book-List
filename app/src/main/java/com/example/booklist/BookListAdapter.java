package com.example.booklist;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class BookListAdapter extends ArrayAdapter<BookAttributes> {

    private final static String TAG = "BookListAdapter";
    List<BookAttributes> bookAttList;
    Context context;

    public BookListAdapter(Context context, List<BookAttributes> objects) {
        super(context, 0, objects);
        this.context = context;
        this.bookAttList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_adapter_view, parent, false);

        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        TextView authorsView = (TextView) convertView.findViewById(R.id.author);
        TextView avgRatingView = (TextView) convertView.findViewById(R.id.rating);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);

        titleView.setText(bookAttList.get(position).getTitle());
        Log.d(TAG, titleView.getText()+"");
        authorsView.setText(bookAttList.get(position).getAuthor());
        imageView.setImageBitmap(bookAttList.get(position).getImage());

//        avgRatingView.setText(bookAttList.get(position).getAvgRating().toString());
        return convertView;
    }
}
