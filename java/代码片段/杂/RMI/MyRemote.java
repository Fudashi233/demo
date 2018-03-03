import java.rmi.Remote;
import java.rmi.RemoteException;

/**
* 1.制作远程调用接口
*/
public interface MyRemote extends Remote {

    public String sayHello() throws RemoteException;
}