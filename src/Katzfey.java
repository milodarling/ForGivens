import java.awt.Point;
import java.awt.Image;
import java.awt.Graphics;
import java.util.ArrayList;

public class Katzfey extends Character {
	Point location;
	Board board;
	Image image;
	int currentSprite = 1;
	//Laser laser;
	boolean isShootingLaser;
	ArrayList<Laser> lasers = new ArrayList<Laser>();
	int whichNip = 0;
	boolean isKill;

	public Katzfey(Board board) {
		this.board = board;
		this.location = new Point(800, 150);
		
		image = board.getImage("/images/katzfeySprite1.png");
		
		
	}
	
	public void clear() {
		lasers.clear();
	}
	
	public void switchBodyState() {
		//switch sprite between 1 and 2
		currentSprite = (currentSprite == 1) ? 2 : 1;
		//load new image
		image = board.getImage(String.format("/images/katzfeySprite%d.png", currentSprite));
	}
	
	public void shootLaser(Graphics g) {
		if (isKill) return;
		ArrayList<Laser> lasersToRemove = new ArrayList<Laser>();
		for (Laser laser : lasers) {
			laser.draw(g);
			double slope = Math.tan(laser.angle);
			laser.location.x -= Math.abs(-10 * slope) > 10 ? Math.abs(10 / slope) : 10;
			laser.location.y += Math.abs(-10 * slope) > 10 ? -10 * (slope/Math.abs(slope)) : -10 * slope;
			this.isShootingLaser = true;
			if (laser.location.x <= 0 || laser.location.x >= 1000 || laser.location.y >= 600 || laser.location.y <= 0) {
				lasersToRemove.add(laser);
			}
		}
		for (Laser laser : lasersToRemove) {
			lasers.remove(laser);
		}
		
	}
	
	public void prepare() {
		Point chrisCenter = board.chris.center();
		Point selfCenter = this.center();
		double rotationRequired = Math.atan(((double)(selfCenter.y - chrisCenter.y)) / ((double)(selfCenter.x - chrisCenter.x)));
		lasers.add(new Laser(rotationRequired, selfCenter));
	}
	
	public Point center() {
		//ALTERNATE NIPPLES
		whichNip++;
		if (whichNip == 2) whichNip = 0;
		if (whichNip == 0)
			return new Point((int)(this.location.x + 75), (int)(this.location.y + 138));
		return new Point((int)(this.location.x + 105), (int)(this.location.y + 140));
	}
	
	
	
}