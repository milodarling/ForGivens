import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
public class Laser {
	//image
	Image image;
	//angle it points at
	double angle;
	//used to rotate the image
	AffineTransformOp op;
	//location of the laser
	Point location;
	
	public Laser(double angle, Point start) {
		//assign angle and initial location
		this.angle = angle;
		this.location = start;
		//load image
		this.image = getImage("/images/laserStart.png");
		//get midpoint
		double locationX = this.image.getWidth(null) / 2;
		double locationY = this.image.getHeight(null) / 2;
		//rotation stuff
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
	
	//convert Image to BufferedImage for rotation
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
