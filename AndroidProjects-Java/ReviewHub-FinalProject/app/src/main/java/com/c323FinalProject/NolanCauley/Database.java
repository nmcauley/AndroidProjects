package com.c323FinalProject.NolanCauley;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {TableCategory.class, TableFavorites.class, TableReview.class,
TableReviewList.class, TableTrash.class},exportSchema = false, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "final_db";
    private static Database instance;

    public static synchronized Database getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    //DAOs
    public abstract TableCategoryDao tableCategoryDao();
    public abstract TableFavoritesDao tableFavoritesDao();
    public abstract TableReviewDao tableReviewDao();
    public abstract TableReviewListDao tableReviewListDao();
}
