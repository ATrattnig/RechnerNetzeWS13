import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleHTTPServer {

	static int    port = 6668;
	
	public static void main(String[] args) {
		String clientSentence; 
		String capitalizedSentence;
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(port);
			while(true)
			{
				Socket connectionSocket = welcomeSocket.accept();
				ClientHandler handler = new ClientHandler(connectionSocket, "documentRoot");
				handler.handleClient();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
}
