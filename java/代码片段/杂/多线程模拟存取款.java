import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test
{
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException   
    {   
		Account account = new Account();
		Deposit depositTask = new Deposit(account);
		Withdraw withdrawTask = new Withdraw(account);
		Thread depositThread = new Thread(depositTask);
		Thread withdrawThread = new Thread(withdrawTask);
		depositThread.start();
		withdrawThread.start();
    }
}
class Deposit implements Runnable
{
	private Account account;
	public Deposit()
	{
		
	}
	public Deposit(Account account)
	{
		this.account = account;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				int deposit = account.deposit((int)(Math.random()*8));
				System.out.println("deposit:"+deposit+"			"+account);
				TimeUnit.MILLISECONDS.sleep(500);
			}
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
}
class Withdraw implements Runnable
{
	private Account account;
	public Withdraw()
	{
		
	}
	public Withdraw(Account account)
	{
		this.account = account;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				int withdraw = account.withdraw((int)(Math.random()*10));
				System.out.println("withdraw:"+withdraw+"			"+account);
				TimeUnit.MILLISECONDS.sleep(500);
			}
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
}
class Account
{
	private int deposit;
	private Lock lock = new ReentrantLock();
	private Condition depositCondition = lock.newCondition();
	public Account()
	{
		
	}
	public Account(int deposit)
	{
		this.deposit = deposit;
	}
	public int getDeposit()
	{
		return deposit;
	}
	public int deposit(int depositX)
	{
		lock.lock();
		deposit +=depositX;
		depositCondition.signal();
		lock.unlock();
		return depositX;
	}
	public int withdraw(int withdraw)
	{
		lock.lock();
		try
		{
			while(withdraw>deposit)
			{
				System.out.println("*** Wait for deposit ***");
				depositCondition.await();
				Thread.sleep(100);
			}
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}		
		deposit -= withdraw;
		lock.unlock();
		return withdraw;
	}
	@Override
	public String toString()
	{
		return "Your deposit:"+deposit;
	}
}		









import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test
{
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException   
    {   
		Account account = new Account();
		Deposit depositTask = new Deposit(account);
		Withdraw withdrawTask = new Withdraw(account);
		Thread depositThread = new Thread(depositTask);
		Thread withdrawThread = new Thread(withdrawTask);
		depositThread.start();
		withdrawThread.start();
    }
}
class Deposit implements Runnable
{
	private Account account;
	public Deposit()
	{
		
	}
	public Deposit(Account account)
	{
		this.account = account;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				int deposit = account.deposit((int)(Math.random()*15));
				System.out.println("deposit:"+deposit+"			"+account);
				TimeUnit.MILLISECONDS.sleep(500);
			}
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
}
class Withdraw implements Runnable
{
	private Account account;
	public Withdraw()
	{
		
	}
	public Withdraw(Account account)
	{
		this.account = account;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				int withdraw = account.withdraw((int)(Math.random()*10));
				System.out.println("withdraw:"+withdraw+"			"+account);
				TimeUnit.MILLISECONDS.sleep(500);
			}
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
}
class Account
{
	private int deposit;
	private Lock lock = new ReentrantLock();
	private Condition withdrawCondition = lock.newCondition();
	private Condition depositCondition = lock.newCondition();
	public Account()
	{
		
	}
	public Account(int deposit)
	{
		this.deposit = deposit;
	}
	public int getDeposit()
	{
		return deposit;
	}
	public int deposit(int depositX)
	{
		lock.lock();
		try
		{
			while(deposit>=10)
			{
				System.out.println("\t*** Wait for withdraw ***");
				depositCondition.await();
			}
			deposit +=depositX;
			withdrawCondition.signal();
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			lock.unlock();
		}
		return depositX;
	}
	public int withdraw(int withdraw)
	{
		lock.lock();
		try
		{
			while(withdraw>deposit)
			{
				System.out.println("\t*** Wait for deposit ***");
				withdrawCondition.await();
			}
			deposit -= withdraw;
			depositCondition.signal();
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}		
		finally
		{
			lock.unlock();
		}
		return withdraw;
	}
	@Override
	public String toString()
	{
		return "Your deposit:"+deposit;
	}
}


















import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test
{
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException   
    {   
		Account account = new Account();
		Deposit depositTask = new Deposit(account);
		Withdraw withdrawTask = new Withdraw(account);
		Thread depositThread = new Thread(depositTask);
		Thread withdrawThread = new Thread(withdrawTask);
		depositThread.start();
		withdrawThread.start();
    }
}
class Deposit implements Runnable
{
	private Account account;
	public Deposit()
	{
		
	}
	public Deposit(Account account)
	{
		this.account = account;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				int deposit = account.deposit((int)(Math.random()*15));
				System.out.println("deposit:"+deposit+"			"+account);
				TimeUnit.MILLISECONDS.sleep(500);
			}
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
}
class Withdraw implements Runnable
{
	private Account account;
	public Withdraw()
	{
		
	}
	public Withdraw(Account account)
	{
		this.account = account;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				int withdraw = account.withdraw((int)(Math.random()*10));
				System.out.println("withdraw:"+withdraw+"			"+account);
				TimeUnit.MILLISECONDS.sleep(500);
			}
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
}
class Account
{
	private int deposit;
	private Lock lock = new ReentrantLock();
	private Condition withdrawCondition = lock.newCondition();
	private Condition depositCondition = lock.newCondition();
	public Account()
	{
		
	}
	public Account(int deposit)
	{
		this.deposit = deposit;
	}
	public int getDeposit()
	{
		return deposit;
	}
	public int deposit(int depositX)
	{
		lock.lock();
		try
		{
			while(deposit>=10)
			{
				System.out.println("\t*** Wait for withdraw ***");
				depositCondition.await();
			}
			deposit +=depositX;
			withdrawCondition.signal();
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			lock.unlock();
		}
		return depositX;
	}
	public int withdraw(int withdraw)
	{
		lock.lock();
		try
		{
			while(withdraw>deposit)
			{
				System.out.println("\t*** Wait for deposit ***");
				withdrawCondition.await();
			}
			deposit -= withdraw;
			depositCondition.signal();
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}		
		finally
		{
			lock.unlock();
		}
		return withdraw;
	}
	@Override
	public String toString()
	{
		return "Your deposit:"+deposit;
	}
}