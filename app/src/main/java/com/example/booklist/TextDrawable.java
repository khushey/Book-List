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

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.round;

public class TextDrawable extends Drawable {

    String title;
    Paint paint;
    int lineNum;
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
        paint.getTextBounds("Yj", 0, 1, bounds);

    }

    @Override
    public void draw(Canvas canvas) {
//        String[] title_words = adjustTitle(title, (canvas.getWidth() - 230));
        ArrayList<String> title_words = adjustTitle(title, (canvas.getWidth() - 230));
        float[][] xyPos = calXYPos(title_words, canvas);
        int count = 0;
        for (String string: title_words)
        {
            if(string == null)
                break;
            canvas.drawText(string, xyPos[count][0], xyPos[count++][1], paint);
        }
    }

    private float[][] calXYPos(ArrayList<String> title, Canvas canvas){
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        double offset = 1.7;
        float xyPos[][] = new float[title.size()][2];

        int yPos = (height - lineNum * (int) ceil(bounds.height() * 1.3)) / 2;
        int count = 0;
        for (String string: title){
            if (string == null)
                break;
            yPos = yPos + (int) ceil(bounds.height() * 1.3); //offset y be its height as well, danke.
            xyPos[count][0] = (int) (width - paint.measureText(string))/2; //x position
            xyPos[count++][1] = yPos;
        }
        return xyPos;
    }

    private ArrayList<String>  adjustTitle(String title, int width){
        int length = (int) ceil(paint.measureText(title) / (width));
        String split_title[] = title.split(" ");

        ArrayList<String> titleList = new ArrayList<>();

        int index = 0;
        titleList.add("");
        for(String string: split_title){
            if (string == null)
                break;
            titleList.set(index, (titleList.get(index) + " " + string));
            if(width < (paint.measureText(titleList.get(index)))){
               titleList.add("");
                index++;
            }
        }

        lineNum = index;

        Log.d("chipotle", "LineNum: " + lineNum);
        return titleList;
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