package database;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to establish connectivity between server and database. This is the final layer before database straight access.
 * Uses {@link Connection Connection} for connecting and  sending requests.
 */
public class DBConnection {
    private static DBConnection instance;

    private static final String URL = "jdbc:mysql://localhost:3306/products";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345";
    
    private Connection conn;
    
    DBConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            FabricMySQLDriver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch(SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection(){
        return conn;
    }
    
    public static DBConnection getInstance() {
        if(instance == null){
            instance = new DBConnection();
        }
        return instance;
    }
}
