package main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class HTTPTestClient {

	static String host = "localhost";
	static int    port = 6667;
	
	public static void main(String[] args) {
		try {
			request("test\r\n");
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
		out.write(message);
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
