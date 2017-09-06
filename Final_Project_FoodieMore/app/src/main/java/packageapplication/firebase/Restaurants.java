package packageapplication.firebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import packageapplication.shoppingcart.Product;

/**
 * Created by wentian on 5/8/2017.
 */

public class Restaurants implements Serializable {

    public String name;
    public String address;
    public String phone;
    public String waiting;
    public float rating;
    public String latitude;
    public String longitude;
    public  ArrayList<Product> menu;
    public String RestUid;
    public String resPhoto;

    Restaurants() {};

    // Constructor构造器：设置饭店的名字，地址，电话，等待时间，和评价
    Restaurants(String name, String address, String phone, String waiting,
                String rating, HashMap menu, String latitude, String longitude, String RestUid, String resPhoto) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.waiting = waiting;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = Float.valueOf( rating );
        this.resPhoto = resPhoto;
        if(menu!=null)
            this.menu = productsArray(menu);
        else
            this.menu=null;
        this.RestUid = RestUid;
    }

    private  ArrayList<Product> productsArray(HashMap menu){
        Set<String> keys = menu.keySet();
        ArrayList<Product> newMenu = new ArrayList<>();
        for(String key : keys){
            HashMap productInfor = (HashMap) menu.get(key);
            Long price = (Long) productInfor.get("dishPrice");
            String species = (String) productInfor.get("dishSpecies");
            String dishUrl = (String) productInfor.get("dishUrl");
            try{
                newMenu.add(new Product(key, price.doubleValue(), species, dishUrl ));
            }catch (Exception e){
                throw new NullPointerException("refresh");
            }
        }
        return newMenu;
    }

    public String getRestaurantName(){
        return name==null? "Name no found": name;
    }

    public String getRestaurantAddress(){
        return address==null? "Address no found": address;
    }

    public String getRestaurantPhone(){
        return phone==null? "Phone no found": phone;
    }

    public String getRestaurantWaiting(){
        return waiting=="none"? "0": waiting;
    }

    public String getResPhoto() {return resPhoto; }

    public float getRestaurantRating(){
        return rating;
    }

    public String getRestaurantLatitude() {return latitude; }

    public String getRestaurantLongitude() { return longitude; }

    public boolean hasMenu(){
        if(menu==null)
            return false;
        else
            return true;
    }

    public ArrayList<Product> getMenu(){
        return menu;
    }

    public String toString(){
        return "name:"+name+", address:"+address+", phone:"+phone+", waiting:"+waiting+", rating:"+rating;
    }


}