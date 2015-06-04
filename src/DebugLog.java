
public class DebugLog {
	public static boolean debug = true;

	public static void logf(String pattern, Object... args) {
		if (debug) {
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		    String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		    int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			System.out.printf("[" + className + "." + methodName + "():" + lineNumber + "] " + pattern + "\n", args);
		}
	}
	
	public static void log(Object arg) {
		if (debug) {
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		    String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		    int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			System.out.println("[" + className + "." + methodName + "():" + lineNumber + "] " + arg);
		}
	}

}
