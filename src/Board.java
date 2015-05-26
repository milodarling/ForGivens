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
	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		//change image state for givens
		int imageNumber = chris.imageNumber % 6;
		if (imageNumber > 3) imageNumber -= 2 * (imageNumber - 3);
		System.out.println(imageNumber);
		if (chris.imageNumber == 6)
			chris.imageNumber = 0;
		//reload the image
		chris.refresh(imageNumber);
		
		g.drawImage(chris.image, 100, 100, null);
		//finish up
		chris.imageNumber += 1;
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
		
	}
	
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		chris.move(keyCode);
	}
	
	public void keyReleased(KeyEvent key) {
		
	}
	
	public void keyTyped(KeyEvent key) {
		
	}
}
