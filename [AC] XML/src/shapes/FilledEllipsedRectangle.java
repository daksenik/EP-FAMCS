package shapes;

import java.awt.*;

/**
 * Created by daksenik on 02.05.2016.
 */
public class FilledEllipsedRectangle extends EllipsedRectangle implements Shape {
    Color fillColor;
    public FilledEllipsedRectangle(int width, int height, Color color){
        super(width,height);
        fillColor = color;
    }

    @Override
    public void draw(Graphics g){
        super.draw(g);
        g.setColor(fillColor);
        g.fillOval(10,10,height,width);
    }

    @Override
    public String toString(){
        return "FilledEllipsedRectangle";
    }

    public String getParameterString(){
        return super.getParameterString()+" \nRED: "+fillColor.getRed()+" \nGREEN: "+fillColor.getGreen()+" \nBLUE: "+fillColor.getBlue();
    }

    public void setParameterString(String s){
        String[]pars = s.split(" ");
        if(pars.length<10)return;
        super.setParameterString(s.substring(0,s.indexOf("RED")));
        int red,green,blue;
        try{
            red = Integer.parseInt(pars[5]);
            green = Integer.parseInt(pars[7]);
            blue = Integer.parseInt(pars[9]);
        }catch(Exception e){return;}
        fillColor = new Color(red,green,blue);
    }

    public Color getColor(){return fillColor;}
}
