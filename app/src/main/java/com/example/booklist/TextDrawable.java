package com.example.booklist;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Collections;

public class TextDrawable extends Drawable {

    String title;
    Paint paint;

    public TextDrawable(String title){
        this.title = title;
        this.paint = new Paint();
        paint.setColor(Color.parseColor("#8D6E63"));
        paint.setTextSize(30f);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);

    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(title, canvas.getWidth()/2, canvas.getHeight()/2, paint);
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