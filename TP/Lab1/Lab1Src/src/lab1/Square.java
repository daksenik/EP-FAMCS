package lab1;

/**
 * @author user
 * @version 1.0
 * @created 18-���-2017 22:10:30
 */
public class Square extends Rectangle {

	private double side;

	public Square(int x, int y, int side){
		super(x,y,side,side);
		this.side = side;
	}
	
	public Square(int x, int y){
		this(x,y,20);
	}

	public double getSide(){
		return getVertical();
	}

	/**
	 * 
	 * @param d
	 */
	public void setSide(int d){
		setHorizontal(d);
		setVertical(d);
	}

}