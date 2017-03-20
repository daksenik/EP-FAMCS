package lab1;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class IsoscelesTriangle extends SymmetricPolygon {

	public IsoscelesTriangle(int x, int y, int width, int height){
		super(x,y);
		points = new ArrayList<>();
		points.add(new Point(x,y));
		points.add(new Point(x-width/2,y+height));
		points.add(new Point(x+width/2,y+height));
	}
	
	public IsoscelesTriangle(int x, int y){
		this(x,y,40,20);
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		super.draw(g);
	}

	/**
	 * 
	 * @param p
	 */
	public void setPoints(ArrayList<Point> p){
		if(p.size() != 3)return;
		if(((int)Point.distance(p.get(0).x,p.get(0).y,p.get(1).x,p.get(1).y)) != ((int)Point.distance(p.get(0).x,p.get(0).y,p.get(2).x,p.get(2).y)))return;
		this.points = (ArrayList<Point>)p.clone();
	}
	
	public void setHeight(int newHeight){
		points.get(1).y += newHeight-(points.get(1).y-points.get(0).y);
		points.get(2).y = points.get(1).y;
	}
	
	public void setWidth(int newWidth){
		points.get(1).x = points.get(0).x - newWidth/2;
		points.get(2).x = points.get(0).x + newWidth/2;
	}

}