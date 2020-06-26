package com.example.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.inputmethod.InputContentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {
    private final static String TAG = "QueryUtils1";
//    private URL url = null;

    public static URL buildQuery(String query){
        StringBuilder urlBuilder = new StringBuilder("https://www.googleapis.com/books/v1/volumes?maxResults=10&q=");
        urlBuilder = removeWhiteSpace(urlBuilder.length(), urlBuilder.append(query));
        URL url = createURL(urlBuilder.toString());
        return url;
    }

    //to be understood
    public static void copyStream(InputStream inputStream, OutputStream outputStream){
        final int bufferSize = 1024;
        byte[] byteArray = new byte[bufferSize];

        try{

            for(;;){
                //Read byte from input stream
                int count = inputStream.read(byteArray, 0, bufferSize);
                if (count == -1)
                    break;

                outputStream.write(byteArray, 0, count);
            }
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }
    public static URL createURL (String urlString){
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


    public static String getJsonResponse(URL url){
        InputStream inputStream = null;
        String jsonResponse;
        inputStream = makeHttpRequest(url);
        jsonResponse = readFromStream(inputStream);
        return jsonResponse;
        }

    public static InputStream makeHttpRequest(URL url){
            HttpURLConnection connection;
            InputStream inputStream = null;
            String jsonResponse;
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();

                inputStream = connection.getInputStream();
                Log.d(TAG, "inputstream =  " + inputStream);
                return inputStream;
            }
            catch (IOException exception){
                Log.d(TAG, "makeHTTPRequest: IOException" + exception.getMessage());
            }
            return  null;
        }

    private static String readFromStream(InputStream inputStream){
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader;
        BufferedReader reader;
        try{
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
            return output.toString();
        }
        catch (IOException exception){
            Log.d(TAG, exception.getMessage());
        }
        catch (RuntimeException exception){
            Log.d(TAG,"Runtime Exc");

        }        return null;
    }

    public static Bitmap downloadImage(String urlString, File file){

        URL bitmapURL = createURL(urlString);
        InputStream inputStream = null;
        inputStream = makeHttpRequest(bitmapURL);
        if(inputStream!=null)
            Log.d("ImageLoader", "input stream is not null");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            copyStream(inputStream, outputStream);
            outputStream.close();
            FileInputStream fileInputStream = new FileInputStream(file);

//            Bitmap bitmapImg = BitmapFactory.decodeStream(inputStream);
            Bitmap bitmapImg = BitmapFactory.decodeStream(fileInputStream);

            Log.d("ImageLoader", "input stream: "+inputStream + "");
            Log.d("ImageLoader", "1A: Done downloading");

            if(bitmapImg == null)
            Log.d("ImageLoader", "1B: Image is null" );

            return bitmapImg;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<BookAttributes> extractBooks(String jsonString){
        ArrayList<BookAttributes> bookList = new ArrayList<>();
        Log.d(TAG, "extractBooks");
        try
        {
            Log.d(TAG, "extractBooks1");
            //catch for null object reference!
            //convert string to a json object
            JSONObject bookListObject = new JSONObject(jsonString);
            JSONArray itemsArray = bookListObject.optJSONArray("items");
            //we need an array list of an array list.
            JSONArray authorsArray;

            //clear authors here.
            Log.d(TAG, itemsArray.length() + "");
            for(int i = 0; i < itemsArray.length(); i++){
                Log.d(TAG, "extractBooks2");
                JSONObject volumeInfo = itemsArray.optJSONObject(i).optJSONObject("volumeInfo");
                JSONObject accessInfo = itemsArray.getJSONObject(i).optJSONObject("accessInfo");
                authorsArray = volumeInfo.optJSONArray("authors");
                ArrayList<String> authors = new ArrayList<>();


//                downloadImage(volumeInfo.optJSONObject("imageLinks").optString("thumbnail"));

                try{
                    Log.d(TAG, "Thank you, next!");

                    for (int j = 0; j < authorsArray.length(); j++){
                        authors.add(authorsArray.optString(j));
                    }
                }
                catch (NullPointerException exception){
                    Log.d(TAG, "Internet not working");
                }

                Log.d(TAG, "GOT HERE");
                bookList.add(new BookAttributes(
                        volumeInfo.optString("title"),
                        authors,
                        volumeInfo.optString("description"),
                        volumeInfo.optDouble("averageRating"),
                        volumeInfo.optInt("ratingsCount"),
                        volumeInfo.optJSONObject("imageLinks").optString("thumbnail"),
                        accessInfo.optString("webReaderLink"),
                        volumeInfo.optJSONObject("imageLinks").optString("thumbnail")));

                Log.d(TAG, "webreaderlink is this: " + accessInfo.optString("webReaderLink"));
            }
            return bookList;
        }
        catch (JSONException exception){

        }
        catch(NullPointerException exception){
            Log.d(TAG, "Null Pointer Exception");
        }
        return null;
    }

}
