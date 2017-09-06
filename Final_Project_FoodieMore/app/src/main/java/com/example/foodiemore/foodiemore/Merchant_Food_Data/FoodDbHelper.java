package com.example.foodiemore.foodiemore.Merchant_Food_Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FoodDbHelper extends SQLiteOpenHelper {

    /*Create constants for database name and database version*/
    public static final String LOG_TAG = FoodDbHelper.class.getSimpleName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "foodData.db";

    /*Create a constructor*/
    public FoodDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*Implement the onCreate() method*/
    @Override
    public void onCreate(SQLiteDatabase db){
        /*Create a string that contains the SQL statement to create the pets table*/
        String SQL_CREATE_FOOD_TABLE = "CREATE TABLE " + FoodContract.FoodEntry.TABLE_NAME + " ("
                + FoodContract.FoodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FoodContract.FoodEntry.COLUMN_FOOD_NAME + " TEXT NOT NULL,"
                + FoodContract.FoodEntry.COLUMN_FOOD_DESCRIPTION + " TEXT,"
                + FoodContract.FoodEntry.COLUMN_FOOD_PRICE + " INTEGER NOT NULL DEFAULT 0,"
                + FoodContract.FoodEntry.COLUMN_FOOD_URL + " TEXT ,"
                + FoodContract.FoodEntry.COLUMN_FOOD_SPECIES + " INTEGER NOT NULL DEFAULT 0);";
        /*String SQL_CREATE_PETS_TABLE =
          (in sqlite3) CREATE TABLE pets (_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        breed  TEXT,
                        gender INTEGER NOT NULL DEFAULT 0,
                        weight INTEGER NOT NULL);

        * */

        /*Execute the SQL statement*/
        db.execSQL(SQL_CREATE_FOOD_TABLE);
    }

    /*This is called when the database need to be upgraded*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // The database is still at the version 1, so there's nothing to de be done here.
    }

}
