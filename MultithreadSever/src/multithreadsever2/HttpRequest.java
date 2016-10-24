/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithreadsever2;
import java.io.*;
import java.net.*;
import java.util.*;

public final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
	Socket socket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception 
	{
		this.socket = socket;
	}

	// Implement the run() method of the Runnable interface.
	public void run()
	{
		try {
                    processRequest();
                } catch (Exception e) {
                    System.out.println(e);
                }
	}

	private void processRequest() throws Exception
	{
		// Get a reference to the socket's input and output streams.
            InputStream is = socket.getInputStream();
            DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());

            // Set up input stream filters.
            //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            /**
             * Set InputStream to the socket input
             * Set the DataOutputStream to the socket output
             * Made InputStreamReader a variable to declare inside of the BufferedReader
             */
            
            // Get the request line of the HTTP request message.
            String requestLine = br.readLine();

            // Display the request line.
            System.out.println("Request-Line:");
            System.out.println(requestLine);
            
            // Get and display the header lines.
            String headerLine = null;
            while ((headerLine = br.readLine()).length() != 0) {
                System.out.println(headerLine);
            }
            /**
             * while the header has some text then print it
             */
            
            // Extract the filename from the request line.
            StringTokenizer tokens = new StringTokenizer(requestLine);
            tokens.nextToken();  // skip over the method, which should be "GET"
            String fileName = tokens.nextToken();

            // Prepend a "." so that file request is within the current directory.
            if(fileName.startsWith("/"))
                fileName = "." + fileName;            
            
            // Open the requested file.
            FileInputStream fis = null;
            boolean fileExists = true;
            boolean emptyRequest=false;
            if (fileName.length()< 3){
                emptyRequest = true;
                fileExists = false;
            }
            else{
                try {
                    fis = new FileInputStream(fileName);
                } catch (FileNotFoundException e) {
                    fileExists = false;
                }
            }
            
            // Construct the response message.
            String statusLine = null;
            String contentTypeLine = null;
            String entityBody = null;
            if (fileExists) {
                statusLine =  "HTTP/1.1 220 OK:";
                contentTypeLine = "Content-type: " + 
                    contentType( fileName ) + CRLF;
            } else {
                statusLine = "HTTP/1.1 404 Not Found:";
                contentTypeLine = "Content-Type: text/html" + CRLF;
                entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + "<BODY>404 Not Found</br> Request not found on our server, stop giving us bad requests you scrub</BODY></HTML>";
            }
            /**
             * If the fileExists then give an 200 OK message and return the file
             * If the file doesn't exist then give a 404 Not Found error and direct to a 404 page
             */
            
            // Send the status line.
            os.writeBytes(statusLine);
            System.out.println(statusLine);

            // Send the content type line.
            os.writeBytes(contentTypeLine);
            System.out.println(contentTypeLine);

            // Send a blank line to indicate the end of the header lines.
            os.writeBytes(CRLF);
            
            // Send the entity body.
            if (fileExists)	{
                sendBytes(fis, os);
                fis.close();
            } else {
                os.writeBytes(entityBody);
            }

            
            // Close streams and socket.
            os.close();
            br.close();
            socket.close();
        }
        
        private static void sendBytes(FileInputStream fis, OutputStream os) 
        throws Exception
        {
            // Construct a 1K buffer to hold bytes on their way to the socket.
            byte[] buffer = new byte[1024];
            int bytes = 0;

            // Copy requested file into the socket's output stream.
            while((bytes = fis.read(buffer)) != -1 ) {
                os.write(buffer, 0, bytes);
            }
        }
        /**
         * sent bits section
         * @param fileName
         * @return 
         */
        
        private static String contentType(String fileName)
        {
            if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
		return "text/html";
            }
            if(fileName.endsWith(".jpg")) {
		return "text/jpg";
            }
            if(fileName.endsWith(".gif")) {
		return "text/gif";
            }
            return "application/octet-stream";
        }
        /**
         * contentType section
         */
}