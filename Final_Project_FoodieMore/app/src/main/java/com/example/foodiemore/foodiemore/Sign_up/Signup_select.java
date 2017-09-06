package com.example.foodiemore.foodiemore.Sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.harlan.myapplication.R;

/**
 * Created by harlan on 2017/5/22.
 */

public class Signup_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_select);
    }

    // 跳转至 merchant_sign_up_page
    public void onClickMerchant(View v){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setClass(this,Signup_merchant_email.class);
        startActivity(intent);
    }

    // 跳转至 consumer_sign_up_page
    public void onClickConsumer(View v){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setClass(this,Signup_consumer_email.class);
        startActivity(intent);
    }
}