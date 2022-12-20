package com.example.newsflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkingService.NetworkingListener, NewsAdapter.ItemListener, DBManager.DataBaseListener {
    RecyclerView recyclerView;
    NewsAdapter adapter;
    int count = -1;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    ArrayList<News> finalList = new ArrayList<>();
    //String queryStr = "";
    int index = -1;
    ProgressBar bar;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
           // queryStr = savedInstanceState.getString("Str");
            finalList = savedInstanceState.getParcelableArrayList("list");
        }
        ((MyApp) getApplication()).dbManager.listener = this;
        ((MyApp) getApplication()).dbManager.getDB(this);
        ((MyApp) getApplication()).networkingService.listener = this;
        recyclerView = findViewById(R.id.news_recycler);
        bar = findViewById(R.id.progress_bar);
        adapter = new NewsAdapter(this, finalList);
        adapter.listener = this;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
      //  outState.putString("Str", queryStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_search_menu, menu);
        MenuItem searchViewMenu = menu.findItem(R.id.news_search_view);

        searchView = (SearchView) searchViewMenu.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                finalList.clear();
                bitmapList.clear();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  Log.d("Donation app change",newText);
               // queryStr = newText;
                finalList.clear();
                bitmapList.clear();
                if (newText.length() >= 3) {
                    ((MyApp) getApplication()).networkingService.getNewsBySearch(newText);
                    bar.setVisibility(View.VISIBLE);
                    searchView.setClickable(false);

                    // bar.startAnimation();

                } else {
                    adapter.list = new ArrayList<>(0);
                    adapter.notifyDataSetChanged();
                    // finalList.clear();
                    // bitmapList.clear();
                    //mylist.clear();
                }

                return false;
            }
        });


        return true;
    }

    @Override
    public void gettingJsonIsCompleted(String json) {
        // Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
        //get array list of cities from json string
        finalList = JsonService.fromJsonToNewsList(json);

        count = finalList.size();
        if (count == 0) {
            bar.setVisibility(View.INVISIBLE);

        }
        adapter.list = finalList;
        adapter.notifyDataSetChanged();

        for (int i = 0; i < finalList.size(); i++) {
            ((MyApp) getApplication()).networkingService.gettingImage(finalList.get(i).content_url);

        }


    }

    @Override
    public void gettingImageIsCompleted(Bitmap bitmap) {

        bitmapList.add(bitmap);
        int i = bitmapList.size() - 1;
        News obj = finalList.get(i);
        BitmapConvertor convertor = new BitmapConvertor();
        obj.bytes = convertor.bitmapToByteArray(bitmap);
        finalList.set(i, obj);
        adapter.list = finalList;
        adapter.notifyDataSetChanged();
        if (bitmapList.size() == count) {
            bar.setVisibility(View.INVISIBLE);
            searchView.setClickable(true);
        }


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

        Intent intent = new Intent(MainActivity.this, DetailNewsActivity.class);
        intent.putExtra("isSaved", val);
        startActivity(intent);


    }
}