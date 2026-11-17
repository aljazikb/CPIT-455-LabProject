package project_cpit305;

import project_cpit305.DB.DataBase;
import GUI.folder.UIUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author aljaz
 */

/*
 Production Management class
 Controls the whole production process:
 - checks worker availability
 - checks machine status
 - checks stock
 - inserts production record
 - starts machine execution
 - updates usage time
*/
public class ProductionManagement {

    public static boolean processProduction(int units, String workerName, String machineName, int runkey) {

        // check 1- Is worker already busy? to prevents assigning same worker twice
        if (WorkerAnalysis.workerBusy.containsKey(workerName) && WorkerAnalysis.workerBusy.get(workerName)) {
            
            UIUtils.showGlobalPopup(workerName + " is BUSY — Production cannot start");
            return false;//  stop production process
        }

        try (Connection con = DataBase.getConnection()) {
            
            //Get IDs from database using names
            int workerID = WorkerAnalysis.getWorkerID(workerName); 
            int machineID = MachineOperation.getMachineID(machineName);

           // check 2- Is machine already running? to prevents double booking machine
            if (MachineOperation.isMachineBusy(machineID)) {
                UIUtils.showGlobalPopup(machineName + " is BUSY — Production cannot start");
                return false; // stop production process
            }

             // CHECK 3: Is there enough stock? to ensures materials are available
            if (!StockManagement.decreaseStock(machineID, units)) {
                System.out.println("Not enough stock!");
                return false; // stop production process
            }
            
           
            //  insert production record
            int productionID = (int) (Math.random() * 100000);
            PreparedStatement ps = con.prepareStatement("INSERT INTO ProductionManagement " + "(ProductionID, UnitsProduced, WorkerID, MachineID) " + "VALUES (?, ?, ?, ?)");

            ps.setInt(1, productionID);
            ps.setInt(2, units);
            ps.setInt(3, workerID);
            ps.setInt(4, machineID);

            ps.executeUpdate();
            ps.close();

             //make worker as busy to prevents overlapping jobs
            WorkerAnalysis.workerBusy.put(workerName, true);
            
            //start machine processing thread This simulates machine working + UI progress
            MachineOperation.runMachine(machineID, workerName, runkey);

            // update machine usage time for performance tracking
            MachineOperation.updateUsageTime(machineID, units);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error in production: " + e.getMessage());
            return false;
        }
    }

}
