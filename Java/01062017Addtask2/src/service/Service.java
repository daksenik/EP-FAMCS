package service;

import beans.ProductDesc;
import beans.ProductItem;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Implementation of {@link ServerMethods ServerMethods} interface. Provides access to database layer.
 * 
 * @see UnicastRemoteObject
 */
public class Service extends UnicastRemoteObject implements ServerMethods{
    
    
    
    public Service() throws RemoteException {
        super();
    }


    /**
     * Use this method to retrieve the list of {@link beans.ProductItem ProductItem} objects from the database.
     * @return the list of {@link beans.ProductItem ProductItem} objects.
     * @throws RemoteException
     */
    @Override
    public ArrayList<ProductItem> getProducts() throws RemoteException {
        return database.DBRequests.selectItems();
    }

    /**
     * Use this method to retrieve the list of {@link beans.ProductDesc ProductDesc} objects from the database.
     * @return the list of {@link beans.ProductDesc ProductDesc} objects.
     * @throws RemoteException
     */
    @Override
    public ArrayList<ProductDesc> getDescriptions() throws RemoteException {
        return database.DBRequests.selectDescriptions();
    }

    /**
     * Use this method to update the group of products in the database.
     * @param productDesc target group to be updated.
     * @throws RemoteException
     */
    @Override
    public void updateProduct(ProductDesc productDesc) throws RemoteException {
        database.DBRequests.updateProduct(productDesc);
    }
    
    /**
     * Use this method to update prices of all products in the database. 
     * @param percentage how much the price shound be risen.
     * @throws RemoteException
     */
    @Override
    public void updatePrices(double percentage) throws RemoteException {
        database.DBRequests.updatePrices(percentage);
    }

    /**
     * Use this method to remove specified product item from the database.
     * @param productItem target product item to be removed.
     * @throws RemoteException
     */
    @Override
    public void deleteProduct(ProductItem productItem) throws RemoteException {
        database.DBRequests.deleteItem(productItem);
    }

    /**
     * Use this method to update the price of specified product item.
     * @param productItem target item to be updated.
     * @throws RemoteException
     */
    @Override
    public void updateItem(ProductItem productItem) throws RemoteException {
        database.DBRequests.updateItem(productItem);
    }
    
    public int checkLogin(String login, String password) throws RemoteException {
        return database.DBRequests.checkLogin(login, password);
    }
    
    public void updatePassword(String login, String password) throws RemoteException {
        database.DBRequests.updatePassword(login, password);
    }
}
