package shapes;

import java.awt.*;

/**
 * Created by daksenik on 01.05.2016.
 */
public class Parabola implements Shape {
    double a,b,c;
    double dx;
    public Parabola(double a,double b,double c,double delta){
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.dx = delta;
    }

    public double getA(){return a;}
    public double getB(){return b;}
    public double getC(){return c;}
    public double getD(){return dx;}

    public void setA(double na){a = na;}
    public void setB(double nb){b = nb;}
    public void setC(double nc){c = nc;}
    public void setD(double nd){dx = nd;}

    private double function(double x){
        return a*x*x+b*x+c;
    }

    public void draw(Graphics g){
        double from = -dx/2,to = dx/2;
        double maxY = a>0?function(from):function(-b/(2*a));
        for(int x = (int)from;x<=to;x++)g.drawLine(x-1+(int)dx/2,(int)function(x-1),x+(int)dx/2,(int)function(x));
    }

    public double measure(){
        return (a*Math.pow(-dx/2,3)/3+b*Math.pow(-dx/2,2)/2+c*(-dx/2))-(a*Math.pow(dx/2,3)/3+b*Math.pow(dx/2,2)/2+c*(dx/2));
    }

    public String toString(){
        return "Parabola";
    }

    public String getParameterString(){
        return "A: "+Double.toString(a)+" \nB: "+Double.toString(b)+" \nC: "+Double.toString(c)+" \nDelta: "+Double.toString(dx);
    }

    public void setParameterString(String s){
        String[]pars = s.split(" ");
        if(pars.length<8)return;
        double na,nb,nc,ndx;
        try{
            na = Double.parseDouble(pars[1]);
            nb = Double.parseDouble(pars[3]);
            nc = Double.parseDouble(pars[5]);
            ndx = Double.parseDouble(pars[7]);
        }catch(Exception e){return;}
        a = na;
        b = nb;
        c = nc;
        dx = ndx;
    }
}
