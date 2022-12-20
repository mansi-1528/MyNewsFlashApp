package com.example.newsflash;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends
        RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    interface ItemListener{
        void onClicked(int post);
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
        holder.newsTitle.setText(list.get(position).name);
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

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle =  itemView.findViewById(R.id.news_title);
            imageView=itemView.findViewById(R.id.news_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClicked( getAdapterPosition());

        }
    }


}

