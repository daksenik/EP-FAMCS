package lab1;

import java.awt.*;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Line extends Shape {

	protected Point direction;

	public Line(int x, int y, int x1, int y1){
		super(x,y);
		direction = new Point(x1,y1);
	}
	public Line(int x, int y){
		this(x,y,x+10,y+10);
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		int deltaX = direction.x - theCenter.x, deltaY = direction.y - theCenter.y;
		int curX = theCenter.x, curY = theCenter.y;
		for(int i=0;i<100;i++){
			g.drawLine(curX, curY, curX + deltaX, curY + deltaY);
			curX += deltaX;
			curY += deltaY;
		}
		curX = theCenter.x;
		curY = theCenter.y;
		for(int i=0;i<100;i++){
			g.drawLine(curX, curY, curX - deltaX, curY - deltaY);
			curX -= deltaX;
			curY -= deltaY;
		}
	}

	public Point getDirection(){
		return direction;
	}

	/**
	 * 
	 * @param newDirection
	 */
	public void setDirection(Point newDirection){
		this.direction.x = newDirection.x;
		this.direction.y = newDirection.y;
	}
	
	public void move(Point newPoint){
		int deltaX = newPoint.x - this.theCenter.x, deltaY = newPoint.y - this.theCenter.y;
		this.theCenter.x += deltaX;
		this.theCenter.y += deltaY;
		this.direction.x += deltaX;
		this.direction.y += deltaY;
	}

}