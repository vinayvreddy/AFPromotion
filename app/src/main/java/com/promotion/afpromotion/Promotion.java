package com.promotion.afpromotion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Promotion extends AppCompatActivity {

    ImageView image_1;
    ImageView image_2;
    TextView text_1;
    TextView text_2;
    TableRow row_1;
    TableRow row_2;
    ArrayList<Promotions> promotionsObjects = new ArrayList<Promotions>();
    PromotionHelper helper = new PromotionHelper(this);
    public static final String MY_PREFERENCE = "sharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        image_1 = (ImageView) findViewById(R.id.imageView_1);
        image_2 = (ImageView) findViewById(R.id.imageView_2);
        text_1 = (TextView) findViewById(R.id.title_1);
        text_2 = (TextView) findViewById(R.id.title_2);
        row_1 = (TableRow) findViewById(R.id.row_1);
        row_2 = (TableRow) findViewById(R.id.row_2);
        SharedPreferences prefs = getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        if (!isNetworkAvailable() && !prefs.getBoolean("dataCached",false)){
            row_1.setClickable(false);
            row_2.setClickable(false);
        }

            row_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getApplicationContext(),CardViewActivity.class);
                    i.putExtra("promotionObject",promotionsObjects.get(0));
                    startActivity(i);
                }
            });
            row_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getApplicationContext(),CardViewActivity.class);
                    i.putExtra("promotionObject",promotionsObjects.get(1));
                    startActivity(i);
                }
            });

            if (isNetworkAvailable()) {
                new GetData().execute();
            } else {
                if(prefs.getBoolean("dataCached",false)) {
                    promotionsObjects = helper.getAllPromotions();
                    text_1.setText(promotionsObjects.get(0).getTitle());
                    image_1.setImageBitmap(Util.loadBitmap(Util.getPreferenceFilepath(getApplicationContext()),promotionsObjects.get(0).getTitle()));
                    text_2.setText(promotionsObjects.get(1).getTitle());
                    image_2.setImageBitmap(Util.loadBitmap(Util.getPreferenceFilepath(getApplicationContext()),promotionsObjects.get(1).getTitle()));
                }
            }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private class GetImage extends AsyncTask<String,Void,String>{
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            ArrayList urls = new ArrayList<>() ;
            String s="";
            urls.add(promotionsObjects.get(0).getImageUrl());
            urls.add(promotionsObjects.get(1).getImageUrl());
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();

            for (int i=0;i<urls.size();i++) {
                try {
                    URL url = new URL(""+urls.get(i));
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream input = new BufferedInputStream(urlConnection.getInputStream());
                    final Bitmap bitmap = BitmapFactory.decodeStream(input);
                    String filePath = Util.saveToInternalStorage(bitmap,promotionsObjects.get(i).getTitle(),getApplicationContext());
                    editor.putString("filePath",filePath);
                    editor.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    urlConnection.disconnect();
                }
            }
            SharedPreferences.Editor editor1 = getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
            editor1.putBoolean("dataCached",true);
            editor1.apply();

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            text_1.setText(promotionsObjects.get(0).getTitle());
            image_1.setImageBitmap(Util.loadBitmap(Util.getPreferenceFilepath(getApplicationContext()),promotionsObjects.get(0).getTitle()));
            text_2.setText(promotionsObjects.get(1).getTitle());
            image_2.setImageBitmap(Util.loadBitmap(Util.getPreferenceFilepath(getApplicationContext()),promotionsObjects.get(1).getTitle()));
        }
    }


    private class GetData extends AsyncTask<String,Void,String>{
        HttpURLConnection urlConnection;
        StringBuilder result = new StringBuilder();

        @Override
        protected String doInBackground(String... params) {
            String urlString = "www.abercrombie.com/anf/nativeapp/Feeds/promotions.json";
            try{
                URL url = new URL("https://"+urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream input = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseJson(s);
        }
    }
    public void parseJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray =jsonObject.optJSONArray("promotions");
            for(int i=0;i<jsonArray.length();i++){
                Promotions pro =new Promotions();

                JSONObject jObject = jsonArray.getJSONObject(i);

                if(jObject.optJSONObject("button") instanceof JSONObject){
                    pro.setButtonTarget(jObject.optJSONObject("button").optString("target"));
                    pro.setButtonTitle(jObject.optJSONObject("button").optString("title"));
                } else {
                    JSONArray jArray =jObject.optJSONArray("button");
                    pro.setButtonTarget(jArray.getJSONObject(0).optString("target"));
                    pro.setButtonTitle(jArray.getJSONObject(0).optString("title"));
                }

                pro.setDescription(jObject.optString("description"));
                pro.setFooter(jObject.optString("footer"));
                pro.setImageUrl(jObject.optString("image"));
                pro.setTitle(jObject.optString("title"));
                SharedPreferences prefs = getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);

                if(!prefs.getBoolean("dataCached",false)) {
                    PromotionHelper helper = new PromotionHelper(this);
                    helper.addPromotion(pro);
                }
                promotionsObjects.add(pro);
            }
            new GetImage().execute();
        } catch (Exception e){

        }
    }
}
