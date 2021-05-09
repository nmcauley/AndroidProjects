package com.c323FinalProject.NolanCauley;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tablecategory")
public class TableCategory {
    @PrimaryKey(autoGenerate = true)
    int CategoryId;

    @ColumnInfo(name = "CategoryName")
    String CategoryName;

    @ColumnInfo(name = "ReviewId")
    byte[] Image;

    @Ignore
    public TableCategory(String CategoryName, byte[] Image){
        this.CategoryName = CategoryName;
        this.Image = Image;
    }

    public TableCategory(int CategoryId, String CategoryName, byte[] Image) {
        this.CategoryId = CategoryId;
        this.CategoryName = CategoryName;
        this.Image = Image;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
