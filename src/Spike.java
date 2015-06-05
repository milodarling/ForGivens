import java.awt.Image;

import javax.swing.ImageIcon;


public class Spike {
	//y coordinate if you want the spikes on the floor
	public static final int ON_FLOOR = 480;
	//location
	int x;
	int y;
	//number of spikes
	int length;
	//image
	final Image image = getImage("/images/spikes.png");
	
	public Spike(int x, int y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}
	
	//get the ending x-coordinate
	public int endX() {
		return this.x + this.image.getWidth(null) * this.length;
	}
	
	//grab image from resources
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}

}
