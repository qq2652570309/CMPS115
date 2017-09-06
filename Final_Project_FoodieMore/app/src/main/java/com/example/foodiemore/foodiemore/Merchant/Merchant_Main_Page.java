package com.example.foodiemore.foodiemore.Merchant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodContract.FoodEntry;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodDbHelper;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodInfo;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoContract.MerchantInfoEntry;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoDbHelper;
import com.example.harlan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by harlan on 2017/5/22.
 */

public class Merchant_Main_Page extends AppCompatActivity {
    private ListView lv;
    private ListView lv2;
    //private String currentSpeciesInString;
    private ArrayList<FoodInfo> foodList = new ArrayList<>();

    // List of the foodType
    private ArrayList<FoodInfo> breakfastList;
    private ArrayList<FoodInfo> lunchList;
    private ArrayList<FoodInfo> dinnerList;
    private ArrayList<FoodInfo> dessertList;
    private ArrayList<FoodInfo> mainList;

    private TextView displayView;

    private TextView resNameText;
    private TextView resAddressText;

    private String resUid;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_main_page);

        resNameText = (TextView) findViewById(R.id.restaurantName);
        resAddressText = (TextView) findViewById(R.id.restaurantAddress);

        lv = (ListView) findViewById(R.id.list_view_food);
        //lv.clear;
        setUpFab();
        initFoodList();
        //createInfoTable();
        displayMerchantInfo();
        Log.d("harlan1", "onCreate");
        displayDatabaseInfo();
    }
    /* 原有的onCreate方法没有调用，所以需要onStart方法来复写Info*/

    @Override
    protected void onStart(){
        super.onStart();
        initFoodList();

        displayMerchantInfo();
        Log.d("harlan1", "onStart");
        displayDatabaseInfo();

    }

    /*
    * 初始化各种food list
    * */
    public void initFoodList(){
        breakfastList = new ArrayList<>();
        lunchList = new ArrayList<>();
        dinnerList = new ArrayList<>();
        dessertList = new ArrayList<>();
        mainList = new ArrayList<>();
    }

    /*
    * 初始化Fab悬浮按钮
    * */
    public void setUpFab(){
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Merchant_Main_Page.this, Merchant_Add_New_Dish.class);
                startActivity(intent);
            }
        });
    }


    /*
    * 设置中间四个按钮的跳转
    * */
    public void onClickImage(View v) {
        Intent intent = new Intent();
        int Id = v.getId();
        switch (Id) {
            case R.id.houseImage:
                intent.setClass(this, Merchant_Main_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.UserImage:
                intent.setClass(this, Merchant_Information_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.notificationImage:
                intent.setClass(this, Merchant_Notification_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.settingImage:
                intent.setClass(this, Merchant_Setting_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /*
    * @displayMerchantInfo()
    * Display merchant information
    * The restaurant name and the address on the top
    * */
    private void displayMerchantInfo(){
        // Initialize the TextView
        //resNameText = (TextView) findViewById(R.id.restaurantName);
        //resAddressText = (TextView) findViewById(R.id.restaurantAddress);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        resUid = user.getUid(); // Get Uid

        MerchantInfoDbHelper mDbHelper = new MerchantInfoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); // Get read

        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        String[] Projection = {
                MerchantInfoEntry._ID,
                MerchantInfoEntry.COLUMN_MERCHANT_NAME,
                MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS,
                MerchantInfoEntry.COLUMN_MERCHANT_PHONE,
                MerchantInfoEntry.COLUMN_MERCHANT_UID
        };

        Cursor cursor = db.query(
                MerchantInfoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int idColumnIndex = cursor.getColumnIndex(MerchantInfoEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_NAME);
        int addressColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS);
        int phoneColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_PHONE);
        int uidColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_UID);

        while(cursor.moveToNext()) {
            String currentUid = cursor.getString(uidColumnIndex);
            // If the we get the resUid
            String currentName = cursor.getString(nameColumnIndex);
            Log.d("harlan1", currentName);
            String currentAddress = cursor.getString(addressColumnIndex);

            // Set the restaurant name and address to the main page
            resNameText.setText(currentName);
            resAddressText.setText(currentAddress + ", Santa Cruz, CA, 95060");
        }
        cursor.close();
    }

    /**
     * 将数据在右侧的ListView中生成
     */
    private void displayDatabaseInfo() {

        foodList.clear();
        // Each time display the data, first clear all information in the foodList

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        FoodDbHelper mDbHelper = new FoodDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); // 获得读取权限


        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        resUid = user.getUid(); // Get Uid

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Define a projection that specifies which column from the database
        // you will actually use after this query
        String[] Projection = {
                FoodEntry._ID,
                FoodEntry.COLUMN_FOOD_NAME,
                FoodEntry.COLUMN_FOOD_DESCRIPTION,
                FoodEntry.COLUMN_FOOD_SPECIES,
                FoodEntry.COLUMN_FOOD_PRICE,
                FoodEntry.COLUMN_FOOD_URL
        };

        Cursor cursor = db.query(
                FoodEntry.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null);

        try {

            int idColumnIndex = cursor.getColumnIndex(FoodEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_NAME);
            int breedColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_DESCRIPTION);
            int genderColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_SPECIES);
            int weightColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_PRICE);
            int iUrlColumnIndex = cursor.getColumnIndex(FoodEntry.COLUMN_FOOD_URL);

            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDescription = cursor.getString(breedColumnIndex);
                int currentSpecies = cursor.getInt(genderColumnIndex);
                int currentPrice = cursor.getInt(weightColumnIndex);
                String priceInString = String.valueOf(currentPrice);
                String imageUrl = cursor.getString(iUrlColumnIndex);
                String currentSpeciesInString = null;
                // Store information into database
                mDatabase.child("RestaurantData").child(resUid).child("MenuInfo").child(currentName).child("dishDes").setValue(currentDescription);
                mDatabase.child("RestaurantData").child(resUid).child("MenuInfo").child(currentName).child("dishPrice").setValue(currentPrice);
                mDatabase.child("RestaurantData").child(resUid).child("MenuInfo").child(currentName).child("dishUrl").setValue(imageUrl);
                FoodInfo fd = new FoodInfo();
                switch (currentSpecies) {
                    case 0:
                        currentSpeciesInString = "Breakfast";
                        // 创建新的object
                        fd.setName(currentName);
                        fd.setDescription(currentDescription);
                        fd.setPrice(priceInString);
                        //fd1.setSpecies(currentSpeciesInString);
                        breakfastList.add(fd); // 把数据添加到breakfastList中

                        break;
                    case 1:
                        currentSpeciesInString = "Lunch";
                        // 创建新的object
                        fd.setName(currentName);
                        fd.setDescription(currentDescription);
                        fd.setPrice(priceInString);
                        //fd2.setSpecies(currentSpeciesInString);
                        lunchList.add(fd); // 把数据添加到breakfastList中

                        break;
                    case 2:
                        currentSpeciesInString = "Dinner";
                        // 创建新的object
                        fd.setName(currentName);
                        fd.setDescription(currentDescription);
                        fd.setPrice(priceInString);
                        //fd2.setSpecies(currentSpeciesInString);
                        dinnerList.add(fd); // 把数据添加到breakfastList中
                        break;
                    case 3:
                        currentSpeciesInString = "Dessert";
                        // 创建新的object
                        fd.setName(currentName);
                        fd.setDescription(currentDescription);
                        fd.setPrice(priceInString);
                        //fd2.setSpecies(currentSpeciesInString);
                        dessertList.add(fd); // 把数据添加到breakfastList中
                        break;
                    case 4:
                        currentSpeciesInString = "Main";
                        // 创建新的object
                        fd.setName(currentName);
                        fd.setDescription(currentDescription);
                        fd.setPrice(priceInString);
                        //fd2.setSpecies(currentSpeciesInString);
                        mainList.add(fd); // 把数据添加到breakfastList中
                        break;
                }
                // Get current species for string
                mDatabase.child("RestaurantData").child(resUid).child("MenuInfo").child(currentName).child("dishSpecies").setValue(currentSpeciesInString);

            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        // Set up ListView
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return (breakfastList.size() + lunchList.size() + dinnerList.size() + dessertList.size() + mainList.size() + 3);
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            // ListView 的每一个条目都是一个view对象
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;

                view = View.inflate(getBaseContext(), R.layout.merchant_main_list_item, null);
                // 初始化所有TextView
                TextView foodName = (TextView) view.findViewById(R.id.lvfoodName);
                TextView foodDes = (TextView) view.findViewById(R.id.lvfoodDes);
                TextView foodPrice = (TextView) view.findViewById(R.id.lvfoodPrice);
                //TextView foodSpecies = (TextView) view.findViewById(R.id.lvfoodSpecies);


                FoodInfo fd;
                //foodName.setText("Breakfast");
                if (position == 0) {
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("Breakfast : " + breakfastList.size());
                    tv.setBackgroundColor(Color.parseColor("#D5D4D4"));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.BLACK);
                    return tv;
                } else if (position <= breakfastList.size()) {
                    //添加breakfast data
                    fd = breakfastList.get(position - 1);
                    //因为有个标题 所以position需要-1
                    foodName.setText(fd.getName());
                    foodDes.setText(fd.getDes());
                    foodPrice.setText(fd.getPrice());

                } else if (position == breakfastList.size() + 1) {

                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("Lunch : " + lunchList.size());
                    tv.setBackgroundColor(Color.parseColor("#D5D4D4"));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.BLACK);
                    return tv;
                } else if (position > breakfastList.size() + 1 && position <= (lunchList.size() + breakfastList.size() + 1)) {
                    //添加lunch data
                    fd = lunchList.get(position - breakfastList.size() - 2);
                    foodName.setText(fd.getName());
                    foodDes.setText(fd.getDes());
                    foodPrice.setText(fd.getPrice());

                } else if (position == (lunchList.size() + breakfastList.size() + 2)) {

                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("Dinner : " + dinnerList.size());
                    tv.setBackgroundColor(Color.parseColor("#D5D4D4"));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.BLACK);
                    return tv;

                } else if (position > (lunchList.size() + breakfastList.size() + 2)
                        && position <= (lunchList.size() + breakfastList.size() + dinnerList.size() + 2)) {

                    fd = dinnerList.get(position - breakfastList.size() - lunchList.size() - 3);
                    foodName.setText(fd.getName());
                    foodDes.setText(fd.getDes());
                    foodPrice.setText(fd.getPrice());



                } /*else if (position == (lunchList.size() + breakfastList.size() + dinnerList.size() + 3)) {

                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("Dessert : " + dessertList.size());
                    tv.setBackgroundColor(Color.parseColor("#D5D4D4"));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.BLACK);
                    return tv;


                } else if (position > (lunchList.size() + breakfastList.size() + dinnerList.size() + 3)
                        && position <= (lunchList.size() + breakfastList.size() + dinnerList.size() + dessertList.size() + 3)) {

                    fd = dessertList.get(position - breakfastList.size() - dinnerList.size() - lunchList.size() - 4);
                    foodName.setText(fd.getName());
                    foodDes.setText(fd.getDes());
                    foodPrice.setText(fd.getPrice());

                } else if (position == (lunchList.size() + breakfastList.size() + dinnerList.size() + dessertList.size() + 4)) {
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("Main : " + mainList.size());
                    tv.setBackgroundColor(Color.parseColor("#D5D4D4"));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextColor(Color.BLACK);
                    return tv;

                } else if (position > (lunchList.size() + breakfastList.size() + dinnerList.size() + dessertList.size() + 4)
                        && position <= (lunchList.size() + breakfastList.size() + dinnerList.size() + dessertList.size() + mainList.size() + 4)) {

                    fd = mainList.get(position - breakfastList.size() - dinnerList.size() - lunchList.size() - dessertList.size() - 5);
                    foodName.setText(fd.getName());
                    foodDes.setText(fd.getDes());
                    foodPrice.setText(fd.getPrice());
                }*/

                return view;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                FoodDbHelper mDbHelper = new FoodDbHelper(this);
                SQLiteDatabase db = mDbHelper.getReadableDatabase(); // 获得读取权限
                db.execSQL("DELETE FROM food");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
                resUid = user.getUid(); // Get Uid

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("RestaurantData").child(resUid).child("MenuInfo").setValue("");

                Intent intent = new Intent(Merchant_Main_Page.this, Merchant_Main_Page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

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
