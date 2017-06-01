package tablemodels;

import beans.ProductItem;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Model class for product items table.
 * 
 * @see AbstractTableModel
 */
public class ProductTableModel extends AbstractTableModel{
    
    public ArrayList<ProductItem> items;
    
    public ProductTableModel(ArrayList<ProductItem> items) {
        super();
        this.items = items;
    }
    
    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductItem item = items.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return item.getSidProduct();
            case 1:
                return item.getIdProduct();
            case 2:
                return item.getBarcode();
            case 3:
                return item.getReceivingDate();
            case 4:
                return item.getSellingDate();
            default:
                return "";
        }
    }

    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0:
                return "SID_PRODUCT";
            case 1:
                return "ID_PRODUCT";
            case 2:
                return "BARCODE_NUMBER";
            case 3:
                return "RECEIVING_DATE";
            case 4:
                return "SELLING DATE";
            default:
                return "NONE";
        }
    }
}
