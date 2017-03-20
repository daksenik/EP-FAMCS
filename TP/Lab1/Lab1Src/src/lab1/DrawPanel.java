package lab1;

import lab1.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by user on 19.03.2017.
 */
public class DrawPanel extends JPanel {
    ArrayList<lab1.Shape> shapesList;
    
    public DrawPanel(ArrayList<lab1.Shape>list){
        super();
        shapesList = list;
    }
    
    public void paint(Graphics g){
        super.paint(g);
        shapesList.forEach(o -> {
            g.setColor(o.getColor());
            o.draw(g);
            g.setColor(Color.BLACK);
        });
    }
}
