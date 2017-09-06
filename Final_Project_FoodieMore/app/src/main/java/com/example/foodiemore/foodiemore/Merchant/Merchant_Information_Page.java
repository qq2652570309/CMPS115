package com.example.foodiemore.foodiemore.Merchant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoContract.MerchantInfoEntry;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoDbHelper;
import com.example.harlan.myapplication.R;

/**
 * Created by harlan on 2017/5/22.
 */

public class Merchant_Information_Page extends AppCompatActivity {

    private TextView nameText;
    private TextView addText;
    private TextView phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_information_page);
        getMerchantInfo();
    }


    public void onClickImage(View v) {
        Intent intent = new Intent();
        int Id = v.getId();
        switch (Id) {
            case R.id.houseImage:
                intent.setClass(this, Merchant_Main_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.UserImage:
                intent.setClass(this, Merchant_Information_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.notificationImage:
                intent.setClass(this, Merchant_Notification_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.settingImage:
                intent.setClass(this, Merchant_Setting_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getMerchantInfo(){
        MerchantInfoDbHelper mDbHelper = new MerchantInfoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); // Get read

        Cursor cursor = db.query(
                MerchantInfoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int nameColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_NAME);
        int addressColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS);
        int phoneColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_PHONE);
        int latitudeColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_LONGITUDE);

        while(cursor.moveToNext()) {

            String currentName = cursor.getString(nameColumnIndex);
            String currentAddress = cursor.getString(addressColumnIndex);
            String currentPhone = cursor.getString(phoneColumnIndex);
            String currentLatitude = cursor.getString(latitudeColumnIndex);
            String currentLongitude = cursor.getString(longitudeColumnIndex);
            // Set the display String
            nameText = (TextView) findViewById(R.id.merchantName);
            addText = (TextView) findViewById(R.id.merchantAddress);
            phoneText = (TextView) findViewById(R.id.merchantPhone);
            if(currentName.equals("")){
                nameText.setText("It's null");
            }else{
                nameText.setText(currentName);
            }

            if(currentAddress.equals("")){
                addText.setText("It's null");
            }else{
                addText.setText(currentAddress);
            }

            if(currentPhone.equals("")){
                phoneText.setText("It's null");
            }else{
                phoneText.setText(currentPhone);
            }

        }
        cursor.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the menu
        getMenuInflater().inflate(R.menu.menu_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Show the action for click menu item
        switch (item.getItemId()) {
            // Response to the "Edit information"
            case R.id.edit_info_option:
                Intent intent = new Intent();
                intent.setClass(this,Merchant_Information_Edit_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
