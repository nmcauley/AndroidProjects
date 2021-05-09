package com.c323proj10.nolancauley;

import android.net.Uri;

public class Video {
    Uri data;
    String title;
    long duration;
    boolean fav = false;
    public Video(String tit, long parseLong, Uri parse) {
        title = tit;
        duration = parseLong;
        data = parse;
    }
    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
