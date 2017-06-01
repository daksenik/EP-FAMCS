import beans.ProductDesc;
import beans.ProductItem;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by user on 23.03.2017.
 */
public class Service extends UnicastRemoteObject implements ServerMethods{
    
    
    
    public Service() throws RemoteException {
        super();
    }


    @Override
    public ArrayList<ProductItem> getProducts() throws RemoteException {
        return database.DBRequests.selectItems();
    }

    @Override
    public ArrayList<ProductDesc> getDescriptions() throws RemoteException {
        return database.DBRequests.selectDescriptions();
    }

    @Override
    public void updateProduct(ProductDesc productDesc) throws RemoteException {
        database.DBRequests.updateProduct(productDesc);
    }

    @Override
    public void updatePrices(double percentage) throws RemoteException {
        database.DBRequests.updatePrices(percentage);
    }

    @Override
    public void deleteProduct(ProductItem productItem) throws RemoteException {
        database.DBRequests.deleteItem(productItem);
    }
    
    @Override
    public void updateItem(ProductItem productItem) throws RemoteException {
        database.DBRequests.updateItem(productItem);
    }
}
