package lab1;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author user
 * @version 1.0
 * @created 18-???-2017 22:10:30
 */
public class Rhomb extends Parallelogram {

	private int width;
	private int height;

	public Rhomb(int x, int y, int width, int height){
	    super(x,y);
	    this.points = new ArrayList<>(4);
	    for(int i = 0; i < 4; i++) points.add(null);
	    this.width = width;
	    this.height = height;
	    countPoints();
    }
	
	public Rhomb(int x,int y){
		this(x,y,20,40);
	}

	/**
	 * 
	 * @param p
	 */
	public void setPoints(ArrayList<Point> p){
	    if(p.size() != 4) return;
        int a = (int)Point.distance(p.get(0).x, p.get(0).y, p.get(1).x, p.get(1).y);
        if(((int)Point.distance(p.get(1).x, p.get(1).y, p.get(2).x, p.get(2).y)) != a) return;
        if(((int)Point.distance(p.get(2).x, p.get(2).y, p.get(3).x, p.get(3).y)) != a) return;
        if(((int)Point.distance(p.get(3).x, p.get(3).y, p.get(0).x, p.get(0).y)) != a) return;
        this.points = (ArrayList<Point>)p.clone();
	}

	/**
	 * 
	 * @param newWidth
	 */
	public void setWidth(int newWidth){
        this.width = newWidth;
        countPoints();
	}
	
	public void setHeight(int newHeight){
	    this.height = newHeight;
	    countPoints();
    }
    
    public int getWidth(){ return width; }
    
    public int getHeight() { return height; }

    
    private void countPoints(){
        this.points.set(0, new Point(theCenter.x, theCenter.y - height/2));
        this.points.set(1, new Point(theCenter.x + width/2, theCenter.y));
        this.points.set(2, new Point(theCenter.x, theCenter.y + height/2));
        this.points.set(3, new Point(theCenter.x - width/2, theCenter.y));
    }
}