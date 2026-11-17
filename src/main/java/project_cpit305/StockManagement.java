/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;

import project_cpit305.DB.DataBase;
import GUI.folder.UIUtils;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author aljaz
 */
public class StockManagement {

    // This method retrieves all item names from the InventoryManagementTable
    // and returns them as a list to be used in the UI (dropdown )
    public static ArrayList<String> getItemNames() {
        try {
            // Open database connection
            Connection con = DataBase.getConnection();

            // Create statement to execute SQL query
            Statement st = con.createStatement();

            // Query to get all item names ordered alphabetically
            ResultSet rs = st.executeQuery("SELECT ItemName FROM InventoryManagementTable ORDER BY ItemName");
            // List to store all item names
            ArrayList<String> items = new ArrayList<>();

            // Loop through all rows in result set
            while (rs.next()) {
                // Add each item name to the list
                items.add(rs.getString("ItemName"));
            }

            rs.close();
            st.close();
            con.close();

            // Return list of item names to caller
            return items;

        } catch (SQLException e) {
            System.out.println("getItemNames error: " + e.getMessage());
            // Return empty list to avoid crashing the program
            return new ArrayList<>();
        }
    }
    // This method adds a new material into InventoryManagementTable
    public static void addNewMaterial(String name, int quantity) {

        try (java.sql.Connection con = DataBase.getConnection()) {

            // 1. Get next ID
            java.sql.Statement st = con.createStatement();
            java.sql.ResultSet rs = st.executeQuery("SELECT MAX(ItemID) FROM InventoryManagementTable");

            int newID = 1;
            // If table is not empty, increment max ID
            if (rs.next()) {
                newID = rs.getInt(1) + 1;
            }

            rs.close();
            st.close();

            // 2. Insert new material into database
            String sql = "INSERT INTO InventoryManagementTable (ItemID, ItemName, Quantity) VALUES (?, ?, ?)";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, newID);
            ps.setString(2, name);
            ps.setInt(3, quantity);

            ps.executeUpdate();
            ps.close();

            System.out.println("Material added successfully: " + name);

        } catch (Exception e) {
            System.out.println("Error adding material: " + e.getMessage());
        }
    }

    /*
     Entry Point 1 — Called from ProductionManagement
     This method decreases stock quantity when a machine produces units
  */
    public static boolean decreaseStock(int machineID, int units) {

        // SQL query:
        // Reduce Quantity by "units"
        // Only if enough stock exists (Quantity >= units)
        String sql = "UPDATE InventoryManagementTable " + "SET Quantity = Quantity - ? " + "WHERE MachineID = ? AND Quantity >= ?";

        try (Connection con = DataBase.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false); // Begin transaction

            // Set values for query
            ps.setInt(1, units); // how much to reduce
            ps.setInt(2, machineID);// which machine
            ps.setInt(3, units);  // check stock availability

            // Execute update query
            int rows = ps.executeUpdate();

            // If no rows updated --> not enough stock
            if (rows == 0) {
                UIUtils.showGlobalPopup("Not enough stock");
                // Not enough stock -> rollback changes (cancel changes)
                con.rollback();
                return false;
            }
            //confirm changes
            con.commit();
            return true;

        } catch (Exception e) {
            // Handle any database or runtime error
            System.out.println("Error decreasing stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /*
     Entry Point 2 — FROM GUI
     This method checks the stock status of a specific item
     and compares it with a minimum limit
   */
    public static String checkStatus(String itemName, int minLimit) {

        try {
            // Open database connection

            Connection con = DataBase.getConnection();
            // SQL query to get item quantity
            PreparedStatement ps = con.prepareStatement("SELECT Quantity FROM InventoryManagementTable WHERE LOWER(ItemName)=LOWER(?)");

            // Set item name
            ps.setString(1, itemName.trim());
            // Execute query
            ResultSet rs = ps.executeQuery();

             // Check if item exists in database
            if (rs.next()) {
                // Get current quantity from database
                int quantity = rs.getInt("Quantity");

                // Close resources
                rs.close();
                ps.close();
                con.close();

                 // Compare quantity with minimum limit
                if (quantity < minLimit) {
                    return "LOW STOCK (Qty: " + quantity + ")";
                } else {
                    return "STOCK OK (Qty: " + quantity + ")";
                }

            } else {
                  // Item not found in database
                return "ITEM NOT FOUND";
            }

        } catch (Exception e) {
            return "DB ERROR: " + e.getMessage();
        }
    }

}
