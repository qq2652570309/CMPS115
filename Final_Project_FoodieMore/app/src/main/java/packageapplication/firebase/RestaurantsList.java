package packageapplication.firebase;

/**
 * Created by wentian on 5/19/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.foodiemore.foodiemore.Login_Signup_Page;
import com.example.harlan.myapplication.R;
import com.example.harlan.myapplication.user_address_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import packageapplication.shoppingcart.Product;
import packageapplication.shoppingcart.detailOfRestaurant;

public class RestaurantsList extends AppCompatActivity{

    public MyAdapter aa;
    public ArrayList<Restaurants> aList;
    public ArrayList<Restaurants> AllRestaurants;

//    String target=null;
    String target=null;

    private EditText search_content;
    private Button search_button;
    private Button cancel_button;
    private LinearLayout search_bar;
    private HashMap MenuInfo=null;
    private HashMap resInfo=null;
    private FirebaseAuth firebase;
    private String Restuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_restaurants);
        firebase = FirebaseAuth.getInstance();
        onClickSearch();

        aList = new ArrayList<Restaurants>();
        AllRestaurants = new ArrayList<Restaurants>();
        aa = new MyAdapter(this, R.layout.my_listitem, aList);
        ListView myListView = (ListView) findViewById(R.id.rest_listview);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    public void onResume(){
        super.onResume();
        onClickSearch();
        getRestaurantData();
    }

    //获取饭店的信息从firebase
    public void getRestaurantData(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
//        databaseReference.child("restaurant").addValueEventListener(new ValueEventListener() {


//        databaseReference.child("RestaurantData").addValueEventListener(new ValueEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                findViewById(R.id.loading).setVisibility(View.GONE);

                //清除之前的数据，用于防止多次加载造成画面从影
                AllRestaurants.clear();

                //获取所有的饭店信息
                Iterable<DataSnapshot> children = dataSnapshot.child("RestaurantData").getChildren();


                //将每一个饭店的信息输入到 getRestaurant() 方法里面
                for(DataSnapshot child : children){


                    Restuid = child.getKey();
                    //Log.d("restaurant ID: ", child.getKey());
                    // 用getValue的方法获取饭店的详细信息，每一个饭店的value都是以一个HashMap的形式保存的，
                    HashMap RestaurantInformation = (HashMap) child.getValue();
                    //获取菜单信息
                    if(RestaurantInformation.containsKey("MenuInfo")){
                        try{
                            MenuInfo = (HashMap) RestaurantInformation.get("MenuInfo");
                        }catch (Exception e){
                            continue;
                        }

                    }else{
                        MenuInfo=null;
                    }


                    //获取饭店信息
                    if(RestaurantInformation.containsKey("resInfo")){
                        resInfo = (HashMap) RestaurantInformation.get("resInfo");
                        resInfo.put("RestUid", Restuid);
                    }
                    //给 alist 添加一个饭店信息
                    addRestaurant(resInfo);
                }
                filter();

                Log.d("wbai","***********");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //获取单个饭店的每一个详细属性（名字，电话，地址，等待时间，和评价）
    //输入：保存有一个饭店信息的HashMap
    //目的：给 alist 添加一个ListElement，里面存有输入饭店的信息
    private void addRestaurant(HashMap value){
        //各个属性的初始值
        String name = null;
        String phone = null;
        String address = null;
        HashMap menu = null;

        String waiting = "none";
        String rating = "0";
        String latitude = "0";
        String longitude = "0" ;
        String Uid = "";
        String resPhoto = "https://firebasestorage.googleapis.com/v0/b/foodiemore-a1f10.appspot.com/o/overview%2Fimages.png?alt=media&token=781ee2bc-8bf7-4128-8b8d-8fb9db73baa4";

        if(value.containsKey("RestUid")) Uid = value.get("RestUid").toString();


        if(value.containsKey("Name")){
            name = value.get("Name").toString();
        }

        if(value.containsKey("Phone")){
            phone = value.get("Phone").toString();
        }

        if(value.containsKey("Address")){
            address = value.get("Address").toString();
        }

        if(value.containsKey("waiting")){
            waiting = value.get("waiting").toString();
        }

        if(value.containsKey("rating")){
            rating = value.get("rating").toString();
        }

        if(value.containsKey("Latitude")){
            latitude = value.get("Latitude").toString();
        }

        if(value.containsKey("Longitude")){
            longitude = value.get("Longitude").toString();
        }

        if(value.containsKey("resPhoto")){
            resPhoto = value.get("resPhoto").toString();
        }

        if(MenuInfo!=null){
            menu=MenuInfo;
        }else{
            menu=null;
        }
        try{
            AllRestaurants.add(new Restaurants(
                    name,address,phone, waiting, rating,menu, latitude, longitude,Uid, resPhoto
            ));
        }catch (Exception e){}
    }


    public class MyAdapter extends ArrayAdapter<Restaurants> {

        int resource;
        Context context;

        public final static String EXTRA_MESSGE = "packageapplication.firebase";

        public MyAdapter(Context _context, int _resource, List<Restaurants> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LinearLayout newView;

            final Restaurants w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            //load restaurant images
            Picasso.with(context).load(w.resPhoto).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    newView.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.d("onBitmapFailed","onBitmapFailed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d("onPrepareLoad","onPrepareLoad");
                }
            });

            // Fills in the view.
            //获取饭店名字，评价，和等待时间的layout
            TextView rest_name = (TextView) newView.findViewById(R.id.rest_name);
            RatingBar rest_rating = (RatingBar) newView.findViewById(R.id.rest_rating);
            TextView rest_waiting = (TextView) newView.findViewById(R.id.rest_waiting);

            //添加饭店名字，评价，和等待时间的内容
            rest_name.setText(w.name);
            rest_rating.setRating(w.rating);
            rest_waiting.setText("waiting time: "+w.waiting);
            //--------------------------------------------------------------------------------------------------------------------------

            //将评价条设置为不可改变星级（不可触摸）
            rest_rating.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, detailOfRestaurant.class);
                    // message = restaurant.toString();
                    intent.putExtra(EXTRA_MESSGE, w);
                    startActivity(intent);
                }
            });

            return newView;
        }

    }

    public void filter(){
        aList.clear();
        for(Restaurants restaurant:AllRestaurants){
            if(target!=null && restaurant.getRestaurantName().indexOf(target)!=-1){
                aList.add(restaurant);
                aa.notifyDataSetChanged();
            }else if(target==null){
                aList.add(restaurant);
                aa.notifyDataSetChanged();
            }
        }
    }


    public void onClickSearch(){
        search_content = (EditText) findViewById(R.id.search_content);
        search_content.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if(arg1 == EditorInfo.IME_ACTION_SEARCH)
                {
                    search_content = (EditText) findViewById(R.id.search_content);

                    if( TextUtils.isEmpty(search_content.getText()) ){
                        target=null;
                    }else{
                        target=search_content.getText().toString();
                    }
                    getRestaurantData();
                }
                return false;
            }
        });
    }

    //显示搜索条
    public void menuSearchingOnclick(MenuItem item){
        search_bar = (LinearLayout) findViewById(R.id.search_bar);
        search_bar.setVisibility(View.VISIBLE);
    }

    /*
    //搜索方法
    public void seachButtonOnClick(View v){
        search_content = (EditText) findViewById(R.id.search_content);
        if( TextUtils.isEmpty(search_content.getText()) ){
            target=null;
        }else{
            target=search_content.getText().toString();
        }
        getRestaurantData();
    }

*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this,user_address_info.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            return true;
        }

        if (id == R.id.customer_log_out) {
            signOut();
            backToLoginPage();
        }
        if (id == R.id.ratingSort) {
            Comparator comp = new SortRating();
            Collections.sort(AllRestaurants,comp);
            aList.clear();
            for(Restaurants restaurant:AllRestaurants){
                aList.add(restaurant);
                aa.notifyDataSetChanged();
            }
        }
        if (id == R.id.waitingSort) {
            Comparator comp = new SortWaiting();
            Collections.sort(AllRestaurants,comp);
            aList.clear();
            for(Restaurants restaurant:AllRestaurants){
                aList.add(restaurant);
                aa.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //关闭搜索方法
    public void cancelButtonOnClick(View v){
        search_bar = (LinearLayout) findViewById(R.id.search_bar);
        search_bar.setVisibility(View.GONE);
        search_content = (EditText) findViewById(R.id.search_content);
        search_content.setText("");
        target=null;
        getRestaurantData();
    }

    public void sortRating(MenuItem item){
        Comparator comp = new SortRating();
        Collections.sort(AllRestaurants,comp);
        aList.clear();
        for(Restaurants restaurant:AllRestaurants){
                aList.add(restaurant);
                aa.notifyDataSetChanged();
        }
    }

    public void sortWaiting(MenuItem item){
        Comparator comp = new SortWaiting();
        Collections.sort(AllRestaurants,comp);
        aList.clear();
        for(Restaurants restaurant:AllRestaurants){
                aList.add(restaurant);
                aa.notifyDataSetChanged();
        }
    }


    /*
* @signOut()
* 登出当前用户
* */
    public void signOut(){
        try {
            firebase.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    * @backToLoginPage()
    * 跳转至用户登录界面
    * */
    public void backToLoginPage(){
        Intent intent = new Intent();
        intent.setClass(this, Login_Signup_Page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }



}
