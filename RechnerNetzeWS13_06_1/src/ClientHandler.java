import java.net.Socket;


public class ClientHandler 
{
	private Socket clientSocket;
	private String documentRoot;
	
	public ClientHandler(Socket clientSocket, String documentRoot) {
		super();
		this.clientSocket = clientSocket;
		this.documentRoot = documentRoot;
	}
	
	public void handleClient()
	{
		
	}
	
}
