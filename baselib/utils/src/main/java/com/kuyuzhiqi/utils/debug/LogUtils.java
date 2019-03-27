package com.kuyuzhiqi.utils.debug;

import android.util.Log;

/**
 * 仅在debug模式下输出log.
 * 不适合并发情况下使用.
 *
 */
public class LogUtils {

	private static boolean sIsDebug = false;

	static String className;
	static String methodName;
	static int lineNumber;
	
    private LogUtils(){
        /* Protect from instantiations */
    }

	/**
	 * 初始化,在自定义Application类调用
	 * @param isDebug 是否打印log,建议传入BuildConfig.DEBUG
     */
	public static void initialize(boolean isDebug) {
		sIsDebug = isDebug;
	}

	private static boolean isDebuggable() {
		return sIsDebug;
	}

	private static String createLog(String log ) {

		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(methodName);
		builder.append(":");
		builder.append(lineNumber);
		builder.append("]");
		builder.append(log);
		
		return builder.toString();
	}
	
	private static void getMethodNames(StackTraceElement[] sElements){
		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}

	public static void e(String message){
		if (!isDebuggable())
			return;

		// Throwable instance must be created before any methods  
		getMethodNames(new Throwable().getStackTrace());
		Log.e(className, createLog(message));
	}

	public static void i(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.i(className, createLog(message));
	}
	
	public static void d(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.d(className, createLog(message));
	}
	
	public static void v(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.v(className, createLog(message));
	}
	
	public static void w(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.w(className, createLog(message));
	}
	
	public static void wtf(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.wtf(className, createLog(message));
	}
}
