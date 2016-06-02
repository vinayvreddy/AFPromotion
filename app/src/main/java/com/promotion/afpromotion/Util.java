package com.promotion.afpromotion;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Util {
    public static String saveToInternalStorage(Bitmap bitmap, String title,Context context) throws IOException {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("bitmaps", Context.MODE_PRIVATE);
        File image = new File(directory,title);
        FileOutputStream fileOutputStream=null;
        try{
            fileOutputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            fileOutputStream.close();
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadBitmap(String path,String title){
        Bitmap bitmap = null;
        try{
            File file = new File(path,title);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }catch (Exception e ){

        }
        return bitmap;
    }

    public static String getPreferenceFilepath(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Promotion.MY_PREFERENCE,context.MODE_PRIVATE);
        String filePath =  prefs.getString("filePath","");
        return filePath;
    }
}
