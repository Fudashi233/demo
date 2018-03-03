import java.rmi.Naming;

/**
*   4.�����ͻ���
*/
public class Client {

    public static void main(String[] args) {

        try {

            MyRemote remote = (MyRemote)Naming.lookup("rmi://127.0.0.1/RemoteImpl");
            System.out.println(remote.sayHello());
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}