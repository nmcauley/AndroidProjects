package com.c323FinalProject.NolanCauley;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tablereviewlist")
public class TableReviewList {
    @PrimaryKey(autoGenerate = true)
    public int ReviewListId;

    @ColumnInfo(name = "CategoryId")
    public int CategoryId;

    @ColumnInfo(name = "ReviewName")
    public String ReviewName;

    @ColumnInfo(name = "ReviewImage")
    public byte[] Image;

    @Ignore
    public TableReviewList(String ReviewName, byte[] Image){
        this.ReviewName = ReviewName;
        this.Image = Image;
    }

    public TableReviewList(int ReviewListId, int CategoryId, String ReviewName, byte[] Image) {
        this.ReviewListId = ReviewListId;
        this.CategoryId = CategoryId;
        this.ReviewName = ReviewName;
        this.Image = Image;
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

    public String getReviewName() {
        return ReviewName;
    }

    public void setReviewName(String reviewName) {
        ReviewName = reviewName;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
