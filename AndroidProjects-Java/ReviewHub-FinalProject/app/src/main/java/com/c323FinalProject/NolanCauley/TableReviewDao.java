package com.c323FinalProject.NolanCauley;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

@Dao
public interface TableReviewDao {
    @Query("Select * from tablereview")
    TableReview getReview();
}
