package database;

import beans.ProductDesc;
import beans.ProductItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Provides comfortable interface to work with database. Contains database-related code.
 * Uses {@link database.DBConnection DBConnection} to get the database access.
 */
public class DBRequests {
    
    private static final String UPDATE_DESC = "update product_description set price = ? where ID_PRODUCT in (?)";
    private static final String UPDATE_ITEM = "update product_items set selling_date = ? where SID_PRODUCT = ?";
    
    private static DBConnection db = DBConnection.getInstance();
    
    public static ArrayList<ProductItem> selectItems() {
        ArrayList<ProductItem> result = new ArrayList<>();
        try {

            Connection cn = db.getConnection();
            
            ResultSet resultSet = cn.createStatement().executeQuery("select * from product_items");
            while(resultSet.next()) {
                ProductItem item = new ProductItem(resultSet.getInt(1), resultSet.getInt(2),
                        resultSet.getString(3), resultSet.getDate(4), resultSet.getDate(5));
                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ProductDesc> selectDescriptions() {
        ArrayList<ProductDesc> result = new ArrayList<>();
        try {

            Connection cn = db.getConnection();
            
            ResultSet resultSet = cn.createStatement().executeQuery("select * from product_description");
            while(resultSet.next()) {
                ProductDesc item = new ProductDesc(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getDouble(4));
                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static void updateProduct(ProductDesc product) {
        Connection cn = db.getConnection();
        try(PreparedStatement update = cn.prepareStatement(UPDATE_DESC)) {
            
            update.setDouble(1, product.getPrice());
            update.setInt(2, product.getIdProduct());
            
            update.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateItem(ProductItem item) {
        Connection cn = db.getConnection();
        try(PreparedStatement update = cn.prepareStatement(UPDATE_ITEM)) {

            update.setDate(1, item.getSellingDate());
            update.setInt(2, item.getSidProduct());

            update.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }  
    }
    
    public static void deleteItem(ProductItem item){
        try {
            
            Connection cn = db.getConnection();
            cn.createStatement().executeUpdate("delete from product_items where SID_PRODUCT=" + item.getSidProduct());
            
            ResultSet temp = cn.createStatement().executeQuery(
                    "select count(*) from product_items where ID_PRODUCT=" + item.getIdProduct());
            
            if(temp.next()){
                int cnt = temp.getInt(1);
                if(cnt == 0){
                    cn.createStatement().executeUpdate(
                            "delete from product_description where ID_PRODUCT=" + item.getIdProduct());
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updatePrices(double percentage) {
        percentage /= 100;
        try {
            Connection cn = db.getConnection();
            cn.createStatement().executeUpdate("update product_description set price=price*" + percentage);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
