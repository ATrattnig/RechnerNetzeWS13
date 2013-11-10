package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import at.aau.itec.rn.dnsclient.msg.DNSMessage;
import at.aau.itec.rn.dnsclient.msg.DNSConstants;
import at.aau.itec.rn.dnsclient.msg.DNSMessageFactory;
import at.aau.itec.rn.dnsclient.msg.QuestionSection;
import at.aau.itec.rn.dnsclient.msg.ResourceRecord;

public class DNSClientUDP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("DNSClient over UDP");
        if (args.length != 2)
        {
            System.err.println("USAGE: java DNSClientUDP nameserverIP domainName");
            System.exit(-1);
        }
        System.out.println("Nameserver: " + args[0]);
        System.out.println("DomainName: " + args[1]);
        try
        {
            InetAddress inetAddress;

            byte[] b = new byte[4];
            String[] bytes = args[0].split("[.]");
            for (int i = 0; i < bytes.length; i++)
            {
                b[i] = new Integer(bytes[i]).byteValue();
            }

            inetAddress = InetAddress.getByAddress(b);
            
    		DatagramSocket clientSocket = new DatagramSocket();

    		byte[] sendData = new byte[1024];
    		byte[] receiveData = new byte[1024];
    		sendData = args[1].getBytes();
    		DatagramPacket sendPacket =	new DatagramPacket(sendData, sendData.length, inetAddress, 53);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			
			String answerSentence =new String(receivePacket.getData());
			System.out.println("FROM SERVER:" + answerSentence);
			clientSocket.close();

        }  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
	}

}
