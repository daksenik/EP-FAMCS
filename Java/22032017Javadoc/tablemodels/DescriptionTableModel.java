package tablemodels;

import beans.ProductDesc;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Model class for product descriptions table.
 * 
 * @see AbstractTableModel
 */
public class DescriptionTableModel extends AbstractTableModel {

    public ArrayList<ProductDesc> items;

    public DescriptionTableModel(ArrayList<ProductDesc> items) {
        super();
        this.items = items;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductDesc item = items.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return item.getIdProduct();
            case 1:
                return item.getCategory();
            case 2:
                return item.getProductName();
            case 3:
                return item.getPrice();
            default:
                return "";
        }
    }

    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0:
                return "ID_PRODUCT";
            case 1:
                return "CATEGORY_NAME";
            case 2:
                return "PRODUCT_NAME";
            case 3:
                return "PRICE";
            default:
                return "NONE";
        }
    }
}
