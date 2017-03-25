package beans;

import java.io.Serializable;

/**
 * Created by user on 22.03.2017.
 */
public class ProductDesc implements Serializable{
    int idProduct;
    String category;
    String productName;
    double price;
    
    public ProductDesc(int idProduct, String category, String productName, double price){
        this.idProduct = idProduct;
        this.category = category;
        this.productName = productName;
        this.price = price;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public String getCategory() {
        return category;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
