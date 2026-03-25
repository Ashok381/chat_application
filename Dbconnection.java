package chatapplication;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconnection {

    private static final String URL =
            "my_sql_jdbc_url";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_pass";

    public static Connection getConnection() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (Exception e ){
            System.out.println(e.getStackTrace());
        }
       return null ;
    }
}
