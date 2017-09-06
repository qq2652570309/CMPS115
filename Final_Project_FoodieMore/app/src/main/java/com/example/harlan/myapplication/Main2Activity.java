package com.example.harlan.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodiemore.foodiemore.Login_Signup_Page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import packageapplication.firebase.Restaurants;
import packageapplication.firebase.RestaurantsList;

//public class Main2Activity extends AppCompatActivity
public class Main2Activity extends RestaurantsList {
    private TextView userName;
    private FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firebase = FirebaseAuth.getInstance();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        */
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        /*
        * 将用户名显示在侧滑菜单中
        * */
        // 对navigationView进行赋值
        //TextView setUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.side_bar_username);
        /*
        userName = (TextView) findViewById(R.id.main2_username);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        if (user != null) {
            //setUser.setText(user.getEmail().toString());                     // 得到用户邮箱值 并显示在navigationHeader中
        } else {
            //setUser.setText("Can't find username");
        }
        //getUser();
        */


        //白文天写的RestaurantsList.java 里面的oncreate方法
        aList = new ArrayList<Restaurants>();
        aa = new MyAdapter(this, R.layout.my_listitem, aList);
        ListView myListView = (ListView) findViewById(R.id.rest_listview);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    //白文天写的RestaurantsList.java 里面的onResume方法
    public void onResume(){
        super.onResume();
        getRestaurantData();
    }


    public void getUser(){
        TextView setUser = (TextView) findViewById(R.id.side_bar_username);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        if (user != null) {
            setUser.setText(user.getEmail().toString());                     // 得到用户邮箱值
        } else {
            setUser.setText("Can't find username");
        }
    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    */

    /*
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
            return true;
        }
        if (id == R.id.customer_log_out) {
            signOut();
            backToLoginPage();
        }
        return super.onOptionsItemSelected(item);
    }
    */

    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.user_address){
            Intent intent = new Intent();
            intent.setClass(this,user_address_info.class);
            startActivity(intent);
        } else if (id == R.id.sign_out){
            // 登出当前用户
            // 调转到登陆界面
            signOut();
            backToLoginPage();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    */
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
