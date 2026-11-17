/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;

import project_cpit305.DB.DataBase;
import GUI.folder.UIUtils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author aljaz
 */
public class GenerateReport {

    /*
        This method generates a production report file and saves it as a .txt document.
        The report includes total units produced and number of production runs per machine.
    */
    public static void generateProductionReport() {
        String sql = "SELECT m.MachineID, m.MachineName, SUM(p.UnitsProduced) AS TotalUnits, COUNT(p.ProductionID) AS TotalRuns "
                + "FROM ProductionManagement p "
                + "JOIN MachineStatus m ON p.MachineID = m.MachineID "
                + "GROUP BY m.MachineID, m.MachineName";

        try (Connection con = DataBase.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            // Create the file 
            String fileName = "ProductionReport.txt";
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);

            //  Write header 
            bw.write("========================================");
            bw.newLine();
            bw.write("        PRODUCTION REPORT               ");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.write("Date: " + new java.util.Date());
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();

            //  Write each machine row 
            while (rs.next()) {
                int machineID = rs.getInt("MachineID");
                String machineName = rs.getString("MachineName");
                int totalUnits = rs.getInt("TotalUnits");
                int totalRuns = rs.getInt("TotalRuns");

                bw.write("Machine ID   : " + machineID);
                bw.newLine();
                bw.write("Machine Name : " + machineName);
                bw.newLine();
                bw.write("Total Runs   : " + totalRuns);
                bw.newLine();
                bw.write("Units Produced: " + totalUnits);
                bw.newLine();
                bw.write("----------------------------------------");
                bw.newLine();
            }

            // Write footer
            bw.write("========================================");
            bw.newLine();
            bw.write("           END OF REPORT                ");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();

            // Close the file 
            bw.close();
            fw.close();

          
             // Show success message to user
            UIUtils.showGlobalPopup("Production Report has been saved!");

        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
