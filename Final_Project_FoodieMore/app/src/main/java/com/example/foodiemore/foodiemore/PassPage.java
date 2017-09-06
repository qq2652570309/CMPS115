package com.example.foodiemore.foodiemore;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.foodiemore.foodiemore.Merchant.Merchant_Main_Page;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodContract.FoodEntry;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodDbHelper;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoContract.MerchantInfoEntry;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoDbHelper;
import com.example.harlan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by harlan on 2017/5/25.
 */

public class PassPage extends AppCompatActivity{

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passpage);
        displayDatabaseInfo();
        displayMerchantInfo();
    }

    public void onClick(View v){
        Intent i = new Intent();
        i.setClass(this, Merchant_Main_Page.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    /*
    * @displayMerchantInfo()
    * Display merchant information
    * The restaurant name and the address on the top
    * */
    private void displayMerchantInfo(){
        // Initialize the TextView
        //resNameText = (TextView) findViewById(R.id.restaurantName);
        //resAddressText = (TextView) findViewById(R.id.restaurantAddress);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        String resUid = user.getUid(); // Get Uid

        MerchantInfoDbHelper mDbHelper = new MerchantInfoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); // Get read

        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        String[] Projection = {
                MerchantInfoEntry._ID,
                MerchantInfoEntry.COLUMN_MERCHANT_NAME,
                MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS,
                MerchantInfoEntry.COLUMN_MERCHANT_PHONE,
                MerchantInfoEntry.COLUMN_MERCHANT_UID
        };

        Cursor cursor = db.query(
                MerchantInfoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int idColumnIndex = cursor.getColumnIndex(MerchantInfoEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_NAME);
        int addressColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS);
        int phoneColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_PHONE);
        int uidColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_UID);

        while(cursor.moveToNext()) {
            String currentUid = cursor.getString(uidColumnIndex);
            // If the we get the resUid
            String currentName = cursor.getString(nameColumnIndex);
            Log.d("harlan1", currentName);
            String currentAddress = cursor.getString(addressColumnIndex);
        }
        cursor.close();
    }

    /**
     * 将数据在右侧的ListView中生成
     */
    private void displayDatabaseInfo() {

        FoodDbHelper mDbHelper = new FoodDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); // 获得读取权限


        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        String resUid = user.getUid(); // Get Uid

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Define a projection that specifies which column from the database
        // you will actually use after this query
        String[] Projection = {
                FoodEntry._ID,
                FoodEntry.COLUMN_FOOD_NAME,
                FoodEntry.COLUMN_FOOD_DESCRIPTION,
                FoodEntry.COLUMN_FOOD_SPECIES,
                FoodEntry.COLUMN_FOOD_PRICE
        };

        Cursor cursor = db.query(
                FoodEntry.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null);

        try {

            int idColumnIndex = cursor.getColumnIndex(FoodEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_NAME);
            int breedColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_DESCRIPTION);
            int genderColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_SPECIES);
            int weightColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_PRICE);

            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDescription = cursor.getString(breedColumnIndex);
                int currentSpecies = cursor.getInt(genderColumnIndex);
                int currentPrice = cursor.getInt(weightColumnIndex);
                String priceInString = String.valueOf(currentPrice);
                String currentSpeciesInString = null;
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

}
