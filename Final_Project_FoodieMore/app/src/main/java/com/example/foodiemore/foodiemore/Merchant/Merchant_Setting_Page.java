package com.example.foodiemore.foodiemore.Merchant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.foodiemore.foodiemore.Login_Signup_Page;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodDbHelper;
import com.example.harlan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by harlan on 2017/5/22.
 */

public class Merchant_Setting_Page extends AppCompatActivity {

    private ListView settingList;
    private FirebaseAuth firebase;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_setting_page);

        firebase = FirebaseAuth.getInstance();

        setList();
    }

    private void setList(){
        settingList = (ListView) findViewById(R.id.settingList);
        String []data = {"Version 1.0", "Developer Team"};
        ArrayAdapter<String> array = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        settingList.setAdapter(array);
    }

    /*
    * @onClickImage
    * intent jump between four images
    * */
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);     // 取消界面跳转动画
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_merchant_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Log Out"
            case R.id.merchant_log_out:
                signOut();
                backToLoginPage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * @signOut()
    * 登出当前用户
    * */
    public void signOut(){
        try {
            FoodDbHelper mDbHelper = new FoodDbHelper(this);
            SQLiteDatabase db = mDbHelper.getReadableDatabase(); // 获得读取权限
            db.execSQL("DELETE FROM food");
            //db.execSQL("DELETE FROM MerchantInfo");
            // delete contents from table "food" when user sign out
            firebase.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * @backToLoginPage()
    * 跳转至用户登录界面
    * */
    public void backToLoginPage(){
        Intent intent = new Intent();
        intent.setClass(this, Login_Signup_Page.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

}

