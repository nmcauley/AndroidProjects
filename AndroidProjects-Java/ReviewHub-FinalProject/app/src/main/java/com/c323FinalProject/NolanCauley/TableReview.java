package com.c323FinalProject.NolanCauley;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tablereview")
public class TableReview {
    @PrimaryKey(autoGenerate = true)
    public int ReviewId;

    @ColumnInfo(name = "ReviewListId")
    public int ReviewListId;

    @ColumnInfo(name = "CategoryId")
    public int CategoryId;

    @ColumnInfo(name = "ReviewDetails")
    public String ReviewDetails;

    @ColumnInfo(name = "ReviewImage")
    public byte[] Image;

    @Ignore
    public TableReview(String ReviewDetails, byte[] Image) {
        this.ReviewDetails = ReviewDetails;
        this.Image = Image;
    }

    public TableReview(int ReviewId, int ReviewListId, int CategoryId, String ReviewDetails, byte[] Image) {
        this.ReviewId = ReviewId;
        this.ReviewListId = ReviewListId;
        this.CategoryId = CategoryId;
        this.ReviewDetails = ReviewDetails;
        this.Image = Image;
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

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
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
