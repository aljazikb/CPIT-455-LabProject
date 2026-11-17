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
import project_cpit305.Network.Client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author aljaz
 */
public class Login_GUI extends javax.swing.JFrame {

    private JTextField nameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton loginButton;

    private Color bgColor = new Color(30, 58, 95);
    private Color btnColor = new Color(52, 73, 94);
    private Color hoverColor = new Color(41, 128, 185);
    private Color textColor = Color.WHITE;

    public Login_GUI() {
        // Window setup 
        setTitle("Inventory Management System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Gradient background
        setContentPane(new GradientPanel());
        setLayout(null);

        // Centering values 
        int frameWidth = 500;
        int fieldWidth = 400;
        int fieldStartX = (frameWidth - fieldWidth) / 2; // = 50

        // Title label
        ShadowLabel titleLabel = new ShadowLabel("Inventory Management System");
        titleLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(fieldStartX, 40, fieldWidth, 60);
        getContentPane().add(titleLabel);

        //Subtitle 
        JLabel subtitle = new JLabel("Manager Login");
        subtitle.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 24));
        subtitle.setForeground(new Color(160, 160, 180));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBounds(fieldStartX, 100, fieldWidth, 35);
        getContentPane().add(subtitle);

        //Divider line 
        JSeparator sep = new JSeparator();
        sep.setBounds(fieldStartX, 145, fieldWidth, 2);
        sep.setForeground(new Color(41, 128, 185));
        getContentPane().add(sep);

        // Manager ID label
        JLabel nameLabel = new JLabel("Manager Name");
        nameLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(fieldStartX, 165, fieldWidth, 30);
        getContentPane().add(nameLabel);

        //Manager ID field 
        nameField = new JTextField();
        nameField.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 20));
        nameField.setBackground(new Color(52, 73, 94));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        nameField.setBounds(fieldStartX, 200, fieldWidth, 45);
        getContentPane().add(nameField);

        // ── Password label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(fieldStartX, 265, fieldWidth, 30);
        getContentPane().add(passLabel);

        //Password field 
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 20));
        passwordField.setBackground(new Color(52, 73, 94));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        passwordField.setBounds(fieldStartX, 300, fieldWidth, 45);
        getContentPane().add(passwordField);

        //  Message label 
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 18));
        messageLabel.setForeground(new Color(255, 100, 100));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBounds(fieldStartX, 360, fieldWidth, 30);
        getContentPane().add(messageLabel);

        //Login button 
        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 28));
        loginButton.setBackground(btnColor);
        loginButton.setForeground(textColor);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBounds(fieldStartX, 405, fieldWidth, 60);
        getContentPane().add(loginButton);

        // Hover effect 
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(btnColor);
            }
        });

        // Button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                // 1- Get user input from UI fields
                String managerNameText = nameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                 //Validate empty fields
                if (managerNameText.isEmpty() || password.isEmpty()) {
                    messageLabel.setForeground(new Color(255, 100, 100));
                    messageLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 20));
                    messageLabel.setText("Please fill in all fields!");
                    return;
                }
                //Call Client --server authentication
                boolean success = Client.login(managerNameText, password);

                //Handle response
                if (success) {
                     // Login successful -> show success message
                    messageLabel.setForeground(new Color(100, 220, 100));
                    messageLabel.setText("Welcome Manager " + managerNameText + "!");
                     // Close login window
                    dispose();
                    // Open dashboard window
                    new Dashboard().setVisible(true);
                    
                } else {
                      // Login failed ->show error message
                    messageLabel.setForeground(new Color(255, 100, 100));
                    messageLabel.setText("Wrong ID or password!");
                }
            }
        });

        setVisible(true);
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
            .addGap(0, 1300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
