package database;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 25.05.2017.
 */
public class DBAccess {
    private static final String URL = "jdbc:mysql://localhost:3306/jspcw";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345";
    
    private static Connection connection;
    
    public DBAccess() {
    }
    
    private static void initConnection() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            FabricMySQLDriver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch(SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<HashMap<String, String>> getDeals() {
        if(connection == null) initConnection();
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        try{

            ResultSet rs = connection.createStatement().executeQuery("select * from deals");

            while(rs.next()) {
                HashMap<String, String> hs = new HashMap<>();
                hs.put("id", Integer.toString(rs.getInt("id")));
                hs.put("name", rs.getString("name"));
                hs.put("date", rs.getDate("dealdate").toString());
                hs.put("sum", Integer.toString(rs.getInt("sum")));
                hs.put("isComplete", rs.getString("isComplete"));

                result.add(hs);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<HashMap<String, String>> getProducts() {
        if(connection == null) initConnection();
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        try{

            ResultSet rs = connection.createStatement().executeQuery("select * from products");

            while(rs.next()) {
                HashMap<String, String> hs = new HashMap<>();
                hs.put("id", Integer.toString(rs.getInt("id")));
                hs.put("group", rs.getString("groupname"));
                hs.put("name", rs.getString("name"));
                hs.put("price", Integer.toString(rs.getInt("price")));
                hs.put("count", Integer.toString(rs.getInt("count")));
                hs.put("isInternal", rs.getString("isInternal"));

                result.add(hs);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<HashMap<String, String>> getStudents() {
        if(connection == null) initConnection();
        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        try{

            ResultSet rs = connection.createStatement().executeQuery("select * from students");

            while(rs.next()) {
                HashMap<String, String> hs = new HashMap<>();
                hs.put("id", Integer.toString(rs.getInt("id")));
                hs.put("name", rs.getString("name"));
                hs.put("year", Integer.toString(rs.getInt("year")));
                hs.put("group", Integer.toString(rs.getInt("groupname")));
                hs.put("avgmark", Float.toString(rs.getFloat("avgmark")));
                hs.put("isCountry", rs.getString("isCountry"));

                result.add(hs);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*public static void main(String[] args) {
        ArrayList<HashMap<String, String>> res = DBAccess.getDeals();
        for(HashMap<String, String> hm : res) {
            for(String k : hm.keySet()) System.out.println(k + " " + hm.get(k));
        }
    }*/
}
