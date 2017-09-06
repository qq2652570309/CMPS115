package packageapplication.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.harlan.myapplication.R;

import java.util.List;


/**
 * Created by wentian on 5/8/2017.
 */

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
            vi.inflate(resource,  newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        // Fills in the view.
        //获取饭店名字，评价，和等待时间的layout
        TextView rest_name = (TextView) newView.findViewById(R.id.rest_name);
        RatingBar rest_rating = (RatingBar) newView.findViewById(R.id.rest_rating);
        TextView rest_waiting = (TextView) newView.findViewById(R.id.rest_waiting);

        //添加饭店名字，评价，和等待时间的内容
        rest_name.setText(w.name);
        rest_rating.setRating(w.rating);
        rest_waiting.setText(w.waiting);

        //将评价条设置为不可改变星级（不可触摸）
        rest_rating.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                return true;
            }
        });

        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return newView;
    }

}
