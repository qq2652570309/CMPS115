package com.example.harlan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by harlan on 2017/4/10.
 */

public class success_register extends AppCompatActivity {

    private RelativeLayout sr_layout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_register);

        sr_layout = (RelativeLayout) findViewById(R.id.sign_up_successful);
        /*
        * Pressed screen to jump to login page
        * */
        sr_layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(success_register.this,MainActivity.class);
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