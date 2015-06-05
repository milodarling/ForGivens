import java.awt.Image;

import javax.swing.ImageIcon;

public class Brick {
	
	//coordinates
	int x;
	int y;
	//how many bricks
	int length;
	//image file
	Image image = getImage("/images/brick.png");

	
	public Brick(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}
	
	//gray if it's for the final battle
	public Brick(int x, int y, int length, boolean forKatzfey) {
		this.x = x;
		this.y = y;
		this.length = length;
		if (forKatzfey) {
			this.image = getImage("/images/brickGray.png");
		}
	}
	
	//get end x-coordinate
	public int endX() {
		return this.x + this.image.getWidth(null) * this.length;
	}
	
	//get image from resources
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}

}
