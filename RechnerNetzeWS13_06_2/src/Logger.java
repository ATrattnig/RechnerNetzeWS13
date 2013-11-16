import java.util.LinkedList;
import java.util.Queue;
import java.util.TimerTask;


public class Logger extends TimerTask
{
	private Queue<String> logMsg;

	public Logger() {
		super();
		logMsg = new LinkedList<String>();
	}
	
	public synchronized void log(String message) 
	{
		logMsg.add(message);
	}
	
	public void run() 
	{
		while(!logMsg.isEmpty())
			System.out.println(logMsg.poll());
	}
	
	
}
