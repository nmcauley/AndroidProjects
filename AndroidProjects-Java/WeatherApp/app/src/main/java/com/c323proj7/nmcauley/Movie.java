package com.c323proj7.nmcauley;

public class Movie {
    String link, movieTitle, overview, release_date, language;
    int gID;

    public Movie(String posterLink, String title, String lang, int genreID, String description, String release) {
        link = posterLink;
        movieTitle = title;
        overview = description;
        release_date = release;
        gID = genreID;
        language = lang;
    }

    public String getLanguage() {
        return language;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getgID() {
        return gID;
    }

    public void setgID(int gID) {
        this.gID = gID;
    }
}
