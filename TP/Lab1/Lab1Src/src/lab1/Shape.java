package lab1;

import java.awt.*;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public abstract class Shape {

	protected Color color = Color.BLACK;
	protected Point theCenter;

	public Shape(int x, int y){
		theCenter = new Point(x,y);
	}

	/**
	 * 
	 * @param g
	 */
	abstract public void draw(Graphics g);

	public Color getColor(){
		return color;
	}

	public Point location(){
		return theCenter;
	}

	/**
	 * 
	 * @param newLocation
	 */
	public void move(Point newLocation){
		this.theCenter = newLocation;
	}

	/**
	 * 
	 * @param newColor
	 */
	public void setColor(Color newColor){
		this.color = newColor;
	}

}