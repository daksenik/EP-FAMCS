package service;

import beans.ProductDesc;
import beans.ProductItem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This interface defines interacting between RMI client and RMI server.
 * Interface should be used both on client side and server side to setup the connection.
 * This interface is extention of {@link Remote Remote} interface.
 * 
 * @see Remote
 */
public interface ServerMethods extends Remote{
    
    /**
     * This method is needed to access the product items list.
     * @return {@link beans.ProductItem ProductItem} objects list
     * @throws RemoteException
     */
    ArrayList<ProductItem> getProducts() throws RemoteException;

    /**
     * This method is needed to access the product descriptions list
     * @return {@link beans.ProductDesc ProductDesc} objects list
     * @throws RemoteException
     */
    ArrayList<ProductDesc> getDescriptions() throws RemoteException;

    /**
     * This method is used to update the product description in the database.
     * @param productDesc product description object to be changed.
     * @throws RemoteException
     */
    void updateProduct(ProductDesc productDesc) throws RemoteException;

    /**
     * This method is used to update the prices of all products.
     * @param percentage how much prices should be risen
     * @throws RemoteException
     */
    void updatePrices(double percentage) throws RemoteException;

    /**
     * This method is used to remove product item from the database.
     * @param productItem target item to be removed.
     * @throws RemoteException
     */
    void deleteProduct(ProductItem productItem) throws RemoteException;

    /**
     * This method is used to update product item in the database. You can change any field of the item.
     * @param productItem target item to be changed.
     * @throws RemoteException
     */
    void updateItem(ProductItem productItem) throws RemoteException;
}
