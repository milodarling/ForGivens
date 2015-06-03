
public class ForGivens {
	public static boolean debug = true;

	public static void main(String[] args) {
		Application ex = new Application();
		ex.setVisible(true);
	}
	
	public static void debugLog(String pattern, Object... args) {
		if (debug) {
			System.out.printf(pattern + "\n", args);
		}
	}
}
