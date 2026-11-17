/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305.Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author aljaz
 */
public class Client {

     // Server connection details
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    
    /*
     Login request
     Sends username + password to server and waits for response (SUCCESS / FAIL)
    */
    public static boolean login(String managerName, String password) {
        
        try {
             // Connect to server
            Socket socket = new Socket(HOST, PORT);
              // Output stream (send data to server)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Input stream (receive data from server)
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Send login request to server
            out.println("LOGIN"); // operation type
            out.println(managerName); // username
            out.println(password); // password

            // Read server response
            String response = in.readLine();
            // Close connection
            socket.close();
            
            // for test messages
            System.out.println("from client class");
            System.out.println("Connet to the server SUCCESS");
            
             // Return true if login successful
            return response.equals("SUCCESS");

        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    
     /*
     Register request
     Sends new manager data to server and waits for SUCCESS / FAIL response
    */
    
    public static boolean registerManager(String name, String password) {
        
    try {
          // Connect to server
        Socket socket = new Socket(HOST, PORT);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        
        // Send register request to server
        out.println("REGISTER");
        out.println(name);
        out.println(password);

        // Read server response
        String response = in.readLine();
         // Close connection
        socket.close();

        System.out.println("Register response: " + response);

         // Return true if registration successful
        return response.equals("SUCCESS");

    } catch (Exception e) {
        System.out.println("Register error: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}
}
