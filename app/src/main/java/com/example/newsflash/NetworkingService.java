package com.example.newsflash;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkingService {

    interface NetworkingListener {

        void gettingJsonIsCompleted(String json);
        void gettingImageIsCompleted(Bitmap image);
    }


    NetworkingListener listener;

    Handler handler = new Handler(Looper.getMainLooper());

    String cityURLString = "http://gd.geobytes.com/AutoCompleteCity?&q=";
    String iconURL1 = "http://openweathermap.org/img/wn/";
    String iconURL2 = "@2x.png";

  //  private static final String KEY = "247de6380dmshdc2ed41afb51592p1962d8jsnd216291fc12c";
    private static final String SDK = "true";
    private static final String KEY = "247de6380dmshdc2ed41afb51592p1962d8jsnd216291fc12c";
    private static final String HOST_NEWS = "bing-news-search1.p.rapidapi.com";

    //String searchURL1 = "https://real-time-news-data.p.rapidapi.com/search?query=";
    String searchURL1 ="https://bing-news-search1.p.rapidapi.com/news/search?q=";
    String searchURL2 = "&freshness=Day&textFormat=Raw&safeSearch=Off";

    void getNewsBySearch(String query) {
        //String str = cityURLString + query;
        String str = searchURL1 + query + searchURL2;
        connectToNews(str);
    }
    // void getWeatherInSelectedCity(City c){
    //    String fullString = weatherURL1 + c.city +","+c.country + weatherURL2;
    //    connect(fullString);
    //}


    void connectToNews(String urlString) {
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    int value = 0;
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("X-RapidAPI-Key", SDK);
                    urlConnection.setRequestProperty("X-RapidAPI-Key", KEY);
                    urlConnection.setRequestProperty("X-RapidAPI-Host", HOST_NEWS);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    StringBuffer buffer = new StringBuffer();
                    while ((value = in.read()) != -1) {
                        buffer.append((char) value);
                    }
                    // the json content is ready to returned back
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            listener.gettingJsonIsCompleted(buffer.toString());
                            Log.e("news data: ",buffer.toString());
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }

            }
        });


    }


    void gettingImage(String imageURL) {
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int value = 0;
                    URL url = new URL(imageURL);
                    //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = url.openStream();//new BufferedInputStream(urlConnection.getInputStream());
                    Bitmap imageData = BitmapFactory.decodeStream(in);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.gettingImageIsCompleted(imageData);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }

}


