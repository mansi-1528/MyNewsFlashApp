package com.example.newsflash;

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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkingService.NetworkingListener, NewsAdapter.ItemListener {
    RecyclerView recyclerView;
    NewsAdapter adapter;
    int count=-1;
    ArrayList<Bitmap> bitmapList = new ArrayList<>();
    ArrayList<News> mylist = new ArrayList<>();
    ArrayList<News> finalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApp) getApplication()).networkingService.listener = this;
        recyclerView = findViewById(R.id.news_recycler);
        adapter = new NewsAdapter(this,finalList);
        this.setTitle("Search News...");
        adapter.listener = this;
       // ((MyApp)getApplication()).dbManager.listener = this;
       // ((MyApp)getApplication()).dbManager.getDB(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_search_menu, menu);
        MenuItem searchViewMenu = menu.findItem(R.id.news_search_view);

        SearchView searchView = (SearchView) searchViewMenu.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                finalList.clear();
                bitmapList.clear();
                mylist.clear();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  Log.d("Donation app change",newText);
                finalList.clear();
                bitmapList.clear();
                mylist.clear();
                if (newText.length() >=3) {
                    ((MyApp) getApplication()).networkingService.getNewsBySearch(newText);

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
        mylist = JsonService.fromJsonToNewsList(json);
        count=finalList.size();
        finalList=mylist;
        adapter.list = finalList;
        adapter.notifyDataSetChanged();
        for (int i = 0; i < mylist.size(); i++) {
            ((MyApp) getApplication()).networkingService.gettingImage(mylist.get(i).content_url);

        }


// for (int i = 0; i < bitmapList.size(); i++) {
        //     finalList.add(new News(mylist.get(i).title,mylist.get(i).link,
        //             mylist.get(i).photo_url,bitmapList.get(i)));
        //     Log.e("final: ",finalList.get(i).photo_bitmap.toString());
        // }

    }

    @Override
    public void gettingImageIsCompleted(Bitmap bitmap) {
       // Toast.makeText(this, bitmap.toString(), Toast.LENGTH_SHORT).show();
        //Log.e("bitmap", image.toString());

        bitmapList.add(bitmap);
       int i=bitmapList.size()-1;
        News obj=finalList.get(i);
        obj.setPhoto_bitmap(bitmapList.get(i));
        finalList.set(i,obj);
        adapter.list = finalList;
        adapter.notifyDataSetChanged();
        //finalList.add(new News(mylist.get(i).name,mylist.get(i).url,
             //   mylist.get(i).content_url,bitmapList.get(i)));

      //  Toast.makeText(this,"size: "+ bitmapList.size(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClicked(int post) {

        News obj=finalList.get(post);
        ((MyApp)getApplication()).selectedNews=obj;
        Intent intent=new Intent(MainActivity.this,DetailNewsActivity.class);
     //   intent.putExtra("KEY_NAME", obj);

        startActivity(intent);
       // showAlert(finalList.get(post));
    }

    private void showAlert(News news) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save for later or open?");
        builder.setNegativeButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //  ((MyApp)getApplication()).dbManager.insertNewCityAsync(city);
            }
        });
        builder.setPositiveButton("open",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.create().show();
    }
}