/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305.Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import project_cpit305.DB.DataBase;

/**
 *
 * @author aljaz
 */

/*
Server class
This class acts as a multi-threaded  that handles LOGIN and REGISTER requests from
multiple clients (managers) at the same time.
 */
public class Server {

    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT);

        try {
            // Create server socket (listening on port 5000)
            ServerSocket serverSocket = new ServerSocket(PORT);

            // Keep server running foreve and keeps accepting new clients
            while (true) {

                // Wait for client connection-- line blocks until someone connects
                Socket clientSocket = serverSocket.accept();
                System.out.println("New manager connected");

                // Handle each manager in a separate thread(so multiple users can login or register)
                Runnable clientHandler = new Runnable() {
                    @Override
                    public void run() {
                        handleClient(clientSocket);
                    }
                };

                //Starts a new thread so multiple clients can connect at the same time
                Thread t = new Thread(clientHandler);
                t.start();
            }

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Client handler-- Reads request type (login / register) and processes it
    private static void handleClient(Socket clientSocket) {

        try {
            //Reads data from client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //Sends data to client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // First message tells request type
            String type = in.readLine(); // LOGIN or REGISTER

            //-- LOGIN --
            if (type.equals("LOGIN")) {

                String managerName = in.readLine();
                String password = in.readLine();

                boolean success = checkLogin(managerName, password);

                if (success) {
                    out.println("SUCCESS");
                    System.out.println("Manager " + managerName + " logged in");
                } else {
                    out.println("FAIL");
                    System.out.println("Login failed for " + managerName);
                }

                // --REGISTER -
            } else if (type.equals("REGISTER")) {

                String name = in.readLine();
                String password = in.readLine();

                boolean success = registerManager(name, password);

                if (success) {
                    out.println("SUCCESS");
                    System.out.println("New manager registered: " + name);
                } else {
                    out.println("FAIL");
                }
            }

            clientSocket.close();

        } catch (Exception e) {
            System.out.println("Client handler error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // check Login --DB autentication
    private static boolean checkLogin(String managername, String password) {
        String sql = "SELECT * FROM Manager WHERE ManagerName = ? AND Password = ?";

        try (Connection con = DataBase.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, managername);
            ps.setString(2, password);

            // Execute query and get results
            ResultSet rs = ps.executeQuery();

            // Check if any record exists
            if (rs.next()) {
                rs.close(); 
                return true; //login successful
            } else {
                rs.close(); 
                return false; //login failed
            }
               
        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Register manager insert into DB
    private static boolean registerManager(String name, String password) {

        String sql = "INSERT INTO Manager (ManagerID, ManagerName, Password) VALUES (?, ?, ?)";

        try (Connection con = DataBase.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.println("TRY REGISTER -> " + name + " | " + password);
            
            int id = (int) (Math.random() * 100);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, password);

            int rows = ps.executeUpdate();
            //executeUpdate() runs the insert query and returns number of rows affected

            if (rows > 0) {
                return true;// registration succeeded
            } else {
                // If no rows were inserted
                return false; // registration failed
            }

        } catch (Exception e) {
            System.out.println("Register DB error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

}
