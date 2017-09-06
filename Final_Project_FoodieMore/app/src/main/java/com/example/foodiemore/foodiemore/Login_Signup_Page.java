package com.example.foodiemore.foodiemore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodiemore.foodiemore.Merchant.Merchant_Main_Page;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodContract.FoodEntry;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodDbHelper;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoContract.MerchantInfoEntry;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoDbHelper;
import com.example.foodiemore.foodiemore.sign_up_slides.slidesMain;
import com.example.harlan.myapplication.Main2Activity;
import com.example.harlan.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Login_Signup_Page extends AppCompatActivity {
    // define variables
    private EditText username;
    private EditText password;
    //private Button signUpButton;


    private String userValue;

    private FirebaseAuth fmAuth;                                          // Firebase instance
    private FirebaseAuth.AuthStateListener authListener;                 // Firebase auth listener

    private DatabaseReference mDatabase;

    // Variables of restaurant
    private String resName;
    private String resAddress;
    private String resPhone;
    private String resUid;
    private String curUid;
    private String curUserType;
    private String merLatitude;
    private String merLongitude;

    private ArrayList<String> arrayList;

    private String selector_status="cus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

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
                    findViewById(R.id.busImage).setVisibility(View.GONE);
                    findViewById(R.id.edit_username).setVisibility(View.GONE);
                    findViewById(R.id.edit_password).setVisibility(View.GONE);
                    findViewById(R.id.login_button).setVisibility(View.GONE);
                    findViewById(R.id.sign_up_button).setVisibility(View.GONE);
                    userLogin();
                }else{
                    // User is signed out
                    findViewById(R.id.loadPart).setVisibility(View.GONE);
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
    * 当用户按下注册按钮 跳转到用户商家选择界面
    * */
    public void signUpButtonOnClick(){
        Button b = (Button) findViewById(R.id.sign_up_button);
        b.setOnClickListener(new View.OnClickListener(){
            // Set a onClick listener to monitor sign_up_button
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setClass(Login_Signup_Page.this, slidesMain.class);
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
                    successLogin();
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
        // First create the

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // Get current user
        curUid = user.getUid(); // Get Uid
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Intent intent = new Intent();

        DatabaseReference firebaseURL = FirebaseDatabase.getInstance().getReference();
        firebaseURL.child("Accounts").child(curUid);
//        Log.d("wbai",temp);

//        ValueEventListener userValueListener = new ValueEventListener() {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curUserType = dataSnapshot.child("Accounts").child(curUid).getValue().toString();

                if(curUserType.equals("Consumer")){
                    intent.setClass(Login_Signup_Page.this,Main2Activity.class);
                }else if(curUserType.equals("Merchant")){
                    intent.setClass(Login_Signup_Page.this,Merchant_Main_Page.class);
                    createInfoTable();
                    createDishTable();
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mDatabase.addValueEventListener(userValueListener);

    }


    public void userLogin(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // Get current user
        curUid = user.getUid(); // Get Uid
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Intent intent = new Intent();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curUserType = dataSnapshot.child("Accounts").child(curUid).getValue().toString();

                if(curUserType.equals("Consumer")){
                    intent.setClass(Login_Signup_Page.this,Main2Activity.class);
                }else if(curUserType.equals("Merchant")){
                    intent.setClass(Login_Signup_Page.this,Merchant_Main_Page.class);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    /*
    * @createInfoTable()
    * Save current user information into the local database
    * */
    public void createInfoTable(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // Get current user
        resUid = user.getUid(); // Get Uid

        mDatabase = FirebaseDatabase.getInstance().getReference();

        MerchantInfoDbHelper mDbHelper = new MerchantInfoDbHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();

        //db.execSQL("DROP TABLE food"); 清除table表数据

        ValueEventListener userValueListener = new ValueEventListener() {
            // 用监听来获得当前餐厅的名字和地址
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Save all information into created String variables
                resName = dataSnapshot.child("RestaurantData").child(resUid).child("resInfo").child("Name").getValue().toString();
                Log.d("harlan1", "Logging name" + resName);
                resAddress = dataSnapshot.child("RestaurantData").child(resUid).child("resInfo").child("Address").getValue().toString();
                resPhone = dataSnapshot.child("RestaurantData").child(resUid).child("resInfo").child("Phone").getValue().toString();

                merLatitude = dataSnapshot.child("RestaurantData").child(resUid).child("resInfo").child("Latitude").getValue().toString();
                merLongitude = dataSnapshot.child("RestaurantData").child(resUid).child("resInfo").child("Longitude").getValue().toString();

                contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_NAME, resName);
                contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS, resAddress);
                contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_PHONE, resPhone);
                contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_UID, resUid);
                contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_LATITUDE,merLatitude);
                contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_LONGITUDE, merLongitude);
                // Insert information into local database
                db.insert(MerchantInfoEntry.TABLE_NAME, null, contentValues);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(userValueListener);

    }

    /*
    * @createDishTable()
    * Create the dish table
    * */
    public void createDishTable(){
        FoodDbHelper mDbHelper = new FoodDbHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //db.execSQL("DELETE FROM food");                                         // 删除food列表

        arrayList = new ArrayList<String>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        String resUid = user.getUid(); // Get Uid
        final ContentValues contents = new ContentValues();
        Log.d("harlan", "here in the dishtable method");
        Log.d("harlan", resUid);
        DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference();
        mDataBaseRef.child("RestaurantData").child(resUid).child("MenuInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("harlan", "onDataChange?");
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                Log.d("harlan", dataSnapshot.getChildrenCount()+"");
                for(DataSnapshot child : children){
                    Log.d("harlan", "In for loop?");
                    String dishName = child.getKey().toString();
                    //Log.d("harlan", dishName);

                    // Use a arrayList here in order to prevent same dish is saved twice or more
                    if(arrayList.contains(dishName)){ // If arrayList has the dishName
                        return;                     // Skip
                    }
                    else{                              // It is a new element
                        arrayList.add(dishName);
                        Log.d("harlan", "New dish");
                        HashMap RestaurantInformation = (HashMap) child.getValue();
                        String dishDes = RestaurantInformation.get("dishDes").toString();
                        String dishPrice = RestaurantInformation.get("dishPrice").toString();
                        String dishSpecies = RestaurantInformation.get("dishSpecies").toString();
                        String dishUrl = RestaurantInformation.get("dishUrl").toString();

                        int mSpecies = 0;
                        if(dishSpecies.equals("Breakfast")){
                            mSpecies = FoodEntry.SPECIES_BREAKFAST;
                        }else if(dishSpecies.equals("Lunch")){
                            mSpecies = FoodEntry.SPECIES_LUNCH;
                        }else if(dishSpecies.equals("Dinner")){
                            mSpecies = FoodEntry.SPECIES_DINNER;
                        }else if(dishSpecies.equals("Dessert")){
                            mSpecies = FoodEntry.SPECIES_DESSERT;
                        }else if(dishSpecies.equals("Main")){
                            mSpecies = FoodEntry.SPECIES_MAIN;
                        }

                        contents.put(FoodEntry.COLUMN_FOOD_NAME, dishName);
                        contents.put(FoodEntry.COLUMN_FOOD_DESCRIPTION, dishDes);
                        contents.put(FoodEntry.COLUMN_FOOD_PRICE, dishPrice);
                        contents.put(FoodEntry.COLUMN_FOOD_SPECIES, mSpecies);
                        contents.put(FoodEntry.COLUMN_FOOD_URL, dishUrl);

                        db.insert(FoodEntry.TABLE_NAME, null, contents);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

