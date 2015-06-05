import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Point;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class Board extends JPanel implements KeyListener {
	//I have to do this. I don't know why
	private static final long serialVersionUID = 1L;
	//background image
	Image background;
	//level complete flag
	Image flag;
	//our main character
	Givens chris;
	//if the screen needs to move forward rather than the character himself
	boolean animatingForward;
	//user distance and emails
	int distance = 0;
	int unreadEmails = 0;
	//the location of the flag
	Point flagLoc;
	//used for suspending animations and stuff when you've completed a level
	boolean levelComplete;
	//are we at the boss level yet?
	boolean fightingKatzfey = false;
	//our Katzfey object
	Katzfey eric;
	//used to count time in `paint()`
	int repaintCount = 0;
	//we aren't playing when we first launch, we're at the start menu
	boolean isPlaying = false;
	//did you win the game?
	boolean youWin = false;
	//have you started the game?
	boolean hasStartedGame = false;
	//image assets
	Image gameOver = getImage("/images/gameover.png");
	Image youWinImg = getImage("/images/youwin.png");
	Image levelCompleteImg = getImage("/images/levelcomplete.png");
	Image introScreen = getImage("/images/introscreen.png");
	
	//2D array for envelopes for each level
	Mail[][] mailsArray = mailsArrayOrig();
	//ArrayList for the current level's envelopes
	ArrayList<Mail> mails;
	//skip to a given level for testing
	int testingLevel = 0;
	//2D arrays for spikes are bricks for each level
	Spike[][] spikes2D = spikes2DOrig();
	Brick[][] bricksbricks = bricksbricksOrig();
	//our current level
	int level = 0;
	//1D Array for bricks and spikes for the current level
	Brick[] bricks = bricksbricks[level];
	Spike[] spikes = spikes2D[level];
	
	
	public Board() {
		
		//set up some of the ivars
		background = getImage("/images/background.png");
		flag = getImage("/images/flag.png");
		chris = new Givens(this);
		flagLoc = new Point(2165, Givens.MAX_Y + chris.image.getHeight(null) - flag.getHeight(null));
		eric = new Katzfey(this);
		mails = new ArrayList<Mail>(Arrays.asList(mailsArray[level]));
		
		//repaint every tenth of a second
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			    repaint();
			  }
			}, 0, 1000/10);
		//addKeyListener(this);
	}
	//returns the original arrays of mail, bricks, spikes so that they aren't all weird when we change their locations and stuff and then reload them after game over
	public static Mail[][] mailsArrayOrig() {
		Mail[][] result = {
				{
					new Mail(502, 275),
					new Mail(642, 75),
					new Mail(1102, 175),
					new Mail(1502, 165),
					new Mail(1782, Spike.ON_FLOOR - 25),
				},
				{
					new Mail(542, 175),
					new Mail(902, 250),
					new Mail(1352, 75),
					new Mail(1630+92, 375),
					new Mail(2100, 25),
				},
				{
					//empty for katzfey battle
				}
		};
		return result;
	}
	
	public static Brick[][] bricksbricksOrig() {
		Brick[][] result = {
			{
				//initial platforms
				new Brick(500, 300, 5),
				new Brick(600, 100, 3),
				new Brick(800, 200, 4),
				new Brick(1020, 350, 2),
				new Brick(1100, 200, 4),
				
				//pyramid
				new Brick(1300, 350, 1),
				new Brick(1350, 300, 1),
				new Brick(1400, 250, 1),
				new Brick(1450, 200, 1),
				new Brick(1500, 150, 1),
				
				//one by one on floor
				new Brick(1690, Spike.ON_FLOOR, 1),
				new Brick(1780, Spike.ON_FLOOR, 1),
				new Brick(1870, Spike.ON_FLOOR, 1),
			},
			{
				
				//various platforms
				new Brick(300, 350, 4),
				new Brick(500, 200, 6),
				new Brick(600, 100, 3),
				
				//one by one jumping
				new Brick(740, 275, 1),
				new Brick(820, 275, 1),
				new Brick(900, 275, 1),
				new Brick(980, 275, 1),
				new Brick(1060, 275, 1),
				
				//obstacle jumping
				new Brick(1120, 350, 1),
				new Brick(1240, 180, 1),
				new Brick(1350, 100, 1),
				new Brick(1450, 400, 1),
				new Brick(1550, 200, 1),
				
				//pyramid
				new Brick(1590+90, 480, 10),
				new Brick(1630+90, 440, 9),
				new Brick(1670+90, 400, 8),
				new Brick(1710+90, 360, 7),
				new Brick(1750+90, 320, 6),
				new Brick(1790+90, 280, 5),
				new Brick(1830+90, 240, 4),
				new Brick(1870+90, 200, 3),
				new Brick(1910+90, 160, 2),
				new Brick(1950+90, 120, 1)
			},
			{
				//bricks for katzfey battle
				new Brick(200, 420, 3, true),
				new Brick(400, 200, 2, true),
				new Brick(600, 220, 2, true),
				new Brick(780, 140, 1, true),
			}
		};
		return result;
	}
	
	public static Spike[][] spikes2DOrig() {
		Spike[][] result = 
			{
				{
					//initial platforms
					new Spike(500, Spike.ON_FLOOR, 10),
					
					//spikes on bricks
					new Spike(1020, 310, 2),
					
					//below pyramid
					new Spike(1300, Spike.ON_FLOOR, 7),
					
					//one by one on floor
					new Spike(1650, Spike.ON_FLOOR, 1),
					new Spike(1740, Spike.ON_FLOOR, 1),
					new Spike(1830, Spike.ON_FLOOR, 1),
					new Spike(1920, Spike.ON_FLOOR, 1),
					
				},
				{
					
					//various spikes
					new Spike(200, Spike.ON_FLOOR, 1),
					new Spike(420, Spike.ON_FLOOR, 5),
					new Spike(600, 160, 3),
					
					//one by one jumping
					new Spike(780, Spike.ON_FLOOR, 1),
					new Spike(860, Spike.ON_FLOOR, 1),
					new Spike(940, Spike.ON_FLOOR, 1),
					new Spike(1020, Spike.ON_FLOOR, 1),
					
					//obstacle course
					new Spike(1100, Spike.ON_FLOOR, 14),
					
					//pyramid
					new Spike(1550+90, 480, 11),
					new Spike(1630+90, 440, 9),
					new Spike(1630+90, 400, 9),
					new Spike(1710+90, 360, 7),
					new Spike(1710+90, 320, 7),
					new Spike(1790+90, 280, 5),
					new Spike(1790+90, 240, 5),
					new Spike(1870+90, 200, 3),
					new Spike(1870+90, 160, 3),
					new Spike(1950+90, 120, 1)
				},
				{
					//empty for katzfey fight
				}
			};
		return result;
	}
	
	public void reloadSpikesAndBricks() {
		this.spikes2D = spikes2DOrig();
			
		this.bricksbricks = bricksbricksOrig();
		
		this.mailsArray = mailsArrayOrig();
	}
	
	//load image from the resources
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	//REPAINT
	public void paint(Graphics g) {
		super.paint(g);
		//if we aren't playing yet, just show the start screen
		if (!hasStartedGame) {
			g.drawImage(introScreen, 0, 0, null);
			return;
		}
		//take care of givens' animations in his class
		chris.animate();
		//add his forward motion to the user's distance
		if (!chris.atMinX)
			distance += chris.movement;
		
		//shift the bricks over if needed to create motion, and draw them
		for (Brick brick : bricks) {
			if (this.animatingForward && !fightingKatzfey) {
				brick.x -= chris.movement;
			}
			//for Brick objects with more than 1 square brick
			int x = brick.x;
			for (int i = 0; i < brick.length; i++) {
				g.drawImage(brick.image, x, brick.y, null);
				x += brick.image.getWidth(null);
			}
		}
		//same idea for mail
		for (Mail mail : mails) {
			if (this.animatingForward && !fightingKatzfey) {
				mail.x -= chris.movement;
			}
			g.drawImage(mail.image, mail.x, mail.y, null);
		}
		//same idea for spikes
		for (Spike spike : spikes) {
			if (this.animatingForward && !fightingKatzfey) {
				spike.x -= chris.movement;
			}
			int x = spike.x;
			for (int i = 0; i < spike.length; i++) {
				g.drawImage(spike.image, x, spike.y, null);
				x += spike.image.getWidth(null);
			}
		}
		
		//move the flag towards givens
		if (this.animatingForward && !fightingKatzfey)
			flagLoc.x -= chris.movement;
		//draw the flag
		g.drawImage(flag, flagLoc.x, flagLoc.y, null);
		//show the user distance and emails
		g.drawString(String.format("Distance: %d", distance), 20, 20);
		g.drawString(String.format("Unread Emails: %d", unreadEmails), 20, 40);
		//draw givens
		g.drawImage(chris.image, chris.location.x, chris.location.y, null);
		//if they completed the level, move on
		if ((distance >= (level + 1) * 2000) && !fightingKatzfey) {
			if (!levelComplete) {
				//play the yay
				play("yay");
			}
			//set levelComplete to true to suspend key listening and movement
			levelComplete = true;
			//stop moving, chris
			chris.endMovement();
			//draw the level complete text
			g.drawImage(levelCompleteImg, 205, 217, null);
		}
		//he touched a spike and last
		if (chris.isTouchingASpike()) {
			if (!levelComplete) {
				//play game over sound
				play("gameover");
			}
			//set levelComplete to true to suspend movements and stuff
			levelComplete = true;
			//rip chris
			chris.isDead = true;
			//stop moving, you're dead
			chris.endMovement();
			//draw the game over text
			g.drawImage(gameOver, 205, 217, null);
		}
		
		//if we're at the boss level
		if (fightingKatzfey) {
			//as long as katzfey isn't dead, draw him
			if (!eric.isKill)
				g.drawImage(eric.image, eric.location.x, eric.location.y, null);
			//if they're both alive
			if (!chris.isDead && !eric.isKill) {
				//create another laser every 2 seconds
				if (repaintCount == 19) {
					eric.prepare();
				}

				//shoot the lasers
				eric.shootLaser(g);
			}
			
			//switch the body state every 1/5th second
			if (repaintCount%2 == 1) {
				eric.switchBodyState();
			}
			
			//if katzfey's dead
			if (eric.isKill || chris.checkForKillz()) {
				//you win
				if (!levelComplete) {
					play("yay");
				}
				//stop motion and stuff
				levelComplete = true;
				youWin = true;
				chris.endMovement();
				//show win image
				g.drawImage(youWinImg, 205, 217, null);
			} else
			
			//check for collisions with a laser
			if (chris.gotShotByALaser() || chris.isDead) {
				//you died :(
				if (!levelComplete) {
					play("gameover");
				}
				//if we haven't done so already, clear the lasers
				if (!chris.isDead) {
					eric.clear();
				}
				//set the ivars and stuff
				levelComplete = true;
				chris.isDead = true;
				chris.endMovement();
				DebugLog.log("Ded");
				//game over dude
				g.drawImage(gameOver, 205, 217, null);
			}
			
			//increment the seconds counter
			repaintCount++;
			//reset it at 20
			if (repaintCount >= 20) repaintCount = 0;
			
		}
	}
	
	//draw the background
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
		
	}
	
	//change the background to dark, set the ivar
	public void fightKatzfey() {
		background = getImage("/images/backgroundBoss.png");
		fightingKatzfey = true;
	}
	
	//when you've pressed space for a new level
	public void newLevel() {
		//reset the ivars
		levelComplete = false;
		fightingKatzfey = false;
		eric.isKill = false;
		//refresh the background
		background = getImage("/images/background.png");
		chris.location = new Point(100, Givens.MAX_Y);
		//increment the level
		level++;
		//if we're starting over, reset levels and distance, etc.
		if (chris.isDead) {
			DebugLog.log("Givens is ded rip");
			distance = testingLevel * 2000;
			unreadEmails = 0;
			level = testingLevel;
			chris.isDead = false;
			reloadSpikesAndBricks();
		}
		//load the new bricks and spikes and mail
		bricks = bricksbricks[level];
		spikes = spikes2D[level];
		mails = new ArrayList<Mail>(Arrays.asList(mailsArray[level]));
		if (level >= bricksbricks.length - 1) {
			//final boss
			fightKatzfey();
		} else {
			//reset the flag too
			flagLoc = new Point(2165, Givens.MAX_Y + chris.image.getHeight(null) - flag.getHeight(null));
		}
	}
	
	//listen for key presses
	public void keyPressed(KeyEvent key) {
		//key code
		int keyCode = key.getKeyCode();
		//if we're allowed to move givens
		if (!this.levelComplete) {
			//right
			if (keyCode == KeyEvent.VK_RIGHT) {
				chris.movement = 10;
			//left
			} else if (keyCode == KeyEvent.VK_LEFT) {
				chris.movement = -10;
			//jump
			} else if (keyCode == KeyEvent.VK_UP) {
				if (!chris.jumping) {
					//play the jump sound
					play("jump");
				}
				chris.jumping = true;
			//space to start the game
			} else if (keyCode == KeyEvent.VK_SPACE && !hasStartedGame) {
				DebugLog.log("starting game");
				hasStartedGame = true;
			}
		} else {
			//if we should be starting over/new level
			if (keyCode == KeyEvent.VK_SPACE) {
				//if you won, exit the game
				if (youWin) System.exit(0);
				
				//otherwise create a new level
				DebugLog.logf("New Level");
				newLevel();
			}
		}
		//escape to quit
		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}
	
	//stop moving
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT) {
			chris.endMovement();
		}
	}
	
	//we don't need this
	public void keyTyped(KeyEvent key) {
		
	}
	
	//play a sound clip once
	public void play(String name) {
		if (chris.isDead) return;
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(this.getClass().getResource("/audio/" + name + ".wav").toURI()));
			Clip clip = AudioSystem.getClip();
			clip.open(inputStream);
        	clip.loop(0);
		} catch (Exception e) {
			DebugLog.logf(":(");
		}
	}
	
}
