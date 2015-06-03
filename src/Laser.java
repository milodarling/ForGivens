import java.awt.Point;
public class Laser {
	Point end;
	Point start;
	double slope;

	public Laser(Givens chris, Katzfey eric) {
		start = eric.center;
		end = chris.location;
		slope = (end.y - start.y)/(end.x - start.x);
	}

}
