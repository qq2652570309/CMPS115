package packageapplication.shoppingcart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harlan.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class foodAdapterList extends ArrayAdapter<foodListElement> {


    int resource;
    Context context;



    public foodAdapterList(Context _context, int _resource, List<foodListElement> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout newView;


        foodListElement w = getItem(position);


        // Inflate a new view if necessary.
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            LayoutInflater vi = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(resource,  newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        final Cart cur_car = detailOfRestaurant.m_cart ;


        //-------------new-items-in-ListView--------------
        TextView food_name1 = (TextView)newView.findViewById( R.id.food_name);
        food_name1.setText(w.food_name);

        //-------------
        ImageView food_image = (ImageView)newView.findViewById(R.id.food_image);
        Picasso.with(context)
                .load(w.url)
                .into(food_image);

        final TextView food_price = (TextView)newView.findViewById(R.id.food_price);
        food_price.setText(w.food_value);

        final Button food_button = (Button)newView.findViewById(R.id.addFoodButton);
        //set button
        food_button.setTag(w.product);
        food_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Product product = (Product) button.getTag();
                cur_car.addToCart(product);
                Toast.makeText(context,"item added",Toast.LENGTH_SHORT).show();
            }
        });


        return newView;
    }

}
