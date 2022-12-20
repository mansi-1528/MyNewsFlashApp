package com.example.newsflash;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NewsDao {

    @Insert
    void addNewsToReadLater(News news);

    @Query("select * from News")
    News[] getAllNews();

    @Delete
    void deleteNews(News news);

}
