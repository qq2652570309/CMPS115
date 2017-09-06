package com.example.harlan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    // define variables
    private EditText username;
    private EditText password;
    //private Button signUpButton;

    private FirebaseAuth fmAuth;                                          // Firebase instance
    private FirebaseAuth.AuthStateListener authListener;                 // Firebase auth listener


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fmAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.edit_username);
        password = (EditText) findViewById(R.id.edit_password);

        // sign up action
        signUpButtonOnClick();
        // login action
        loginButtonOnClick();

        // Add listener to check if the user is currently login
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // User is signed in
                    successLogin();
                }
                else{
                    // User is signed out
                }
            }
        };

    }


    @Override
    public void onStart(){
        super.onStart();

        fmAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authListener != null){
            fmAuth.removeAuthStateListener(authListener);
        }
    }

    /*
    * @signUpButton()
    * When sign up button is clicked
    * call "onClick" method
    * Intent will start a new activity
    * "FoodieMore" is the current activity
    * "sign_up_page" is the next activity
    * */
    public void signUpButtonOnClick(){
        Button b = (Button) findViewById(R.id.sign_up_button);
        b.setOnClickListener(new View.OnClickListener(){
            // Set a onClick listener to monitor sign_up_button
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, consumer_sign_up_page.class);
                startActivity(intent);
            }
        });
    }

    /*
    * @loginButtonOnClick()
    * First check username and password are valid,
    * then jump to next activity
    * */
    public void loginButtonOnClick(){
        Button b = (Button) findViewById(R.id.login_button);
        b.setOnClickListener(new View.OnClickListener(){
            // Set a onClick listener to monitor login_button
            @Override
            public void onClick(View view){
                if(validUserAndPwd()){
                    // If username and password is valid
                    checkUsernamePwd();
                }
            }
        });
    }

    /*
    * @validUserAndPwd()
    * Check username and password are valid
    * If user not enter username or password,
    * show reminder message
    * */
    public boolean validUserAndPwd(){
        if(username.getText().toString().trim().equals("")){
            reminderMessage("invalidUsername");
            return false;
        }
        else if(password.getText().toString().trim().equals("")){
            reminderMessage("invalidPwd");
            return false;
        }
        return true;
    }

    /*
    * @checkUsernamePwd()
    * Check if username is exist in database,
    * then check if password can match with username.
    * If all match, then login successful
    * */
    public void checkUsernamePwd(){
        String dbUsername = username.getText().toString();
        String UserPwd = password.getText().toString();

        fmAuth.signInWithEmailAndPassword(dbUsername, UserPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    // If not successful
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        reminderMessage("wrongPwd");
                    }
                    else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                        reminderMessage("noSuchUsn");
                    }
                }
                else{
                    //successLogin();
                }
            }
        });
    }

    /*
    * @successLogin()
    * login successfully,
    * jump to the main page
    * */
    public void successLogin(){
        Intent intent = new Intent();
        intent.setClass(this,Main2Activity.class);
        startActivity(intent);
        /*
        * First access the user_main_page
        * Then call transferUserId()
        * End the activity
        * */
        finish();
    }

    /*
    * @reminderMessage()
    * pop message for different nonstandard operation
    * */
    public void reminderMessage(String text){
        switch(text){
            case "noSuchUsn":
                Toast.makeText(this, "No such Username, please check", Toast.LENGTH_SHORT).show();
                return;
            case "invalidUsername":
                //invalid username entered
                Toast.makeText(this, "Please enter the username!", Toast.LENGTH_SHORT).show();
                return;
            case "invalidPwd":
                //invalid password entered
                Toast.makeText(this, "Please enter the password!", Toast.LENGTH_SHORT).show();
                return;
            case "wrongPwd":
                //wrong password entered
                Toast.makeText(this, "Wrong password!", Toast.LENGTH_SHORT).show();
                return;
        }
    }

    /*
    * @onKeyDown()
    * Prohibit bottom back button
    * */
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return true;                //Do not return to previous page
        }
        return super.onKeyDown(keyCode, event); //Keep other super button valid
    }
}

