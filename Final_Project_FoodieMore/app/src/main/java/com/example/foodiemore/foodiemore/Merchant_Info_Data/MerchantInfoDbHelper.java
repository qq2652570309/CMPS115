package com.example.foodiemore.foodiemore.Merchant_Info_Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by harlan on 2017/5/24.
 */

public class MerchantInfoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "merchantInfo.db";
    // Create constructor
    public MerchantInfoDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*Create a string that contains the SQL statement to create the pets table*/
        String SQL_CREATE_MERCHANTINFO_TABLE = "CREATE TABLE " + MerchantInfoContract.MerchantInfoEntry.TABLE_NAME + " ("
                + MerchantInfoContract.MerchantInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MerchantInfoContract.MerchantInfoEntry.COLUMN_MERCHANT_NAME + " TEXT NOT NULL,"
                + MerchantInfoContract.MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS + " TEXT NOT NULL,"
                + MerchantInfoContract.MerchantInfoEntry.COLUMN_MERCHANT_PHONE + " INTEGER NOT NULL,"
                + MerchantInfoContract.MerchantInfoEntry.COLUMN_MERCHANT_LATITUDE+ " INTEGER NOT NULL,"
                + MerchantInfoContract.MerchantInfoEntry.COLUMN_MERCHANT_LONGITUDE+ " INTEGER NOT NULL,"
                + MerchantInfoContract.MerchantInfoEntry.COLUMN_MERCHANT_UID + " TEXT NOT NULL);";

        /*Execute the SQL statement*/
        db.execSQL(SQL_CREATE_MERCHANTINFO_TABLE);
    }

    /*This is called when the database need to be upgraded*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // The database is still at the version 1, so there's nothing to de be done here.
    }

}
