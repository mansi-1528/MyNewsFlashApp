package com.example.ywca_weatherappf22;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

public class DBManager {

    interface DataBaseListener{
        void insertingCityCompleted();
        void gettingCitiesCompleted(City[] list);
    }

    DataBaseListener listener;

    CitiesDatabase cityDB;
    Handler dbHandler = new Handler(Looper.getMainLooper());

    CitiesDatabase getDB(Context context){
        if (cityDB == null)
            cityDB = Room.databaseBuilder(context,CitiesDatabase.class,"city_db").build();

        return cityDB;
    }

    void insertNewCityAsync(City t){

        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                cityDB.getDao().addNewFavCity(t);
                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // notify main thread
                        listener.insertingCityCompleted();
                    }
                });
            }
        });
    }

    void getAllCities(){
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                City[] list = cityDB.getDao().getAllCities();
                dbHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // notify main thread
                        listener.gettingCitiesCompleted(list);
                    }
                });
            }
        });
    }
}
