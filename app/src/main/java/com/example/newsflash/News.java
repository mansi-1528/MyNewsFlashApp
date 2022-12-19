package com.example.newsflash;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class News implements Parcelable {

   // @PrimaryKey(autoGenerate = true)
  //  int id;
    String name;
    String url;
    String description;
    String content_url;
    Bitmap photo_bitmap;


    public News(String name, String url,String description, String content_url) {
        this.name = name;
        this.url = url;
        this.description = description;
       this.content_url = content_url;
    }
    public News(String name, String url,String description, String content_url, Bitmap photo_bitmap) {
        this.name = name;
        this.url = url;
        this.description = description;

        this.content_url = content_url;
        this.photo_bitmap=photo_bitmap;
    }
    public News() {
    }
    protected News(Parcel in) {
        name = in.readString();
        url = in.readString();
        description = in.readString();
        content_url = in.readString();
       // photo_bitmap = in.readString();



    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public Bitmap getPhoto_bitmap() {
        return photo_bitmap;
    }

    public void setPhoto_bitmap(Bitmap photo_bitmap) {
        this.photo_bitmap = photo_bitmap;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(content_url);
       // dest.writeString(photo_bitmap);

    }
}

