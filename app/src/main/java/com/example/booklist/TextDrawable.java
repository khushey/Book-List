package com.example.booklist;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Collections;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.round;

public class TextDrawable extends Drawable {

    String title;
    Paint paint;
    int y_denom;
    Rect bounds;
    private static final String TAG = "TextDrawables";


    public TextDrawable(String title){
        this.title = title;
        this.paint = new Paint();
        paint.setColor(Color.parseColor("#8D6E63"));
        paint.setTextSize(40f);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL);
//        paint.setTextAlign(Paint.Align.CENTER);
        paint.measureText(title);
        bounds = new Rect();
        paint.getTextBounds("J", 0, 1, bounds);

    }

    @Override
    public void draw(Canvas canvas) {
        String[] title_words = adjustTitle(title, (canvas.getWidth() - 200));
        float[][] xyPos = calXYPos(title_words, canvas);
        int count = 0;
        for (String string: title_words)
        {
            if(string == null)
                break;
            canvas.drawText(string, xyPos[count][0], xyPos[count++][1], paint);
        }
    }

    private float[][] calXYPos(String[] title, Canvas canvas){
        int width = canvas.getWidth() - 40;
        int height = canvas.getHeight() - 20;
        double offset = 1.3;
        float xyPos[][] = new float[title.length][2];

        int yPos = (height - y_denom * (int) ceil(bounds.height() * offset)) / 2;
        int count = 0;
        for (String string: title){
            if (string == null)
                break;
            yPos = yPos + (int) ceil(bounds.height() * offset); //offset y be its height as well, danke.
            xyPos[count][0] = (int) (width - paint.measureText(string))/2; //x position
            xyPos[count++][1] = yPos;
            Log.d("chipotle",  string + " YPos: " + yPos + "  y_denom: " + y_denom + " height: " + height);
        }
        return xyPos;
    }

    private String[] adjustTitle(String title, int width){
        int length = (int) ceil(paint.measureText(title) / (width));
        String split_title[] = title.split(" ");
        StringBuilder titleBuilder = new StringBuilder();
        String title_array[] = new String[length];
        int count = 0;
        for(String string: split_title){
            if (string == null)
                break;
            titleBuilder.append(string + " ");
            //not concatenating array whcih is an expensive operation.
            title_array[count] = titleBuilder.toString();
            if(width < (paint.measureText(title_array[count]))){
                title_array[count] = titleBuilder.toString();
                count++;
                titleBuilder = new StringBuilder();
            }

        }
        if ((title_array[title_array.length - 1]) == null) //last string == null
            y_denom = title_array.length; //divide length by number of strings + 1.
        else
            y_denom = title_array.length + 1;

        return title_array;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}