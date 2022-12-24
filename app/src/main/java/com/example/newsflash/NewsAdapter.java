package com.example.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kotlin.text.Regex;

public class NewsAdapter extends
        RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    interface ItemListener{
        void onClicked(int post);
        void onShareClicked(int post);
    }

    Context context;
    ArrayList<News> list;
    ItemListener listener;

    public NewsAdapter(Context context, ArrayList<News> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.news_row,parent,false);

        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
      //  holder.newsTitle.setText(list.get(position).name);
        String str=list.get(position).name;
      str=str.replaceAll("[\\u2018\\u2019\\u201B\\u201F\\u201D\\u201C\\u275C\\u275B\\u275D\\u275E\\u275F]", "\"");
        holder.newsTitle.setText(str);
        if(list.get(position).bytes!=null){
            BitmapConvertor convertor=new BitmapConvertor();
            Bitmap bitmap=convertor.byteArrayToBitmap(list.get(position).bytes);
            holder.imageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // inner class
    public class NewsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        TextView newsTitle;
        ImageView imageView;
        ImageButton shareButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle =  itemView.findViewById(R.id.news_title);
            imageView=itemView.findViewById(R.id.news_image);
            shareButton=itemView.findViewById(R.id.news_share_btn);
            itemView.setOnClickListener(this);
            shareButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.news_share_btn:
                    listener.onShareClicked(getAdapterPosition());
                    break;
                default:
                    listener.onClicked( getAdapterPosition());

            }

        }
    }


}


