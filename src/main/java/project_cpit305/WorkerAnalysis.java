/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_cpit305;

import project_cpit305.DB.DataBase;
import GUI.folder.UIUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkerAnalysis {

    public static ArrayList<String> getWorkersNames() {
        try {
            // Open database connection
            Connection con = DataBase.getConnection();
            Statement st = con.createStatement();

            // Execute query to get all worker names ordered alphabetically
            ResultSet rs = st.executeQuery("SELECT WorkerName FROM WorkerAnalysis ORDER BY WorkerName");
            // List to store worker names
            ArrayList<String> list = new ArrayList<>();

            // Loop through results and add each name to list
            while (rs.next()) {
                list.add(rs.getString("WorkerName"));
            }

            // Close resources
            rs.close();
            st.close();
            con.close();

            // Return list of worker names
            return list;

        } catch (SQLException e) {
            System.out.println("getWorkerName error: " + e.getMessage());
            return null;
        }
    }

    public static int getWorkerID(String workerName) {
        try {
            // Open database connection
            Connection con = DataBase.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT WorkerID FROM workerAnalysis WHERE WorkerName = ?");
            ps.setString(1, workerName);   // Set worker name parameter

            // Execute query
            ResultSet rs = ps.executeQuery();

            // Check if worker exists
            if (rs.next()) {
                // Get worker ID from result
                int id = rs.getInt("workerID");

                rs.close();
                ps.close();
                con.close();

                // Return found ID
                return id;
            }

            // Close resources if no result found
            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // not found
    }

    /*
     Entry Point 1 — Called from ProductionManagement
     This method simulates a worker doing a job. It prevents the same worker from being used
     in multiple tasks at the same time.*/
    // Stores worker status (true = busy, false = free)
    static final Map<String, Boolean> workerBusy = new ConcurrentHashMap<>();

    public static void runWorker(String workerName) {

        // Check if this worker is already busy
        if (workerBusy.containsKey(workerName) && workerBusy.get(workerName)) {

            UIUtils.showGlobalPopup(workerName + " is already BUSY!");
            return; // Stop do not start the thread
        }

        // Mark worker as busy to Prevents multiple jobs at the same time
        workerBusy.put(workerName, true);

        //Simulates worker processing time
        Runnable workerTask = new Runnable() {
            @Override
            public void run() {
                try {
                    // 1- Worker starts job
                    System.out.println("Worker " + workerName + " STARTED");

                    // 2- Simulate working time 
                    Thread.sleep(9100);

                    // 3-Worker finishes job
                    System.out.println("Worker " + workerName + " FINISHED");

                } catch (Exception e) {
                    System.out.println("Worker " + workerName + " Cannot work: " + e.getMessage());
                    e.printStackTrace();

                } finally {
                    // Frees worker even if error happens
                    workerBusy.put(workerName, false);
                    System.out.println("Worker " + workerName + " is now FREE");
                }
            }
        };

        // Start worker thread
        Thread t = new Thread(workerTask);
        t.start();
    }

    /*  Entery Point 2 FROM GUI
        This method:
     - Retrieves worker performance data
     - Shows total units produced
     - Displays top 3 workers ranking
     - Compares worker performance with best worker
          */
    public static String Analysis(String inputName) {
        
       // StringBuilder used to build output
        StringBuilder output = new StringBuilder();

        try {
            // Connect to database
            Connection con = DataBase.getConnection();

            // PART 1: get specific worker performance
            PreparedStatement ps = con.prepareStatement("SELECT w.WorkerID, w.WorkerName, SUM(p.UnitsProduced) AS totalUnits " + "FROM workerAnalysis w " + "JOIN productionManagement p ON w.WorkerID = p.WorkerID "
                    + "WHERE LOWER(w.WorkerName) = LOWER(?) "
                    + "GROUP BY w.WorkerID, w.WorkerName");

            ps.setString(1, inputName.trim());
            ResultSet workerResult = ps.executeQuery();

            int wTotal = 0;
            String wName = "";
            int wId = -1;

             // If worker exists
            if (workerResult.next()) {
                wId = workerResult.getInt("WorkerID");
                wName = workerResult.getString("WorkerName");
                wTotal = workerResult.getInt("totalUnits");
                
                // Add worker details to output
                output.append("ID: ").append(wId).append("\n")
                        .append("Name: ").append(wName).append("\n")
                        .append("Total Units Produced: ").append(wTotal).append("\n");
            } else {
                
                output.append("No worker found with the name: ").append(inputName).append("\n");
            }

            ps.close();
            workerResult.close();

            // PART 2: get TOP 3 workers ranking
            output.append("\n \t--- Top 3 Workers --\n");

            Statement st = con.createStatement();
            ResultSet result = st.executeQuery("SELECT w.WorkerID, w.WorkerName, SUM(p.UnitsProduced) AS totalUnits " + "FROM workerAnalysis w " + "JOIN productionManagement p ON w.WorkerID = p.WorkerID "
                    + "GROUP BY w.WorkerID, w.WorkerName "
                    + "ORDER BY totalUnits DESC");

            int rank = 1;
            String bestWorkerName = "";
            int bestWorkerUnits = 0;

             // Loop through ranking results
            while (result.next()) {
                String name = result.getString("WorkerName");
                int total = result.getInt("totalUnits");

                 // Store top worker data
                if (rank == 1) {
                    bestWorkerName = name;
                    bestWorkerUnits = total;
                }

                  // Show only top 3
                if (rank <= 3) {
                    output.append("Rank ").append(rank)
                            .append("  Name: ").append(name)
                            .append("  Units: ").append(total)
                            .append("\n");
                }

                rank++;
            }

            // PART 3: Performance comparison analysis
            int diff = bestWorkerUnits - wTotal;

            String level;
            if (wTotal == bestWorkerUnits) {
                level = "Top Performer";
            } else if (wTotal >= bestWorkerUnits * 0.7) {
                level = "Good ";
            } else {
                level = "Below Average";
            }

            double percentage = (wTotal * 100.0) / bestWorkerUnits;

            //   Final output
            output.append("\nPerformance Level: ").append(level).append("\n");

            output.append("Difference from Best: -")
                    .append(diff)
                    .append(" units\n");

            output.append("Performance vs Best: ")
                    .append(String.format("%.2f", percentage))
                    .append("%\n");

            result.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }

        return output.toString();
    }

}
