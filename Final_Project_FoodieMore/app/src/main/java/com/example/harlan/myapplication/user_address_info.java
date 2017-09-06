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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Haoran Liang on 2017/4/12.
 * user_address_info界面用于显示用户的地址
 * 读取dataBase中的地址数据并在TextView中显示
 * 默认的地址数据在用户注册成功时自动保存在dataBase中
 * Edit按钮实现用户可以对自己的地址进行编辑
 * 每当用户的地址数据被修改时
 * 此界面的数据会同步更新
 */

public class user_address_info extends AppCompatActivity {

    // 创建字符变量
    private TextView streetText;
    private TextView aptText;
    private TextView phoneText;
    // 创建按钮变量
    private Button editButton;
    // 创建数据库参数
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_address_info);

        // 初始字符变量连接TextView
        streetText = (TextView) findViewById(R.id.userStreet);
        aptText = (TextView) findViewById(R.id.userApt);
        phoneText = (TextView) findViewById(R.id.userPhone);
        // 初始按钮变量链接Button
        editButton = (Button) findViewById(R.id.user_submit);
        // 初始数据库变量mDatabase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getAddressInfo();                                           // 获取地址信息
        onClickEditButton();                                        // 处理Edit案件
    }

    /*
    * getAddressInfo()
    * 获取存在dataBase中
    * 当前用户的地址信息
    * */
    public void getAddressInfo(){
        // 获取当前用户的id
        FirebaseUser dbUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uId = dbUser.getUid();

        // 创建一个监听来获取保存的值
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 读取用户已保存的地址信息并设置到TextView中
                String dbStreet = dataSnapshot.child("UserData").child(uId).child("Street").getValue(String.class);
                streetText.setText(dbStreet);

                String dbApt = dataSnapshot.child("UserData").child(uId).child("Apt").getValue(String.class);
                aptText.setText(dbApt);

                String dbPhone = dataSnapshot.child("UserData").child(uId).child("Phone").getValue(String.class);
                phoneText.setText(dbPhone);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    * @onClickEditButton()
    * 按下Edit按钮进入编辑模式
    * */
    public void onClickEditButton(){
        // 按钮监听，当按下Edit按钮后进入编辑模式
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });
    }

    /*
    * @nextActivity()
    * 跳转到地址编辑页面
    * */
    public void nextActivity(){
        Intent intent = new Intent();
        intent.setClass(user_address_info.this, user_address_info_edit.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        // 从user_address_info跳转至user_address_info_edit页面
        startActivity(intent);
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
