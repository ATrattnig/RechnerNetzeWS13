import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.codec.binary.Base64;

public class ClientHandler extends Thread
{
	private Socket  clientSocket;
	private String  documentRoot;
	private boolean hasContent;
	private boolean isContentText;
	private File    f;
	private String  message;
	private Logger  logger;

	
	public ClientHandler(Socket clientSocket, String documentRoot, Logger logger)  {
		super();
		this.clientSocket = clientSocket;
		this.documentRoot = documentRoot;
		this.hasContent   = false;
		this.isContentText = false;
		this.logger = logger;
	}
	
	 public void run() 
	 {
		 handleClient();
	 }
	
	public void handleClient()
	{
		try 
		{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
			String msgFromClient = inFromClient.readLine();
			logger.log(getServerTime() + " " + msgFromClient + " " + clientSocket.getInetAddress().toString() + " " + clientSocket.getPort());
			this.message = getAnswer(msgFromClient);
			
			
			if (this.hasContent)				
			{
				if (this.isContentText)
					readAndWriteTextFile(outToClient);
				else
					readAndWriteBinaryFile(outToClient);		
			} else
				outToClient.writeBytes(this.message + "\r\n");
			clientSocket.close();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	private String getAnswer(String msg)
	{
		String[] httpFields = msg.split(" ");
		if (httpFields.length != 3)
			return generateBadRequestMessage();
		
		if (!httpFields[0].equals("GET"))
			return generateNotAllowedMessage();
		
		if (!httpFields[2].equals("HTTP/0.9"))
			return generateHTTPVersionNotImplementedMessage();
		
		this.f = new File(documentRoot + httpFields[1]);
		
		if (!f.exists())
			return generateNotFoundMessage();
		
		if (f.isDirectory())
			f = new File(f.getAbsolutePath() + "index.html");
		
		String extension = "";
		int i = f.getName().lastIndexOf('.');
		if (i > 0) {
		    extension = f.getName().substring(i+1);
		}
		
		String header = generateHTTPHeader(extension, new Date(f.lastModified()), f.length());
		this.hasContent = true;
		return header;
	}
	
	private String generateHTTPHeader(String extension, Date lastModified, long size)
	{
		String header =    "HTTP/0.9 200 OK"           + "\r\n" + 
						   "Date: " + getServerTime()  + "\r\n"+ 
						   "Last-Modified: " + formatTime(lastModified)  + "\r\n" +
						   "Content-Length: " + size  + "\r\n";
		if (extension.equals("html"))
		{
			this.isContentText = true;
			header += "Content-Type: " + "text/html"  + "\r\n";
		}
		else 
			header += "Content-Type: " + "image/" + extension  + "\r\n";
		
		return header;
	}
	
	private void readAndWriteTextFile(DataOutputStream outToClient)
	{
		  try
		  {
			  FileInputStream fstream = new FileInputStream(f.getPath());
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  this.message += "\r\n";
			  while ((strLine = br.readLine()) != null)   {
				  this.message += strLine + "\r\n";
			  }
			  in.close();
			  
			  outToClient.writeBytes(this.message);
		  } catch (Exception e)
		  {
			  System.err.println("Error: " + e.getMessage());
		  }
	}
	
	private void readAndWriteBinaryFile(DataOutputStream outToClient)
	{
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(this.f)));
			this.message += "\r\n";
			byte[] bytes = new byte[(int) this.f.length()];
			in.readFully(bytes);
			in.close();

			this.message += new String(Base64.encodeBase64(bytes));
			
			outToClient.writeBytes(this.message);		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String generateBadRequestMessage()
	{
		return "HTTP/0.9 400 Bad Request\r\n" + "Date: " + getServerTime() + "\r\n";
	}
	
	private String generateNotFoundMessage()
	{
		return "HTTP/0.9 404 Not Found\r\n" + "Date: " + getServerTime() + "\r\n";
	}
	
	private String generateNotAllowedMessage()
	{
		return "HTTP/0.9 405 Method Not Allowed\r\n" + "Date: " + getServerTime() + "\r\n";
	}
	
	private String generateHTTPVersionNotImplementedMessage()
	{
		return "HTTP/0.9 505 HTTP Version Not Supported\r\n" + "Date: " + getServerTime() + "\r\n";
	}
	
	private String getServerTime() {
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("EZT"));
	    return dateFormat.format(calendar.getTime());
	}
	
	private String formatTime(Date time) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("EZT"));
	    return dateFormat.format(time);
	}
	
}
