package project_cpit305;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class Dashboard extends JFrame {

    private final StockManagement stock = new StockManagement(200);
    private final MachineOperation machine = new MachineOperation();
    private final WorkerAnalysis worker = new WorkerAnalysis();

    private final ProductionManagement production =
            new ProductionManagement(stock, machine);

    private final JTextPane output = new JTextPane();
    private final JTextField units = new JTextField("10", 8);
    private final JLabel inputLabel = new JLabel();
    private final JLabel selectedLabel = new JLabel();
    private final JButton actionButton = new JButton();

    private String selectedModule = "Stock Management";

    // Information stored inside the application.
    private final String machineName = "Press";
    private final String workerName = "Lara";

    private final int materialPerUnit = 2;
    private final int minutesPerUnit = 3;
    private final int serviceInterval = 120;
    private final int workerTarget = 20;

    private int machineUsage = 0;
    private int totalRuns = 0;

    public Dashboard() {
        super("Inventory Management System");

        JPanel background = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                Graphics2D g = (Graphics2D) graphics.create();

                g.setPaint(new GradientPaint(
                        0, 0, new Color(30, 58, 95),
                        0, getHeight(), new Color(41, 128, 185)
                ));

                g.fillRect(0, 0, getWidth(), getHeight());
                g.dispose();
            }
        };

        background.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        setContentPane(background);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Navigation buttons.
        JPanel navigation = new JPanel(
                new GridLayout(1, 4, 15, 0)
        );

        navigation.setOpaque(false);

        addModuleButton(navigation, "Production Management");
        addModuleButton(navigation, "Stock Management");
        addModuleButton(navigation, "Machine Operation");
        addModuleButton(navigation, "Worker Analysis");

        add(navigation, BorderLayout.NORTH);

        JLabel title = new JLabel(
                "Inventory Management System",
                SwingConstants.CENTER
        );

        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setBorder(
                BorderFactory.createEmptyBorder(25, 0, 25, 0)
        );

        selectedLabel.setForeground(Color.WHITE);
        selectedLabel.setFont(
                new Font("SansSerif", Font.BOLD, 18)
        );
        selectedLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 18)
        );

        units.setFont(new Font("SansSerif", Font.PLAIN, 20));
        units.setHorizontalAlignment(JTextField.CENTER);
        units.setMargin(new Insets(7, 10, 7, 10));

        styleButton(actionButton);
        actionButton.addActionListener(event -> runSelectedModule());

        JPanel inputPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 15)
        );

        inputPanel.setOpaque(false);
        inputPanel.add(inputLabel);
        inputPanel.add(units);
        inputPanel.add(actionButton);

        JPanel controls = new JPanel(new BorderLayout());
        controls.setOpaque(false);
        controls.add(selectedLabel, BorderLayout.NORTH);
        controls.add(inputPanel, BorderLayout.CENTER);

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.add(title, BorderLayout.NORTH);
        heading.add(controls, BorderLayout.CENTER);

        // Results area.
        output.setEditable(false);
        output.setFont(new Font("SansSerif", Font.PLAIN, 20));
        output.setForeground(Color.WHITE);
        output.setBackground(new Color(30, 58, 95));
        output.setMargin(new Insets(25, 25, 25, 25));

        JScrollPane results = new JScrollPane(output);
        results.setBorder(
                BorderFactory.createLineBorder(
                        new Color(110, 155, 190)
                )
        );

        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setOpaque(false);
        center.add(heading, BorderLayout.NORTH);
        center.add(results, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // Generate report button.
        JButton reportButton = new JButton("Generate Report");
        styleButton(reportButton);
        reportButton.addActionListener(event -> saveReport());

        JPanel reportPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        reportPanel.setOpaque(false);
        reportPanel.add(reportButton);

        add(reportPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(actionButton);

        selectModule("Stock Management");

        setSize(1200, 750);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
    }

    private void addModuleButton(JPanel panel, String module) {
        JButton button = new JButton(module);

        styleButton(button);
        button.setPreferredSize(new Dimension(250, 65));

        button.addActionListener(event -> selectModule(module));

        panel.add(button);
    }

    private void selectModule(String module) {
        selectedModule = module;
        selectedLabel.setText(module);

        boolean needsInput = !module.equals("Worker Analysis");

        inputLabel.setVisible(needsInput);
        units.setVisible(needsInput);

        switch (module) {
            case "Stock Management":
                inputLabel.setText("Products to produce:");
                actionButton.setText("Check Materials");

                displayText(
                        "Available material: "
                        + stock.getQuantity() + " pieces"
                        + "\nEach product requires "
                        + materialPerUnit + " material pieces."
                );
                break;

            case "Machine Operation":
                inputLabel.setText("Products to produce:");
                actionButton.setText("Check Machine");

                displayText(
                        "Machine: " + machineName
                        + "\nEnter the number of products "
                        + "to estimate production time."
                );
                break;

            case "Worker Analysis":
                actionButton.setText("View Performance");

                displayText(
                        "Worker: " + workerName
                        + "\nPerformance is calculated "
                        + "from completed production."
                );
                break;

            case "Production Management":
                inputLabel.setText("Products to produce:");
                actionButton.setText("Start Production");

                displayText(
                        "Machine: " + machineName
                        + "\nWorker: " + workerName
                        + "\nAvailable material: "
                        + stock.getQuantity() + " pieces"
                );
                break;
        }
    }

    private int readUnits() {
        int value;

        try {
            value = Integer.parseInt(units.getText().trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(
                    "Please enter a valid whole number."
            );
        }

        if (value <= 0) {
            throw new IllegalArgumentException(
                    "The number of products must be greater than zero."
            );
        }

        return value;
    }

    private void runSelectedModule() {
        try {
            // Worker performance uses actual completed production.
            if (selectedModule.equals("Worker Analysis")) {
                int completed = production.getTotalProduced();

                double efficiency = worker.calculateEfficiency(
                        completed,
                        0,
                        workerTarget
                );

                displayText(
                        "Worker: " + workerName
                        + "\nCompleted products: " + completed
                        + "\nProduction target: " + workerTarget
                        + "\nEfficiency: "
                        + String.format(
                                java.util.Locale.US,
                                "%.2f%%",
                                efficiency
                        )
                );

                return;
            }

            int requestedUnits = readUnits();

            switch (selectedModule) {
                case "Stock Management": {
                    int required = stock.calculateRequiredMaterial(
                            requestedUnits,
                            materialPerUnit,
                            0
                    );

                    displayText(
                            "Available material: "
                            + stock.getQuantity() + " pieces"
                            + "\nRequired material: "
                            + required + " pieces"
                            + "\nStatus: " + stock.checkStatus(20)
                            + "\n\n"
                            + (required <= stock.getQuantity()
                                    ? "Enough material for this order."
                                    : "Not enough material for this order.")
                    );
                    break;
                }

                case "Machine Operation": {
                    int duration = machine.estimateProductionTime(
                            requestedUnits,
                            minutesPerUnit,
                            0
                    );

                    boolean maintenance = machine.needsMaintenance(
                            machineUsage,
                            serviceInterval,
                            0
                    );

                    displayText(
                            "Machine: " + machineName
                            + "\nEstimated production time: "
                            + duration + " minutes"
                            + "\nTotal operating time: "
                            + machineUsage + " minutes"
                            + "\nStatus: "
                            + (maintenance
                                    ? "Maintenance required"
                                    : "Ready for production")
                    );
                    break;
                }

                case "Production Management": {
                    if (machine.needsMaintenance(
                            machineUsage,
                            serviceInterval,
                            0)) {

                        displayText(
                                "Production cannot start."
                                + "\nMachine " + machineName
                                + " requires maintenance."
                        );
                        return;
                    }

                    int required = stock.calculateRequiredMaterial(
                            requestedUnits,
                            materialPerUnit,
                            0
                    );

                    if (required > stock.getQuantity()) {
                        displayText(
                                "Production cannot start."
                                + "\nNot enough material."
                                + "\nRequired: " + required + " pieces"
                                + "\nAvailable: "
                                + stock.getQuantity() + " pieces"
                        );
                        return;
                    }

                    int duration = machine.estimateProductionTime(
                            requestedUnits,
                            minutesPerUnit,
                            0
                    );

                    // Check totals before changing production data.
                    int updatedUsage = Math.addExact(
                            machineUsage,
                            duration
                    );

                    int updatedRuns = Math.addExact(totalRuns, 1);

                    boolean success = production.processProduction(
                            requestedUnits,
                            materialPerUnit,
                            false,
                            false,
                            machineUsage,
                            serviceInterval,
                            0
                    );

                    if (success) {
                        machineUsage = updatedUsage;
                        totalRuns = updatedRuns;

                        displayText(
                                "Production completed."
                                + "\nMachine: " + machineName
                                + "\nWorker: " + workerName
                                + "\nProducts completed: "
                                + requestedUnits
                                + "\nMaterials consumed: " + required
                                + "\nRemaining material: "
                                + stock.getQuantity()
                                + "\nTotal products completed: "
                                + production.getTotalProduced()
                                + "\nSuccessful production runs: "
                                + totalRuns
                        );
                    } else {
                        displayText(
                                "Production could not be completed."
                        );
                    }
                    break;
                }
            }
        } catch (RuntimeException error) {
            JOptionPane.showMessageDialog(
                    this,
                    error.getMessage(),
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveReport() {
        try {
            Path file = GenerateReport.generateProductionReport(
                    machineName,
                    production.getTotalProduced(),
                    totalRuns
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Production report saved:\n" + file,
                    "Report Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException error) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not save the report:\n" + error.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void displayText(String text) {
        output.setText(text);

        SimpleAttributeSet alignment = new SimpleAttributeSet();

        StyleConstants.setAlignment(
                alignment,
                StyleConstants.ALIGN_CENTER
        );

        output.getStyledDocument().setParagraphAttributes(
                0,
                output.getDocument().getLength(),
                alignment,
                false
        );

        output.setCaretPosition(0);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 17));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setMargin(new Insets(12, 15, 12, 15));

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(new Color(52, 73, 94));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Dashboard().setVisible(true)
        );
    }
}