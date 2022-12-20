package com.example.newsflash;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(version = 1, entities = {News.class})
@TypeConverters({BitmapConvertor.class})
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao getDao();

}
