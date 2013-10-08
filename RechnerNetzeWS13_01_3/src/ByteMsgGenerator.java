import java.nio.ByteBuffer;


public class ByteMsgGenerator {

	/**
	 * @param args
	 */
	public static int versionID = 2;
	
	public static void main(String[] args) 
	{
		//no error seq = 1011 0010 0100 1110
		try { printByteArray(createMsg(true, true, 45646, "longertest".getBytes())); } catch (IllegalArgumentException e) { e.printStackTrace(); }
		
//		// Negative SequenceNumber
//		try { createMsg(true, true, -1, "test".getBytes()); } catch (IllegalArgumentException e) { e.printStackTrace(); }
//		
//		// Too big SequenceNumber
//		try { createMsg(true, true, (1 << 16) + 1, "test".getBytes()); } catch (IllegalArgumentException e) { e.printStackTrace(); }
//		
//		// Payload is null
//		try { createMsg(true, true, 1, null); } catch (IllegalArgumentException e) { e.printStackTrace(); }
//		
//		// Payload lenght is 0
//		try { createMsg(true, true, 1, "".getBytes()); } catch (IllegalArgumentException e) { e.printStackTrace(); }


	}

	static byte[] createMsg (boolean isData, boolean isUrgent, int sequenceNumber, byte [] payload) throws IllegalArgumentException
	{
		//check sequenceNumber
		if ((sequenceNumber < 0) || (sequenceNumber > (1 << 16)))
		{
			System.out.println("Invalid SequenceNumber: " + sequenceNumber);
			throw new IllegalArgumentException();
		}
		//check payload
		if ((payload == null) || (payload.length <= 0))
		{
			System.out.println("Invalid Payload");
			throw new IllegalArgumentException();
		}

		//versionID
		Byte versionIDByte = Byte.valueOf((byte) (versionID << 3));		

		//Flags
		int flagsInt = 0;
		if (isData)
			flagsInt += 2;
		if (isUrgent)
			flagsInt += 1;
		Byte flagsByte = Byte.valueOf((byte) flagsInt);	

		//SequenceNumber
		byte[] sequenceNumberBytes = ByteBuffer.allocate(4).putInt(sequenceNumber).array();

		//Payload Length
		byte[] payloadLength = ByteBuffer.allocate(4).putInt(payload.length).array();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(8 + payload.length);
		byteBuffer.put(versionIDByte);
		byteBuffer.put(flagsByte);
		byteBuffer.put(sequenceNumberBytes[2]);
		byteBuffer.put(sequenceNumberBytes[3]);
		byteBuffer.put(payloadLength);
		byteBuffer.put(payload);
		
		return byteBuffer.array();
	}
	
	private static void printByteArray(byte[] ba)
	{
	    for (int i = 0; i < ba.length; i++)
	    {
	    	for (int j = 7; j >= 0; j--)
	    	{
	    		if (isBitSet(ba[i], j))
	    		{
	    			System.out.print("1");
	    		} else
	    		{
	    			System.out.print("0");
	    		}	    		
	    	}
	    	System.out.print("\n");
	    }
	}
	
	private static Boolean isBitSet(byte b, int bit)
	{
	    return (b & (1 << bit)) != 0;
	}
}
