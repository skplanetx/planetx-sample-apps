package com.skp.opx.sns.util;

/**
 * @설명 : 로그 출력 클래스
 * @클래스명 : Logger 
 *
 */
public class Logger {
	private static final String TAG = "SNS";
	private static final boolean DEBUGGABLE_LOG_ENABLED = true;
	
	/**
	 * Information Log
	 */
	public static void i(String msg) {
		
		android.util.Log.i(TAG, msg);
    }
	
	/**
	 * Error Log
	 */
	public static void e(String msg) {
		
		android.util.Log.e(TAG, msg);
    }
	
	/**
	 * Warning Log
	 */
	public static void w(String msg) {
		
		android.util.Log.w(TAG, msg);
    }

    /**
     * Debug Log
     */
    public static void d(String msg) {
    	
    	if(DEBUGGABLE_LOG_ENABLED)
            android.util.Log.d(TAG, msg);
    }
}
