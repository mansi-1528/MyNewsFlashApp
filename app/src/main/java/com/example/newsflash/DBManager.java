package com.example.newsflash;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

public class DBManager {

    interface DataBaseListener{
        void insertNewsCompleted();
        void gettingNewsCompleted(News[] list);
        void deleteNewsCompleted();

    }

    DataBaseListener listener;

    NewsDatabase newsDatabase;
    Handler dbHandler = new Handler(Looper.getMainLooper());

    NewsDatabase getDB(Context context){
        if (newsDatabase == null)
            newsDatabase = Room.databaseBuilder(context,NewsDatabase.class,"db").build();

        return newsDatabase;
    }

    void insertNewsAsync(News t){

        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                newsDatabase.getDao().addNewsToReadLater(t);
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
                News[] list = newsDatabase.getDao().getAllNews();
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
    void deleteNewsAsync(News t){

        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                newsDatabase.getDao().deleteNews(t);
                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // notify main thread
                        listener.deleteNewsCompleted();
                    }
                });
            }
        });
    }
}
