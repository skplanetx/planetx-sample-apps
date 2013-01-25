package com.skp.opx.mss.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtil {
	public static final String PREF_LOGIN = "login";
	public static final String PREF_LOGIN_STATE = "login_def";
	public static final String KEY_AUTO_LOGIN = "auto_login";
	public static final String KEY_LOGIN_STATE = "login_state";
	public static final String ACCESS_TOKEN = "access_token";

	/**
	 * Save auto login state
	 * */
	public static void setAutoLogin(Context context, boolean isAutoLogin) {

		Editor editor = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_AUTO_LOGIN, isAutoLogin);
		editor.commit();
	}

	public static boolean getAutoLogin(Context context) {
		return context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE).getBoolean(KEY_AUTO_LOGIN, false);
	}

	public static void setCyworldLoginState(Context context, boolean isLogin) {
		Editor editor = context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_LOGIN_STATE, isLogin);
		editor.commit();
	}

	public static boolean getCyworldLoginState(Context context) {
		return context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).getBoolean(KEY_LOGIN_STATE, false);
	}

	public static void setFacebookLoginState(Context context, boolean isLogin) {
		Editor editor = context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_LOGIN_STATE, isLogin);
		editor.commit();
	}

	public static boolean getFacebookLoginState(Context context) {
		return context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).getBoolean(KEY_LOGIN_STATE, false);
	}

	public static void setTwitterLoginState(Context context, boolean isLogin) {
		Editor editor = context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_LOGIN_STATE, isLogin);
		editor.commit();
	}

	public static boolean getTwitterLoginState(Context context) {
		return context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).getBoolean(KEY_LOGIN_STATE, false);
	}
	
	public static void setOneIDAccessToken(Context context, String accessToken) {
		SharedPreferences pref = context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE);    
		SharedPreferences.Editor editor = pref.edit();      
		editor.putString(ACCESS_TOKEN, accessToken);      
		editor.commit();
	}
	
	public static String getOneIDAccessToken(Context context) {
		return context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).getString(ACCESS_TOKEN, "");
	}

}
