/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import javax.swing.DefaultListModel;

public class User extends javax.swing.JFrame {
    
    private String loggedInUsername;
    private String currentUserType;
    
   
    public User() {
        initComponents();
        this.setLocationRelativeTo(null);

        PreparedStatement st;
        ResultSet rs;
 
    }
    
    public void setLogInUser(String role, String username) {
        this.currentUserType = role;
        this.loggedInUsername = username;
        jTextField6.setText(role); 
        jTextField2.setText(username);

        //set the visibility of each textfield based on the user's role
        if(currentUserType.equals("Student")){
            jTextField3.setVisible(true);
            jTextField3.setText("Parent(s)");
            jTextField4.setVisible(true);
            jTextField10.setVisible(true);
            jTextField10.setText("Friends");
            textArea2.setVisible(true);
            textArea1.setVisible(true);
            jTextField9.setVisible(true);
                    
        }else if(currentUserType.equals("Parent")){
            jTextField3.setVisible(true);
            jTextField3.setText("Children");
            jTextField4.setVisible(false);
            jTextField10.setVisible(true);
            jTextField10.setText("Past Booking Made");
            textArea2.setVisible(true);
            textArea1.setVisible(true);
            jTextField9.setVisible(false);
                    
        }else if(currentUserType.equals("Educator")){
            jTextField3.setVisible(true);
            jTextField3.setText("Number of Quizzes Created");
            jTextField4.setVisible(false);
            textArea1.setVisible(true);
            
            jTextField9.setVisible(false);
            jTextField10.setVisible(true);
            jTextField10.setText("Number of Events Created");
            textArea2.setVisible(true);
            
        }
        
      
        // Fetch user data from the database
        try {
            Connection con = Database.MyDataBase.establishConnection();
 
            PreparedStatement pstmt;
            PreparedStatement pstmt3=null, pstmt4=null;
            //PreparedStatement pstmt = con.prepareStatement("SELECT Password, Email, X, Y FROM user WHERE Username = ? AND Role = ?"); 
            
            // Modify the SQL query based on the user's role
        if (currentUserType.equals("Student")) {
            pstmt = con.prepareStatement("SELECT Password, Email, X, Y, Parent1, Parent2 FROM user WHERE Username = ? AND Role = ?");
        } else if (currentUserType.equals("Parent")) {
            pstmt = con.prepareStatement("SELECT Password, Email, X, Y, Child1, Child2, Child3, Child4, Child5, Child6, Child7, Child8, Child9, Child10 FROM user WHERE Username = ? AND Role = ?");
        } else {
            pstmt = con.prepareStatement("SELECT Password, Email, X, Y FROM user WHERE Username = ? AND Role = ?");
            pstmt3 = con.prepareStatement("SELECT COUNT(*) FROM event_create WHERE username = ?");
            pstmt4 = con.prepareStatement("SELECT COUNT(*) FROM quiz_create WHERE edu_name = ?");
        }
        
            pstmt.setString(1, loggedInUsername);
            pstmt.setString(2, currentUserType);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String email = rs.getString("Email");
                String x = rs.getString("X");
                String y = rs.getString("Y");
                
                // Update text fields with the fetched data
                jTextField1.setText(email);
                jTextField7.setText(x);
                jTextField8.setText(y);
                
            // Display parent or child names based on the user's role
            if (currentUserType.equals("Student")) {
                String parent1 = rs.getString("Parent1");
                String parent2 = rs.getString("Parent2");

                //StringBuilder parentsText = new StringBuilder();
                if (parent1 != null && parent2 != null) {
                    //parentsText.append(parent1).append("\n").append(parent2);
                    textArea1.setText(parent1+"\n"+parent2);
                } else if (parent1 != null && parent2 == null) {
                    textArea1.setText(parent1);
                } else {
                    textArea1.setText("No parents found");
                }
                
                // Fetch and display friends for the student
                String query = "SELECT Friends FROM student WHERE Username = ?";
        try (PreparedStatement pstmt2 = con.prepareStatement(query)) {
            pstmt2.setString(1, loggedInUsername);
            ResultSet rs2 = pstmt2.executeQuery();
            if (rs2.next()) {
                String friends = rs2.getString("Friends");
                if (friends != null && !friends.isEmpty()) {
                    String[] friendList = friends.split(","); // Split the friends string by comma and optional spaces
                    textArea2.setText("");
                    for (String friend : friendList) {
                        textArea2.append(friend.trim() + "\n");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching friends: " + e.getMessage());
        }
                
            } else if (currentUserType.equals("Parent")) {
                StringBuilder childrenNames = new StringBuilder();
                for (int i = 1; i <= 10; i++) {
                    String childName = rs.getString("Child" + i);
                    if (childName != null) {
                        childrenNames.append(childName).append("\n ");
                    }
                }
                if (childrenNames.length() > 0) {
                    // Remove the trailing comma and space
                    childrenNames.setLength(childrenNames.length() - 2);
                    textArea1.setText(childrenNames.toString());
                } else {
                    textArea1.setText("No children found");
                }
                
                try {
                pstmt = con.prepareStatement("SELECT child_id, destination, booking_date FROM bookings WHERE parent_id = ?");
                pstmt.setString(1, loggedInUsername);
                rs = pstmt.executeQuery();

                StringBuilder bookingsText = new StringBuilder();
                while (rs.next()) {
                    String childId = rs.getString("child_id");
                    String destination = rs.getString("destination");
                    String bookingDate = rs.getString("booking_date");

                    bookingsText.append("Child ID: ").append(childId)
                            .append(", Destination: ").append(destination)
                            .append(", Date: ").append(bookingDate)
                            .append("\n");
                }

                if (bookingsText.length() > 0) {
                    textArea2.setText(bookingsText.toString());
                } else {
                    textArea2.setText("No booking history found.");
                }
                
            } catch (Exception e) {
                System.out.println("Error fetching booking history: " + e.getMessage());
            }
                
            }else if (currentUserType.equals("Educator")) {
                
           if (pstmt3 != null && pstmt4 != null) {
                pstmt3.setString(1, loggedInUsername);
                pstmt4.setString(1, loggedInUsername);

                try (ResultSet rs2 = pstmt3.executeQuery(); ResultSet rs3 = pstmt4.executeQuery()) {
                    if (rs2.next()) {
                        int occ = rs2.getInt(1);
                        textArea1.setText(String.valueOf(occ));
                    } else {
                        textArea1.setText("0");
                    }

                    if (rs3.next()) {
                        int occ = rs3.getInt(1);
                        textArea2.setText(String.valueOf(occ));
                    } else {
                        textArea2.setText("0");
                    }
                }
            }
        
            
            }
            
            
            
            } else {
                System.out.println("User not found in database."+loggedInUsername+currentUserType);
                // Handle the case where the user is not found in the database
            }
            
            // Fetch and display points for the student
            if (currentUserType.equals("Student")) {
                pstmt = con.prepareStatement("SELECT points FROM leaderboard WHERE stu_name = ?");
                pstmt.setString(1, loggedInUsername);
                ResultSet pointsRs = pstmt.executeQuery();

                if (pointsRs.next()) {
                    int points = pointsRs.getInt("points");
                    jTextField9.setText(String.valueOf(points));
                } else {
                    jTextField9.setText("0");
                    System.out.println("Points not found for the student.");
                }
            }

            con.close(); // Close the connection
        }catch (Exception e) {
            System.out.println("Error fetching user data: " + e.getMessage());
            // Handle SQL exception
        }
     
    }
    
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        textArea1 = new java.awt.TextArea();
        jTextField10 = new javax.swing.JTextField();
        textArea2 = new java.awt.TextArea();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(215, 223, 241));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setText("Email");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel2.setText("Username");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel4.setText("Role");

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel5.setText("Location Coordinate");

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(215, 223, 241));
        jTextField3.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(215, 223, 241));
        jTextField4.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jTextField4.setText("Current Points");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel6.setText("User Account");

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField6.setEditable(false);
        jTextField6.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setEditable(false);
        jTextField7.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel7.setText("X");

        jTextField8.setEditable(false);
        jTextField8.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel8.setText("Y");

        jTextField9.setEditable(false);
        jTextField9.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        textArea1.setEditable(false);
        textArea1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N

        jTextField10.setEditable(false);
        jTextField10.setBackground(new java.awt.Color(215, 223, 241));
        jTextField10.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        textArea2.setEditable(false);
        textArea2.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(168, 168, 168)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel5)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField10)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(31, 31, 31)
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(328, 328, 328)
                        .addComponent(jLabel6)))
                .addContainerGap(337, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(textArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private java.awt.TextArea textArea1;
    private java.awt.TextArea textArea2;
    // End of variables declaration//GEN-END:variables
}
