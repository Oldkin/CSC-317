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
        int port = 6666;
        try (ServerSocket listener = new ServerSocket(port)) {
            while (true) {
                try(Socket socket = listener.accept()) {
                    HttpRequest request = new HttpRequest(socket);
                    Thread thread = new Thread(request);
                    thread.start();
                }
            }
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
            InputStream is = new InputStream(listener.getInputStream(), true);
            DataOutputStream os = new DataOutputStream(listener.getOutputStream(), true);

            // Set up input stream filters.
            PrintWriter out = new PrintWriter(listener.getOutputStream(), true);
            //InputStreamReader isr = new InputStreamReader(listener.getInputStream());
            BufferedReader br = new BufferedReader( new InputStreamReader(listener.getInputStream()));
            }
}
