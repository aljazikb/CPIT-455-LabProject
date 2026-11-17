/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;

import project_cpit305.DB.DataBase;
import GUI.folder.UIUtils;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author aljaz
 *
 */
public class MachineOperation {

    // we use it in the dropdown list
    public static ArrayList<String> getMachineName() {
        try {
            // Create database connection
            Connection con = DataBase.getConnection();
            // Create statement object
            Statement st = con.createStatement();
            // Execute SQL query
            ResultSet rs = st.executeQuery("SELECT MachineName FROM machinestatus ORDER BY MachineName");
            // ArrayList to store machine names
            ArrayList<String> items = new ArrayList<>();

            // Loop through all rows
            while (rs.next()) {
                // Add machine name to ArrayList
                items.add(rs.getString("MachineName"));
            }

            // Close resources
            rs.close();
            st.close();
            con.close();

            // Return list of machine names
            return items;

        } catch (SQLException e) {
            System.out.println("getMachineName error: " + e.getMessage());
            // Return empty list if error happens
            return new ArrayList<>();
        }
    }

    public static int getMachineID(String machineName) {
        try {
            // Create connection to database
            Connection con = DataBase.getConnection();

            // SQL query to get MachineID using MachineName
            PreparedStatement ps = con.prepareStatement("SELECT MachineID FROM MachineStatus WHERE MachineName = ?");
            // Replace ? with machineName value
            ps.setString(1, machineName);
            // Execute query
            ResultSet rs = ps.executeQuery();

            // Check if machine exists
            if (rs.next()) {
                // Get MachineID from result
                int id = rs.getInt("MachineID");

                // Close resources
                rs.close();
                ps.close();
                con.close();

                // Return found ID
                return id;
            }

            // Close resources if not found
            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return -1 if machine not found
        return -1;
    }

    public static int addMachine(String name) throws SQLException {

        // Create connection to the database
        Connection con = DataBase.getConnection();

        // 1-Check if machine already exists
        String checkSql = "SELECT MachineID FROM MachineStatus WHERE MachineName = ?";
        PreparedStatement checkPs = con.prepareStatement(checkSql);
        // Replace ? with machine name
        checkPs.setString(1, name);

        ResultSet checkRs = checkPs.executeQuery();

        // If machine exists return -1
        if (checkRs.next()) {
            checkRs.close();
            checkPs.close();
            con.close();
            return -1;
        }

        checkRs.close();
        checkPs.close();

        // 2- Generate new ID
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT MAX(MachineID) FROM MachineStatus");

        int newID = 1;
        // Get largest existing ID then add 1
        if (rs.next()) {
            newID = rs.getInt(1) + 1;
        }

        rs.close();
        st.close();

        // 3- Insert with default UsageTime = 0 && status Idle
        String sql = "INSERT INTO MachineStatus (MachineID, MachineName, Status, UsageTime) " + "VALUES (?, ?, 'Idle', 0)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, newID);
        ps.setString(2, name);

        ps.executeUpdate();

        ps.close();
        con.close();

        return newID;
    }

    /*
     Entry Point 1 — Called from ProductionManagement
     his method simulates a machine working process using threads, database updates, and synchronization to prevent conflicts.
    Lets managers choose machines, run them, track them, and prevent conflicts.
    */
    
    // Lock object map to ensure each machine runs one task at a time
    // Key = MachineID, Value = lock object for that specific machine
    static final Map<Integer, Object> machineLocks = new ConcurrentHashMap<>();

    // Callback used to update progress in the UI (progress bar)
    // Parameters: (runKey==identifies which machine, progress%)
    public static BiConsumer<Integer, Integer> onProgressUpdate = null;

    public static void runMachine(int machineID, String workerName, int runKey) {

        // Create a lock object for the machine if it doesn't exist
        if (!machineLocks.containsKey(machineID)) {
            machineLocks.put(machineID, new Object());
        }

        // Get the lock object for this machine
        Object lock = machineLocks.get(machineID);

        // Define the machine task that will run in a separate thread
        Runnable machineTask = new Runnable() {
            @Override
            public void run() {
                // Synchronize so only ONE thread can use this machine at a time
                synchronized (lock) {
                    try {
                        // 1: Mark machine as Running ──
                        UIUtils.showGlobalPopup("Machine " + machineID + " is RUNNING");

                        Connection con = DataBase.getConnection();
                        
                        PreparedStatement psRun = con.prepareStatement("UPDATE MachineStatus SET Status = 'Running' WHERE MachineID = ?");
                        psRun.setInt(1, machineID);
                        psRun.executeUpdate();
                        
                        psRun.close();
                        con.close();

                        // 2.Total simulated processing time in milliseconds
                        int totalMs = 9100;
                        // We split the work into 100 steps (each step = 1% progress)
                        int steps = 100;
                        // Time to wait for each step so total time = 9100ms
                        int sleepEach = totalMs / steps;

                        //Loop for progress simulation 1 --> 2 --> 3  --> ...  -->100
                        for (int i = 1; i <= steps; i++) {
                             // sleep to simulate processing 
                            Thread.sleep(sleepEach);

                            // Send progress update to UI  (Swing EDT thread)
                            if (onProgressUpdate != null) {
                                final int progress = i;
                                //Update progress bar with current percentage
                                javax.swing.SwingUtilities.invokeLater(() -> onProgressUpdate.accept(runKey, progress));
                            }
                        }

                        // 3: Set machine status back to Idle
                        Connection con2 = DataBase.getConnection();

                        PreparedStatement psIdle = con2.prepareStatement("UPDATE MachineStatus SET Status = 'Idle' WHERE MachineID = ?");

                        psIdle.setInt(1, machineID);
                        psIdle.executeUpdate();

                        psIdle.close();
                        con2.close();

                        UIUtils.showGlobalPopup("Machine " + machineID + " has FINISHED");

                    } catch (InterruptedException e) {
                        // If thread is interrupted during sleep
                        System.out.println("Machine " + machineID + " was interrupted!");
                        Thread.currentThread().interrupt();
                        
                    } catch (Exception e) {
                        // Any unexpected database or runtime error
                        System.out.println("Machine " + machineID + " CRASHED: " + e.getMessage());
                        e.printStackTrace();
                        
                    } finally {
                        // free the worker and fire 100% to the GUI
                        WorkerAnalysis.workerBusy.put(workerName, false);

                        if (onProgressUpdate != null) {
                            javax.swing.SwingUtilities.invokeLater(() -> onProgressUpdate.accept(runKey, 100));
                        }
                    }
                }
            }
        };
        // Start machine in a new thread so UI doesn't freeze
        Thread t = new Thread(machineTask);
        t.start();
    }

     //This method is uesed in the Production class  
    // This method checks whether a machine is currently busy (Running) or not
    public static boolean isMachineBusy(int machineID) {

        // SQL query to get the current status of the machine
        String sql = "SELECT Status FROM MachineStatus WHERE MachineID = ?";

        try (Connection con = DataBase.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            // Set machine ID in query
            ps.setInt(1, machineID);
            // Execute query and get result
            try (ResultSet rs = ps.executeQuery()) {
                // Check if machine exists in database
                if (rs.next()) {
                    // Get status value and compare it with "Running"
                    return rs.getString("Status").equalsIgnoreCase("Running");
                }
            }

        } catch (Exception e) {
            // Print error if something goes wrong
            System.out.println("Error checking machine status: " + e.getMessage());
            e.printStackTrace();
        }
        // Default: machine is not busy
        return false;
    }

    //This method is uesed in the Production class  
    public static void updateUsageTime(int machineID, int units) {

        // SQL query:
        // Increase UsageTime based on number of units produced
        // UsageTime = UsageTime + (units * timePerUnit)
        String query = "UPDATE MachineStatus " + "SET UsageTime = UsageTime + (? * timePerUnit) " + "WHERE MachineID = ?";

        // Open database connection
        try (Connection con = DataBase.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            // Set number of produced units
            ps.setInt(1, units);
            // Set machine ID
            ps.setInt(2, machineID);
             // Execute update query
            int rows = ps.executeUpdate();

             // Check if update was successful
            if (rows > 0) {
                System.out.println("Usage time updated");
            } else {
                System.out.println("Machine not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    /*
     Entry Point 2 — Called from GUI
    */
    public static MachineResult performanceCalc(String machineName) {

        // Object to store calculation results
        MachineResult r = new MachineResult();

        try {
            // Create database connection
            Connection con = DataBase.getConnection();

            // SQL query:
            // - Get machine usage time
            // - Calculate total produced units
            // - LEFT JOIN ensures machine appears even if no production records exist
            PreparedStatement ps = con.prepareStatement("SELECT m.UsageTime, COALESCE(SUM(p.UnitsProduced),0) AS totalUnits " + "FROM MachineStatus m " + "LEFT JOIN ProductionManagement p ON m.MachineID = p.MachineID "
                    + "WHERE LOWER(m.MachineName)=LOWER(?) "
                    + "GROUP BY m.UsageTime");

            // Replace ? with machine name
            // trim() removes extra spaces
            ps.setString(1, machineName.trim());
            // Execute query
            ResultSet rs = ps.executeQuery();

            // Check if machine exists
            if (!rs.next()) {
                r.output = "Machine not found.";
                return r;
            }

            // Get usage time from database
            double usageTime = rs.getDouble("UsageTime");
            // Get total produced units
            int totalUnits = rs.getInt("totalUnits");

            // Prevent division by zero
            if (usageTime <= 0) {
                r.output = "The machine has zero usage time.";
                return r;
            }

            // Performance formula = Total Units Produced / Usage Time
            double performance = (double) totalUnits / usageTime;
            // Store performance value
            r.performance = performance;

            // Check if machine needs maintenance
            //If performance is below 7: machine is considered weak ,maintenance is recommended
            if (performance < 7) {
                r.needsMaintenance = true;
            } else {
                r.needsMaintenance = false;
            }

            // Recommendation message
            String recommendation;

            if (r.needsMaintenance) {
                recommendation = "Machine performance is LOW.\nMaintenance is recommended.";
            } else {
                recommendation = "Machine is operating normally.";
            }

            // Final output message
            r.output = "Performance : " + String.format("%.2f", performance) + "\n" + recommendation;

            // Return result object
            return r;

        } catch (Exception e) {
            r.output = "Database error: " + e.getMessage();
            return r;
        }
    }

    public static void updateMachineStatus(String machineName, String newStatus) {

        try {
            // Get database connection
            Connection con = DataBase.getConnection();
            // SQL query to update machine status
            PreparedStatement ps = con.prepareStatement("UPDATE MachineStatus SET Status=? WHERE LOWER(MachineName)=LOWER(?)");
            // Set new status value
            ps.setString(1, newStatus);
            // Set machine name (trim removes extra spaces)
            ps.setString(2, machineName.trim());
            // Execute update query
            ps.executeUpdate();
            // Close resources
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class used to store and return multiple machine performance results
    // together from the performanceCalc() method.
    public static class MachineResult {
        // Stores calculated machine performance value
        public double performance;
        // Indicates whether the machine needs maintenance
        public boolean needsMaintenance;
        // Stores final output message shown to the user
        public String output;
    }

}
