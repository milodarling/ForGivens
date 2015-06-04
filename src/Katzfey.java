import java.awt.Point;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Katzfey extends Character {
	Point location;
	Board board;
	Image image;
	Laser laser;
	boolean isShootingLaser;

	public Katzfey(Board board) {
		this.board = board;
		this.location = new Point(800, 150);
		image = board.getImage("/images/katzfeySprite1.png");
		
		
	}
	
	public void shootLaser(Graphics g) {
		laser.draw(g);
		laser.location.x -= 10;
		laser.location.y -= 10 * Math.abs(Math.tan(laser.angle));
		this.isShootingLaser = true;
		if (laser.location.x <= 0) {
			this.isShootingLaser = false;
		}
		
	}
	
	public void prepare() {
		Point chrisCenter = board.chris.center();
		Point selfCenter = this.center();
		double rotationRequired = Math.atan(((double)(selfCenter.y - chrisCenter.y)) / ((double)(selfCenter.x - chrisCenter.x)));
		laser = new Laser(rotationRequired, selfCenter);
	}
	
	public Point center() {
		return new Point((int)(this.location.x + (((double)this.image.getWidth(null)) / 2)), (int)(this.location.y + (((double)this.image.getHeight(null)) / 2)));
	}
	
	
	
}