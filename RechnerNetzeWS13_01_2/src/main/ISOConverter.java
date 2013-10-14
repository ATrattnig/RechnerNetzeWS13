package main;//TEST

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ISOConverter {

	/**
	 *  
	 */
	public static void main(String[] args) {
		System.out.println("ISOConverter");
		
		if (args.length != 2)
		{
			System.out.println("Usage: ISOConverter inputfile outputfile");
			System.exit(-1);
		} 
		
		System.out.println("Called with " + args[0] + " " + args[1]);
		
		try 
		{
			File fileDir        = new File(args[0]);
			BufferedReader in   = new BufferedReader(
									new InputStreamReader(
										new FileInputStream(fileDir), "UTF8"));
			ZipOutputStream out = new ZipOutputStream(
	        						new BufferedOutputStream(
	        							new FileOutputStream(args[1])));
			ZipEntry entry      = new ZipEntry(args[0]);
	        out.putNextEntry(entry);
			String str;
			while ((str = in.readLine()) != null) 
			{
			    System.out.println(str);
			    str = str + "\r\n";
			    out.write(str.getBytes("ISO-8859-1"), 0, str.getBytes("ISO-8859-1").length);
			}
            in.close();
            out.close();
	    } 
	    catch (UnsupportedEncodingException e) 
	    {
			System.out.println(e.getMessage());
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
			System.out.println(e.getMessage());
	    }
				
		System.out.println("Finished.");
	}
}
