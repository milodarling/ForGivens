import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Point;
import java.util.ArrayList;

public class Givens extends Character {
	int imageNumber = 0;
	int movement = 0;
	boolean increasing = true;
	boolean jumping;
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
		location = new Point(100, MAX_Y);
		this.board = board;
		image = images[0];
	}
	
	public void refresh(int index) {
		image = images[index];
	}
	
	public boolean gotShotByALaser() {
		for (Laser laser : board.eric.lasers) {
			if (
					//right side of body
					laser.location.x < this.location.x + this.image.getWidth(null) - 2 && 
					//left side of body
					this.location.x + 19 < laser.location.x + laser.image.getWidth(null) && 
					//feet
					this.location.y + this.image.getHeight(null) > laser.location.y &&
					//head
					this.location.y < laser.location.y + laser.image.getHeight(null)
					) {
					return true;
				}
		}
		return false;
	}
	
	public Point center() {
		DebugLog.logf("X: this.location.x: %d, this.image.getWidth(null)/2: %f, sum: %d", this.location.x, (((double)this.image.getWidth(null)) / 2),  (int)(this.location.x + (((double)this.image.getWidth(null)) / 2)));
		DebugLog.logf("Y: this.location.y: %d, this.image.getHeight(null)/2: %f, sum: %d", this.location.y, (((double)this.image.getHeight(null)) / 2), (int)(this.location.y + (((double)this.image.getHeight(null)) / 2)));
		return new Point((int)(this.location.x + (((double)this.image.getWidth(null)) / 2)), (int)(this.location.y + (((double)this.image.getHeight(null)) / 2)));
	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	public void youveGotMail() {
		Mail result = null;
		for (Mail mail : board.mails) {
			if (
					//right side of body
					mail.x < this.location.x + this.image.getWidth(null) - 2 && 
					//left side of body
					this.location.x + 19 < mail.x + mail.image.getWidth(null) && 
					//feet
					this.location.y + this.image.getHeight(null) > mail.y &&
					//head
					this.location.y < mail.y + mail.image.getHeight(null)
				) {
				result = mail;
			}
		}
		if (result != null) {
			board.mails.remove(result);
			board.unreadEmails++;
			board.play("mail");
		}
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
		//this.jumping = false;
		this.refresh(0);
	}
	
	public boolean checkForKillz() {
		if (
				jumping &&
				board.eric.location.x + 100 < this.location.x + this.image.getWidth(null) - 2 &&
				this.location.x + 19 < board.eric.location.x + board.eric.image.getWidth(null) - 81 && 
				this.location.y + this.image.getHeight(null) < board.eric.location.y &&
				this.location.y + this.image.getHeight(null) + jumpAmount > board.eric.location.y
			) {
			board.eric.lasers.clear();
			board.eric.isKill = true;
			return true;
		}
		return false;
	}
	
	//public 
	
	public void animate() {
		youveGotMail();
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
