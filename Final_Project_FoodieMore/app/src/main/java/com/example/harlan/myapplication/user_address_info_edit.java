package com.example.harlan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Haoran Liang on 2017/4/12.
 * user_address_info_edit界面用于用户编辑地址信息
 * 当用户输入个人信息并按下Save按钮
 * 新的地址信息将储存在dataBase中
 * 并当用户返回user_address_info界面时
 * 其地址信息已被更新
 */

public class user_address_info_edit extends AppCompatActivity {

    // 创建字符变量
    private EditText editStreet;
    private EditText editApt;
    private EditText editPhone;
    // 创建字符串变量用于获取输入的地址
    private String userStreet;
    private String userApt;
    private String userPhone;
    // 创建按钮变量
    private Button saveButton;
    // 创建数据库参数
    private DatabaseReference editRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_address_info_edit);

        //初始化编辑字符
        editStreet = (EditText) findViewById(R.id.edit_street);
        editApt = (EditText) findViewById(R.id.edit_apt);
        editPhone = (EditText) findViewById(R.id.edit_phone);

        // 调用onClickSaveButton方法
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
                nextActivity();
            }
        });
    }

    /*
    * @nextActivity()
    * 返回user_address_info界面查看地址信息
    * */
    public void nextActivity(){
        Intent intent = new Intent();
        intent.setClass(this,user_address_info.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        // 调用方法来保存地址值
        saveInfo();
        finish();
    }

    /*
    * @saveInfo()
    * 保存用户输入的地址值
    * 并存入数据库中
    * */
    public void saveInfo(){
        // 获得用户输入的新地址信息
        userStreet = editStreet.getText().toString();
        userApt = editApt.getText().toString();
        userPhone = editPhone.getText().toString();
        // 获取当前用户
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uId = user.getUid();                                     // 获取用户的uId
        editRef = FirebaseDatabase.getInstance().getReference();
        // 更新dataBase中的地址信息
        editRef.child("UserData").child(uId).child("Street").setValue(userStreet);
        editRef.child("UserData").child(uId).child("Apt").setValue(userApt);
        editRef.child("UserData").child(uId).child("Phone").setValue(userPhone);
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
