package com.example.newsflash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity implements DBManager.DataBaseListener, NewsAdapter.ItemListener {
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ArrayList<News> newsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ((MyApp) getApplication()).dbManager.listener = this;
        ((MyApp) getApplication()).dbManager.getDB(this);


        recyclerView = findViewById(R.id.favourite_recycler);
        adapter = new NewsAdapter(this, newsList);

        this.setTitle("Library...");
        adapter.listener = this;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ((MyApp)getApplication()).dbManager.getAllNewsData();

    }
    @Override
    protected void onResume() {
        super.onResume();
        ((MyApp) getApplication()).dbManager.listener = this;
        ((MyApp)getApplication()).dbManager.getAllNewsData();


    }
    @Override
    public void insertNewsCompleted() {
        //
    }

    @Override
    public void gettingNewsCompleted(News[] list) {
        newsList.clear();
        for (int i = 0; i < list.length; i++) {
            newsList.add(i, list[i]);
        }
        adapter.list = newsList;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteNewsCompleted() {

    }

    @Override
    public void onClicked(int post) {
        News obj=newsList.get(post);
        ((MyApp)getApplication()).selectedNews=obj;
        Intent intent=new Intent(FavouritesActivity.this,DetailNewsActivity.class);
        intent.putExtra("isSaved", true);
        intent.putExtra("isFromFav", true);

        startActivity(intent);
    }
    @Override
    public void onShareClicked(int post) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, newsList.get(post).getUrl());
        shareIntent.setType("text/html");
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, null));
    }
}