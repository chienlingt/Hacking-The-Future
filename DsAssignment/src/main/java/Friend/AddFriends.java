package Friend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class AddFriends extends javax.swing.JFrame {

    Connection conn;
    DefaultListModel<String> friendModel;
    String selectedUsername;
    private String loggedInUsername;
    private String currentUserType;
    /**
     * Creates new form AddFriends
     */
    
    public AddFriends(){
        initComponents();
        this.setLocationRelativeTo(null); 
    }
    
    public AddFriends(String loggedInUsername, String currentUserType) {
        this.loggedInUsername = loggedInUsername;
        this.currentUserType = currentUserType;
        initComponents();
        this.setLocationRelativeTo(null); 
        friendModel = new DefaultListModel<>();
        jList1.setModel(friendModel);

        conn = Database.MyDataBase.establishConnection();
        populateProfileList();
        
        // Add event listener to jList1
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                // When an item is selected from the list
                if (!evt.getValueIsAdjusting()) {
                    selectedUsername = jList1.getSelectedValue();
                    // Display profile details for the selected user
                    displayProfileDetails(selectedUsername);
                }
            }
        });
    }
    
    private void displayProfileDetails(String username) {
        try {
            // Fetch profile details from the database
            String query = "SELECT username, email, `Parent1`, `Parent2`, X, Y, Friend_Requests FROM student WHERE username=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userEmail = rs.getString("Email");
                String parent1 = rs.getString("Parent1");
                String parent2 = rs.getString("Parent2");
                String X = rs.getString("X");
                String Y = rs.getString("Y");
                String friendRequests = rs.getString("Friend_Requests");

                // Construct the profile details message
                String profileDetailsMessage = "Username: " + username + "\n" +
                        "Email: " + userEmail + "\n" +
                        "Parent 1: " + parent1 + "\n" +
                        "Parent 2: " + parent2 + "\n" +
                        "X: " + X + "\n" +
                        "Y: " + Y;

                // Check if the selected user is already a friend or a friend request has been sent or received
                if (areFriends(loggedInUsername, username)) {
                    profileDetailsMessage += "\nYou are already friends with this user.";
                    // Display the profile details with a disabled button
                    JOptionPane.showMessageDialog(this, profileDetailsMessage, "Profile Details", JOptionPane.INFORMATION_MESSAGE);
                } else if (isRequestSent(loggedInUsername, username)) {
                    profileDetailsMessage += "\nFriend request has already been sent to this user.";
                    // Display the profile details with option to delete friend request
                    int option = JOptionPane.showOptionDialog(this, profileDetailsMessage,
                            "Profile Details", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                            new String[]{"Delete Friend Request", "Close"}, "Delete Friend Request");

                    if (option == JOptionPane.YES_OPTION) {
                        // Delete friend request
                        deleteFriendRequest(username);
                    }
                } else if (isRequestReceived(loggedInUsername, username)) {
                    profileDetailsMessage += "\nYou have already received a friend request from this user.";
                    // Display the profile details with a disabled button
                    JOptionPane.showMessageDialog(this, profileDetailsMessage, "Profile Details", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Display the profile details with option to send friend request
                    int option = JOptionPane.showOptionDialog(this, profileDetailsMessage,
                            "Profile Details", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                            new String[]{"Send Friend Request", "Close"}, "Send Friend Request");

                    if (option == JOptionPane.YES_OPTION) {
                        // Send friend request
                        sendFriendRequest(username);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    
    private void loadFriendRequests() {
        try {

            // Query to fetch friend requests for the current user
            String query = "SELECT Friend_Requests FROM student WHERE Username=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, loggedInUsername);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String friendRequests = rs.getString("friend_requests");
                if (friendRequests != null && !friendRequests.isEmpty()) {
                    // Split friend requests string and add them to the list model
                    String[] requests = friendRequests.split(",");
                    for (String request : requests) {
                        friendModel.addElement(request);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void populateProfileList() {
        try {
            // Fetch profiles from the database excluding the current user's username
            String query = "SELECT Username FROM student WHERE Username != ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, loggedInUsername);
            ResultSet rs = pstmt.executeQuery();

            // Clear the existing list model
            friendModel.clear();

            // Add fetched usernames to the list model
            while (rs.next()) {
                String username = rs.getString("Username");
                friendModel.addElement(username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to search for a username in the database
    private boolean searchUsername(String username) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
    
    // Method to check if the user is already friends with the searched username
    private boolean areFriends(String username, String friend) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=? AND Friends LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, "%" + friend + "%");
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    // Method to check if a friend request has already been sent to the searched username
    private boolean isRequestSent(String username, String friend) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=? AND sent_friends LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, "%" + friend + "%");
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
    
    private boolean isRequestReceived(String username, String friend) throws SQLException {
        String query = "SELECT * FROM student WHERE Username=? AND  Friend_Requests LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, "%" + friend + "%");
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    // Method to delete a friend request
    private void deleteFriendRequest(String friendUsername) throws SQLException {

        // Remove friend from friend_requests column
        String deleteFriendRequestsQuery = "UPDATE student SET Friend_Requests = REPLACE(Friend_Requests, ?, '') WHERE Username = ?";
        PreparedStatement deleteFriendRequestsStmt = conn.prepareStatement(deleteFriendRequestsQuery);
        deleteFriendRequestsStmt.setString(1, loggedInUsername + ",");
        deleteFriendRequestsStmt.setString(2, friendUsername);
        deleteFriendRequestsStmt.executeUpdate();

        // Remove friend from sent_friends column
        String deleteSentFriendsQuery = "UPDATE student SET sent_friends = REPLACE(sent_friends, ?, '') WHERE Username = ?";
        PreparedStatement deleteSentFriendsStmt = conn.prepareStatement(deleteSentFriendsQuery);
        deleteSentFriendsStmt.setString(1, friendUsername + ",");
        deleteSentFriendsStmt.setString(2, loggedInUsername);
        deleteSentFriendsStmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Friend request deleted successfully!");
    }



    // Method to send a friend request
    private void sendFriendRequest(String friendUsername) throws SQLException {

        // Insert into Sent_Friends table for the current user
        String sentFriendsQuery = "UPDATE student SET sent_friends = CONCAT(IFNULL(sent_friends, ''), ?) WHERE Username=?";
        PreparedStatement sentFriendsStmt = conn.prepareStatement(sentFriendsQuery);
        sentFriendsStmt.setString(1, friendUsername + ",");
        sentFriendsStmt.setString(2, loggedInUsername);
        sentFriendsStmt.executeUpdate();

        // Update Friend_Requests column of the friend
        String friendRequestQuery = "UPDATE student SET Friend_Requests = CONCAT(IFNULL(Friend_Requests, ''), ?) WHERE Username=?";
        PreparedStatement friendRequestStmt = conn.prepareStatement(friendRequestQuery);
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
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 204, 204));

        jTextField1.setFont(new java.awt.Font("Myanmar Text", 1, 10)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Myanmar Text", 0, 10)); // NOI18N
        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Myanmar Text", 0, 10)); // NOI18N
        jButton3.setText("Back");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        jLabel1.setText("Type the username here");

        jLabel2.setFont(new java.awt.Font("Myanmar Text", 1, 18)); // NOI18N
        jLabel2.setText("Meet new friends!");

        jList1.setFont(new java.awt.Font("Myanmar Text", 1, 12)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel3.setFont(new java.awt.Font("Myanmar Text", 1, 14)); // NOI18N
        jLabel3.setText("View Profile & Add Friends");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(jButton3)
                .addGap(48, 48, 48))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(140, 140, 140)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(16, 16, 16)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jButton1ActionPerformed(evt);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String friendUsername = jTextField1.getText().trim();

        try {
            if (friendUsername.equals(loggedInUsername)) {
                // If user tries to add themselves as a friend
                JOptionPane.showMessageDialog(this, "Error: You cannot add yourself as a friend.");
                return; // Exit the method
            }

            if (searchUsername(friendUsername)) {
                if (!areFriends(loggedInUsername, friendUsername)) {
                    if (!isRequestSent(loggedInUsername, friendUsername)) {
                        if (!isRequestReceived(loggedInUsername, friendUsername)) {
                            // If no request has been sent or received, show the option to send friend request
                            int option = JOptionPane.showOptionDialog(this, "You can send a friend request to this user.",
                                    "Send Friend Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                    new String[]{"Send Friend Request", "Cancel"}, "Send Friend Request");
                            if (option == JOptionPane.YES_OPTION) {
                                // Send friend request
                                sendFriendRequest(friendUsername);
                            }
                        } else {
                            // Inform the user that a friend request has been received
                            JOptionPane.showMessageDialog(this, "You have already received a friend request from this user. Check your friend requests.");
                        }
                    } else {
                        // If request has been sent, show option to delete friend request
                        int option = JOptionPane.showOptionDialog(this, "Friend request has already been sent to this user.",
                                "Delete Friend Request", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
                                new String[]{"Delete Friend Request", "Cancel"}, "Delete Friend Request");
                        if (option == JOptionPane.YES_OPTION) {
                            // Delete friend request
                            deleteFriendRequest(friendUsername);
                        }
                    }
                } else {
                    // If they are already friends
                    JOptionPane.showMessageDialog(this, "You are already friends with this user.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose(); // Close the current window
        new Friends(loggedInUsername, currentUserType).setVisible(true); // Open the Friends class
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(AddFriends.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddFriends.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddFriends.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddFriends.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddFriends().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
