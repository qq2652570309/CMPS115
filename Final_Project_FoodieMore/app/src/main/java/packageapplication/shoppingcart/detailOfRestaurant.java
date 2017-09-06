package packageapplication.shoppingcart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.harlan.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import packageapplication.firebase.MyAdapter;
import packageapplication.firebase.Restaurants;

public class detailOfRestaurant extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    TextView restName;
    TextView restWaitingTime;
    TextView restAddress;

    Button goToCartView;

    Restaurants restaurant;
    //String message;
    RatingBar ratingBar;

    //create a shopping cart
    static Cart m_cart;


    private ArrayList<foodListElement> aList;
    private foodAdapterList fal;

    //------ menue variable -------
    private ArrayList<Product> products;
    private String option="All";

    //-----------map variables---------------
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLocation;
    private double mLatitude;
    private double mLongitude;
    private double reLatitude;
    private double reLongitude;
    //-----------------------------------

    public final static String REST_MESSGE="packageapplication.firebase.detailOfRestaurant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail_of_restaurant);
//        setContentView(R.layout.test);
        setContentView(R.layout.main_restaurant_page);

        Intent intent = getIntent();

        //测试有没有饭店信息传过来
//        Log.d("wbai",intent.getSerializableExtra(detailOfShoppingCart.Cart_MESSGE)==null?"yes":"no");

        Restaurants getRestaurantInfor = (Restaurants) intent.getSerializableExtra(detailOfShoppingCart.Cart_MESSGE);

        //get object from viewItem
        if(getRestaurantInfor!=null)
            restaurant = getRestaurantInfor;
        else
           restaurant = (Restaurants) intent.getSerializableExtra(MyAdapter.EXTRA_MESSGE);


        //--------------create ListView-----------------
        aList = new ArrayList<foodListElement>();
        fal = new foodAdapterList(this, R.layout.species_layout, aList);
        ListView mListView = (ListView) findViewById(R.id.listview1);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mListView.setAdapter(fal);
        fal.notifyDataSetChanged();

        //------------create Map------------------
        buildGoogleApiClient();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        DataChange();
    }



    /*
    * Create menu for the shopping cart icon
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_cart_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Check the action id
        if (id == R.id.shopping_cart_icon) {
            Intent intent = new Intent(this, detailOfShoppingCart.class);
            intent.putExtra(REST_MESSGE, restaurant);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void DataChange(){
        restName = (TextView) findViewById(R.id.restaurantName);
        restWaitingTime = (TextView) findViewById(R.id.restaurantWaitingTime);
        restAddress = (TextView) findViewById(R.id.restaurantAddress);

        restName.setText(restaurant.name);
        restWaitingTime.setText("Waiting Time is " + restaurant.waiting);
        restAddress.setText(restaurant.address);

        //display the rating of restaurant
        ratingBar = (RatingBar) findViewById(R.id.restaurantRating);
        ratingBar.setRating( restaurant.rating );
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        /*
        //Go to check the detail of shopping cart
        goToCartView = (Button) findViewById(R.id.goToCartView);
        goToCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCart(v);
            }
        });
        */

        //inmplement code for shopping cart
        m_cart = new Cart();


        //---------------创建ListView-----------------------------
        //String url = "https://firebasestorage.googleapis.com/v0/b/foodiemore-a1f10.appspot.com/o/overview%2Fsaber.jpg?alt=media&token=4d953cf9-b77c-4a3a-bd6f-b86bbd5bdbb4";

        if(restaurant.hasMenu()){
            products = restaurant.getMenu();
        }else{
            return;
        }
        aList.clear();
        for (int i = 0; i < products.size(); i++) {
            switch (option){
                case "All":
                    aList.add(new foodListElement(products.get(i).getName(), products.get(i).getUrl(), products.get(i).getValue()));
                    break;
                case "Breakfast":
                    if( products.get(i).getSpecies().equals("Breakfast"))
                        aList.add(new foodListElement(products.get(i).getName(), products.get(i).getUrl(), products.get(i).getValue()));
                    break;
                case "Lunch":
                    if( products.get(i).getSpecies().equals("Lunch"))
                        aList.add(new foodListElement(products.get(i).getName(), products.get(i).getUrl(), products.get(i).getValue()));
                    break;
                case "Dinner":
                    if( products.get(i).getSpecies().equals("Dinner"))
                        aList.add(new foodListElement(products.get(i).getName(), products.get(i).getUrl(), products.get(i).getValue()));
                    break;
            }
        }

        fal.notifyDataSetChanged();
    }



    //--------- implements four onClick fucntion for Button All, Breakfast, Lunch, and Main--------------
    public void AllProductsClick(View v){ option="All"; DataChange();}
    public void BreakfastClick(View v){option="Breakfast"; DataChange();}
    public void LunchClick(View v){option="Lunch"; DataChange();}
    public void MainClick(View v){option="Dinner"; DataChange();}
    //---------------------------------------------------------------------------------------------------

    /*
    //create an intent to goto detailOfShoppingCart
    public void viewCart(View v)
    {
        Intent intent = new Intent(this, detailOfShoppingCart.class);
        intent.putExtra(REST_MESSGE, restaurant);
        startActivity(intent);
    }
    */


    //-----------map part------------------------------
    protected synchronized void buildGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            Log.d("Permission_lack","lack of ACCESS_FINE_LOCATION");
        }else{
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLocation != null){

                mLatitude = mLocation.getLatitude();
                mLongitude = mLocation.getLongitude();

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapPart);

                //getMapAsync call the onMapReady(GoogleMap googleMap) function
                mapFragment.getMapAsync(this);
            }

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("MainActivity", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("MainActivity","Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission_lack", "lack of ACCESS_FINE_LOCATION");
            return;
        }

        mMap.setMyLocationEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        String a = mLatitude+"";
        String b = mLongitude+"";
        reLatitude = Double.parseDouble(restaurant.latitude);
        reLongitude = Double.parseDouble(restaurant.longitude);
        Log.d("LATLNG",reLatitude+"   "+reLongitude);

        // Add a marker in Sydney and move the camera
        LatLng myLatLng = new LatLng(mLatitude, mLongitude);
        LatLng restLatLng = new LatLng(reLatitude,reLongitude);
        builder.include(myLatLng);
        builder.include(restLatLng);
        LatLngBounds bounds = builder.build();
        mMap.addMarker(new MarkerOptions().position(restLatLng).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,40));

        //add my location to mMap
        MarkerOptions options1 = new MarkerOptions();
        options1.position(myLatLng);
        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(options1);

        //add seller location to mMap
        MarkerOptions options2 = new MarkerOptions();
        options2.position(restLatLng);
        options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options2);


        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(myLatLng, restLatLng);
        Log.d("MapPath", url);

        // Start downloading json data from Google Directions API
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                //download data from json of given url
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            //return json
            return data;
        }

        //using got json to create the path
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        //create the final path
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            //all points of locations
            ArrayList points = null;
            //line of path
            PolylineOptions lineOptions = null;

            //traverse all points to create the path
            for (int i = 0; i < result.size(); i++) {
                //current point
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                //current point add all position
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    String Lat_Lng = j+" : " + lat+"    "+lng;
                    Log.d("Lat&Lng",Lat_Lng);
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                //add all line to create the path
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    //get latitude and longitude of origin and destination to create json
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled, 传感器来确定用户的位置
        String sensor = "sensor=false";

        //set travel modes, which could be driving, walking BICYCLING";
        String mode = "mode=walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        //json data
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            //get the path json
            URL url = new URL(strUrl);

            //open and connect to google json web
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            //connect steam input to json
            iStream = urlConnection.getInputStream();
            //create butteredReader to save all data of json
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            //create StringBuffer to read every line of BufferedRead
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            //save into data
            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        //Log.d("JSONPART",data);
        return data;
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

}
