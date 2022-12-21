package com.example.newsflash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailNewsActivity extends AppCompatActivity implements DBManager.DataBaseListener, View.OnClickListener {
    News object = new News();
    TextView title, desc;
    ImageView imageView;
    Button readMore;
    Boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isSaved = extras.getBoolean("isSaved");
        }
        object = ((MyApp) getApplication()).selectedNews;
        title = findViewById(R.id.select_title);

        ((MyApp) getApplication()).dbManager.listener = this;
        ((MyApp) getApplication()).dbManager.getDB(this);

        desc = findViewById(R.id.select_desc);
        imageView = findViewById(R.id.select_img);
        readMore = findViewById(R.id.select_btn_readmore);
        title.setText(object.name);
        desc.setText(object.description);
        BitmapConvertor convertor = new BitmapConvertor();
        Bitmap bitmap = convertor.byteArrayToBitmap(object.bytes);
        imageView.setImageBitmap(bitmap);
        ImageButton ib = (ImageButton) findViewById(R.id.select_btn_back);
        ib.setOnClickListener(this);
        readMore.setOnClickListener(this);

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.getUrl()));
                startActivity(myIntent);
            }
        });

        //    return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_btn_readmore) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.getUrl()));
            startActivity(myIntent);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.save:
                if (isSaved) {
                    Toast.makeText(this, "Item already added to database..", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_baseline_bookmark_24);
                    News newObj = new News(object.name, object.url, object.description, object.content_url, object.bytes);
                    ((MyApp) getApplication()).dbManager.insertNewsAsync(newObj);

                }

                return true;

            case R.id.share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, object.getUrl());
                shareIntent.setType("text/html");
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, null));
                return true;

            default:
                return false;
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isSaved) {
            //in production you'd probably be better off keeping a reference to the item
            menu.findItem(R.id.save)
                    .setIcon(R.drawable.ic_baseline_bookmark_24)
                    .setTitle("saved");
        } else {
            menu.findItem(R.id.save)
                    .setIcon(R.drawable.ic_baseline_bookmark_border_24)
                    .setTitle("save");
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void insertNewsCompleted() {
        ((MyApp) getApplication()).dbManager.getAllNewsData();

    }

    @Override
    public void gettingNewsCompleted(News[] list) {
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
       // finish();
        isSaved = true;
    }
}