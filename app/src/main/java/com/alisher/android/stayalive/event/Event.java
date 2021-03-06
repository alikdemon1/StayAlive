package com.alisher.android.stayalive.event;

import android.graphics.Bitmap;

/**
 * Created by Alisher Kozhabay on 05.12.2015.
 */
public class Event {
     String name;
     String descr;
     Bitmap photoId;
     String start;
     String end;

    public Event() {
    }

    public Bitmap getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Bitmap photoId) {
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
