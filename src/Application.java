
import javax.swing.JFrame;
import java.net.URISyntaxException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import javax.sound.sampled.Clip;

public class Application extends JFrame {

	/**
	 * Hi
	 */
	private static final long serialVersionUID = 1L;
	SimpleAudioPlayer sap;

	public Application() {

		initUI();
	}

	private void initUI() {
		
		Board board = new Board();
		
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(this.getClass().getResource("/audio/music.wav").toURI()));
			Clip clip = AudioSystem.getClip();
			clip.open(inputStream);
        	clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			System.out.println(":(");
		}
		//sap = new SimpleAudioPlayer(this.getClass().getResource("/audio/music.wav").toString().substring(5));
		
		// Here we put the Board to the center of the JFrame container.
		add(board); 
		
		addKeyListener(board);

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