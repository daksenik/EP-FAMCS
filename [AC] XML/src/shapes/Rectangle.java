package shapes;

import java.awt.*;

/**
 * Created by daksenik on 01.05.2016.
 */
public class Rectangle implements Shape{
    int width;
    int height;
    public Rectangle(int a,int b){
        height = a;
        width = b;
    }

    public int getA(){
        return width;
    }
    public int getB(){
        return height;
    }
    public void setA(int na){
        width = na;
    }
    public void setB(int nb){
        height = nb;
    }

    public double measure(){
        return height*width;
    }

    public void draw(Graphics g){
        g.drawRect(10,10,height,width);
    }

    public String toString(){
        return "Rectangle";
    }

    public String getParameterString(){
        return "Width: "+Integer.toString(width)+" \nHeight: "+Integer.toString(height);
    }
    public void setParameterString(String s){
        String[]pars = s.split(" ");
        if(pars.length<4)return;
        int nWidth,nHeight;
        try{
            nWidth = Integer.parseInt(pars[1]);
            nHeight = Integer.parseInt(pars[3]);
        }catch(Exception e){return;}
        width = nWidth;
        height = nHeight;
    }
}
