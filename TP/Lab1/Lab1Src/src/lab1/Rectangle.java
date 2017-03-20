package lab1;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author user
 * @version 1.0
 * @created 18-???-2017 22:10:30
 */
public class Rectangle extends Parallelogram {

	private int horizontalSide;
	private int verticalSide;

	public Rectangle(int x, int y, int width, int height){
		super(new Point(x,y), new Point(x,y-height), new Point(x+width, y-height));
		horizontalSide = width;
		verticalSide = height;
	}
	
	public Rectangle(int x, int y){
		this(x,y,20,10);
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		super.draw(g);
	}

	public int getHorizontal(){
		return horizontalSide;
	}

	public int getVertical(){
		return verticalSide;
	}

	/**
	 * 
	 * @param d
	 */
	public void setHorizontal(int d){
		this.horizontalSide = d;
		points.get(2).x = theCenter.x+d;
		points.get(3).x = theCenter.x+d;
	}

	/**
	 * 
	 * @param p
	 */
	public void setPoints(ArrayList<Point> p){
		if(p.size() != 4) return;
		if(((int)Point.distance(p.get(0).x, p.get(0).y, p.get(1).x, p.get(1).y)) != ((int)(Point.distance(p.get(2).x, p.get(2).y, p.get(3).x, p.get(3).y)))) return;
		if(((int)Point.distance(p.get(1).x, p.get(1).y, p.get(2).x, p.get(2).y)) != ((int)(Point.distance(p.get(3).x, p.get(3).y, p.get(0).x, p.get(0).y)))) return;
		this.points = (ArrayList<Point>)p.clone();
		verticalSide = (int)Point.distance(p.get(0).x, p.get(0).y, p.get(1).x, p.get(1).y);
		horizontalSide = (int)Point.distance(p.get(1).x, p.get(1).y, p.get(2).x, p.get(2).y);
	}

	/**
	 * 
	 * @param d
	 */
	public void setVertical(int d){
		this.verticalSide = d;
		points.get(1).y = theCenter.y + d;
		points.get(2).y = theCenter.y + d;
	}

}