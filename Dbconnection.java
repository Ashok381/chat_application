package chatapplication;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/chatdata";
    private static final String USER = "root";
    private static final String PASSWORD = "ashok7061@";

    public static Connection getConnection() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");    // used to load driver manager of JdbC
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (Exception e ){
            System.out.println(e.getStackTrace());
        }
       return null ;
    }
}
