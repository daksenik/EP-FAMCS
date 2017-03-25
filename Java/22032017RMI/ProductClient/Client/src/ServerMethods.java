import beans.ProductDesc;
import beans.ProductItem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by user on 22.03.2017.
 */
public interface ServerMethods extends Remote{
    ArrayList<ProductItem> getProducts() throws RemoteException;
    ArrayList<ProductDesc> getDescriptions() throws RemoteException;
    void updateProduct(ProductDesc productDesc) throws RemoteException;
    void updatePrices(double percentage) throws RemoteException;
    void deleteProduct(ProductItem productItem) throws RemoteException;
    void updateItem(ProductItem productItem) throws RemoteException;
}
