

/**
 * @author user
 * @version 1.0
 * @created 20-мар-2017 17:06:14
 */
public abstract class Shape2D extends Shape {

	protected Color fillColor = Color.BLACK;

	public Shape2D(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Shape2D(int x, int y){

	}

	public Color getFillColor(){
		return null;
	}

	/**
	 * 
	 * @param newColor    newColor
	 */
	public void setFillColor(Color newColor){

	}

}