package com.example.harlan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by harlan on 2017/4/10.
 */

public class consumer_sign_up_page extends AppCompatActivity {

    private EditText username;                          //Username
    private EditText password;                          //Password
    private EditText confirmPassword;                  //ConfirmPassword

    private FirebaseAuth signUpAuth;                           //FireBase reference
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_sign_up_page);

        // Get Firebase auth instance
        signUpAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.csup_username);
        password = (EditText) findViewById(R.id.csup_password);
        confirmPassword = (EditText) findViewById(R.id.csup_confirmpassword);

        Button nextButton = (Button) findViewById(R.id.csup_next);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Check information
                registerCheck();
            }
        });
    }

    /*
    * @registerCheck()
    * Check register
    * First check if Username and Password are valid
    * Then check if two passwords are the same
    * Jump to next activity
    * */
    public void registerCheck() {
        if (validNameAndPwd()) {
            //Username and password is valid
            if (checkTwoPwd()) {
                // Add a database value event listener for single event
                // Check if the user enter username is already exist
                String fbEmail = username.getText().toString();
                String fbPwd = password.getText().toString();

                signUpAuth.createUserWithEmailAndPassword(fbEmail, fbPwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                reminderMessage("dupUsername");
                            }
                            else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                reminderMessage("notEmail");
                            }
                        }
                        else{
                            createUserInfo();
                            registerSuccess();
                        }
                    }
                });
            }
        }
    }

    /*
    * @validNameAndPwd()
    * Check username, password, confirmPassword is valid or not
    * The return value is true/false
    * */
    public boolean validNameAndPwd(){
        if(username.getText().toString().trim().equals("")){
            reminderMessage("invalidUsername");
            return false;
        }
        else if(password.getText().toString().trim().equals("") ||
                confirmPassword.getText().toString().trim().equals("")){
            reminderMessage("invalidPassword");
            return false;
        }
        return true;
    }

    /*
    * @checkTwoPwd()
    * check if password and confirm password is the same
    * if same, pop message
    * if not return true and continue
    * */
    public boolean checkTwoPwd(){
        String dbPwd = password.getText().toString().trim();
        String dbCfmPwd = confirmPassword.getText().toString().trim();
        if(dbPwd.equals(dbCfmPwd)==false){      //Password and confirmPassword is not same
            reminderMessage("notSamePwd");
            return false;
        }
        return true;
    }

    /*
    * @registerSuccess()
    * create new intent,
    * and jump to the next consumer sign up page for entering address
    * */
    public void registerSuccess(){
        Intent intent = new Intent();
        intent.setClass(this,success_register.class);
        startActivity(intent);
        finish();
    }

    /*
    * @createUserInfo()
    * When create the new account
    * We create the user data in the database
    * Then create children such as address, apartment, phone number
    * 注册成功后，获取当前注册用户的Uid，以及注册邮箱
    * 建立在根目录下创建UserData,并创建属于Uid的子目录
    * */
    public void createUserInfo(){
        String fbEmail = username.getText().toString();
        //String fbPwd = password.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        String uId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("UserData").child(uId).setValue(fbEmail);
        // 在根目录下创建属于用户的街道，门房号，电话等信息
        mDatabase.child("UserData").child(uId).child("Street").setValue("Street name");
        mDatabase.child("UserData").child(uId).child("Apt").setValue("Apartment number");
        mDatabase.child("UserData").child(uId).child("Phone").setValue("( )-___-____");
    }

    /*
    * @reminderMessage()
    * pop message for different nonstandard operation
    * */
    public void reminderMessage(String text){
        switch (text){
            case "dupUsername":
                //duplicate username when register
                Toast.makeText(this, "Username exist, please enter a new one", Toast.LENGTH_SHORT).show();
                return;
            case "invalidUsername":
                //invalid username entered
                Toast.makeText(this, "Please enter the username!", Toast.LENGTH_SHORT).show();
                return;
            case "invalidPassword":
                //invalid password entered
                Toast.makeText(this, "Please enter the password!", Toast.LENGTH_SHORT).show();
                return;
            case "notSamePwd":
                // password and confirm password are not same
                Toast.makeText(this, "Please enter the same password", Toast.LENGTH_SHORT).show();
                return;
            case "notEmail":
                // username is not a email
                Toast.makeText(this, "Please enter email address as username", Toast.LENGTH_SHORT).show();
                return;
        }
    }
}

