package com.example.project_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageGen {

    public static String generatePlaceholderImage(String name) {
        int width = 200;
        int height = 50;

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(image);
        canvas.drawColor(Color.WHITE);

        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        android.graphics.Rect bounds = new android.graphics.Rect();
        paint.getTextBounds(name, 0, name.length(), bounds);
        int x = (width - bounds.width()) / 2;
        int y = (height + bounds.height()) / 2;
        canvas.drawText(name, x, y, paint);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

}

