import java.awt.Image;

import javax.swing.ImageIcon;

public class Mail {
	//coordinates
	int x, y;
	//image
	Image image = getImage("/images/mail.png");

	public Mail(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//get image from resources
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}

}
