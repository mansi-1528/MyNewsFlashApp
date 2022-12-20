package com.example.newsflash;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

public class DBManager {

    interface DataBaseListener{
        void insertNewsCompleted();
        void gettingNewsCompleted(News[] list);
    }

    DataBaseListener listener;

    NewsDatabase cityDB;
    Handler dbHandler = new Handler(Looper.getMainLooper());

    NewsDatabase getDB(Context context){
        if (cityDB == null)
            cityDB = Room.databaseBuilder(context,NewsDatabase.class,"city_db").build();

        return cityDB;
    }

    void insertNewsAsync(News t){

        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                cityDB.getDao().addNewsToReadLater(t);
                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // notify main thread
                        listener.insertNewsCompleted();
                    }
                });
            }
        });
    }

    void getAllNewsData(){
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                News[] list = cityDB.getDao().getAllNews();
                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // notify main thread
                        listener.gettingNewsCompleted(list);
                    }
                });
            }
        });
    }
}
