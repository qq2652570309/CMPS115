package com.example.foodiemore.foodiemore.Merchant.Notification_Order_Part;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by wentian on 6/5/2017.
 */

public class Order implements Serializable {
    private String ID;
    private HashMap dishes;
    private int maxlength;

    public Order(){}
    public Order(String orderID, HashMap newDishes){
        ID = orderID;
        dishes = newDishes;
        maxlength=0;
    }

    public String getOrderId(){ return ID; }

    public String getDishes(){
        Set set = dishes.entrySet();
        Iterator iterator = set.iterator();
        String returnValue="";
        printSetting();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            String dishName = mentry.getKey().toString();
            String dishNumber = mentry.getValue().toString();
            returnValue += dishName;
            for(int i=0; (i+dishName.length())<(maxlength+3); i++){
                returnValue+=" ";
            }
            returnValue += dishNumber+"\n";
        }
        return returnValue;
    }

    // 调整dishes的输出排版，使其更规整
    // Adjust the output of the dishes layout to make it more regular
    private void printSetting(){
        Set set = dishes.entrySet();
        Iterator iterator = set.iterator();
        String returnValue="";
        //找到最长名字的订单
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            String dishName = mentry.getKey().toString();
            if(dishName.length()>maxlength)
                maxlength = dishName.length();
        }

    }

}
