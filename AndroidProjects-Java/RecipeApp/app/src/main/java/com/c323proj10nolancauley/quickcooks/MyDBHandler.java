package com.c323proj10nolancauley.quickcooks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RecipesDB.db";
    private static final String TABLE_RECIPES = "Recipes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_RECIPE_ID = "_recipeID";
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_PATH = "_path";

    public MyDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_EXPENSE_TABLE = "CREATE TABLE "+ TABLE_RECIPES +
                "("+COLUMN_ID+" INTEGER PRIMARY KEY,"+ COLUMN_RECIPE_ID +" TEXT,"+ COLUMN_NAME + " TEXT," + COLUMN_PATH + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(sqLiteDatabase);
    }

    public void addRecipe(String id, String name, String imagePath){
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID,id);
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_PATH, imagePath);
        //obtain db instance to insert values into
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }
    //pulls data from db
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
        return res;
    }

    public void deleteEntry(int id){
//        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_ID + " = \"" + id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
        db.delete(TABLE_RECIPES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
