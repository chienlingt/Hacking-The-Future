package Login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class Student extends javax.swing.JFrame {

    private String studentUsername;
    private String loggedInUsername;
    private String currentUserType;
    Connection con;
    
   
    public Student() {
        initComponents();
        this.setLocationRelativeTo(null);
        jTextArea1.setEditable(false);
        con = Database.MyDataBase.establishConnection(); // Establish database connection when the form is initialized
    }

    Student(String loggedInUsername) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setUser(String username, String loggedInUsername, String currentUserType) {
        this.studentUsername = username;
        this.loggedInUsername = loggedInUsername;
        this.currentUserType = currentUserType;
        jTextField2.setText(username);

        // Fetch user data from the database
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT Role, Email, X, Y, Parent1, Parent2 FROM user WHERE Username = ?");
            pstmt.setString(1, studentUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("Role");
                String email = rs.getString("Email");
                String x = rs.getString("X");
                String y = rs.getString("Y");
                
                
                if ("student".equalsIgnoreCase(currentUserType)) {
                    addf.setVisible(true);
                } else {
                    addf.setVisible(false);
                }
                
                if (studentUsername.equals(loggedInUsername)) {//himself
                    addf.setVisible(false);
                }

                String parent1 = rs.getString("Parent1");
                String parent2 = rs.getString("Parent2");
                    
                if (parent1 != null && parent2 != null) {
                    //parentsText.append(parent1).append("\n").append(parent2);
                    jTextArea1.setText(parent1+"\n"+parent2);
                }else if (parent1 != null && parent2 == null) {
                    jTextArea1.setText(parent1);
                }else{
                    jTextArea1.setText("No parents found");
                }

                // Update text fields with the fetched data
                jTextField6.setText(role);
                jTextField1.setText(email);
                jTextField7.setText(x);
                jTextField8.setText(y);
                
                displayFriends();
            } else {
                System.out.println("User not found in database: " + studentUsername);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user data: " + e.getMessage());
        }
        
        fetchCurrentPoints();
    }
    
    public void fetchCurrentPoints() {
    try {
        PreparedStatement pstmt = con.prepareStatement("SELECT points FROM leaderboard WHERE stu_name = ?");
        pstmt.setString(1, studentUsername);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            // Set the poins field with the user's points fetch from leaderboard table
            int points = rs.getInt("points");
            jTextField9.setText(String.valueOf(points)); 
        } else {
            // Set default value 0 if no points is found
            jTextField9.setText("0"); 
            System.out.println("No points found for user: " + studentUsername);
        }
    } catch (SQLException e) {
        // Set default value 0 if an error occurs
        jTextField9.setText("0"); 
        System.out.println("Error fetching current points: " + e.getMessage());
    }
}

    
    private void displayFriends() {
        DefaultListModel<String> model = new DefaultListModel<>();
        String query = "SELECT Friends FROM student WHERE Username = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, studentUsername);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String friends = rs.getString("Friends");
                if (friends != null && !friends.isEmpty()) {
                    String[] friendList = friends.split(","); // Split the friends string by comma and optional spaces
                    for (String friend : friendList) {
                        model.addElement(friend);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching friends: " + e.getMessage());
        }
        jList1.setModel(model);
    }
    
    // Method to check if the user is already friends with the searched username
    private boolean areFriends(String username, String friend) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=? AND Friends LIKE ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, "%" + friend + "%");
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    // Method to check if a friend request has already been sent to the searched username
    private boolean isRequestSent(String username, String friend) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=? AND sent_friends LIKE ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, "%" + friend + "%");
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
    
    private boolean isRequestReceived(String username, String friend) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=? AND  Friend_Requests LIKE ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, "%" + friend + "%");
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    // Method to delete a friend request
    private void deleteFriendRequest(String friendUsername) throws SQLException {

        // Remove friend from friend_requests column
        String deleteFriendRequestsQuery = "UPDATE student SET Friend_Requests = REPLACE(Friend_Requests, ?, '') WHERE Username = ?";
        PreparedStatement deleteFriendRequestsStmt = con.prepareStatement(deleteFriendRequestsQuery);
        deleteFriendRequestsStmt.setString(1, loggedInUsername + ",");
        deleteFriendRequestsStmt.setString(2, friendUsername);
        deleteFriendRequestsStmt.executeUpdate();

        // Remove friend from sent_friends column
        String deleteSentFriendsQuery = "UPDATE student SET sent_friends = REPLACE(sent_friends, ?, '') WHERE Username = ?";
        PreparedStatement deleteSentFriendsStmt = con.prepareStatement(deleteSentFriendsQuery);
        deleteSentFriendsStmt.setString(1, friendUsername + ",");
        deleteSentFriendsStmt.setString(2, loggedInUsername);
        deleteSentFriendsStmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Friend request deleted successfully!");
    }



    // Method to send a friend request
    private void sendFriendRequest(String friendUsername) throws SQLException {

        // Insert into Sent_Friends table for the current user
        String sentFriendsQuery = "UPDATE student SET sent_friends = CONCAT(IFNULL(sent_friends, ''), ?) WHERE Username=?";
        PreparedStatement sentFriendsStmt = con.prepareStatement(sentFriendsQuery);
        sentFriendsStmt.setString(1, friendUsername + ",");
        sentFriendsStmt.setString(2, loggedInUsername);
        sentFriendsStmt.executeUpdate();

        // Update Friend_Requests column of the friend
        String friendRequestQuery = "UPDATE student SET Friend_Requests = CONCAT(IFNULL(Friend_Requests, ''), ?) WHERE Username=?";
        PreparedStatement friendRequestStmt = con.prepareStatement(friendRequestQuery);
        friendRequestStmt.setString(1, loggedInUsername + ",");
        friendRequestStmt.setString(2, friendUsername);
        friendRequestStmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Friend request sent successfully!");
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
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        addf = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

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

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        jLabel6.setText("View Profile");

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

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel9.setText("Friends");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel10.setText("Current Points");

        addf.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        addf.setText("Add Friends");
        addf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addfActionPerformed(evt);
            }
        });

        jList1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel11.setText("Parent(s)");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(417, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(416, 416, 416))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(168, 168, 168)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(jTextField1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(113, 113, 113)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addf)))))
                .addGap(194, 194, 194))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel6)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel10))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(addf, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel11))
                .addContainerGap(62, Short.MAX_VALUE))
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

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed

    }//GEN-LAST:event_jTextField9ActionPerformed

    private void addfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addfActionPerformed
        // Check if the selected user is already a friend or a friend request has been sent or received
        try {
            if (areFriends(loggedInUsername, studentUsername)) {
                JOptionPane.showMessageDialog(this, "You are already friends with this user.", "Profile Details", JOptionPane.INFORMATION_MESSAGE);
            } else if (isRequestSent(loggedInUsername, studentUsername)) {
                int option = JOptionPane.showOptionDialog(this, "Friend request has already been sent to this user.",
                        "Profile Details", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        new String[]{"Delete Friend Request", "Close"}, "Delete Friend Request");

                if (option == JOptionPane.YES_OPTION) {
                    // Delete friend request
                    deleteFriendRequest(studentUsername);
                }
            } else if (isRequestReceived(loggedInUsername, studentUsername)) {
                JOptionPane.showMessageDialog(this, "You have already received a friend request from this user.", "Profile Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int option = JOptionPane.showOptionDialog(this, "Send Friend Request to " + studentUsername + "?",
                        "Profile Details", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        new String[]{"Send Friend Request", "Close"}, "Send Friend Request");

                if (option == JOptionPane.YES_OPTION) {
                    // Send friend request
                    sendFriendRequest(studentUsername);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking friend status: " + e.getMessage());
        }
    }//GEN-LAST:event_addfActionPerformed

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
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Student.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Student().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
