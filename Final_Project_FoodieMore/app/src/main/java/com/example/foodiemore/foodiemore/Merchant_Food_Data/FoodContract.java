package com.example.foodiemore.foodiemore.Merchant_Food_Data;

import android.provider.BaseColumns;


public final class FoodContract {
    //To prevent from some accidentally instantiating the contract class
    //make the constructor private
    private FoodContract(){}

    /*Inner class that define the table contents */
    // 创建一个含有参数的class 包含餐厅食物的信息
    public static class FoodEntry implements BaseColumns {
        // 数据表格的title为food
        public static final String TABLE_NAME = "food";
        // ID为食物信息的id，会根据insert来自动增长
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FOOD_NAME = "name";
        public static final String COLUMN_FOOD_PRICE = "price";
        public static final String COLUMN_FOOD_DESCRIPTION = "description";
        public static final String COLUMN_FOOD_SPECIES = "species";
        public static final String COLUMN_FOOD_URL = "url";

        /*possible values for the style of the gender*/
        public static final int SPECIES_BREAKFAST = 0;              // 早餐
        public static final int SPECIES_LUNCH = 1;                  // 午餐
        public static final int SPECIES_DINNER = 2;                 // 晚餐
        public static final int SPECIES_DESSERT = 3;                // 甜点
        public static final int SPECIES_MAIN = 4;                   // 主食
    }
}
