import java.net.*;
import java.io.*;

public class Whois {

	public final static int DEFAULT_PORT = 43;
	public final static String DEFAULT_HOST = "whois.nic.at";
	
	
	public static void main(String[] args) {
		
		String serverName = System.getProperty("WHOIS_SERVER", DEFAULT_HOST);  // 
	    
	    InetAddress server = null; //initialisieren des Servers als Inet Adresse (klasse die Jave zur Verfügung stellt und eine Internetadresse repräsentiert)
	    try {
	      server = InetAddress.getByName(serverName);// Klassenmethode von InetAdress die die IP Adresse vom servername holt
	    }
	    catch (UnknownHostException ex) {
	      System.err.println("Error: Could not locate whois server " + server);  //Whois Server Abfrage kann nicht durchgeführt werden
	      System.err.println("Usage: java -DWHOIS_SERVER=hostname WhoisClient name");   //CMD Kommandozeilenparameter       
	      return;
	    }       

	    try {
	      Socket theSocket = new Socket(server, DEFAULT_PORT); //Socket baut die Verbindung auf
	      Writer out = new OutputStreamWriter(theSocket.getOutputStream(), "8859_1"); //iso Standard Latin 1 
	      for (int i = 0; i < args.length; i++) 
	      {
	    	  out.write(args[i] + " ");
	      }
	      out.write("\r\n");
	      out.flush(); //jetzt wird aau.at an den Server gesendet
	      //InputStream raw = theSocket.getInputStream();
	      InputStream in  = new BufferedInputStream(theSocket.getInputStream());//lesen was Server zurückgibt
	      int c; // int = 4 Byte daher wird immer 1 Byte gelesen
	      while ((c = in.read()) != -1) //wenn -1 gelesen wird, dann Ende (von BufferedInputStream so definiert)
	      {
	    	  System.out.write(c); //Byte für Byte wird ausgegeben und von Java in Text umgewandelt
	      }
	    }
	    catch (IOException ex) {
	      System.err.println(ex);
	    }

	  }

	}