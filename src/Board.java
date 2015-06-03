import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

public class Board extends JPanel implements KeyListener {
	//I have to do this. I don't know why
	private static final long serialVersionUID = 1L;
	
	Image background;
	Givens chris;
	boolean animatingForward;
	
	final Brick[] bricks = {
		new Brick(500, 200, 5)	
	};
	
	public Board() {
		
		background = getImage("/images/lcc.png");
		chris = new Givens(this);
		
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
		
		for (Brick brick : bricks) {
			if (this.animatingForward) {
				brick.x -= 10;
			}
			int x = brick.x;
			for (int i = 0; i < brick.length; i++) {
				g.drawImage(brick.image, x, brick.y, null);
				x += brick.image.getWidth(null);
			}
		}
		
		g.drawImage(chris.image, chris.location.x, chris.location.y, null);
		
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
		
	}
	
	//@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode == KeyEvent.VK_RIGHT) {
			chris.movement = 10;
		} else if (keyCode == KeyEvent.VK_LEFT) {
			chris.movement = -10;
		} else if (keyCode == KeyEvent.VK_UP) {
			chris.jumping = true;
		}
	}
	
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT) {
			chris.movement = 0;
			this.animatingForward = false;
		}
	}
	
	public void keyTyped(KeyEvent key) {
		
	}
	
}
