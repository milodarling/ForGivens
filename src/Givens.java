import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Point;

public class Givens implements Character {
	Image image;
	int imageNumber = 0;
	int movement = 0;
	Point location = new Point(100, 275);
	boolean increasing = true;
	boolean jumping;
	Board board;
	int jumpAmount = 70;
	boolean imOnABrick = false;
	private int jumpCount = 0;
	private static final int MAX_X = 350;
	private static final int MIN_X = 50;
	
	public final Image[] images = { getImage("/images/givensW1.png"), getImage("/images/givensW2.png"), getImage("/images/givensW3.png"), getImage("/images/givensW4.png") };
	
	public Givens(Board board) {
		this.board = board;
		image = images[0];
	}
	
	public void refresh(int index) {
		System.out.println(index);
		image = images[index];
	}
	
	public void refresh() {
		
	}
	
	public void move(int direction) {
		System.out.println("Direction: " + direction);
	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	public Brick aboveBrick() {
		for (Brick brick : board.bricks) {
			//System.out.printf("this.location.y: %d, this.image.getHeight(null): %d, sum: %d, brick.y: %d\n", this.location.y, this.image.getHeight(null), this.location.y + this.image.getHeight(null), brick.y);
			if (brick.x < this.location.x + this.image.getWidth(null) && this.location.x < brick.endX() && this.location.y + this.image.getHeight(null) - jumpAmount < brick.y) {
				System.out.printf("(%d (brick.x) < %d (%d (this.location.x) + %d (this.image.getWidth(null))) && %d (this.location.x) < %d (brick.endX))", brick.x, (this.location.x + (int)this.image.getWidth(null)), this.location.x, this.image.getWidth(null), this.location.x, brick.endX());
				return brick;
			}
		}
		return null;
	}
	
	public void doneJumping() {
		this.jumping = false;
		this.jumpCount = 0;
		this.jumpAmount = 70;
	}
	
	public void animate() {
		if (this.movement != 0) {
			this.imageNumber += 1;
			//change image state for givens
			int imageNumber = this.imageNumber % 6;
			if (imageNumber > 3) imageNumber -= 2 * (imageNumber - 3);
			//System.out.println(imageNumber);
			if (this.imageNumber == 6)
				this.imageNumber = 0;
			//reload the image
			this.refresh(this.movement > 0 ? imageNumber : 3 - imageNumber);
			this.location.x += this.movement;
		}
		
		if (this.imOnABrick && !this.jumping && this.aboveBrick() == null) {
			this.location.y += 40;
		}
		
		//don't let him leave the screen
		if (location.x > MAX_X) {
			location.x = MAX_X;
			board.animatingForward = true;
		} else if (location.x < MIN_X) {
			location.x = MIN_X;
		}
		if (location.y > 275) {
			location.y = 275;
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
				System.out.println("[Givens.java:111] Falling");
				//acceleration due to gravity
				jumpAmount += 7;
				this.jumpCount++;
				this.location.y += jumpAmount;
				if (this.location.y > 275) {
					this.location.y = 275;
					doneJumping();
					this.imOnABrick = false;
				}
				Brick aboveBrick = this.aboveBrick();
				System.out.println("[Given.java:104] aboveBrick: " + aboveBrick);
				if (aboveBrick != null && aboveBrick.y < this.location.y + (jumpAmount + 7) + this.image.getHeight(null)) {
					this.location.y = aboveBrick.y - this.image.getHeight(null);
					doneJumping();
					this.imOnABrick = true;
				}
			//done jumping
			} else {
				System.out.println("[Givens.java:111] Done jumping");
				doneJumping();
			}
		}
	}
}
