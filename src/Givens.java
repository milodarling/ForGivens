import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Point;

public class Givens implements Character {
	Image image;
	int imageNumber = 0;
	int movement = 0;
	Point location = new Point(100, 275);
	boolean increasing = true;
	boolean jumping;
	private int jumpCount = 0;
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
	
	public void animate() {
		if (this.movement > 0) {
			this.imageNumber += 1;
			//change image state for givens
			int imageNumber = this.imageNumber % 6;
			if (imageNumber > 3) imageNumber -= 2 * (imageNumber - 3);
			//System.out.println(imageNumber);
			if (this.imageNumber == 6)
				this.imageNumber = 0;
			//reload the image
			this.refresh(imageNumber);
			this.location.x += this.movement;
		} else if (this.movement < 0) {
			this.imageNumber += 1;
			//change image state for givens
			int imageNumber = this.imageNumber % 6;
			if (imageNumber > 3) imageNumber -= 2 * (imageNumber - 3);
			//System.out.println(imageNumber);
			if (this.imageNumber == 6)
				this.imageNumber = 0;
			this.imageNumber = 3 - this.imageNumber;
			//reload the image
			this.refresh(imageNumber);
			this.location.x += this.movement;
		}
		if (this.jumping) {
			int jumpAmount = 10;
			if (this.jumpCount < 5) {
			this.location.y -= jumpAmount;
			this.jumpCount++;
			} else if (this.jumpCount < 10) {
				this.location.y += jumpAmount;
				this.jumpCount++;
			} else {
				this.jumping = false;
				this.jumpCount = 0;
			}
		}
	}
}
