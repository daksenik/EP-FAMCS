package lab1;

import java.awt.*;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Ellipse extends Shape2D {

	protected Point leftBottomPoint;
	protected Point rightTopPoint;

	public Ellipse(int x, int y, int width, int height){
		super(x,y);
		leftBottomPoint = new Point(x - width/2, y - height/2);
		rightTopPoint = new Point(x + width/2, y + height/2);
	}
	
	public Ellipse(int x, int y){
		this(x,y,20,10);
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		theCenter.x = (leftBottomPoint.x + rightTopPoint.x)/2;
		theCenter.y = (leftBottomPoint.y + rightTopPoint.y)/2;
		Color temp = g.getColor();
		g.setColor(fillColor);
		g.fillOval(leftBottomPoint.x, leftBottomPoint.y, rightTopPoint.x - leftBottomPoint.x, rightTopPoint.y - leftBottomPoint.y);
		g.setColor(temp);		
		g.drawOval(leftBottomPoint.x, leftBottomPoint.y, rightTopPoint.x - leftBottomPoint.x, rightTopPoint.y - leftBottomPoint.y);
	}

	public Point getLeftBottom(){
		return leftBottomPoint;
	}

	public Point getRightTop(){
		return rightTopPoint;
	}

	/**
	 * 
	 * @param newLocation
	 */
	public void move(Point newLocation){
		int width = (theCenter.x-leftBottomPoint.x)*2, height = (theCenter.y-leftBottomPoint.y)*2;
		theCenter.x = newLocation.x;
		theCenter.y = newLocation.y;
		leftBottomPoint.x = theCenter.x-width/2;
		leftBottomPoint.y = theCenter.y-height/2;
		rightTopPoint.x = theCenter.x+width/2;
		rightTopPoint.y = theCenter.y+height/2;
	}

	/**
	 * 
	 * @param newPoint
	 */
	public void setLeftBottom(Point newPoint){
		leftBottomPoint.x = newPoint.x;
		leftBottomPoint.y = newPoint.y;
		theCenter.x = (leftBottomPoint.x + rightTopPoint.x)/2;
		theCenter.y = (leftBottomPoint.y + rightTopPoint.y)/2;
	}

	/**
	 * 
	 * @param newPoint
	 */
	public void setRightTop(Point newPoint){
		rightTopPoint.x = newPoint.x;
		rightTopPoint.y = newPoint.y;
		theCenter.x = (leftBottomPoint.x + rightTopPoint.x)/2;
		theCenter.y = (leftBottomPoint.y + rightTopPoint.y)/2;
	}

}