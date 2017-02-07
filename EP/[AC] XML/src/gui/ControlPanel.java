package gui;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shapes.*;
import shapes.Rectangle;
import shapes.Shape;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by daksenik on 30.04.2016.
 */
public class ControlPanel implements ActionListener, TreeSelectionListener {
    ArrayList<Shape>xmlData;
    ArrayList<Integer>xmlPrev;
    ArrayList<DefaultMutableTreeNode>xmlNodes;

    JFrame mainFrame;

    private JPanel panel1;
    JTree xmlTree;
    JScrollPane jsp;
    private JTextField openFileName;
    private JButton openButton;
    private JTextField saveFileName;
    private JButton saveButton;
    private JPanel savePanel;
    private JPanel openPanel;
    private JPanel fileControlPanel;
    private ShapeDisplay viewPanel;
    private EditorPane editPanel;

    private DefaultMutableTreeNode shapes;

    public ControlPanel() {
        xmlData = new ArrayList<>();
        xmlPrev = new ArrayList<>();
        xmlNodes = new ArrayList<>();

        panel1 = new JPanel(new BorderLayout());

        shapes = new DefaultMutableTreeNode("Shapes");

        xmlTree = new JTree(shapes);
        xmlTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        xmlTree.addTreeSelectionListener(this);
        jsp = new JScrollPane(xmlTree);
        panel1.add(jsp, BorderLayout.WEST);

        viewPanel = new ShapeDisplay();
        panel1.add(viewPanel,BorderLayout.CENTER);

        editPanel = new EditorPane(viewPanel);
        panel1.add(editPanel,BorderLayout.EAST);

        fileControlPanel = new JPanel(new FlowLayout());

        openFileName = new JTextField("File name",15);
        openButton = new JButton("Open");
        openPanel = new JPanel(new FlowLayout());
        openPanel.add(openFileName);
        openPanel.add(openButton);
        fileControlPanel.add(openPanel);
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseXML(openFileName.getText());
            }
        });

        saveFileName = new JTextField("File name",15);
        saveButton = new JButton("Save");
        savePanel = new JPanel(new FlowLayout());
        savePanel.add(saveFileName);
        savePanel.add(saveButton);
        fileControlPanel.add(savePanel);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveXML(saveFileName.getText());
            }
        });

        panel1.add(fileControlPanel,BorderLayout.SOUTH);
        mainFrame = new JFrame("XML Parsing : Shapes");
        init();
        mainFrame.setContentPane(panel1);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void init(){

    }


    public void parseXML(String fileName){
        try{
            xmlData.clear();
            xmlPrev.clear();
            xmlNodes.clear();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file = new File(fileName);
            Document doc = builder.parse(file);
            Element root = doc.getDocumentElement();

            for(Node shape = root.getFirstChild();shape!=null;shape = shape.getNextSibling()){
                if(shape.getNodeName().equals("Parabola")){
                    Element cur = (Element)shape;
                    xmlData.add(new Parabola(Double.parseDouble(cur.getAttribute("A").trim()),
                            Double.parseDouble(cur.getAttribute("B").trim()),Double.parseDouble(cur.getAttribute("C").trim()),
                            Double.parseDouble(cur.getAttribute("DX").trim())));
                    xmlPrev.add(-1);
                }else if(shape.getNodeName().equals("Rectangle")){
                    Element cur = (Element)shape;
                    int width = Integer.parseInt(cur.getAttribute("Width").trim()),
                            height = Integer.parseInt(cur.getAttribute("Height"));

                    xmlData.add(new Rectangle(width,height));
                    xmlPrev.add(-1);
                    int prev = xmlData.size()-1;
                    for(Node ellipsedRect = shape.getFirstChild();ellipsedRect!=null;ellipsedRect = ellipsedRect.getNextSibling()){
                        if(ellipsedRect.getNodeName().equals("EllipsedRectangle")){
                            xmlData.add(new EllipsedRectangle(width,height));
                            xmlPrev.add(prev);
                            int pprev = xmlData.size()-1;
                            for(Node filledEllRect = ellipsedRect.getFirstChild();filledEllRect!=null;
                                filledEllRect = filledEllRect.getNextSibling())if(filledEllRect.getNodeName().equals("FilledEllipsedRectangle")){
                                Element curFdRt = (Element)filledEllRect;
                                int r = Integer.parseInt(curFdRt.getAttribute("Red").trim());
                                int g = Integer.parseInt(curFdRt.getAttribute("Green").trim());
                                int b = Integer.parseInt(curFdRt.getAttribute("Blue").trim());
                                xmlData.add(new FilledEllipsedRectangle(width,height,new Color(r,g,b)));
                                xmlPrev.add(pprev);
                            }
                        }else if(ellipsedRect.getNodeName().equals("FilledEllipsedRectangle")){
                            Element curFdRt = (Element)ellipsedRect;
                            int r = Integer.parseInt(curFdRt.getAttribute("Red").trim());
                            int g = Integer.parseInt(curFdRt.getAttribute("Green").trim());
                            int b = Integer.parseInt(curFdRt.getAttribute("Blue").trim());
                            xmlData.add(new FilledEllipsedRectangle(width,height,new Color(r,g,b)));
                            xmlPrev.add(prev);
                        }
                    }
                }
            }
            fillTree();
        }catch(Exception e){
            System.out.println(e + "\n :'(");
        }
    }

    public void saveXML(String fileName){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("configuration");
            for(int i=0;i<xmlData.size();i++)if(xmlData.get(i) instanceof Parabola){
                Element cur = doc.createElement("Parabola");
                cur.setAttribute("A",Double.toString(((Parabola)xmlData.get(i)).getA()));
                cur.setAttribute("B",Double.toString(((Parabola)xmlData.get(i)).getB()));
                cur.setAttribute("C",Double.toString(((Parabola)xmlData.get(i)).getC()));
                cur.setAttribute("DX",Double.toString(((Parabola)xmlData.get(i)).getD()));
                root.appendChild(cur);
            }else if(xmlPrev.get(i)==-1){
                Element cur = doc.createElement("Rectangle");
                cur.setAttribute("Width",Integer.toString(((Rectangle)xmlData.get(i)).getB()));
                cur.setAttribute("Height",Integer.toString(((Rectangle)xmlData.get(i)).getA()));
                for(int j=0;j<xmlData.size();j++)if(xmlPrev.get(j)==i){
                    Element curSub = doc.createElement(xmlData.get(j).toString());
                    if(xmlData.get(j) instanceof FilledEllipsedRectangle){
                        curSub.setAttribute("Red",Integer.toString(((FilledEllipsedRectangle)xmlData.get(j)).getColor().getRed()));
                        curSub.setAttribute("Green",Integer.toString(((FilledEllipsedRectangle)xmlData.get(j)).getColor().getGreen()));
                        curSub.setAttribute("Blue",Integer.toString(((FilledEllipsedRectangle)xmlData.get(j)).getColor().getBlue()));
                    }else{
                        for(int k=0;k<xmlData.size();k++)if(xmlPrev.get(k)==j){
                            Element curSubChild = doc.createElement(xmlData.get(k).toString());
                            curSubChild.setAttribute("Red",Integer.toString(((FilledEllipsedRectangle)xmlData.get(k)).getColor().getRed()));
                            curSubChild.setAttribute("Green",Integer.toString(((FilledEllipsedRectangle)xmlData.get(k)).getColor().getGreen()));
                            curSubChild.setAttribute("Blue",Integer.toString(((FilledEllipsedRectangle)xmlData.get(k)).getColor().getBlue()));
                            curSub.appendChild(curSubChild);
                        }
                    }
                    cur.appendChild(curSub);
                }
                root.appendChild(cur);
            }

            doc.appendChild(root);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(fileName)));
        }catch(Exception e){
            System.out.println(e+"\n :'(");
        }
    }

    public void fillTree(){
        for(int i=0;i<xmlData.size();i++)if(xmlPrev.get(i)!=-1){
            DefaultMutableTreeNode cur = new DefaultMutableTreeNode(xmlData.get(i));
            xmlNodes.add(cur);
            xmlNodes.get(xmlPrev.get(i)).add(cur);
        }else{
            DefaultMutableTreeNode cur = new DefaultMutableTreeNode(xmlData.get(i));
            xmlNodes.add(cur);
            shapes.add(cur);
        }
        ((DefaultTreeModel)xmlTree.getModel()).reload();
    }

    public static void main(String[] args) {
        ControlPanel content = new ControlPanel();
    }

    public void actionPerformed(ActionEvent ae){
        String command = ae.getActionCommand();
    }

    public void valueChanged(TreeSelectionEvent te){
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)xmlTree.getLastSelectedPathComponent();
        if(selectedNode==null)return;
        if(selectedNode.getUserObject() instanceof String)return;
        viewPanel.setDrawableContent((Shape)selectedNode.getUserObject());
        viewPanel.repaint();
        editPanel.setShape((Shape)selectedNode.getUserObject());
    }
}