
import java.awt.EventQueue;
import javax.swing.JFrame;


public class Application extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Application() {

		initUI();
	}

	private void initUI() {
		
		// Here we put the Board to the center of the JFrame container.
		add(new Board()); 

		// This line sets the size of the window.
		setSize(500, 500); 

		// sets title
		setTitle("ForGivens"); 

		// This will close the application when we click on the close button. It is not the default behavior.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		// Passing null to the setLocationRelativeTo() method centers the window on the screen.
		setLocationRelativeTo(null);
	}    

	public static void main(String[] args) {
		
		// We create an instance and make it visible on the screen.

				Application ex = new Application();
				ex.setVisible(true);

	}
}