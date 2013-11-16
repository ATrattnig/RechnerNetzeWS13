package main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class HTTPTestClient extends Thread {

	static String host = "localhost";
	static int    port = 6668;
	
	static String requestOnIndex = "GET /index.html HTTP/0.9";
	static String requestOnGif   = "GET /images/logo.gif HTTP/0.9";
	static String requestOnPng   = "GET /images/TechnikErleben.png HTTP/0.9";
	
	private String message;
	
	public HTTPTestClient(String message) {
		super();
		this.message = message;
	}

	public static void main(String[] args) {
		try {
			for (int i = 0; i < 5; i++)
			{
				HTTPTestClient client = new HTTPTestClient(requestOnIndex);
				client.start();
				client = new HTTPTestClient(requestOnGif);
				client.start();
				client = new HTTPTestClient(requestOnPng);
				client.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run() 
	{
		try {
			request(this.message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String request(String message) throws Exception
	{
		String response = "";
		Socket theSocket = new Socket(host, port);
		Writer out = new OutputStreamWriter(theSocket.getOutputStream(), "8859_1");
		out.write(message + "\r\n");
		out.flush();
		InputStream in  = new BufferedInputStream(theSocket.getInputStream());
		int c; 
		while ((c = in.read()) != -1) 
		{
			response = response + c;
			System.out.write(c);
		}
		return response;
	}

}
