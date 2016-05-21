package com.alisher.android.stayalive.news;

import android.graphics.Bitmap;

public class NewsItem {
    private String mName;
    private String mDes;
    private Bitmap mThumbnail;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        this.mDes = des;
    }

    public Bitmap getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(Bitmap mThumbnail) {
        this.mThumbnail = mThumbnail;
    }
}
