import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class to run <b style='color: red'>RMI server</b>.
 * 
 * @author Dzmitry Aksenik
 * @version 1.0
 */
public class Server {
    public static void main(String[] args) {        
        try {
            Registry registry = LocateRegistry.createRegistry(7755);
            System.setProperty("java.rmi.server.hostname", "10.150.5.102");
            ServerMethods service = new Service();
            registry.rebind("Service", service);
            System.out.println("Service added.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
