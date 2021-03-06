package com.example.booklist;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.ArrayList;

public class BookAttributes {
    private String title, description;
    private ArrayList<String> authors;
    private String thumbnail;
    private double avgRating;
    private int ratingsCount;
    private String webReaderLink;
    private String imageUrl;

//    private BitmapWithURL bitmapWithURL;
    //creaate an arraylist inside, of a class object that has bitmap against url.
    //the url part is updated with extractbooks.

    public BookAttributes(String title, ArrayList<String> authors, String description, double avgRating,
                          int ratingsCount, String thumbnail, String webReaderLink, String imageUrl){
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.avgRating = avgRating;
        this.ratingsCount = ratingsCount;
        this.thumbnail = thumbnail;
        this.avgRating = avgRating;
        this.ratingsCount = ratingsCount;
        this.webReaderLink = webReaderLink;
        this.imageUrl = imageUrl;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        StringBuilder authorList = new StringBuilder();
        for(int i = 0; i < authors.size(); i++){
            if(i+1 != authors.size())
            authorList.append(authors.get(i)).append(", ");
            else
                authorList.append(authors.get(i));
        }
        return authorList.toString();
    }

    public String getDescription(){
        return description;
    }

//    public URL getUrl(){
//        return QueryUtils.createURL(thumbnail);
//    }

    public String getWebReaderLink(){
        return webReaderLink;
    }
    public String getUrlString(){
        return imageUrl;
    }

    public double getAvgRating(){
        return avgRating;
    }
}
