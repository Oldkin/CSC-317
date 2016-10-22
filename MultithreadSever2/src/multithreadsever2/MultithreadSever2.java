/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadsever2;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author rconrardy17
 * @author asurti17
 */
public class MultithreadSever2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String argv[]) throws Exception {
        //set port number
        int port = 6789;
            System.out.println("the port in use is " + port);
        ServerSocket listener = new ServerSocket(port); 
            while (true) {
                Socket socket = listener.accept();
                    HttpRequest request = new HttpRequest(socket);
                    Thread thread = new Thread(request);
                    thread.start();
                    /**
                     * Opens socket at port 6666
                     * Waits for TCP requests
                     * Starts thread
                     */
            }
    }
    
}


