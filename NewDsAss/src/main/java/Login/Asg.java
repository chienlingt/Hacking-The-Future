package Login;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Asg {

    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/dsass",
                "root", 
                "host@123");
            System.out.println("Success");
           
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Asg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Asg.class.getName()).log(Level.SEVERE, null, ex);
        }   
    } 
}
