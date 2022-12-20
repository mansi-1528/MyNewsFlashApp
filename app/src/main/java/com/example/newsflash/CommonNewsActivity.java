package com.example.newsflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class CommonNewsActivity extends AppCompatActivity implements View.OnClickListener, NetworkingService.NetworkingListener, NewsAdapter.ItemListener, DBManager.DataBaseListener {
    Button business, entertainment, politics, sports, world;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    int count = -1;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    ArrayList<News> finalList = new ArrayList<>();
    int index=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_news);
        initviews();
        setButtonListeners();
        if (savedInstanceState != null) {
            finalList = savedInstanceState.getParcelableArrayList("list");
        }
        ((MyApp) getApplication()).dbManager.listener = this;
        ((MyApp) getApplication()).dbManager.getDB(this);
        ((MyApp) getApplication()).networkingService.listener = this;
        recyclerView = findViewById(R.id.common_recycler);
        adapter = new NewsAdapter(this, finalList);
        adapter.listener = this;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((MyApp) getApplication()).networkingService.getNewsByCategory("business");

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyApp) getApplication()).dbManager.listener = this;

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", finalList);
    }

    private void setButtonListeners() {
        business.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        politics.setOnClickListener(this);
        sports.setOnClickListener(this);
        world.setOnClickListener(this);
    }

    private void initviews() {
        business = findViewById(R.id.btn_business);
        entertainment = findViewById(R.id.btn_entertainment);
        politics = findViewById(R.id.btn_politics);
        sports = findViewById(R.id.btn_sports);
        world = findViewById(R.id.btn_world);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {


            case R.id.favourites:
                Intent intent = new Intent(CommonNewsActivity.this, FavouritesActivity.class);
                startActivity(intent);
                return true;

            case R.id.search:
                Intent intent1 = new Intent(CommonNewsActivity.this, MainActivity.class);
                startActivity(intent1);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        finalList.clear();
        bitmapList.clear();
        ((MyApp) getApplication()).networkingService.getNewsByCategory(button.getText().toString());

    }

    @Override
    public void gettingJsonIsCompleted(String json) {
        finalList = JsonService.fromJsonToNewsList(json);
        count=finalList.size();

        adapter.list = finalList;
        adapter.notifyDataSetChanged();

        for (int i = 0; i < finalList.size(); i++) {
            ((MyApp) getApplication()).networkingService.gettingImage(finalList.get(i).content_url);

        }

    }

    @Override
    public void gettingImageIsCompleted(Bitmap image) {
        bitmapList.add(image);
        int i=bitmapList.size()-1;
        News obj=finalList.get(i);
        BitmapConvertor convertor=new BitmapConvertor();
        obj.bytes= convertor.bitmapToByteArray(image);
        finalList.set(i,obj);
        adapter.list = finalList;
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClicked(int post) {
        index = post;

        News obj = finalList.get(post);
        ((MyApp) getApplication()).dbManager.getAllNewsData();
        ((MyApp) getApplication()).selectedNews = obj;
    }
    @Override
    public void onShareClicked(int post) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, finalList.get(post).getUrl());
        shareIntent.setType("text/html");
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, null));
    }

    @Override
    public void insertNewsCompleted() {

    }

    @Override
    public void gettingNewsCompleted(News[] list) {
        boolean val = false;
        for (int i = 0; i < list.length; i++) {
            if (finalList.get(index).name.equals(list[i].name)) {
                val = true;
                break;
            }
        }

        Intent intent = new Intent(CommonNewsActivity.this, DetailNewsActivity.class);
        intent.putExtra("isSaved", val);
        startActivity(intent);
    }
}