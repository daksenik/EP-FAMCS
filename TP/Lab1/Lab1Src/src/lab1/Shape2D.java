package lab1;

import java.awt.*;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public abstract class Shape2D extends Shape {

	protected Color fillColor = Color.BLACK;

	public Shape2D(int x,int y){
		super(x,y);
	}

	public Color getFillColor(){
		return fillColor;
	}

	/**
	 * 
	 * @param newColor
	 */
	public void setFillColor(Color newColor){
		this.fillColor = newColor;
	}

}