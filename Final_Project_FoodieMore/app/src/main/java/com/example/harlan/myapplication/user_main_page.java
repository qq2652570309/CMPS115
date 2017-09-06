package com.example.harlan.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by harlan on 2017/4/10.
 */

public class user_main_page extends AppCompatActivity {

    private TextView textView1;
    ///private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_page);

        textView1 = (TextView) findViewById(R.id.textView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        if (user != null) {
            textView1.setText(user.getEmail().toString());                     // 得到用户邮箱值
        } else {
            textView1.setText("Can't find username");
        }
    }
}
