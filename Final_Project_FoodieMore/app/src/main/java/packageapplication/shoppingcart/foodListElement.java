package packageapplication.shoppingcart;

public class foodListElement {
    foodListElement() {};

    foodListElement(String food_name1, String url1, double value1) {
        food_name = food_name1;
        url = url1;
        food_value = "$"+value1;
        product = new Product(food_name, value1);
    }

    public String food_name;
    public String url;
    public String food_value;
    Product product;
}
