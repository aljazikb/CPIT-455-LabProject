/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.folder.GradientPanel;
import GUI.folder.ShadowLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import project_cpit305.Network.Client;

/**
 *
 * @author aljaz
 */
public class SignUp_GUI extends javax.swing.JFrame {
 
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JLabel messageLabel;
    private JButton signupButton;

    private Color bgColor = new Color(30, 58, 95);
    private Color btnColor = new Color(52, 73, 94);
    private Color hoverColor = new Color(41, 128, 185);
    private Color textColor = Color.WHITE;

    public SignUp_GUI() {

        setTitle("Inventory System - Sign Up");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(new GradientPanel());
        getContentPane().setLayout(null);

        int frameWidth = 500;
        int fieldWidth = 400;
        int x = (frameWidth - fieldWidth) / 2;

        //Title 
        ShadowLabel title = new ShadowLabel("Sign Up");
        title.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(x, 40, fieldWidth, 60);
        add(title);

        // Name 
        JLabel nameLabel = new JLabel("Manager Name");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
        nameLabel.setBounds(x, 120, fieldWidth, 30);
        add(nameLabel);

        nameField = new JTextField();
        styleField(nameField, x, 155, fieldWidth);
        add(nameField);

        //  Password 
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
        passLabel.setBounds(x, 220, fieldWidth, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        styleField(passwordField, x, 255, fieldWidth);
        add(passwordField);

        //  Confirm Password 
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
        confirmLabel.setBounds(x, 320, fieldWidth, 30);
        add(confirmLabel);

        confirmField = new JPasswordField();
        styleField(confirmField, x, 355, fieldWidth);
        add(confirmField);

        // Message 
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(x, 420, fieldWidth, 30);
        add(messageLabel);

        // Button 
        signupButton = new JButton("SIGN UP");
        signupButton.setBounds(x, 470, fieldWidth, 60);
        styleButton(signupButton);
        add(signupButton);

        signupButton.addActionListener((ActionEvent e) -> {
             /*Handles user registration from UI and sends data to Client -- server registration*/

             //Get user input from fields
            String name = nameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            String confirm = new String(confirmField.getPassword()).trim();
            //Validate empty fields
            if (name.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                messageLabel.setText("Please fill all fields!");
                return;
            }

            // Check password confirmation
            if (!pass.equals(confirm)) {
                messageLabel.setText("Passwords do not match!");
                return;
            }

            // Send data to backend --server
            boolean success = Client.registerManager(name, pass);

            //Handle response
            if (success) {
                 // Registration successful
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Account created successfully!");
                 // Inform user
                JOptionPane.showMessageDialog(this, "Please Sign In now");
                // Close signup window
                dispose();
                 // Open login screen
                new Login_GUI().setVisible(true);

            } else {
                 // Registration failed
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Registration failed!");
            }
        });

        setVisible(true);
    }

    private void styleField(JTextField field, int x, int y, int w) {
        field.setBounds(x, y, w, 45);
        field.setBackground(new Color(52, 73, 94));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41,128,185), 2),
                BorderFactory.createEmptyBorder(5,10,5,10)
        ));
    }

    private void styleButton(JButton btn) {
        btn.setBackground(btnColor);
        btn.setForeground(textColor);
        btn.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 28));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(btnColor);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(SignUp_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUp_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUp_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUp_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUp_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
