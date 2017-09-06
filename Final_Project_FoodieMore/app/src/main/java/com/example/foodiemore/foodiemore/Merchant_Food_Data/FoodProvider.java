package com.example.foodiemore.foodiemore.Merchant_Food_Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by harlan on 2017/1/9.
 */

public class FoodProvider extends ContentProvider {
/*PetProvider类必须包含onCreate,query,getType,insert,delete,update这6个类*/

    private FoodDbHelper mDbHelper;

    @Override
    public boolean onCreate(){
        mDbHelper = new FoodDbHelper(getContext());
        //获取内容
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1){
        return null;
    }

    @Override
    public String getType(Uri uri){ return null;}

    @Override
    public Uri insert(Uri uri, ContentValues contentValues){ return null; }

    @Override
    public int delete(Uri uri, String s, String[] strings) { return 0; }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings){
        return 0;
    }
}
