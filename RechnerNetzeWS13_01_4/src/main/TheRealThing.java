package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TheRealThing extends Thread {
	private static float result = -1;
	private String filename;
	private int start;
	private int end;
	private float threadResult = 1;
	/**
	* Creates a new TheRealThing thread which operates
	* on the indexes start to end.
	*/
	public TheRealThing(String filename, int start, int end) {
		this.filename = filename;
		this.start = start;
		this.end = end;
	}
	/**
	* Performs "eine komplizierte Berechnung" on array and
	* returns the result
	*/
	public float eine_komplizierte_Berechnung(float[] array) {
		float result = 1;
		for (int i = this.start; i  < this.end; i++)
		{
			result *= array[i];
		}
		System.out.println(this.getName() + "'s result: " + result);
		return result;		
	}
	public void run() {
		try {
			String fileInputStr = readData(this.filename);
			float[] array       = convertStringToArray(fileInputStr);
			this.threadResult   = eine_komplizierte_Berechnung(array);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public float getResult()
	{
		return this.threadResult;
	}
	
	private float[] convertStringToArray(String arrayStr) 
	{
		String[] arrayElements = arrayStr.split(" ");
		int floatElements = Integer.parseInt(arrayElements[2]);
		float[] array = new float[floatElements];
		for (int i = 0; i < floatElements; i++)
		{
			array[i] = Float.parseFloat(arrayElements[i + 3]);
		}
		return array;
	}
	
	
	public static void main(String[] args) {
		String pathToFile = "./myArrayData.dat";
		int numThreads = 5;
		int arraySize = 10;
		TheRealThing[] threads = new TheRealThing[5];
		for (int i = 0; i < numThreads; i++)
		{
			threads[i] = new TheRealThing(pathToFile, i, i+5);
			threads[i].start();
		}
		for (int i = 0; i < numThreads; i++)
		{
			try {
				threads[i].join();
				addToResult(threads[i].getResult());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Result: " + result);
	}
	
	private static synchronized String readData(String filename) throws Exception
	{
		BufferedReader in = new BufferedReader(
								new InputStreamReader(
									new FileInputStream(filename)));
		
		String returnStr = in.readLine();
		in.close();
		return returnStr;
	}
	
	private static synchronized void addToResult(float value)
	{
		result += value;
	}
}