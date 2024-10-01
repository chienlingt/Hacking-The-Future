/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Login;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextField;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class signupforparent extends javax.swing.JFrame {
    
    private String[] childNames = new String[20];
    private int currentIndex = 0; // To keep track of the current index in the array
    
    public signupforparent() {
        initComponents();
        this.setLocationRelativeTo(null); //set the frame to the middle of the screen
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label2 = new java.awt.Label();
        label1 = new java.awt.Label();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        label5 = new java.awt.Label();
        label6 = new java.awt.Label();
        Name = new javax.swing.JTextField();
        Username = new javax.swing.JTextField();
        X = new javax.swing.JTextField();
        Y = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        confirmpassword = new javax.swing.JPasswordField();
        label8 = new java.awt.Label();
        label9 = new java.awt.Label();
        show1 = new javax.swing.JCheckBox();
        show2 = new javax.swing.JCheckBox();
        button1 = new java.awt.Button();
        label10 = new java.awt.Label();
        Email = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        label2.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        label2.setText("Register as : Parent");

        label1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        label1.setText("Full Name");

        label3.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        label3.setText("Username");

        label4.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        label4.setText("Password");

        label5.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        label5.setText("Confirm Password");

        label6.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        label6.setText("Coordinate");

        Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameActionPerformed(evt);
            }
        });

        Username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameActionPerformed(evt);
            }
        });

        X.setEditable(false);
        X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XActionPerformed(evt);
            }
        });

        Y.setEditable(false);
        Y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YActionPerformed(evt);
            }
        });

        Password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordActionPerformed(evt);
            }
        });

        label8.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        label8.setText("X");

        label9.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        label9.setText("Y");

        show1.setLabel("show password");
        show1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                show1ActionPerformed(evt);
            }
        });

        show2.setLabel("show password");
        show2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                show2ActionPerformed(evt);
            }
        });

        button1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        button1.setLabel("Register");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        label10.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        label10.setText("Email");

        Email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(show1)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(X, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Y, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(confirmpassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(show2, javax.swing.GroupLayout.Alignment.TRAILING))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(show1)))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(confirmpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(show2))
                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(X, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Y, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        label2.getAccessibleContext().setAccessibleName("Register as : Student");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameActionPerformed

    private void UsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsernameActionPerformed

    private void XActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XActionPerformed
        
    }//GEN-LAST:event_XActionPerformed

    private void YActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YActionPerformed
        
    }//GEN-LAST:event_YActionPerformed

    private void PasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordActionPerformed
         
    }//GEN-LAST:event_PasswordActionPerformed

    private void show1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show1ActionPerformed
        if (show1.isSelected()) {
                Password.setEchoChar((char) 0); // Show the password
            } else {
                Password.setEchoChar('*'); // Hide the password
        }
    }//GEN-LAST:event_show1ActionPerformed

    private void show2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_show2ActionPerformed
        if (show2.isSelected()) {
                confirmpassword.setEchoChar((char) 0); // Show the password
            } else {
                confirmpassword.setEchoChar('*'); // Hide the password
        }
    }//GEN-LAST:event_show2ActionPerformed

    private void EmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        String role,fullName, email, username, password, confirmp, query, x, y;
        
        Random r = new Random();
        double randomX = r.nextInt(1001)-500.0;
        X.setText(String.valueOf(randomX));

        double randomY = r.nextInt(1001)-500.0;
        Y.setText(String.valueOf(randomY));
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = Database.MyDataBase.establishConnection();
            Statement st = con.createStatement();
            
           if("".equals(Name.getText())){
                JOptionPane.showMessageDialog(new JFrame(), "Full Name is require", "Error", JOptionPane.ERROR_MESSAGE);
            }else if("".equals(Email.getText())){
                JOptionPane.showMessageDialog(new JFrame(), "Email is require", "Error", JOptionPane.ERROR_MESSAGE);
            }else if("".equals(Username.getText())){
                JOptionPane.showMessageDialog(new JFrame(), "Username is require", "Error", JOptionPane.ERROR_MESSAGE);
            }else if("".equals(Password.getText())){
                JOptionPane.showMessageDialog(new JFrame(), "Password is require", "Error", JOptionPane.ERROR_MESSAGE);
            }else if("".equals(confirmpassword.getText())){
                JOptionPane.showMessageDialog(new JFrame(), "Confirm Password is required", "Error", JOptionPane.ERROR_MESSAGE);
            }else if(!confirmpassword.getText().equals(Password.getText())){
                JOptionPane.showMessageDialog(new JFrame(), "Please confirm Password", "Error", JOptionPane.ERROR_MESSAGE);
            }else if(usernameAlreadyExists(Username.getText(), "Parent")){
                JOptionPane.showMessageDialog(new JFrame(), "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            }else if(!isValidEmail(Email.getText())){
                JOptionPane.showMessageDialog(new JFrame(), "Please eneter a valid Email", "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                
                fullName = Name.getText();
                email = Email.getText();
                username = Username.getText();
                password = encrypt(Password.getText());
                confirmp = confirmpassword.getText();
                x = X.getText();
                y = Y.getText();
                
                //Parent is required to fill in thier child's details
                EnterChildren c = new EnterChildren(this);
                c.setUser(username);
                
                EnterParent p = new EnterParent(this);
                p.setparentacc(username);
                
                query = "INSERT INTO user(Role, Name, Email, Username, Password, Confirm, X, Y) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, "Parent");
                pstmt.setString(2, fullName);
                pstmt.setString(3, email);
                pstmt.setString(4, username);
                pstmt.setString(5, password);
                pstmt.setString(6, confirmp);
                pstmt.setString(7, x);
                pstmt.setString(8, y);
                pstmt.executeUpdate();
               
                Name.setText("");
                Email.setText("");
                Username.setText("");
                Password.setText("");
                confirmpassword.setText("");
                X.setText("");
                Y.setText("");
                
                
                c.setVisible(true);
                dispose();
            }
        }catch(Exception e){
            System.out.println("Error!"+e.getMessage());
        }
        
    }//GEN-LAST:event_button1ActionPerformed

    //method to check if the username already exists in database
    private boolean usernameAlreadyExists(String username, String role) {

        String query = "SELECT Username FROM user WHERE Username = ? AND Role = ?";
    
        try (Connection con = Database.MyDataBase.establishConnection();
            PreparedStatement pstmt = con.prepareStatement(query)) {
        
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            ResultSet rs = pstmt.executeQuery();
        
            return rs.next(); // Return true if the username exists in the database
        } catch (Exception e) {
            System.out.println("Error checking username existence: " + e.getMessage());
            return false; // Return false in case of any exception
        }
    }
    
    //method to check whether the user enter a valid Email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+(?:com|net|org|edu|gov|mil|biz|info|name|museum|us|ca|uk|de|fr|au|jp|kr|cn)$";
        return email.matches(emailRegex);
    }
    
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //method to encrypt password
    public static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
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
            java.util.logging.Logger.getLogger(signupforparent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(signupforparent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(signupforparent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(signupforparent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new signupforparent().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Email;
    private javax.swing.JTextField Name;
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField Username;
    private javax.swing.JTextField X;
    private javax.swing.JTextField Y;
    private java.awt.Button button1;
    private javax.swing.JPasswordField confirmpassword;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JCheckBox show1;
    private javax.swing.JCheckBox show2;
    // End of variables declaration//GEN-END:variables
}
