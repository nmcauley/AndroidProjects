package com.c323FinalProject.NolanCauley;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TableFavoritesDao {
    @Query("Select * from tablefavorites")
    List<TableFavorites> getFavoritesList();

    @Insert
    void insertFavorite(TableFavorites tableFavorites);

    @Update
    void updateFavorite(TableFavorites tableFavorites);

    @Delete
    void deleteFavorite(TableFavorites tableFavorites);
}
