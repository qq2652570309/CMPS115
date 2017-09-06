package com.example.foodiemore.foodiemore.sign_up_slides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.foodiemore.foodiemore.Sign_up.Signup_consumer_email;
import com.example.foodiemore.foodiemore.Sign_up.Signup_merchant_email;
import com.example.harlan.myapplication.R;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.view.ViewPager;

/**
 * Created by harlan on 2017/6/6.
 */

public class slidesMain extends Activity {

    private static final String TAG = "test";
    private ViewPager viewpager = null;
    private List<View> list = null;
    private ImageView[] img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.slides_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        list = new ArrayList<View>();
        list.add(getLayoutInflater().inflate(R.layout.slides_merchant, null));
        list.add(getLayoutInflater().inflate(R.layout.slides_customer, null));

        img = new ImageView[list.size()];
        LinearLayout layout = (LinearLayout) findViewById(R.id.viewGroup);
        img[0] = new ImageView(slidesMain.this);
        img[0].setBackgroundResource(R.drawable.dot_50);
        //img[i].setPadding(0, 0, 20, 0);
        img[0].setPadding(0, 0, 0, 20);
        layout.addView(img[0]);
        viewpager.setAdapter(new ViewPagerAdapter(list));
        viewpager.setOnPageChangeListener(new slidesAdapter());
    }

    public void onCustomerOnclick(View v){
        Intent intent = new Intent();
        intent.setClass(this, Signup_consumer_email.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void onMerchantOnClick(View v){
        Intent intent = new Intent();
        intent.setClass(this, Signup_merchant_email.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    class ViewPagerAdapter extends PagerAdapter {

        private List<View> list = null;

        public ViewPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}