package lab1;

import java.awt.*;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Ray extends Line {

	public Ray(int x, int y, int xd, int yd){
		super(x,y,xd,yd);
	}
	public Ray(int x, int y){
		super(x,y);
	}
	
	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		int deltaX = direction.x - theCenter.x, deltaY = direction.y - theCenter.y, curX = theCenter.x, curY = theCenter.y;
		for(int i=0;i<100;i++){
			g.drawLine(curX,curY,curX + deltaX, curY + deltaY);
			curX += deltaX;
			curY += deltaY;
		}
	}

}