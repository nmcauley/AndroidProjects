package com.c323FinalProject.NolanCauley;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TableCategoryDao {
    @Query("SELECT * FROM tablecategory ORDER BY CategoryId")
    List<TableCategory> getCategoryList();

    @Insert
    void insertCategory(TableCategory tableCategory);

    @Update
    void updateTableCategory(TableCategory tableCategory);

    @Delete
    void deleteTableCategory(TableCategory tableCategory);


}