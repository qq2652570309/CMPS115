package com.example.foodiemore.foodiemore.Merchant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodDbHelper;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoContract.MerchantInfoEntry;
import com.example.foodiemore.foodiemore.Merchant_Info_Data.MerchantInfoDbHelper;
import com.example.harlan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by harlan on 2017/5/25.
 */

public class Merchant_Information_Edit_Page extends AppCompatActivity {

    private EditText editName;
    private EditText editAddress;
    private EditText editPhone;

    private String resUid;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_information_edit_page);

        editName = (EditText) findViewById(R.id.merchant_edit_name);
        editAddress = (EditText) findViewById(R.id.merchant_edit_street_apt);
        editPhone = (EditText) findViewById(R.id.merchant_edit_contact);

        getMerchantInfo();
    }


    private void getMerchantInfo(){
        MerchantInfoDbHelper mDbHelper = new MerchantInfoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); // Get read

        Cursor cursor = db.query(
                MerchantInfoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        int nameColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_NAME);
        int addressColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS);
        int phoneColumnIndex = cursor.getColumnIndex(MerchantInfoEntry.COLUMN_MERCHANT_PHONE);

        while(cursor.moveToNext()) {

            String currentName = cursor.getString(nameColumnIndex);
            String currentAddress = cursor.getString(addressColumnIndex);
            String currentPhone = cursor.getString(phoneColumnIndex);
            // Set the display String

            if(currentName.equals("")){
                editName.setText("It's null");
            }else{
                editName.setText(currentName);
            }

            if(currentAddress.equals("")){
                editAddress.setText("It's null");
            }else{
                editAddress.setText(currentAddress);
            }

            if(currentPhone.equals("")){
                editPhone.setText("It's null");
            }else{
                editPhone.setText(currentPhone);
            }

        }
        cursor.close();
    }

    private void saveInfo(){
        String newName = editName.getText().toString().trim();
        String newAdd = editAddress.getText().toString().trim();
        String newPhone = editPhone.getText().toString().trim();

        FoodDbHelper mDbHelper = new FoodDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_NAME, newName);
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_ADDRESS, newAdd);
        contentValues.put(MerchantInfoEntry.COLUMN_MERCHANT_PHONE, newPhone);
        // Save new value to the SQLite database
        db.insert(MerchantInfoEntry.TABLE_NAME, null, contentValues);

        // Save information to firebase at the same time
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        resUid = user.getUid(); // Get Uid

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("RestaurantData").child(resUid).child("resInfo").child("Name").setValue(newName);
        mDatabase.child("RestaurantData").child(resUid).child("resInfo").child("Address").setValue(newAdd);
        mDatabase.child("RestaurantData").child(resUid).child("resInfo").child("Phone").setValue(newPhone);

        Toast.makeText(this,"Saved successfully", Toast.LENGTH_SHORT).show();
    }
    /*
    * @onCreateOptionsMenu()
    * Initialize the top bar menu
    * Contains the save button
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu from res/menu/menu_editor.xml
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /*
    * @onOptionsItemSelected()
    * Set the select option for the user
    * When user click "save" button on the action bar
    * Call insertFood() function, and save all input information
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to "Save" option
            case R.id.action_save:
                saveInfo();
                //finish();
                return true;
            // Respond to left-side back option
            case android.R.id.home:
                // Back to the Merchant_Main_page
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
