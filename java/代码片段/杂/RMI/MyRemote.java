import java.rmi.Remote;
import java.rmi.RemoteException;

/**
* 1.����Զ�̵��ýӿ�
*/
public interface MyRemote extends Remote {

    public String sayHello() throws RemoteException;
}