import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Point;
import java.util.ArrayList;

public class Givens extends Character {
	//used to switch off walking states
	int imageNumber = 0;
	//how far forwards or backwards he is moving
	int movement = 0;
	//if he is jumping (or freefalling)
	boolean jumping;
	//how far he jumps (initially, he experiences downward acceleration)
	int jumpAmount = 70;
	//if he is standing on a brick
	boolean imOnABrick = false;
	//the brick he is standing on
	Brick currentBrickOn;
	//if he is as far back as we'll let him
	boolean atMinX = false;
	//if he's dead :(
	boolean isDead = false;
	//used to know when to move up, down, accelerate, etc.
	private int jumpCount = 0;
	//maximum displacements
	private static final int MAX_X = 850;
	private static final int MIN_X = 50;
	static final int MAX_Y = 350;
	
	//walking states
	public final Image[] images = { getImage("/images/givensW1.png"), getImage("/images/givensW2.png"), getImage("/images/givensW3.png"), getImage("/images/givensW4.png") };
	
	
	public Givens(Board board) {
		location = new Point(100, MAX_Y);
		this.board = board;
		image = images[0];
	}
	
	//load the new image (to change walking states)
	public void refresh(int index) {
		image = images[index];
	}
	
	//check if a laser is touching us
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
	
	//return the midpoint of Givens
	public Point center() {
		return new Point((int)(this.location.x + (((double)this.image.getWidth(null)) / 2)), (int)(this.location.y + (((double)this.image.getHeight(null)) / 2)));
	}
	
	//grab image from file
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	//check if we're touching an envelope
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
			//remove it from the array (so it's no longer drawn)
			board.mails.remove(result);
			//increment the number of unread emails
			board.unreadEmails++;
			//play "you've got mail"
			board.play("mail");
		}
	}
	
	//check if we are touching a spike
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
	
	//check if we are above a brick, and return the instance of that brick
	public Brick aboveBrick() {
		ArrayList<Brick> bricks = new ArrayList<Brick>();
		for (Brick brick : board.bricks) {
			if (
				//if we are parallel with it
				brick.x < this.location.x + this.image.getWidth(null) - 2 &&
				this.location.x + 19 < brick.endX() && 
				//and above it
				this.location.y + this.image.getHeight(null) - jumpAmount < brick.y
				) {
				bricks.add(brick);
			}
		}
		//return the highest brick; that's what we want to land on
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
	
	//clean up jumping ivars and whatnot
	public void doneJumping() {
		this.jumping = false;
		this.jumpCount = 0;
		this.jumpAmount = 70;
		board.play("thud");
	}
	
	//stop moving and change image to standing
	public void endMovement() {
		this.movement = 0;
		board.animatingForward = false;
		this.imageNumber = 0;
		this.refresh(0);
	}
	
	//check if we killed Katzfey
	public boolean checkForKillz() {
		if (
				//we've got to be in the air to land on him
				jumping &&
				//check if we are parallel to him
				board.eric.location.x + 100 < this.location.x + this.image.getWidth(null) - 2 &&
				this.location.x + 19 < board.eric.location.x + board.eric.image.getWidth(null) - 81 && 
				
				//above him
				this.location.y + this.image.getHeight(null) < board.eric.location.y &&
				//and we're going to land on him
				this.location.y + this.image.getHeight(null) + jumpAmount > board.eric.location.y
			) {
			//get rid of the lasers
			board.eric.lasers.clear();
			//set him as dead
			board.eric.isKill = true;
			return true;
		}
		return false;
	}
	 
	//called by Board.paint()
	public void animate() {
		//check for coins (envelopes)
		youveGotMail();
		//allows us to move
		this.atMinX = false;
		
		//if we should be moving
		if (this.movement != 0) {
			//change image state for givens
			this.imageNumber += 1;
			int imageNumber = this.imageNumber % 6;
			if (imageNumber > 3) imageNumber -= 2 * (imageNumber - 3);
			if (this.imageNumber == 6)
				this.imageNumber = 0;
			//reload the image (do the opposite image if we're moving backwards)
			this.refresh(this.movement > 0 ? imageNumber : 3 - imageNumber);
			//move the character
			this.location.x += this.movement;
		}
		
		//if we were on a brick, and we are no longer on that same brick, fall down
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
		//if he is falling and goes below the threshold, set him on the floow
		if (location.y > MAX_Y) {
			location.y = MAX_Y;
			board.play("thud");
		}
		
		//if he's jumping
		if (this.jumping) {
			//jump around
			//going up
			if (this.jumpCount < 5) {
				//move up
				this.location.y -= jumpAmount;
				this.jumpCount++;
				//acceleration due to gravity!
				jumpAmount -= 7;
			//going down
			} else if (this.jumpCount < 10 || this.imOnABrick) {
				//acceleration due to gravity
				jumpAmount += 7;
				this.jumpCount++;
				//move down
				this.location.y += jumpAmount;
				//check that we don't go into the ground
				if (this.location.y > MAX_Y) {
					this.location.y = MAX_Y;
					//now we're done jumping
					doneJumping();
					this.imOnABrick = false;
				}
				//check if we should fall onto a brick, not the ground
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
