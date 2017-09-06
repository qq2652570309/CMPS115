package packageapplication.shoppingcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.harlan.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import packageapplication.firebase.Restaurants;

import static packageapplication.shoppingcart.detailOfRestaurant.m_cart;

public class detailOfShoppingCart extends AppCompatActivity {

    private ArrayList<Product> listOfSelectedProduct;
    final String EXTRA_MESSAGE = "Shopping cart";
    PayPalConfiguration m_configuration;
    String m_paypalClientId = "ASMLdW5-vuzySuqUji1t_E1K1FYkleGETLHiSvAG8AKRvPUg4lypGdtA4lPXUyLlWQ7kXnE9gELZWJVj";
    Intent m_service;
    int m_paypalRequestCode = 999; // or any number you want
    TextView m_response;
    String UserUid;

    Restaurants restaurant;
    public final static String Cart_MESSGE="packageapplication.firebase.detailOfShoppingCart";

    private class shoppingCartAdapter extends ArrayAdapter<Product>{
        int resource;
        Context context;

        public shoppingCartAdapter(Context _context, int _resource, List<Product> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            Product w = getItem(position);

            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource,  newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            TextView productName = (TextView) newView.findViewById(R.id.NameOfProduct);
            TextView productNumber = (TextView) newView.findViewById(R.id.NumberOfProduct);
            TextView productPrice = (TextView) newView.findViewById(R.id.priceOfProduct);

            productName.setText(w.productName);
            productNumber.setText(w.number+"");
            productPrice.setText(w.productValue+"");

            return newView;
        }
    }

    private shoppingCartAdapter getShoppingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_shopping_cart);
        //setContentView(R.layout.test);

        m_response = (TextView) findViewById(R.id.totalPrice);

        m_configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // sandbox for test, production for real
                .clientId(m_paypalClientId);

        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration); // configuration above
        startService(m_service); // paypal service, listening to calls to paypal app



        //set a new ArrayList to Store the product
        listOfSelectedProduct = new ArrayList<Product>();

        //get the new Adapter to store the value
        getShoppingList = new shoppingCartAdapter(this, R.layout.product_detail, listOfSelectedProduct);
        ListView shoppingCart = (ListView) findViewById(R.id.cart);
        shoppingCart.setAdapter(getShoppingList);
        getShoppingList.notifyDataSetChanged();

        Cart cart = m_cart;
        m_response.setText( "$ "+String.format("%.2f", cart.getValue()));

        Set<Product> products = cart.getProducts();

        Iterator iterator = products.iterator();
        while(iterator.hasNext())
        {
            Product product = (Product) iterator.next();

            listOfSelectedProduct.add(new Product(
                    product.getName(), product.getValue(), m_cart.getQuantity(product)
            ));
            getShoppingList.notifyDataSetChanged();
        }

        Intent intent = getIntent();
        restaurant = (Restaurants) intent.getSerializableExtra(detailOfRestaurant.REST_MESSGE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();        // 获取当前用户
        UserUid = user.getUid(); // Get Uid
        //Log.d("test Uid" , "The RestUid is: " + restaurant.RestUid);
    }


    public void pay(View view)
    {
        PayPalPayment cart = new PayPalPayment(new BigDecimal(m_cart.getValue()), "USD", "Cart",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class); // it's not paypalpayment, it's paymentactivity !
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, cart);

        startActivityForResult(intent, m_paypalRequestCode);

    }

    //after finish order we send an order to the corresponding seller root.
    public void sendOrder(Cart cart){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        Set set = cart.m_cart.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            Product temp = (Product) mentry.getKey();
            //Log.d("key is ", "key " +  temp.getName());
            //Log.d("quantity is ", "quantity" + m_cart.getQuantity(temp));
            databaseReference.child("RestaurantData").
                    child(restaurant.RestUid).child("Order").child(UserUid).child(temp.getName()).setValue(m_cart.getQuantity(temp));
        }
    }

    // upload all products in shopping cart to firebase
    // 上传所有在购物车里面的东西去数据库

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == m_paypalRequestCode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                // we have to confirm that the payment worked to avoid fraud
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if(confirmation != null)
                {
                    String state = confirmation.getProofOfPayment().getState();

                    if(state.equals("approved")) {// if the payment worked, the state equals approved
                        m_response.setText("payment approved");
                        sendOrder(m_cart);
                    }else
                        m_response.setText("error in the payment");
                }
                else
                    m_response.setText("confirmation is null");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shoppingcat_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Check the action id
        if(id == R.id.shopping_return){
            Intent intent = new Intent(detailOfShoppingCart.this, detailOfRestaurant.class);
            intent.putExtra(Cart_MESSGE, restaurant);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            //NavUtils.navigateUpFromSameTask(this);
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
