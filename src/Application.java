
import javax.swing.JFrame;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import javax.sound.sampled.Clip;

public class Application extends JFrame {

	/**
	 * Hi
	 */
	private static final long serialVersionUID = 1L;

	public Application() {

		initUI();
	}

	private void initUI() {
		
		Board board = new Board();
		
		//don't play soundtrack while we're debugging; it's annoying
		if (!DebugLog.debug || true) {
			//play soundtrack
			try {
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(this.getClass().getResource("/audio/music.wav").toURI()));
				Clip clip = AudioSystem.getClip();
				clip.open(inputStream);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} catch (Exception e) {
				DebugLog.logf(":(");
			}
		}
		
		// Here we put the Board to the center of the JFrame container.
		add(board); 
		
		addKeyListener(board);

		// This line sets the size of the window.
		setSize(1000, 600); 

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