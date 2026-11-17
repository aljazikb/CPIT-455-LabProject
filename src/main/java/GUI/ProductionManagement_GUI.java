/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.folder.UIUtils;
import GUI.folder.ShadowLabel;
import GUI.folder.GradientPanel;

/**
 *
 * @author aljaz
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import project_cpit305.MachineOperation;
import project_cpit305.ProductionManagement;
import project_cpit305.WorkerAnalysis;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public class ProductionManagement_GUI extends javax.swing.JFrame {

    Color bgColor = new Color(30, 58, 95);
    Color btnColor = new Color(52, 73, 94);
    Color hoverColor = new Color(41, 128, 185);
    Color textColor = Color.WHITE;

    /* Stores each active production run progress bar 
       Key = runCounter (unique ID for each run)
       Value = the JProgressBar of that run  */
    private final Map<Integer, JProgressBar> machineProgressBars = new HashMap<>();

    //This is the main container where all progress bars are displayed in the ui
    private JPanel barsPanel = new JPanel();

    ///Scroll pane that allows scrolling when many bars exist
    private JScrollPane scrollPane;

    /*  Counter used to generate a unique ID for each production run
        every click = new runCounter value */
    private int runCounter = 0;

    public ProductionManagement_GUI() {

        setContentPane(new GradientPanel());
        setLayout(null);
        setSize(1300, 800);
        setLocationRelativeTo(null);

        ShadowLabel shadowLabel = new ShadowLabel("Production Management");
        shadowLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 60));
        shadowLabel.setForeground(Color.WHITE);
        shadowLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        shadowLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        shadowLabel.setBounds(379, 20, 1063, 140);
        getContentPane().add(shadowLabel);

        initComponents();
        setExtendedState(JFrame.MAXIMIZED_HORIZ);

        jButton1.setBounds(30, 150, 220, 60);
        styleButton(jButton1);
        addHoverEffect(jButton1);
        jButton1.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 30));

        styleButton(RunProduction);
        addHoverEffect(RunProduction);
        RunProduction.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 35));

        Font labelFont = new Font("Tw Cen MT Condensed", Font.BOLD, 34);
        jLabel1.setFont(labelFont);
        jLabel4.setFont(labelFont);
        jLabel6.setFont(labelFont);

        jLabel1.setForeground(Color.WHITE);
        jLabel4.setForeground(Color.WHITE);
        jLabel6.setForeground(Color.WHITE);
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setBackground(new Color(30, 58, 95));
        barsPanel.setOpaque(true);

        // Scroll pane
        scrollPane = new JScrollPane(barsPanel);

        scrollPane.setBounds(350, 520, 600, 180);

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        add(scrollPane);
        loadWorkers();
        loadIMechine();
    }

    private void styleButton(JButton btn) {
        btn.setBackground(btnColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
    }

    private void addHoverEffect(JButton btn) {
        Color normal = btnColor;
        Color hover = hoverColor;
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(normal);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        unitText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        RunProduction = new javax.swing.JButton();
        WorkerName = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        MachineName = new javax.swing.JComboBox<>();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        unitText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel1.setText("Units Produced:");

        jLabel4.setText("Workers: ");

        RunProduction.setText("Run Production");
        RunProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunProductionActionPerformed(evt);
            }
        });

        WorkerName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        WorkerName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        WorkerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WorkerNameActionPerformed(evt);
            }
        });

        jLabel6.setText("Mechine");

        MachineName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        MachineName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MachineName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MachineNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(508, 508, 508)
                        .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(315, 315, 315))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(182, 182, 182)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(MachineName, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unitText, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(WorkerName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(RunProduction, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(338, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jButton1)
                .addGap(100, 100, 100)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(unitText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(WorkerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(MachineName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(RunProduction)
                .addGap(160, 160, 160)
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed


    private void RunProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunProductionActionPerformed
         // Get number of units from text field
        String units = unitText.getText();

         // Validate input
        if (units.isEmpty()) {
            UIUtils.showGlobalPopup("Please Enter Value ");
            return;
        }

         // Get selected worker and machine from UI
        String worker = (String) WorkerName.getSelectedItem();
        String machineName = (String) MachineName.getSelectedItem();

        // Convert units from String to int
        int unitInt = Integer.parseInt(units);

        //Create unique ID for each production run to track each progress bar separately
        runCounter++;
        final int runKey = runCounter; // final so the lambda below can capture it
        
        // Start production process (backend call)
        boolean started = ProductionManagement.processProduction(unitInt,worker,machineName,runKey);
        
        // If production failed, stop here
        if (!started) {
            return;
        }

        
        // UI: Create a container row for progress bar
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.Y_AXIS));
        rowPanel.setOpaque(true);
        rowPanel.setBackground(new Color(52, 73, 94));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

          // Label showing run details
        JLabel rowLabel = new JLabel("Run #" + runKey + "  —  Machine: " + machineName + "  |  Worker: " + worker);
        rowLabel.setForeground(Color.WHITE);
        rowLabel.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 22));
        rowLabel.setAlignmentX(LEFT_ALIGNMENT);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setString("Starting...");
        bar.setValue(0);
        bar.setForeground(new Color(41, 128, 185));
        bar.setBackground(new Color(52, 73, 94));
        bar.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 16));
        bar.setPreferredSize(new Dimension(500, 30));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        bar.setAlignmentX(LEFT_ALIGNMENT);

        rowPanel.add(rowLabel);
        rowPanel.add(bar);
        
        barsPanel.add(rowPanel);
        barsPanel.revalidate();
        barsPanel.repaint();
        this.revalidate();
        this.repaint();

         // Store progress bar using runKey for  tracking
        machineProgressBars.put(runKey, bar);

      
         // Updates progress bar from backend
        MachineOperation.onProgressUpdate = (id, progress) -> {
             // Get correct progress bar using run ID
            JProgressBar targetBar = machineProgressBars.get(id);
            if (targetBar == null) {
                return;
            }
             // Update progress UI
            targetBar.setValue(progress);
            targetBar.setString("Running... " + progress + "%");

             // If finished
            if (progress >= 100) {
                targetBar.setString("Done");
                targetBar.setForeground(new Color(39, 174, 96)); 
                machineProgressBars.remove(id);
            }
        };


    }//GEN-LAST:event_RunProductionActionPerformed

    private void WorkerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WorkerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_WorkerNameActionPerformed

    private void MachineNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MachineNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MachineNameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductionManagement_GUI.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new ProductionManagement_GUI().setVisible(true));
    }

    
    private void loadWorkers() {
        try {
            ArrayList<String> workers = WorkerAnalysis.getWorkersNames();
            WorkerName.removeAllItems();
            for (String w : workers) {
                WorkerName.addItem(w);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadIMechine() {

        ArrayList<String> items = MachineOperation.getMachineName();

        MachineName.removeAllItems();

        for (String item : items) {
            MachineName.addItem(item);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> MachineName;
    private javax.swing.JButton RunProduction;
    private javax.swing.JComboBox<String> WorkerName;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField unitText;
    // End of variables declaration//GEN-END:variables
}
