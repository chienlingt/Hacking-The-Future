/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class ParentChildTxtGenerator {
    public static void main(String[] args) {
       
        // Define variables for database connection and resources
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FileWriter writer = null;
        
        // Set to store unique pairs
        Set<String> uniquePairs = new HashSet<>();
        
        try {
            // Connect to the database
            con = Database.MyDataBase.establishConnection();

            // Prepare a SQL statement based on the user's role
            pstmt = con.prepareStatement("SELECT Username, Role, Parent1, Parent2, Child1, Child2, Child3, Child4, Child5, Child6, Child7, Child8, Child9, Child10 FROM user");
            
            // Execute the query
            rs = pstmt.executeQuery();
            
            // Create a FileWriter to write to the text file
            writer = new FileWriter("ParentChild.txt");

            // Iterate over the result set
            while (rs.next()) {
                String role = rs.getString("Role");
                String username = rs.getString("Username");

                // Construct output based on the role
                if (role.equals("Student")) {
                    String parent1 = rs.getString("Parent1");
                    String parent2 = rs.getString("Parent2");
                    
                    if (parent1 != null) {
                        String pair1 = parent1 + ", " + username;
                        if (uniquePairs.add(pair1)) { // Add the pair to the set only if it's not already present
                            writer.write(pair1 + "\n");
                        }
                    }
                    if (parent2 != null) {
                        String pair2 = parent2 + ", " + username;
                        if (uniquePairs.add(pair2)) { // Add the pair to the set only if it's not already present
                            writer.write(pair2 + "\n");
                        }
                    }
                } else if (role.equals("Parent")) {
                    for (int i = 1; i <= 10; i++) {
                        String child = rs.getString("Child" + i);
                        if (child != null) {
                            String pair = username + ", " + child;
                            if (uniquePairs.add(pair)) { // Add the pair to the set only if it's not already present
                                writer.write(pair + "\n");
                            }
                        }
                    }
                }
            }
            
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Close resources in a finally block to ensure they are always closed
            try {
                if (writer != null) {
                    writer.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
