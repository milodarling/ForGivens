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

import java.util.Timer;
import java.util.TimerTask;

public class Board extends JPanel implements KeyListener {
	//I have to do this. I don't know why
	private static final long serialVersionUID = 1L;
	
	Image background;
	Image flag;
	Givens chris;
	boolean animatingForward;
	int distance = 0;
	int unreadEmails = 0;
	Point flagLoc;
	boolean levelComplete;
	
	final Spike[][] spikes2D = 
	{
		{
			new Spike(500, Spike.ON_FLOOR, 3),
		},
		{
			new Spike(500, Spike.ON_FLOOR, 3)
		}
	};
	
	final Brick[][] bricksbricks = 
	{
		{
			new Brick(500, 300, 5),
			new Brick(600, 100, 3),
			new Brick(800, 200, 4)
		},
		{
			new Brick(500, 200, 5),
			new Brick(600, 100, 3)
		},
	};
	int level = 0;
	Brick[] bricks = bricksbricks[level];
	Spike[] spikes = spikes2D[level];
	
	
	public Board() {
		
		background = getImage("/images/lcc.png");
		flag = getImage("/images/flag.png");
		chris = new Givens(this);
		flagLoc = new Point(2165, Givens.MAX_Y + chris.image.getHeight(null) - flag.getHeight(null));
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
			    repaint();
			  }
			}, 0, 1000/10);
		//addKeyListener(this);
	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		chris.animate();
		if (!chris.atMinX)
			distance += chris.movement;
		
		for (Brick brick : bricks) {
			if (this.animatingForward) {
				brick.x -= chris.movement;
			}
			int x = brick.x;
			for (int i = 0; i < brick.length; i++) {
				g.drawImage(brick.image, x, brick.y, null);
				x += brick.image.getWidth(null);
			}
		}
		for (Spike spike : spikes) {
			if (this.animatingForward) {
				spike.x -= chris.movement;
			}
			int x = spike.x;
			for (int i = 0; i < spike.length; i++) {
				g.drawImage(spike.image, x, spike.y, null);
				x += spike.image.getWidth(null);
			}
		}
		if (this.animatingForward)
			flagLoc.x -= chris.movement;
		g.drawImage(flag, flagLoc.x, flagLoc.y, null);
		g.drawString(String.format("Distance: %d", distance), 20, 20);
		g.drawString(String.format("Unread Emails: %d", unreadEmails), 20, 40);
		g.drawImage(chris.image, chris.location.x, chris.location.y, null);
		if (distance >= (level + 1) * 2000) {
			if (!levelComplete) {
				play("yay");
			}
			levelComplete = true;
			chris.endMovement();
			g.drawString("Level complete!", 500, 300);
		}
		if (chris.isTouchingASpike()) {
			if (!levelComplete) {
				play("yay");
			}
			levelComplete = true;
			chris.isDead = true;
			chris.endMovement();
			g.drawString("You died!", 500, 300);
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
		
	}
	
	public void fightKatzfey() {
		System.exit(0);
	}
	
	public void newLevel() {
		levelComplete = false;
		chris.location = new Point(100, Givens.MAX_Y);
		//increment the level
		level++;
		if (chris.isDead) {
			distance = 0;
			unreadEmails = 0;
			level = 0;
		}
		if (level >= bricksbricks.length) {
			//final boss
			fightKatzfey();
		} else {
			//load the new set of bricks
			flagLoc = new Point(2165, Givens.MAX_Y + chris.image.getHeight(null) - flag.getHeight(null));
			bricks = bricksbricks[level];
		}
	}
	
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if (!this.levelComplete) {
		if (keyCode == KeyEvent.VK_RIGHT) {
			chris.movement = 10;
		} else if (keyCode == KeyEvent.VK_LEFT) {
			chris.movement = -10;
		} else if (keyCode == KeyEvent.VK_UP) {
			if (!chris.jumping) {
				//play the jump sound
				play("jump");
			}
			chris.jumping = true;
		}
		} else {
			if (keyCode == KeyEvent.VK_SPACE) {
				DebugLog.logf("New Level");
				newLevel();
			}
		}
	}
	
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT) {
			chris.endMovement();
		}
	}
	
	public void keyTyped(KeyEvent key) {
		
	}
	
	public void play(String name) {
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
