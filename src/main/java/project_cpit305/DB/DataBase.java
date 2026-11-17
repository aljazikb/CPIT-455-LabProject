/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_cpit305.DB;

/**
 *
 * @author noufa
 */


import java.sql.*;


public class DataBase{
    
    public static Connection con = null;
    
    public DataBase(){
        
        try{
       
        //connecting to mySQL//
        String ConnectionURL = "jdbc:mysql://localhost:3306";
        con = DriverManager.getConnection(ConnectionURL,"root","root") ;
        Statement st = con.createStatement();
        
        //creating the database//
        //st.executeUpdate("CREATE DATABASE project");
        st.executeUpdate("USE project");
        System.out.println("DB CREATED");
        
        
        //creating the tables//
        String productionManagement = ("CREATE TABLE IF NOT EXISTS ProductionManagement (" +"ProductionID INT PRIMARY KEY, " +"UnitsProduced INT," +"TimeHours DECIMAL(5,2)," +"WorkerID INT,"+"MachineID INT)");
        String inventoryManagament = ("CREATE TABLE IF NOT EXISTS InventoryManagementTable (" +"ItemID INT PRIMARY KEY, " +"ItemName VARCHAR(100), " + "Quantity INT, " +"MinLimit INT)");
        String machinStatus = ("CREATE TABLE IF NOT EXISTS MachineStatus (" +"MachineID INT PRIMARY KEY, " +"MachineName VARCHAR(100), " +"Status VARCHAR(50), " +"UsageTime DECIMAL(6,2))");
        String workerAnalysis = ("CREATE TABLE IF NOT EXISTS WorkerAnalysis (" +"WorkerID INT PRIMARY KEY, " +"WorkerName VARCHAR(100))");
        String manager = ("CREATE TABLE IF NOT EXISTS Manager (" +"ManagerID INT PRIMARY KEY,"+ "ManagerName VARCHAR(100),"+"Password VARCHAR(255));");
        
        st.executeUpdate(productionManagement);
        st.executeUpdate(inventoryManagament);
        st.executeUpdate(machinStatus);
        st.executeUpdate(workerAnalysis);
        st.executeUpdate(manager);
        
        System.out.println("TABLES CREATED");
        
        //adding login details for workers in worker analysis//
        String insert1 = "INSERT INTO WorkerAnalysis VALUES " +"(1, 'Lara'), " +"(2, 'Jhon'), " +"(3, 'Lucy')";
        String insert2 = "INSERT INTO ProductionManagement VALUES " +"(10, 100, 6.00, 1,1), " +"(11, 190, 7.0, 2,2), " +"(12, 300, 10.50, 3,3), " +"(13, 89, 8.0, 1,1)";
        String insert3 = "INSERT INTO InventoryManagementTable VALUES " +"(1, 'bolts', 200, 50), " +"(2, 'wires',600, 40), " +"(3, 'fiber cables', 150, 60)";
        String insert4 = "INSERT INTO MachineStatus VALUES " +"(1, 'printer', 'Inactive', 133.0), " +"(2, 'laptop', 'Active', 110.0), " +"(3, 'server', 'Active', 200.50)";
        String insert5 = "INSERT INTO manager VALUES " +"(100,'Ali', 'p@ss1')," +"(101,'Arwa', 'p@ss2')," +"(102,'Sara','pass@3');";
        
        st.executeUpdate(insert1);
        st.executeUpdate(insert2);
        st.executeUpdate(insert3);
        st.executeUpdate(insert4);
        st.executeUpdate(insert5);

        
        
        con.close();
        
         }
  
  
        catch (SQLException s){
        //System.out.println("SQL statement is not executed!"); 
        s.printStackTrace();
        s.getErrorCode();
        s.getSQLState();
        }
    
    }
    
    public static Connection getConnection()throws SQLException {
        
        String ConnectionURL = "jdbc:mysql://localhost:3306/project";
        return DriverManager.getConnection(ConnectionURL,"root","root") ;
        
    }


    
    
    
    
}
