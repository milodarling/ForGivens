import java.awt.Image;

import javax.swing.ImageIcon;

public class Brick {
	int x;
	int y;
	int length;
	final Image image = getImage("/images/brick.png");

	public Brick(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}
	
	public int endX() {
		return this.x + this.image.getWidth(null) * this.length;
	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}

}
