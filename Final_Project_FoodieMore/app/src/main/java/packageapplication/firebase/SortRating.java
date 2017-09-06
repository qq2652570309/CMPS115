package packageapplication.firebase;

import java.util.Comparator;

/**
 * Created by wentian on 5/21/2017.
 */

public class SortRating implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        Restaurants a = (Restaurants) lhs;
        Restaurants b = (Restaurants) rhs;
        return ((int)(b.getRestaurantRating()*10) - (int) (a.getRestaurantRating()*10));
    }
}