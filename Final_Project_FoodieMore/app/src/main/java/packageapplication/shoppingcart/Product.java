package packageapplication.shoppingcart;

import java.io.Serializable;

/**
 * Created by wentian on 5/18/2017.
 */

public class Product implements Serializable {
    String productName;
    double productValue;
    String dishSpecies;
    String url = "https://firebasestorage.googleapis.com/v0/b/foodiemore-a1f10.appspot.com/o/overview%2Fimages.png?alt=media&token=781ee2bc-8bf7-4128-8b8d-8fb9db73baa4";
    int number;

    public Product(String name, double value)
    {
        productName = name;
        productValue = value;
        dishSpecies = "All";
    }

    public Product(String name, double value, int num)
    {
        productName = name;
        productValue = value;
        number = num;
        dishSpecies = "All";
    }

    public Product(String name, double value, String dishSpecies, String url)
    {
        productName = name;
        productValue = value;
        this.dishSpecies = dishSpecies;
        if(url!=null) this.url=url;
    }

    String getName()
    {
        return productName;
    }

    double getValue()
    {
        return productValue;
    }

    String getSpecies(){ return dishSpecies==null? "ALl": dishSpecies ; }

    public String getUrl(){
        return url;
    }

    public String toString(){
        return "name:"+getName()+" price:"+getValue();
    }

}
