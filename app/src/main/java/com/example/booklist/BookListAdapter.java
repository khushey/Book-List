package com.example.booklist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;

public class BookListAdapter extends ArrayAdapter<BookAttributes> {

    private final static String TAG = "BookListAdapter";
    private static final int LAZY_LOADER_ID = 1;
    private ViewHolder holder;
    private int pos;
    private TextDrawable textDrawable;
    ImageLoader imageLoader;
    List<BookAttributes> bookAttList;
    Context context;

    public BookListAdapter(Context context, List<BookAttributes> objects) {
        super(context, 0, objects);
        this.context = context;
        this.bookAttList = objects;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        pos = position;
        if(convertView == null || convertView.getTag() == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_adapter_view, parent, false);

            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.title);
            holder.authorsView = (TextView) convertView.findViewById(R.id.author);
            holder.avgRatingView = (RatingBar) convertView.findViewById(R.id.rating);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView2);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setTag(position);
        try{
            textDrawable = new TextDrawable(bookAttList.get(position).getTitle());
            holder.titleView.setText(bookAttList.get(position).getTitle());
            holder.authorsView.setText(bookAttList.get(position).getAuthor());
            imageLoader.displayImage(bookAttList.get(position).getUrlString(),
            holder.imageView, bookAttList.get(position).getTitle());
            holder.avgRatingView.setRating((float)(bookAttList.get(position).getAvgRating()));
        }
        catch (NullPointerException exception)
        {
            Log.d(TAG, exception.getMessage());
        }
        return convertView;
    }

    static class ViewHolder{
        TextView titleView;
        TextView authorsView;
        RatingBar avgRatingView;
        ImageView imageView;
    }
}
