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
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Board extends JPanel implements KeyListener {
	//I have to do this. I don't know why
	private static final long serialVersionUID = 1L;
	
	Image background;
	Image givens;
	Image katzfey;
	Image email;
	Image mob;
	
	public Board() {

		background = getImage("/images/lcc.png");
		//givens = getImage("/images/givens.png");
		//katzfey = getImage("/images/katzfey.png");
		//email = getImage("/images/email.png");
		
		
	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/lcc.png"));
		return icon.getImage();
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, null);
	}
	
	public void keyPressed(KeyEvent key) {
		
	}
	
	public void keyReleased(KeyEvent key) {
		
	}
	
	public void keyTyped(KeyEvent key) {
		
	}
}
