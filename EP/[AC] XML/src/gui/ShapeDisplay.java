package gui;

import shapes.Shape;

import javax.swing.*;
import java.awt.*;

/**
 * Created by daksenik on 02.05.2016.
 */
public class ShapeDisplay extends JPanel {
    Shape drawableContent;
    public ShapeDisplay(){
        super();
        drawableContent = null;
    }

    @Override
    public void paint(Graphics g){
        if(g==null)return;
        g.setColor(Color.WHITE);
        g.fillRect(0,0,1000,1000);
        g.setColor(Color.RED);
        if(drawableContent!=null)drawableContent.draw(g);
    }

    @Override
    public void repaint(){
        paint(getGraphics());
    }

    public void setDrawableContent(Shape s){
        drawableContent = s;
    }
}
