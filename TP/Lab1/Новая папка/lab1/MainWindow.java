package lab1;

import lab1.*;
import lab1.Polygon;
import lab1.Rectangle;
import lab1.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Vector;

/**
 * Created by user on 19.03.2017.
 */
public class MainWindow extends JFrame {
    JPanel panel = new JPanel(new BorderLayout());
    JPanel controlPanel = new JPanel(new BorderLayout());
    JPanel selectingPanel = new JPanel();
    JPanel shapeControl = new JPanel();
    DrawPanel paintPanel;
    Color oldColor;
    
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    int currentId = -1;
    
    JComboBox<Class> typeList;
    
    public MainWindow(){
        super("Shapes demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        setSize(1600,800);
        setResizable(false);
        paintPanel = new DrawPanel(shapes);
        panel.add(paintPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.EAST);
        shapeControl.setLayout(new BoxLayout(shapeControl, BoxLayout.Y_AXIS));

        Vector<Class>types = new Vector<Class>();
        try{
            types.add(Class.forName("lab1.PolyLine"));
            types.add(Class.forName("lab1.Segment"));
            types.add(Class.forName("lab1.Ray"));
            types.add(Class.forName("lab1.Line"));
            types.add(Class.forName("lab1.Circle"));
            types.add(Class.forName("lab1.Ellipse"));
            types.add(Class.forName("lab1.Polygon"));
            types.add(Class.forName("lab1.Parallelogram"));
            types.add(Class.forName("lab1.IsoscelesTriangle"));
            types.add(Class.forName("lab1.Rhomb"));
            types.add(Class.forName("lab1.Rectangle"));
            types.add(Class.forName("lab1.Square"));
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        typeList = new JComboBox<Class>(types);
        JButton addNewShape = new JButton("Add shape");
        selectingPanel.add(typeList);
        selectingPanel.add(addNewShape);
        controlPanel.add(selectingPanel, BorderLayout.NORTH);
        controlPanel.add(shapeControl, BorderLayout.CENTER);
        addNewShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addShapeToCanvas((Class)typeList.getSelectedItem());
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX(), y = e.getY() - 32;
                findNearest(x,y);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        
        setVisible(true);
    }
    
    private void findNearest(int x, int y){
        if(x > paintPanel.getWidth() || y > paintPanel.getHeight()) return;
        double min = 1e9, temp;
        int id = -1;
        for(int i = 0; i < shapes.size(); i++){
            if((temp = distanceTo(x,y,shapes.get(i).location().x, shapes.get(i).location().y)) < min){
                id = i;
                min = temp;
            }
        }
        setSelected(min < 30 ? id : -1);
    }
    
    private void setSelected(int id){
        if(currentId != -1){
            shapeControl.removeAll();
            shapeControl.validate();
            shapeControl.repaint();
            shapes.get(currentId).setColor(oldColor);
            paintPanel.repaint();
        }
        if(id != -1){
            oldColor = shapes.get(id).getColor();
            shapes.get(id).setColor(Color.RED);
        }
        currentId = id;
        paintPanel.repaint();
        if(id != -1) buildControlPane();
    }
    
    private void buildControlPane(){
        JTextField moveX = new JTextField(4);
        JTextField moveY = new JTextField(4);
        JPanel movePanel = new JPanel();
        movePanel.add(new JLabel("X: "));
        movePanel.add(moveX);
        movePanel.add(new JLabel("Y: "));
        movePanel.add(moveY);
        JButton applyMove = new JButton("Move");
        applyMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int newX = Integer.parseInt(moveX.getText());
                    int newY = Integer.parseInt(moveY.getText());
                    shapes.get(currentId).move(new Point(newX,newY));
                    paintPanel.repaint();
                }catch(Exception exc){}
            }
        });
        movePanel.add(applyMove);
        if(shapes.get(currentId) instanceof Line){
            JButton setDir = new JButton("Direct");
            setDir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        int newX = Integer.parseInt(moveX.getText());
                        int newY = Integer.parseInt(moveY.getText());
                        ((Line) shapes.get(currentId)).setDirection(new Point(newX,newY));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });
            movePanel.add(setDir);
        }
        
        JPanel borderColorPanel = new JPanel();
        JTextField fRed = new JTextField(4);
        JTextField fGreen = new JTextField(4);
        JTextField fBlue = new JTextField(4);
        JButton applyColor = new JButton("Set color");
        applyColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int red = Integer.parseInt(fRed.getText());
                    int green = Integer.parseInt(fGreen.getText());
                    int blue = Integer.parseInt(fBlue.getText());
                    shapes.get(currentId).setColor(new Color(red,green,blue));
                    paintPanel.repaint();
                }catch(Exception exc){}
            }
        });
        borderColorPanel.add(new JLabel("Red: "));
        borderColorPanel.add(fRed);
        borderColorPanel.add(new JLabel("Green: "));
        borderColorPanel.add(fGreen);
        borderColorPanel.add(new JLabel("Blue: "));
        borderColorPanel.add(fBlue);
        borderColorPanel.add(applyColor);
        if(shapes.get(currentId) instanceof Shape2D){
            JButton applyFillColor = new JButton("Set fill");
            applyFillColor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        int red = Integer.parseInt(fRed.getText());
                        int green = Integer.parseInt(fGreen.getText());
                        int blue = Integer.parseInt(fBlue.getText());
                        ((Shape2D)shapes.get(currentId)).setFillColor(new Color(red,green,blue));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });   
            borderColorPanel.add(applyFillColor);
        }
        
        shapeControl.add(movePanel);        
        shapeControl.add(borderColorPanel);
            
        if(shapes.get(currentId) instanceof Ellipse){
            JPanel ellipseControl = new JPanel();
            if(shapes.get(currentId) instanceof Circle) {
                JTextField radius = new JTextField(4);
                JButton setRadius = new JButton("Set radius");
                setRadius.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int rad = Integer.parseInt(radius.getText());
                            ((Circle) shapes.get(currentId)).setRadius(rad);
                            paintPanel.repaint();
                        } catch (Exception exc) {
                        }
                    }
                });
                ellipseControl.add(new JLabel("Radius: "));
                ellipseControl.add(radius);
                ellipseControl.add(setRadius);
            }else{
                JTextField xCoord = new JTextField(4);
                JTextField yCoord = new JTextField(4);
                JButton leftBot = new JButton("Set left bottom");
                JButton rightTop = new JButton("Set right top");
                
                leftBot.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            int x = Integer.parseInt(xCoord.getText());
                            int y = Integer.parseInt(yCoord.getText());
                            ((Ellipse)shapes.get(currentId)).setLeftBottom(new Point(x,y));
                            paintPanel.repaint();
                        }catch(Exception exc){}
                    }
                });
                rightTop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            int x = Integer.parseInt(xCoord.getText());
                            int y = Integer.parseInt(yCoord.getText());
                            ((Ellipse)shapes.get(currentId)).setRightTop(new Point(x,y));
                            paintPanel.repaint();
                        }catch(Exception exc){exc.printStackTrace();}                        
                    }
                });
                ellipseControl.add(new JLabel("X: "));
                ellipseControl.add(xCoord);
                ellipseControl.add(new JLabel("Y: "));
                ellipseControl.add(yCoord);
                ellipseControl.add(leftBot);
                ellipseControl.add(rightTop);
            }
            shapeControl.add(ellipseControl);
        }
        
        if(shapes.get(currentId) instanceof IsoscelesTriangle){
            JPanel trianglePanel = new JPanel();
            JTextField size = new JTextField(4);
            JButton setWidth = new JButton("Width");
            JButton setHeight = new JButton("Height");
            
            setWidth.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        ((IsoscelesTriangle)shapes.get(currentId)).setWidth(Integer.parseInt(size.getText()));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });
            setHeight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        ((IsoscelesTriangle)shapes.get(currentId)).setHeight(Integer.parseInt(size.getText()));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });
            
            trianglePanel.add(new JLabel("Size: "));
            trianglePanel.add(size);
            trianglePanel.add(setWidth);
            trianglePanel.add(setHeight);
            
            shapeControl.add(trianglePanel);
        }
        
        if(shapes.get(currentId) instanceof Rhomb){
            JPanel rhombPanel = new JPanel();
            JTextField size = new JTextField(4);
            JButton setWidth = new JButton("Width");
            JButton setHeight = new JButton("Height");

            setWidth.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        ((Rhomb)shapes.get(currentId)).setWidth(Integer.parseInt(size.getText()));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });
            setHeight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        ((Rhomb)shapes.get(currentId)).setHeight(Integer.parseInt(size.getText()));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });

            rhombPanel.add(new JLabel("Size: "));
            rhombPanel.add(size);
            rhombPanel.add(setWidth);
            rhombPanel.add(setHeight);

            shapeControl.add(rhombPanel);
        } else if(shapes.get(currentId) instanceof Square){
            JPanel squarePanel = new JPanel();
            JTextField side = new JTextField(4);
            JButton setSide = new JButton("Set side");
            
            setSide.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        int a = Integer.parseInt(side.getText());
                        ((Square)shapes.get(currentId)).setSide(a);
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });
            
            squarePanel.add(new JLabel("Side: " ));
            squarePanel.add(side);
            squarePanel.add(setSide);
            shapeControl.add(squarePanel);
        } else if(shapes.get(currentId) instanceof Rectangle){
            JPanel rectanglePanel = new JPanel();
            JTextField size = new JTextField(4);
            JButton setWidth = new JButton("Width");
            JButton setHeight = new JButton("Height");

            setWidth.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        ((Rectangle)shapes.get(currentId)).setHorizontal(Integer.parseInt(size.getText()));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });
            setHeight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        ((Rectangle)shapes.get(currentId)).setVertical(Integer.parseInt(size.getText()));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });

            rectanglePanel.add(new JLabel("Size: "));
            rectanglePanel.add(size);
            rectanglePanel.add(setWidth);
            rectanglePanel.add(setHeight);

            shapeControl.add(rectanglePanel);
        } else if(shapes.get(currentId) instanceof Parallelogram){
            JPanel pgmPanel = new JPanel();
            pgmPanel.setLayout(new BoxLayout(pgmPanel, BoxLayout.Y_AXIS));
            List<JTextField> x = new List<JTextField>();
            List<JTextField> y = new List<JTextField>();
            for(int i=0;i<3;i++){
                x.add(new JTextField(4));
                y.add(new JTextField(4));
            }
            
            for(int i=0;i<3;i++){
                JPanel temp = new JPanel();
                temp.add(new JLabel("X" + i + ": "));
                temp.add(x.get(i));
                temp.add(new JLabel("Y" + i + ": "));
                temp.add(y.get(i));
                pgmPanel.add(temp);
            }
            
            JButton change = new JButton("Change");
            change.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        List<Point>points = new List<Point>();
                        for(int i=0;i<3;i++){
                            int xc = Integer.parseInt(x.get(i).getText());
                            int yc = Integer.parseInt(y.get(i).getText());
                            points.add(new Point(xc,yc));
                        }
                        ((Parallelogram)shapes.get(currentId)).setPoints(points);
                    }catch(Exception exc){}
                    paintPanel.repaint();
                }
            });
            
            pgmPanel.add(change);
            shapeControl.add(pgmPanel);
        } else if(shapes.get(currentId) instanceof Polygon){
            JPanel temp = new JPanel();
            
            JTextField x = new JTextField(4);
            JTextField y = new JTextField(4);
            JButton addBut = new JButton("Add point");
            addBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        int xc = Integer.parseInt(x.getText());
                        int yc = Integer.parseInt(y.getText());
                        ((Polygon)shapes.get(currentId)).getPoints().add(new Point(xc,yc));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });
            
            temp.add(new JLabel("X: "));
            temp.add(x);
            temp.add(new JLabel("Y: "));
            temp.add(y);
            temp.add(addBut);
            
            shapeControl.add(temp);
        }

        if(shapes.get(currentId) instanceof PolyLine){
            JPanel addPane = new JPanel();
            JTextField addX = new JTextField(4);
            JTextField addY = new JTextField(4);
            JButton append = new JButton("Append {X,Y}");

            append.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PolyLine temp = (PolyLine) shapes.get(currentId);
                    try{
                        int newX = Integer.parseInt(addX.getText());
                        int newY = Integer.parseInt(addY.getText());
                        temp.getLines().add(new Segment(temp.getLines().get(temp.getLines().size()-1).getDirection().x,
                                temp.getLines().get(temp.getLines().size()-1).getDirection().y,
                                newX, newY));
                        paintPanel.repaint();
                    }catch(Exception exc){}
                }
            });

            addPane.add(new JLabel("X: "));
            addPane.add(addX);
            addPane.add(new JLabel("Y: "));
            addPane.add(addY);
            addPane.add(append);
            shapeControl.add(addPane);
        }
        
        JButton removeBut = new JButton("Remove");
        removeBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int temp = currentId;
                setSelected(-1);
                shapes.remove(temp);
                paintPanel.repaint();
            }
        });
        shapeControl.add(removeBut);
        
        shapeControl.updateUI();
    }
    
    private double distanceTo(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }
    
    private void addShapeToCanvas(Class shapeType){
        try {
            lab1.Shape newShape = (lab1.Shape)shapeType.getConstructor(int.class, int.class).newInstance(800, 400);
            shapes.add(newShape);
            paintPanel.repaint();
            setSelected(shapes.size()-1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        new MainWindow();
    }
}
