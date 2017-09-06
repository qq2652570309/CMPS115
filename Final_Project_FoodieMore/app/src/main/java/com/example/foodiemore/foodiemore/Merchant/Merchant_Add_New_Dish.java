package com.example.foodiemore.foodiemore.Merchant;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodContract.FoodEntry;
import com.example.foodiemore.foodiemore.Merchant_Food_Data.FoodDbHelper;
import com.example.harlan.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Haoran Liang
 * This activity contains action for create new dish
 * Allow user enter new dish information
 * Save new dish to the local SQLite database
 */

public class Merchant_Add_New_Dish extends AppCompatActivity {

    // EditText field to enter food information
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private EditText mPriceEditText;
    // Spinner to select dish species
    private Spinner mSpeciesSpinner;
    // Species number from 0-5
    private int mSpecies = 0;

    private String descriptionString;
    private String priceString;
    private String nameString;
    private int priceInt;


    //variables to upload image
    private StorageReference mStorage;
    private static final int GI = 2;
    private ProgressDialog mProgressDialog;
    private String imageDownloadUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_new_dish_page);

        // Find all relevant views that need user to enter
        mNameEditText = (EditText) findViewById(R.id.edit_food_name);
        mDescriptionEditText = (EditText) findViewById(R.id.edit_food_description);
        mPriceEditText = (EditText) findViewById(R.id.edit_food_price);
        mSpeciesSpinner = (Spinner) findViewById(R.id.spinner_species);

        //Firebase object
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        // Call function to set up spinner
        setupSpinner();
    }

    /*
     * @setupSpinner()
     * Set up the spinner for user to select food species
     * Possible species contains "Breakfast, Lunch, Dinner, Dessert, Main"
     */
    private void setupSpinner() {
        // Create adapter for spinner
        // food_species_options is created in res/values/arrays.xml
        ArrayAdapter speciesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_species_options, android.R.layout.simple_spinner_item);

        // dropdown style simple_dropdown_item_1line
        speciesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSpeciesSpinner.setAdapter(speciesSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSpeciesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.species_breakfast))) {      // Breakfast
                        mSpecies = FoodEntry.SPECIES_BREAKFAST;
                    } else if (selection.equals(getString(R.string.species_lunch))) {   // Lunch
                        mSpecies = FoodEntry.SPECIES_LUNCH;
                    } else if (selection.equals(getString(R.string.species_dinner))) {  // Dinner
                        mSpecies = FoodEntry.SPECIES_DINNER;
                    //} else if (selection.equals(getString(R.string.species_dessert))) { // Dessert
                        //mSpecies = FoodEntry.SPECIES_DESSERT;
                    //} else if (selection.equals(getString(R.string.species_main))) {    // Main
                        //mSpecies = FoodEntry.SPECIES_MAIN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            // We set default as "Breakfast"
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpecies = 0;
            }
        });
    }

    /*
    * @insertFood()
    * Insert a new dish into the SQLite database
    * Information contains food name, food description, food price and food species
    * User need to enter name, description and price
    * Otherwise there will be a toast message shows
    * */
    private void insertFood(){
        // Get the input name and check available
        nameString = mNameEditText.getText().toString().trim();
        if(nameString.equals("")){
            reminderMessage("noName");  // Show the toast message
        }else{
            descriptionString = mDescriptionEditText.getText().toString().trim();   // Check description
            if(descriptionString.equals("")){
                reminderMessage("noDes");
            }else{
                priceString = mPriceEditText.getText().toString().trim();   // Check price
                if(priceString.equals("")){
                    reminderMessage("noPrice");
                }else{
                    priceInt = Integer.parseInt(priceString);   // Get price in int

                    // Create new food database helper
                    FoodDbHelper mDbHelper = new FoodDbHelper(this);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    // New content values
                    ContentValues values = new ContentValues();
                    // Inset all information into the data table
                    values.put(FoodEntry.COLUMN_FOOD_NAME, nameString);
                    values.put(FoodEntry.COLUMN_FOOD_DESCRIPTION, descriptionString);
                    values.put(FoodEntry.COLUMN_FOOD_PRICE, priceInt);
                    values.put(FoodEntry.COLUMN_FOOD_SPECIES, mSpecies);
                    values.put(FoodEntry.COLUMN_FOOD_URL, imageDownloadUri);

                    long newRoadId = db.insert(FoodEntry.TABLE_NAME, null, values);

                    // Show weather the data is saved correctly
                    if(newRoadId==-1){
                        Toast.makeText(this,"Error with saving food", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(this,"Food saved with row id: "+ newRoadId, Toast.LENGTH_SHORT).show();

                    //finish();
                }
            }
        }


        /*Create a new database file named mDbHelper*/
        /*
        FoodDbHelper mDbHelper = new FoodDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        if(nameString.equals("")){

            return;
        }else{
            values.put(FoodEntry.COLUMN_FOOD_NAME, nameString);
        }
        values.put(FoodEntry.COLUMN_FOOD_DESCRIPTION, descriptionString);
        values.put(FoodEntry.COLUMN_FOOD_PRICE, priceInt);
        values.put(FoodEntry.COLUMN_FOOD_SPECIES, mSpecies);

        long newRoadId = db.insert(FoodEntry.TABLE_NAME, null, values);
        */

        /* Show whether the data is saving correctly*/
        /*
        if(newRoadId==-1){
            Toast.makeText(this,"Error with saving food", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Food saved with row id: "+ newRoadId, Toast.LENGTH_SHORT).show();
            */
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
                // save dish to database
                insertFood();
                return true;
            // Respond to left-side back option
            case android.R.id.home:
                // Back to the Merchant_Main_page
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.take_photo:
                uploadImage();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * @reminderMessage()
    * Show the toast message when needed
    * */
    public void reminderMessage(String text){
        switch(text){
            case "noName":
                Toast.makeText(this, "No dish name, please check!", Toast.LENGTH_SHORT).show();
                return;
            case "noPrice":
                //invalid username entered
                Toast.makeText(this, "No dish price, please check!", Toast.LENGTH_SHORT).show();
                return;
            case "noDes":
                //invalid username entered
                Toast.makeText(this, "No dish description, please check!", Toast.LENGTH_SHORT).show();
                return;
        }
    }


    //on click function to upload images
    public void uploadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //open android gallery
        startActivityForResult(intent,GI);
    }

    //store chose image to store in FireBase and get url
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GI && resultCode == RESULT_OK){
            mProgressDialog.setMessage("Uploading....");
            mProgressDialog.show();

            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(Merchant_Add_New_Dish.this, "Upload Done", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    imageDownloadUri = downloadUrl.toString();
                    //Log.d("URIOUT",imageDownloadUri);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Merchant_Add_New_Dish.this, "invalid image, try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

        }

    }


}
