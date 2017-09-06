package packageapplication.firebase;

import java.util.Comparator;

/**
 * Created by wentian on 5/21/2017.
 */

public class SortWaiting implements Comparator {
    public int compare(Object lhs, Object rhs) {
        Restaurants a = (Restaurants) lhs;
        Restaurants b = (Restaurants) rhs;
//        int ra = Integer.valueOf(a.getRestaurantWaiting());
//        int rb = Integer.valueOf(b.getRestaurantWaiting());
        return ((int)(Integer.valueOf(a.getRestaurantWaiting()) - Integer.valueOf(b.getRestaurantWaiting())));
    }
}
