/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.folder;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author aljaz
 */
public class UIUtils {

    public static void showGlobalPopup(String message) {
       

        SwingUtilities.invokeLater(() -> {

            JDialog dialog = new JDialog();
            dialog.setUndecorated(true);
            dialog.setSize(500, 250);
            dialog.setLocationRelativeTo(null);
            dialog.setAlwaysOnTop(true);

            Color bgColor = new Color(30, 58, 95);
            Color btnColor = new Color(52, 73, 94);
            Color hoverColor = new Color(41, 128, 185);

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(bgColor);

            JLabel label = new JLabel(message, SwingConstants.CENTER);
            label.setBounds(30, 40, 440, 80);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 26));

            JButton ok = new JButton("OK");
            ok.setBounds(180, 150, 120, 45);
            ok.setBackground(btnColor);
            ok.setForeground(Color.WHITE);

            ok.addActionListener(e -> dialog.dispose());

            panel.add(label);
            panel.add(ok);

            dialog.setContentPane(panel);
            dialog.setVisible(true);
        });
    }
}
    

