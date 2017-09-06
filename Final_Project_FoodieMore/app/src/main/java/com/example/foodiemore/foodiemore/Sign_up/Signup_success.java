package com.example.foodiemore.foodiemore.Sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.foodiemore.foodiemore.Merchant.Merchant_Main_Page;
import com.example.harlan.myapplication.Main2Activity;
import com.example.harlan.myapplication.R;

/**
 * Created by harlan on 2017/5/22.
 */

public class Signup_success extends AppCompatActivity {

    private RelativeLayout sr_layout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_success);

        final String message = getIntent().getStringExtra("signup");
//        Log.d("wbai",message);

        sr_layout = (RelativeLayout) findViewById(R.id.sign_up_successful);
        /*
        * Pressed screen to jump to login page
        * */
        sr_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                if(message.equals("consumer")){
                    intent.setClass(Signup_success.this,Main2Activity.class);
                }else{
                    intent.setClass(Signup_success.this,Merchant_Main_Page.class);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    /*
    * Prohibit the return button
    * */
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return true;                //Do not return to previous page
        }
        return super.onKeyDown(keyCode, event); //Keep other super button valid
    }
}