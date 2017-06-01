package database;

import beans.ProductDesc;
import beans.ProductItem;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by user on 23.03.2017.
 */
public class DBRequests {
    private static final String DATA_ITEMS = "Server\\resources\\items.xml";
    private static final String DATA_DESCRIPTIONS = "Server\\resources\\products.xml";
    private static final String ITEMS_SCHEMA = "Server\\resources\\items.xsd";
    private static final String DESCS_SCHEMA = "Server\\resources\\products.xsd";
    
    private static XMLStreamReader xmlReaderItems;
    private static XMLStreamReader xmlReaderDescs;
    private static Validator validatorItems;
    private static Validator validatorDescs;
    
    static{
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            Schema schema = sf.newSchema(new File(ITEMS_SCHEMA));
            validatorItems = schema.newValidator();
            
            schema = sf.newSchema(new File(DESCS_SCHEMA));
            validatorDescs = schema.newValidator();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    
    private static void loadDataItems(){
        try {
            xmlReaderItems = XMLInputFactory.
                    newInstance().
                    createXMLStreamReader(new FileInputStream(DATA_ITEMS));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void loadDataDescs(){
        try {
            xmlReaderDescs = XMLInputFactory.
                    newInstance().
                    createXMLStreamReader(new FileInputStream(DATA_DESCRIPTIONS));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveDataItems(ArrayList<ProductItem> items) {
        try {
            XMLStreamWriter xmlWriterItems = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(DATA_ITEMS));
            
            xmlWriterItems.writeStartDocument();
            xmlWriterItems.writeStartElement("items");
            
            items.forEach(item -> {
                try {
                    xmlWriterItems.writeStartElement("item");
                    xmlWriterItems.writeAttribute("SID_PRODUCT", Integer.toString(item.getSidProduct()));
                    xmlWriterItems.writeAttribute("ID_PRODUCT", Integer.toString(item.getIdProduct()));
                    xmlWriterItems.writeAttribute("BARCODE_NUMBER", item.getBarcode());
                    xmlWriterItems.writeAttribute("RECEIVING_DATE", item.getReceivingDate().toString());
                    xmlWriterItems.writeAttribute("SELLING_DATE", 
                            item.getSellingDate() == null ? "NULL" : item.getSellingDate().toString());
                    
                    xmlWriterItems.writeEndElement();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            });
            
            xmlWriterItems.writeEndElement();
            xmlWriterItems.writeEndDocument();
            xmlWriterItems.close();
            
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveDataDescs(ArrayList<ProductDesc> products) {
        try {
            XMLStreamWriter xmlWriterDescs = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(DATA_DESCRIPTIONS));

            xmlWriterDescs.writeStartDocument();
            xmlWriterDescs.writeStartElement("products");

            products.forEach(product -> {
                try {
                    xmlWriterDescs.writeStartElement("product");

                    xmlWriterDescs.writeAttribute("ID_PRODUCT", Integer.toString(product.getIdProduct()));
                    xmlWriterDescs.writeAttribute("CATEGORY_NAME", product.getCategory());
                    xmlWriterDescs.writeAttribute("PRODUCT_NAME", product.getProductName());
                    xmlWriterDescs.writeAttribute("PRICE", Double.toString(product.getPrice()));

                    xmlWriterDescs.writeEndElement();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            });

            xmlWriterDescs.writeEndElement();
            xmlWriterDescs.writeEndDocument();
            xmlWriterDescs.close();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    public static ArrayList<ProductItem> selectItems() {//select all items from the db
        ArrayList<ProductItem> result = new ArrayList<>();
        loadDataItems();
        synchronized (xmlReaderItems) {
            try {
                validatorItems.validate(new StAXSource(xmlReaderItems));
                xmlReaderItems.close();
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }

            try {
                loadDataItems();
                while (xmlReaderItems.hasNext()) {
                    int event = xmlReaderItems.next();
                    switch (event) {
                        case XMLStreamReader.START_ELEMENT:
                            if (xmlReaderItems.getLocalName().equals("item")) {
                                String sidProductString = xmlReaderItems.getAttributeValue(null, "SID_PRODUCT");
                                String idProductString = xmlReaderItems.getAttributeValue(null, "ID_PRODUCT");
                                String barcode = xmlReaderItems.getAttributeValue(null, "BARCODE_NUMBER");
                                String receivingDateString = xmlReaderItems.getAttributeValue(null, "RECEIVING_DATE");
                                String sellingDateString = xmlReaderItems.getAttributeValue(null, "SELLING_DATE");

                                result.add(new ProductItem(Integer.parseInt(sidProductString),
                                        idProductString.equals("NULL") ? null : Integer.parseInt(idProductString),
                                        barcode.equals("NULL") ? null : barcode,
                                        receivingDateString.equals("NULL") ? null : new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(receivingDateString).getTime()),
                                        sellingDateString.equals("NULL") ? null : new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(sellingDateString).getTime())));
                            }

                            break;
                        default:
                            break;
                    }
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                xmlReaderItems.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static ArrayList<ProductDesc> selectDescriptions() {//select all descriptions from the db
        ArrayList<ProductDesc> result = new ArrayList<>();
        loadDataDescs();
        synchronized (xmlReaderDescs) {
            try {
                validatorDescs.validate(new StAXSource(xmlReaderDescs));
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }

            try {
                xmlReaderDescs.close();
                loadDataDescs();
                while (xmlReaderDescs.hasNext()) {
                    int event = xmlReaderDescs.next();
                    switch (event) {
                        case XMLStreamReader.START_ELEMENT:
                            if (xmlReaderDescs.getLocalName().equals("product")) {
                                String idProduct = xmlReaderDescs.getAttributeValue(null, "ID_PRODUCT");
                                String categoryName = xmlReaderDescs.getAttributeValue(null, "CATEGORY_NAME");
                                String productName = xmlReaderDescs.getAttributeValue(null, "PRODUCT_NAME");
                                String price = xmlReaderDescs.getAttributeValue(null, "PRICE");

                                result.add(new ProductDesc(Integer.parseInt(idProduct),
                                        categoryName.equals("NULL") ? null : categoryName,
                                        productName.equals("NULL") ? null : productName,
                                        price.equals("NULL") ? null : Double.parseDouble(price)));
                            }

                            break;
                        default:
                            break;
                    }
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }

            try {
                xmlReaderDescs.close();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public static void updateProduct(ProductDesc product) {//change price of product specified by product.getIdProduct 
                                                           // to product.getPrice
        ArrayList<ProductDesc> productList = selectDescriptions();
                productList.
                        stream().
                        filter(desc -> desc.getIdProduct() == product.getIdProduct()).
                        findFirst().get().setPrice(product.getPrice());
        saveDataDescs(productList);
    }
    
    public static void updateItem(ProductItem item) {// set selling date of getSidProduct to getSellingDate
        ArrayList<ProductItem> itemList = selectItems();
        itemList.
                stream().
                filter(filtItem -> filtItem.getSidProduct() == item.getSidProduct()).
                findFirst().get().setSellingDate(item.getSellingDate());
        saveDataItems(itemList);
    }
    
    public static void deleteItem(ProductItem item){//delete item specified by getSidProduct. Remove product if items count is 0
        ArrayList<ProductItem> itemList = selectItems();
        itemList.removeIf(filtItem -> filtItem.getSidProduct() == item.getSidProduct());
        if(itemList.stream().anyMatch(filtItem -> filtItem.getIdProduct() == item.getIdProduct()) == false) {
            ArrayList<ProductDesc> productList = selectDescriptions();
            productList.removeIf(product -> product.getIdProduct() == item.getIdProduct());
            saveDataDescs(productList);
        }
        saveDataItems(itemList);
    }
    
    public static void updatePrices(double percentage) {//update prices of all products to price * percentage / 100
        ArrayList<ProductDesc> productList = selectDescriptions();
         productList.forEach(product -> product.setPrice(product.getPrice() * percentage / 100));
        saveDataDescs(productList);
    }
}