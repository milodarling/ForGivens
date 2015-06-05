//very useful debug logging class
public class DebugLog {
	//debug variable
	public static boolean debug = false;

	//log with formatting
	public static void logf(String pattern, Object... args) {
		if (debug) {
			//get class name, method name, line number, and message sent
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		    String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		    int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
			System.out.printf("[" + className + "." + methodName + "():" + lineNumber + "] " + pattern + "\n", args);
		}
	}
	
	//log just one thing
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
