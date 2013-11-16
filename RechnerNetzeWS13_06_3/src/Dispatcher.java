import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;


public class Dispatcher extends Thread {
	private ClientHandler[]      handlers;
	private int                  handlerAmount;
	private int                  next;
	private int                  queueSize;
	private int                  currentHandlersInQueue;
	private Queue<ClientHandler> waitingHandlers;

	public Dispatcher(int handlerAmount, int queueSize) 
	{
		super();
		this.handlerAmount   = handlerAmount;
		this.queueSize       = queueSize;
		this.handlers        = new ClientHandler[handlerAmount];
		this.waitingHandlers = new LinkedList<ClientHandler>();
		this.next = 0;
		this.currentHandlersInQueue = 0;
	}
	
	public synchronized void schedule(Socket clientSocket, String documentRoot, Logger logger)
	{
		if (this.currentHandlersInQueue >= this.queueSize)
			try { clientSocket.close();	logger.log("Discarded Request"); } catch (IOException e) { e.printStackTrace();	} //discard request
		else
		{
			increaseHandlersInQueue();
			this.waitingHandlers.add(new ClientHandler(clientSocket, documentRoot, logger));			
		}
	}
	
	private synchronized void decreaseHandlersInQueue()
	{
		this.currentHandlersInQueue--;
	}
	private synchronized void increaseHandlersInQueue()
	{
		this.currentHandlersInQueue++;
	}
	
	public void run() 
	{
		while (true)
		{
			if (waitingHandlers.isEmpty())
				try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
			else
			{
				ClientHandler nextHandler = handlers[next];
				if ((nextHandler != null) && (nextHandler.isAlive()))
					try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
				else 
				{
					nextHandler = waitingHandlers.poll();
					decreaseHandlersInQueue();
					nextHandler.start();
					this.next = (this.next + 1) % this.handlerAmount;
				}
			}
		}
	}
	
}
