package lab1;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class PolyLine extends Shape{

	private ArrayList<Segment> lines;

	public PolyLine(int x, int y){
		super(x,y);
		lines = new ArrayList<>();
		lines.add(new Segment(x-10, y+10, x,y));
		lines.add(new Segment(x,y));
	}

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g){
		lines.forEach(l -> l.draw(g));
	}

	public ArrayList<Segment> getLines(){
		return lines;
	}

	/**
	 * 
	 * @param newPoint
	 */
	public void move(Point newPoint){
		int mid = lines.size()/2;
		Segment center = lines.get(mid);
		int deltaX = newPoint.x - center.location().x, deltaY = newPoint.y - center.location().y;
		lines.forEach(s -> {
			s.move(new Point(s.location().x + deltaX, s.location().y + deltaY));
		});
		
	}

	/**
	 * 
	 * @param newLines
	 */
	public void setLines(ArrayList<Segment> newLines){
		this.lines = (ArrayList)(newLines.clone());
		int mid = lines.size()/2;
		Segment center = lines.get(mid);
		this.theCenter = center.location();
	}

}