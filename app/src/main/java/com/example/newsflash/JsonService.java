package com.example.newsflash;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonService {
    static ArrayList<News> fromJsonToNewsList(String json) {
        ArrayList<News> list = new ArrayList<>(0);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray newsArray = jsonObject.getJSONArray("value");
            for (int i = 0; i < newsArray.length(); i++) {
                String title = newsArray.getJSONObject(i).getString("name");
                String link = newsArray.getJSONObject(i).getString("url");
                String desc = newsArray.getJSONObject(i).getString("description");
                JSONObject imageArray= newsArray.getJSONObject(i).getJSONObject("image");
                JSONObject thumbnailArray=imageArray.getJSONObject("thumbnail");
                String photo_url=thumbnailArray.getString("contentUrl");
               // String photo_url = newsArray.getJSONObject(i).getString("photo_url");
                list.add(new News(title, link,desc, photo_url));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
