import beans.ProductDesc;
import beans.ProductItem;
import tablemodels.DescriptionTableModel;
import tablemodels.ProductTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.sql.Date;

/**
 * Created by user on 23.03.2017.
 */
public class ClientGUI extends JFrame {
    JPanel mainPane = new JPanel(new BorderLayout());
    JPanel controlPanel = new JPanel();
    
    JButton removeSelected = new JButton("Remove");
    JButton changePrice = new JButton("Change price");
    JTextField priceModifier = new JTextField(4);
    JLabel changeModifier = new JLabel("New price (%):");
    JTextField newDate = new JTextField(10);
    JButton setDate = new JButton("Set selling date");
    JButton showDesc = new JButton("Description");
    
    JTable productTable;
    JTable descriptionTable;
    JScrollPane scrollProduct;
    JScrollPane scrollDesc;
    
    boolean isProductTableNow = true;
    
    ArrayList<ProductItem> productItems;
    ArrayList<ProductDesc> productDescs;
    ServerMethods server;
    
    public ClientGUI(ServerMethods serverMethods){
        super();
        
        try {
            productItems = serverMethods.getProducts();
            productDescs = serverMethods.getDescriptions();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.server = serverMethods;
        setMinimumSize(new Dimension(1000,700));
        
        productTable = new JTable(new ProductTableModel(productItems));
        descriptionTable = new JTable(new DescriptionTableModel(productDescs));
        
        scrollProduct = new JScrollPane(productTable);
        scrollDesc = new JScrollPane(descriptionTable);
        
        mainPane.add(scrollProduct, BorderLayout.CENTER);
        
        setupButtons();
        setupProductButtons();
        mainPane.add(controlPanel, BorderLayout.NORTH);
        
        setTitle("Products database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel temp = new JPanel();
        JButton switchTableBut = new JButton("Switch table");
        switchTableBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchTable();
            }
        });
        temp.add(switchTableBut);
        mainPane.add(temp, BorderLayout.SOUTH);
        
        //TODO: add buttons and fields
        
        add(mainPane);
        setVisible(true);        
    }
    
    void switchTable(){
        if(isProductTableNow) {
            mainPane.remove(scrollProduct);
            mainPane.add(scrollDesc, BorderLayout.CENTER);
            setupDescriptionButtons();
        } else {
            mainPane.remove(scrollDesc);
            mainPane.add(scrollProduct, BorderLayout.CENTER);
            setupProductButtons();
        }
        
        isProductTableNow = !isProductTableNow;
        mainPane.updateUI();
        mainPane.repaint();
    }
    
    void setupButtons(){
        removeSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = productTable.getSelectedRows();
                for(int i = rows.length-1; i >= 0; i--){
                    ProductItem itemToRemove = ((ProductTableModel)productTable.getModel()).items.remove(rows[i]);
                    ((ProductTableModel) productTable.getModel()).fireTableDataChanged();
                    try{
                        server.deleteProduct(itemToRemove);
                    } catch(RemoteException exc){
                        exc.printStackTrace();
                    }
                }
                try {
                    ((DescriptionTableModel)descriptionTable.getModel()).items.clear();
                    ((DescriptionTableModel)descriptionTable.getModel()).items.addAll(server.getDescriptions());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });
        changePrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double percentage = 100;
                try{
                    percentage = Double.parseDouble(priceModifier.getText());
                }catch(Exception exc){
                    exc.printStackTrace();
                    percentage = 100;
                }
                DescriptionTableModel model = (DescriptionTableModel) descriptionTable.getModel();
                int[] rows = descriptionTable.getSelectedRows();
                if(rows.length == descriptionTable.getRowCount()){
                    for(int i = 0; i< ((DescriptionTableModel) descriptionTable.getModel()).items.size(); i++){
                        ((DescriptionTableModel) descriptionTable.getModel()).
                                items.get(i).
                                    setPrice(
                                            ((DescriptionTableModel) descriptionTable.getModel()).
                                                    items.get(i).getPrice() * percentage / 100
                                    );
                    }
                    try {
                        server.updatePrices(percentage);
                        model.fireTableRowsUpdated(rows[0], rows[rows.length-1]);
                    } catch (RemoteException exc) {
                        exc.printStackTrace();
                    }
                } else {
                    for(int i = 0; i < rows.length; i++) {
                        try {
                            ProductDesc product = ((DescriptionTableModel) descriptionTable.getModel()).items.get(rows[i]);
                            product.setPrice(product.getPrice() * percentage / 100);
                            server.updateProduct(product);
                            model.fireTableCellUpdated(rows[i], 3);
                        } catch (RemoteException exc) {
                            exc.printStackTrace();
                        }
                    }
                }
            }
        });
        setDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = productTable.getSelectedRow();
                if(row == -1) return;
                ((ProductTableModel)productTable.getModel()).items.get(row).setSellingDate(Date.valueOf(newDate.getText()));
                try {
                    server.updateItem(((ProductTableModel)productTable.getModel()).items.get(row));
                    ((ProductTableModel)productTable.getModel()).fireTableRowsUpdated(row,row);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });
        showDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = productTable.getSelectedRow();
                if(row == -1) return;
                int id = ((ProductTableModel) productTable.getModel()).items.get(row).getIdProduct();
                switchTable();
                ArrayList<ProductDesc> ar = ((DescriptionTableModel)descriptionTable.getModel()).items;
                for(int i=0;i<ar.size();i++){
                    if(ar.get(i).getIdProduct() == id){
                        descriptionTable.setRowSelectionInterval(i,i);
                        
                        break;
                    }
                }
            }
        });
    }
    
    void setupProductButtons(){
        controlPanel.removeAll();
        
        controlPanel.add(newDate);
        controlPanel.add(setDate);
        controlPanel.add(removeSelected);
        controlPanel.add(showDesc);
        
        controlPanel.updateUI();
    }
    
    void setupDescriptionButtons(){
        controlPanel.removeAll();
        
        controlPanel.add(changeModifier);
        controlPanel.add(priceModifier);
        controlPanel.add(changePrice);
        
        controlPanel.updateUI();
    }

    public static void main(String[] args) {
        try {
            //Registry registry = LocateRegistry.getRegistry("10.150.5.102", 7755);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 7755);

            new ClientGUI((ServerMethods) registry.lookup("Service"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
