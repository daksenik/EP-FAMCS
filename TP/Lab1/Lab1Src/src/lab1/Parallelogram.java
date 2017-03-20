package lab1;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Parallelogram extends SymmetricPolygon {

	public Parallelogram(Point a, Point b, Point c){
		super(a.x, a.y);
		this.points = new ArrayList<>();
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(new Point(a.x + (c.x-b.x), a.y));
	}
	public Parallelogram(int x, int y){
		this(new Point(x,y),new Point(x+10,y-20), new Point(x+50, y-20));
	}

	public void move(Point newLocation){
		super.move(newLocation);
	}
	
	/**
	 * 
	 * @param p
	 */
	public void setPoints(ArrayList<Point> p){
		if(p.size() != 3)return;
		this.points = (ArrayList<Point>)p.clone();
		this.points.add(new Point(p.get(0).x + (p.get(2).x-p.get(1).x), p.get(0).y));
		this.theCenter = this.points.get(0);	
	}

}