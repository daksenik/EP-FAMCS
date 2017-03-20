package lab1;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Polygon extends Shape2D {
	
	protected ArrayList<Point> points = new ArrayList<>();

	public Polygon(int x,int y){
		super(x,y);
		points.add(new Point(x,y));
		points.add(new Point(x+10,y));
		points.add(new Point(x+20,y+10));
		points.add(new Point(x+20,y+20));
		points.add(new Point(x+10,y+30));
		points.add(new Point(x,y+30));
		points.add(new Point(x-10,y+20));
		points.add(new Point(x-10,y+10));
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		int[]x = points.stream().mapToInt(p -> p.x).toArray();
		int[]y = points.stream().mapToInt(p -> p.y).toArray();
		Color temp = g.getColor();
		g.setColor(fillColor);
		g.fillPolygon(x,y,x.length);
		g.setColor(temp);
		g.drawPolygon(x,y,x.length);
	}

	public ArrayList<Point> getPoints(){
		return points;
	}

	/**
	 * 
	 * @param newLocation
	 */
	public void move(Point newLocation){
		int deltaX = newLocation.x - theCenter.x, deltaY = newLocation.y - theCenter.y;
		theCenter.x = newLocation.x;
		theCenter.y = newLocation.y;
		points.forEach(p -> {
			p.x += deltaX;
			p.y += deltaY;
		});
	}

	/**
	 * 
	 * @param p
	 */
	public void setPoints(ArrayList<Point> p){
		this.points = (ArrayList<Point>)p.clone();
	}

}