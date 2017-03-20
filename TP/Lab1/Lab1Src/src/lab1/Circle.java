package lab1;

import java.awt.*;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Circle extends Ellipse {

	private int radius;

	public Circle(int x, int y, int radius){
		super(x,y,radius/2,radius/2);
	}
	public Circle(int x, int y){
		super(x,y, 20, 20);
	}

	public double getRadius(){
		return radius;
	}

	/**
	 * 
	 * @param newRadius
	 */
	public void setRadius(int newRadius){
		this.radius = newRadius;
		this.leftBottomPoint.x = theCenter.x-radius;
		this.leftBottomPoint.y = theCenter.y-radius;
		this.rightTopPoint.x = theCenter.x+radius;
		this.rightTopPoint.y = theCenter.y+radius;
	}
}