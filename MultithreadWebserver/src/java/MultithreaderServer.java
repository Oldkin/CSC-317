/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rconrardy17, asurti17
 */
import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class MultithreaderServer {
    public static void main(String argv[]) throws Exception {
        //set port number
        int port = 80;
        ServerSocket listener = new ServerSocket(port); 
            while (true) {
                Socket socket = listener.accept();
                    HttpRequest request = new HttpRequest(socket);
                    Thread thread = new Thread(request);
                    thread.start();
            }
    }
}

final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
	Socket socket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception 
	{
		this.socket = socket;
	}

	// Implement the run() method of the Runnable interface.
    @Override
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
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            // Set up input stream filters.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader( new InputStreamReader(is));
            
            // Get the request line of the HTTP request message.
            String requestLine = br.readLine();

            // Display the request line.
            System.out.println();
            System.out.println(requestLine);
            
            // Get and display the header lines.
            String headerLine = null;
            while ((headerLine = br.readLine()).length() != 0) {
                System.out.println(headerLine);
            }
            
            // Close streams and socket.
            os.close();
            br.close();
            socket.close();
        }
}
