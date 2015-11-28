package montecarlo;

/**
 * Diese Klasse repraesentiert einen Punkt mit seinen (x,y)-Koordinaten.
 * 
 * @author Lukas Schramm
 * @version 1.0
 * 
 */
public class Punkt {
	
	private double x = 0;
	private double y = 0;
	
	public Punkt(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}