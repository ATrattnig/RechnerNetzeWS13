import java.nio.ByteBuffer;


public class ByteMsgGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// Negative SequenceNumber
		try { createMsg(true, true, -1, "test".getBytes()); } catch (IllegalArgumentException e) { e.printStackTrace(); }
		
		// Too big SequenceNumber
		try { createMsg(true, true, (1 << 16) + 1, "test".getBytes()); } catch (IllegalArgumentException e) { e.printStackTrace(); }
		
		// Payload is null
		try { createMsg(true, true, 1, null); } catch (IllegalArgumentException e) { e.printStackTrace(); }
		
		// Payload lenght is 0
		try { createMsg(true, true, 1, "".getBytes()); } catch (IllegalArgumentException e) { e.printStackTrace(); }


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
		//convert
		ByteBuffer byteBuffer = ByteBuffer.allocate(8 + payload.length);
		
		return null;
	}
}
