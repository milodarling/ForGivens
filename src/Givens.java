import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Point;

public class Givens implements Character {
	Image image;
	int imageNumber = 0;
	//public final Image[] images = {};
	Point center;
	boolean increasing = true;
	public final Image[] images = { getImage("/images/givensW1.png"), getImage("/images/givensW2.png"), getImage("/images/givensW3.png"), getImage("/images/givensW4.png") };
	
	public Givens() {
		image = images[0];
	}
	
	public void refresh(int index) {
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
}
