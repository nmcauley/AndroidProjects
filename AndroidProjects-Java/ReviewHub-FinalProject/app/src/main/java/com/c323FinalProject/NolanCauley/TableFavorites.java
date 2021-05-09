package com.c323FinalProject.NolanCauley;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tablefavorites")
public class TableFavorites {
    @PrimaryKey(autoGenerate = true)
    public int FavoriteId;

    @ColumnInfo(name = "ReviewId")
    public int ReviewId;

    @ColumnInfo(name = "ReviewListId")
    public int ReviewListId;

    @ColumnInfo(name = "ReviewName")
    public String ReviewName;

    @ColumnInfo(name = "ReviewDetails")
    public String ReviewDetails;

    @ColumnInfo(name = "ReviewImage")
    public byte[] Image;

    @Ignore
    public TableFavorites(String ReviewName, String ReviewDetails, byte[] Image){
        this.ReviewName = ReviewName;
        this.ReviewDetails = ReviewDetails;
        this.Image = Image;
    }

    public TableFavorites(int FavoriteId, int ReviewId, int ReviewListId, String ReviewName, String ReviewDetails, byte[] Image) {
        this.FavoriteId = FavoriteId;
        this.ReviewId = ReviewId;
        this.ReviewListId = ReviewListId;
        this.ReviewName = ReviewName;
        this.ReviewDetails = ReviewDetails;
        this.Image = Image;
    }

    public int getFavoriteId() {
        return FavoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        FavoriteId = favoriteId;
    }

    public int getReviewId() {
        return ReviewId;
    }

    public void setReviewId(int reviewId) {
        ReviewId = reviewId;
    }

    public int getReviewListId() {
        return ReviewListId;
    }

    public void setReviewListId(int reviewListId) {
        ReviewListId = reviewListId;
    }

    public String getReviewName() {
        return ReviewName;
    }

    public void setReviewName(String reviewName) {
        ReviewName = reviewName;
    }

    public String getReviewDetails() {
        return ReviewDetails;
    }

    public void setReviewDetails(String reviewDetails) {
        ReviewDetails = reviewDetails;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}