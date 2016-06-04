package shapes;

import java.awt.*;

/**
 * Created by daksenik on 01.05.2016.
 */
public class EllipsedRectangle extends Rectangle implements Shape{
    public EllipsedRectangle(int a,int b){
        super(a,b);
    }

    public void draw(Graphics g){
        super.draw(g);
        g.drawOval(10,10,height,width);
    }

    public String toString(){
        return "EllipsedRectangle";
    }
}
