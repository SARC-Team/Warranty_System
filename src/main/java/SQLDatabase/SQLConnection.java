package main.java.SQLDatabase;

import java.sql.Connection;
import java.sql.DriverManager;


public class SQLConnection {
    
    private static Connection connection;

    // mysql connection
    public static Connection createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://db4free.net:3306/sarcorganization?autoReconnect=true&useSSL=false", "sarcuser", "Sarc1234");
        } catch (Exception e) {
            return null;
        }
    
    }

}