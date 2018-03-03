import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerProducer 
{
	public static void main(String[] args)
	{
		Buffer buffer = new Buffer();
		ProducerTask pTask = new ProducerTask(1,buffer);
		ConsumerTask cTask = new ConsumerTask(buffer);
		Thread pThread = new Thread(pTask);
		Thread cThread = new Thread(cTask);
		pThread.start();
		cThread.start();
	}
}
class ProducerTask implements Runnable
{
	private int i;
	private Buffer buffer;
	public ProducerTask(int i,Buffer buffer)
	{
		this.buffer = buffer;
		this.i = i ;
	}
	@Override
	public void run()
	{
		while(true)
		{
			buffer.write(i);
			i++;
		}
	}
}
class ConsumerTask implements Runnable
{
	private Buffer buffer;
	public ConsumerTask(Buffer buffer)
	{
		this.buffer = buffer;
	}
	@Override
	public void run()
	{
		while(true)
		{
			buffer.read();
		}
	}
}
class Buffer
{
	public final static int CAPACITY = 10;
	private Queue<Integer> queue = new LinkedList<Integer>();
	private Lock lock = new ReentrantLock();
	private Condition readCondition = lock.newCondition();
	private Condition writeCondition = lock.newCondition();
	public void write(int value)
	{
		lock.lock();
		try
		{
			while(queue.size()>=CAPACITY)	//ÒÑÂú
			{
				System.out.println("* Wait for read *");
				writeCondition.await();
			}
			queue.offer(value);
			readCondition.signal();Thread.yield();
//			TimeUnit.MILLISECONDS.sleep(250);
			System.out.println("\twrite");
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			lock.unlock();
		}
	}
	public void read()
	{
		lock.lock();
		int value = 0;
		try
		{
			while(queue.size()==0)	//ÒÑ¿Õ
			{
				System.out.println("\t\t\t* Wait for write *");		
				readCondition.await();
			}
			value = queue.poll();
			writeCondition.signal();
			Thread.yield();
			System.out.println("\t\t\t\t read");
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			lock.unlock();
		}
	}
}