package com.c323FinalProject.NolanCauley;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TableReviewListDao {
    @Query("Select * from tablereviewlist")
    List<TableReviewList> getReviewList();

    @Insert
    void insertReviewList(TableReviewList tableReviewList);

    @Update
    void updateReviewList(TableReviewList tableReviewList);

    @Delete
    void deleteReviewList(TableReviewList tableReviewList);
}
