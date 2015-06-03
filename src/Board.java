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
	public Board() {
		
		background = getImage("/images/lcc.png");
		chris = new Givens();
		
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
		g.drawImage(chris.image, chris.location.x, chris.location.y, null);
		
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
		
	}
	
	//@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		System.out.println(keyCode);
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
		System.out.println(keyCode);
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT) {
			chris.movement = 0;
		}
	}
	
	public void keyTyped(KeyEvent key) {
		
	}
	
}
