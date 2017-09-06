package com.example.foodiemore.foodiemore.Merchant;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodiemore.foodiemore.Merchant.Notification_Order_Part.Order;
import com.example.harlan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by harlan on 2017/5/22.
 */

public class Merchant_Notification_Page extends AppCompatActivity {

    public MyAdapter aa;
    public ArrayList<Order> aList;

    private String waiting = "";
    private DatabaseReference firebaseURL;
    private String resUid;
    private String UserUid;
    private static DatabaseReference Orders;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_notification_page);

        //Listview part
        aList = new ArrayList<Order>();
        aa = new MyAdapter(this, R.layout.order_listitem, aList);
        ListView myListView = (ListView) findViewById(R.id.orders_listview);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    public void updateWaitingTime(View v){
        EditText waitingTime = (EditText) findViewById(R.id.WaitingTime);
        waiting = waitingTime.getText().toString().trim();

        firebaseURL = FirebaseDatabase.getInstance().getReference();

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        resUid = user.getUid(); // Get Uid

        firebaseURL.child("RestaurantData").child(resUid).child("resInfo").child("waiting").setValue(waiting);


    }

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


     public void onResume(){
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        UserUid = user.getUid(); // Get Uid

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        Orders = databaseReference.child("RestaurantData").
                child(UserUid).child("Order");

        Orders.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clean old data
                //清除之前的信息
                aList.clear();
                //get all orders
                //获取全部订单
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children){
                    String orderId = child.getKey();
                    HashMap OrderInformation = (HashMap) child.getValue();
                    Order newOrder = new Order(orderId, OrderInformation);
                    aList.add(newOrder);
                    aa.notifyDataSetChanged();
                }
                aa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class MyAdapter extends ArrayAdapter<Order> {
        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<Order> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final LinearLayout newView;

            final Order w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            //获取订单的layout
            final TextView order_id = (TextView) newView.findViewById(R.id.order_id);
            final Button view_order = (Button) newView.findViewById(R.id.view_order);
            final Button delete_order = (Button) newView.findViewById(R.id.delete_order);
            final TextView order_detail = (TextView) newView.findViewById(R.id.order_detail);

            // add value to order ID for TextView order_id in order_listitem.xml
            order_id.setText("Order NO."+(position+1));
            view_order.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    order_detail.setText(w.getDishes());
                }

            });

            delete_order.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
                    UserUid = user.getUid(); // Get Uid

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference();

                    Orders = databaseReference.child("RestaurantData").
                            child(UserUid).child("Order");

                    Orders.child(w.getOrderId()).removeValue();
                }

            });

            return newView;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

}
