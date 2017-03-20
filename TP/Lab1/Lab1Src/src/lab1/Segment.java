package lab1;

import java.awt.*;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Segment extends Ray {

	public Segment(int x, int y, int xd, int yd){
		super(x,y,xd,yd);
	}
	public Segment(int x, int y){
		super(x,y);
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		g.drawLine(theCenter.x, theCenter.y, direction.x, direction.y);
	}

}