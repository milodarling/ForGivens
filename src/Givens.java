import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Point;
import java.util.ArrayList;

public class Givens implements Character {
	Image image;
	int imageNumber = 0;
	int movement = 0;
	Point location = new Point(100, MAX_Y);
	boolean increasing = true;
	boolean jumping;
	Board board;
	int jumpAmount = 70;
	boolean imOnABrick = false;
	Brick currentBrickOn;
	boolean atMinX = false;
	boolean isDead = false;
	private int jumpCount = 0;
	private static final int MAX_X = 850;
	private static final int MIN_X = 50;
	static final int MAX_Y = 350;
	
	public final Image[] images = { getImage("/images/givensW1.png"), getImage("/images/givensW2.png"), getImage("/images/givensW3.png"), getImage("/images/givensW4.png") };
	
	public Givens(Board board) {
		this.board = board;
		image = images[0];
	}
	
	public void refresh(int index) {
		image = images[index];
	}
	
	public void refresh() {
		
	}
	
	public void move(int direction) {
		DebugLog.logf("Direction: " + direction);
	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	public boolean isTouchingASpike() {
		for (Spike spike : board.spikes) {
			if (
				//right side of body
				spike.x < this.location.x + this.image.getWidth(null) - 2 && 
				//left side of body
				this.location.x + 19 < spike.endX() && 
				//feet
				this.location.y + this.image.getHeight(null) > spike.y &&
				//head
				this.location.y < spike.y + spike.image.getHeight(null)
				) {
				return true;
			}
		}
		return false;
	}
	
	public Brick aboveBrick() {
		ArrayList<Brick> bricks = new ArrayList<Brick>();
		for (Brick brick : board.bricks) {
			if (
				brick.x < this.location.x + this.image.getWidth(null) - 2 &&
				this.location.x + 19 < brick.endX() && 
				this.location.y + this.image.getHeight(null) - jumpAmount < brick.y
				) {
				bricks.add(brick);
			}
		}
		if (bricks.size() > 0) {
			Brick highestBrick = bricks.get(0);
			//int minY = highestBrick.y;
			for (Brick brick : bricks) {
				if (brick.y < highestBrick.y) {
					highestBrick = brick;
				}
			}
			return highestBrick;
		}
		return null;
	}
	
	public void doneJumping() {
		this.jumping = false;
		this.jumpCount = 0;
		this.jumpAmount = 70;
		board.play("thud");
	}
	
	public void endMovement() {
		this.movement = 0;
		board.animatingForward = false;
		this.imageNumber = 0;
		this.refresh(0);
	}
	
	public void animate() {
		this.atMinX = false;
		if (this.movement != 0) {
			this.imageNumber += 1;
			//change image state for givens
			int imageNumber = this.imageNumber % 6;
			if (imageNumber > 3) imageNumber -= 2 * (imageNumber - 3);
			if (this.imageNumber == 6)
				this.imageNumber = 0;
			//reload the image
			this.refresh(this.movement > 0 ? imageNumber : 3 - imageNumber);
			this.location.x += this.movement;
		}
		
		if (this.imOnABrick && !this.jumping && (this.aboveBrick() == null || this.aboveBrick() != this.currentBrickOn)) {
			this.jumpAmount -= 35;
			this.jumpCount = 5;
			this.jumping = true;
			//this.location.y += 40;
		}
		
		//don't let him leave the screen
		if (location.x > MAX_X) {
			location.x = MAX_X;
			board.animatingForward = true;
		} else if (location.x < MIN_X) {
			location.x = MIN_X;
			this.atMinX = true;
		}
		if (location.y > MAX_Y) {
			location.y = MAX_Y;
			board.play("thud");
		}
		
		if (this.jumping) {
			//jump around
			//going up
			if (this.jumpCount < 5) {
				this.location.y -= jumpAmount;
				this.jumpCount++;
				//acceleration due to gravity!
				jumpAmount -= 7;
			//going down
			} else if (this.jumpCount < 10 || this.imOnABrick) {
				//acceleration due to gravity
				jumpAmount += 7;
				this.jumpCount++;
				this.location.y += jumpAmount;
				if (this.location.y > MAX_Y) {
					this.location.y = MAX_Y;
					doneJumping();
					this.imOnABrick = false;
				}
				Brick aboveBrick = this.aboveBrick();
				if (aboveBrick != null && aboveBrick.y < this.location.y + (jumpAmount + 7) + this.image.getHeight(null)) {
					this.location.y = aboveBrick.y - this.image.getHeight(null);
					doneJumping();
					this.currentBrickOn = aboveBrick;
					this.imOnABrick = true;
				}
			//done jumping
			} else {
				doneJumping();
			}
		}
	}
}
