package com.c323proj10.nolancauley;

public class Song {
    String id, title, artist, album, duration, data;
    boolean fav = false;
    public Song(String i, String track, String art, String alb, String len, String path) {
        id = i;
        title = track;
        artist = art;
        album = alb;
        duration = len;
        data = path;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean faved) {
        this.fav = faved;
    }
}
