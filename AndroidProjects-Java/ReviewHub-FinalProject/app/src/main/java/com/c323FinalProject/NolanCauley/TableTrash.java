package com.c323FinalProject.NolanCauley;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TableTrash {
    @PrimaryKey(autoGenerate = true)
    public int TrashId;

    @ColumnInfo(name = "FavoriteId")
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

}
