package com.sahilhans0605.memesapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ImageView memeXML;
    String memeURL;

    public void generateNewMeme(){
        DownloadMeme content = new DownloadMeme();
        try {
            String result= null;
            result = content.execute("https://meme-api.com/gimme").get();
            Log.i("Entered", result);
            try {
                String crappyPrefix = "null";

                if (result.startsWith(crappyPrefix)) {
                    result = result.substring(crappyPrefix.length(), result.length());
                }
                JSONObject jsonObject = new JSONObject(result);
                memeURL = jsonObject.getString("url");
                Log.i("Meme Url", memeURL);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        memeXML=findViewById(R.id.imageView);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memeXML=findViewById(R.id.imageView);

    }




public class DownloadMeme extends AsyncTask<String,Void, String> {


    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection httpConnection;
        String result = null;
        try {
            url = new URL(strings[0]);
            httpConnection = (HttpURLConnection) url.openConnection();
            InputStream in = httpConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    }

    public class DownloadMemeImage extends AsyncTask<String, Void, Bitmap> {
        URL url;
        HttpURLConnection httpConnection;
        Bitmap memeImage;

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                memeImage = BitmapFactory.decodeStream(in);
                return memeImage;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    public void nextMeme(View view) {

        generateNewMeme();
        Bitmap memeImage2;
        DownloadMemeImage imageContent = new DownloadMemeImage();
        try {
            memeImage2 = imageContent.execute(memeURL).get();
            memeXML.setImageBitmap(memeImage2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
