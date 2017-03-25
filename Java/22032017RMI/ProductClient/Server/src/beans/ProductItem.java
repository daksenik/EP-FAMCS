package beans;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by user on 22.03.2017.
 */
public class ProductItem implements Serializable {
    int sidProduct;
    int idProduct;
    String barcode;
    Date receivingDate;
    Date sellingDate;
    
    public ProductItem(int sidProduct, int idProduct, String barcode, Date receivingDate, Date sellingDate){
        this.sidProduct = sidProduct;
        this.idProduct = idProduct;
        this.barcode = barcode;
        this.receivingDate = receivingDate;
        this.sellingDate = sellingDate;
    }


    public int getSidProduct() {
        return sidProduct;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public String getBarcode() {
        return barcode;
    }

    public Date getReceivingDate() {
        return receivingDate;
    }

    public Date getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(Date sellingDate) {
        this.sellingDate = sellingDate;
    }
}
