import java.awt.Point;
import java.awt.Image;
import java.awt.Graphics;
import java.util.ArrayList;

public class Katzfey extends Character {
	//location
	Point location;
	//board (so we can access its objects)
	Board board;
	//image for Katzfey
	Image image;
	//currentSprite (for switching states)
	int currentSprite = 1;
	//lasers we're shooting
	ArrayList<Laser> lasers = new ArrayList<Laser>();
	//switch which nipple the laser comes from
	int whichNip = 0;
	//is he ded
	boolean isKill;

	public Katzfey(Board board) {
		this.board = board;
		this.location = new Point(800, 150);
		//set image
		image = board.getImage("/images/katzfeySprite1.png");
		
		
	}
	
	//clear out the lasers and stuffs
	public void clear() {
		lasers.clear();
	}
	
	//See my ring chain and my Rolex when I’m flexin'
	public void switchBodyState() {
		//switch sprite between 1 and 2
		currentSprite = (currentSprite == 1) ? 2 : 1;
		//load new image
		image = board.getImage(String.format("/images/katzfeySprite%d.png", currentSprite));
	}
	
	//shoot the lasers!!!
	public void shootLaser(Graphics g) {
		//don't shoot if you're dead
		if (isKill) return;
		//lasers to remove (we cannot access and mutate the arraylist at the same time, so we'll mutate it later)
		ArrayList<Laser> lasersToRemove = new ArrayList<Laser>();
		for (Laser laser : lasers) {
			//draw the laser
			laser.draw(g);
			//calculate the slope from the angle
			double slope = Math.tan(laser.angle);
			//x displacement - maximum of 10, obeys the slope
			laser.location.x -= Math.abs(-10 * slope) > 10 ? Math.abs(10 / slope) : 10;
			//y displacement - maximum of 10, obeys the slope
			laser.location.y += Math.abs(-10 * slope) > 10 ? -10 * (slope/Math.abs(slope)) : -10 * slope;
			//if it's out of the screen, say bye bye
			if (laser.location.x <= 0 || laser.location.x >= 1000 || laser.location.y >= 600 || laser.location.y <= 0) {
				lasersToRemove.add(laser);
			}
		}
		//now actually remove the lasers that are out of the screen since we aren't accessing the arraylist anymore
		for (Laser laser : lasersToRemove) {
			lasers.remove(laser);
		}
		
	}
	
	//create a new laser
	public void prepare() {
		//calculate the start and end points
		Point chrisCenter = board.chris.center();
		Point selfCenter = this.center();
		//calculate the rotation (arc tangent of slope)
		double rotationRequired = Math.atan(((double)(selfCenter.y - chrisCenter.y)) / ((double)(selfCenter.x - chrisCenter.x)));
		//add the lasers
		lasers.add(new Laser(rotationRequired, selfCenter));
	}
	
	//change which nipple the laser shoots out of
	public Point center() {
		//ALTERNATE NIPPLES
		whichNip++;
		if (whichNip == 2) whichNip = 0;
		if (whichNip == 0)
			return new Point((int)(this.location.x + 75), (int)(this.location.y + 138));
		return new Point((int)(this.location.x + 105), (int)(this.location.y + 140));
	}
	
	
	
}