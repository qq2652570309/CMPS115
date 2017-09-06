package com.example.foodiemore.foodiemore.Sign_up;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoContract.MerchantInfoEntry;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoDbHelper;
import com.example.harlan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by harlan on 2017/5/22.
 */

public class Signup_merchant_address extends AppCompatActivity {

    // 创建字符变量
    private EditText editName;
    private EditText editStreet;
    private EditText editPhone;

    // 创建字符串变量用于获取输入的地址
    private String resName;
    private String resStreet;
    private String resPhone;

    // 创建按钮变量
    private Button saveButton;
    // 创建数据库参数
    private DatabaseReference editRef;

    private String uId;

    // current location
    protected Location mLocation;
    private double mLatitude;
    private double mLongitude;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_merchant_address);

        //初始化编辑字符
        editName = (EditText) findViewById(R.id.edit_name);
        editStreet = (EditText) findViewById(R.id.edit_address);
        editPhone = (EditText) findViewById(R.id.edit_phone);

        onClickSaveButton();
    }


    /*
    * @onClickSaveButton()
    * 处理Save按钮被点击的情况
    * 被点击后保存地址数据
    * 并返回上一个界面
    * */
    public void onClickSaveButton(){
        saveButton = (Button) findViewById(R.id.edit_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果点击按钮
                // Check information are valid
                if(checkInfo()){            // All entered information are valid
                    getLocation(v);
                    nextActivity();
                }
            }
        });
    }

    /*
    * @nextActivity()
    * 返回user_address_info界面查看地址信息
    * */

    public void nextActivity(){
        // First save information
        saveInfo();

        Intent intent = new Intent();
        intent.setClass(this,Signup_success.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("signup","merchant");
        startActivity(intent);

        // 调用方法来保存地址值
        finish();
    }

    /*
    * @saveInfo()
    * 保存用户输入的地址值
    * 并存入数据库中
    * and save into the local database at the same time
    * */
    public void saveInfo(){
        // 获得商家的地址信息
        resName = editName.getText().toString();
        resStreet = editStreet.getText().toString();
        resPhone = editPhone.getText().toString();
        // 获取当前用户
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uId = user.getUid();                                     // 获取用户的uId
        editRef = FirebaseDatabase.getInstance().getReference();
        // 更新dataBase中的地址信息
        editRef.child("RestaurantData").child(uId).child("resInfo").child("Name").setValue(resName);
        editRef.child("RestaurantData").child(uId).child("resInfo").child("Address").setValue(resStreet);
        editRef.child("RestaurantData").child(uId).child("resInfo").child("Phone").setValue(resPhone);
        editRef.child("RestaurantData").child(uId).child("resInfo").child("Latitude").setValue(mLatitude);
        editRef.child("RestaurantData").child(uId).child("resInfo").child("Longitude").setValue(mLongitude);

        // Save information locally
        MerchantInfoDbHelper mDbHelper = new MerchantInfoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();

        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_NAME, resName);
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS, resStreet);
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_PHONE, resPhone);
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_UID, uId);
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_LATITUDE, mLatitude);
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_LONGITUDE, mLongitude);

        db.insert(MerchantInfoEntry.TABLE_NAME, null, contentValues);
    }

    /*
    * @CheckInfo()
    * check information are valid
    * */
    public boolean checkInfo(){
        if(editName.getText().toString().trim().equals("")){
            reminderMessage("noName");
            return false;
        }else if(editStreet.getText().toString().trim().equals("")){
            reminderMessage("noAddress");
            return false;
        }else if(editPhone.getText().toString().trim().equals("")){
            reminderMessage("noPhone");
            return false;
        }else{
            return true;
        }
    }


    /*
    * @reminderMessage
    * get the reminder message
    * */
    public void reminderMessage(String text){
        switch (text){
            case "noName":
                //duplicate username when register
                Toast.makeText(this, "Please enter merchant name", Toast.LENGTH_SHORT).show();
                return;
            case "noAddress":
                //invalid username entered
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
                return;
            case "noPhone":
                //invalid password entered
                Toast.makeText(this, "Please enter contact information", Toast.LENGTH_SHORT).show();
                return;
        }
    }

    public void getLocation(View v){
        //Log.d("YESORNO","shuchul");

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Log.d("LMERROR","lmsdf");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission_lack", "lack of ACCESS_FINE_LOCATION");
            return;
        }else{
            mLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(mLocation!=null){
                Log.d("LOCATIONLL","current location is not null");
                mLongitude = mLocation.getLongitude();
                mLatitude = mLocation.getLatitude();
            }else{
                Log.d("LOCATIONLL","current location is null");
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_address_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Check the action id
        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
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
