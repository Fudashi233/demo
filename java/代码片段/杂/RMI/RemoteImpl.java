import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.net.MalformedURLException;

/**
*   2.实现远程接口
*/
public class RemoteImpl extends UnicastRemoteObject implements MyRemote {

    public RemoteImpl() throws RemoteException {


    }
    @Override
    public String sayHello() throws RemoteException{

        return "Hello world";
    }
	public static void main(String[] args) {
		
		try {
			MyRemote remote = new RemoteImpl();
			Naming.rebind("RemoteImpl",remote);
			System.out.println("Server start.");
		} catch (RemoteException ex) {
			
			ex.printStackTrace();
		} catch (MalformedURLException ex) {
			
			ex.printStackTrace();
		}
    }
}