

/**
 * @author user
 * @version 1.0
 * @created 20-мар-2017 17:06:14
 */
public abstract class Shape {

	protected Color color = Color.BLACK;
	protected Point theCenter;

	public Shape(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Shape(int x, int y){

	}

	/**
	 * 
	 * @param g    g
	 */
	public abstract void draw(Graphics g);

	public Color getColor(){
		return null;
	}

	public Point location(){
		return null;
	}

	/**
	 * 
	 * @param newLocation    newLocation
	 */
	public void move(Point newLocation){

	}

	/**
	 * 
	 * @param newColor    newColor
	 */
	public void setColor(Color newColor){

	}

}