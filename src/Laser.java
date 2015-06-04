import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
public class Laser {
	Image image;
	double angle;
	AffineTransformOp op;
	Point location;
	
	public Laser(double angle, Point start) {
		this.angle = angle;
		this.location = start;
		this.image = getImage("/images/laserStart.png");
		double locationX = this.image.getWidth(null) / 2;
		double locationY = this.image.getHeight(null) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(angle, locationX, locationY);
		this.op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

	}
	
	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(path));
		return icon.getImage();
	}
	
	public void draw(Graphics g) {
		// Drawing the rotated image at the required drawing locations
		((Graphics2D)g).drawImage(op.filter(toBufferedImage(this.image), null), location.x, location.y, null);
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

}
