package com.example.foodiemore.foodiemore.Merchant_Food_Data;

/**
 * Created by harlan on 2017/5/19.
 */

/*
* Class contained basic food information
* */
public class FoodInfo {

    public FoodInfo(){

    }
    private String foodName;
    private String foodDescription;
    private String foodPrice;
    private String foodSpecies;
    private String foodUrl;

    public void setName(String name){
        foodName = name;
    }

    public void setDescription(String description){
        foodDescription = description;
    }

    public void setPrice(String price){
        foodPrice = price;
    }

    public void setSpecies(String species){
        foodSpecies = species;
    }

    public void setFoodUrl(String foodUrl1){foodUrl = foodUrl1;}

    public String getName(){
        return foodName;
    }

    public String getDes(){
        return foodDescription;
    }

    public String getPrice(){
        return foodPrice;
    }

    public String getSpecies(){
        return foodSpecies;
    }

    public String getFoodUrl(){return foodUrl;}

}
