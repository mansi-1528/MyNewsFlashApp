package com.example.newsflash;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
@ProvidedTypeConverter
public class BitmapConvertor {
    @TypeConverter
    public Bitmap byteArrayToBitmap(byte[] bytearray) {
        //Bitmap bmp = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
       /* Log.e("byte: ", String.valueOf(bytearray.length));
        if (bytearray.length != 0) {
            InputStream is = new ByteArrayInputStream(bytearray);

            Bitmap bmp = BitmapFactory.decodeStream(is);
            Log.e("bitmap: ",bmp.toString());
            return bmp;
        } else {
            return null;
        }*/
        Bitmap bmp = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);

        return bmp;

    }
    @TypeConverter
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        /*  ByteArrayOutputStream stream = new ByteArrayOutputStream();
         bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
         byte[] byteArray = stream.toByteArray();
         bmp.recycle();
         return byteArray;*/
        // Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
