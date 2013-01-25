package com.skp.opx.mul.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @설명 : 계정 및 자동 로그인 정보 저장 PreferenceUtil 
 * @클래스명 : PreferenceUtil
 * 
 */
public class PreferenceUtil {
	public static final String PREF_LOGIN = "login";
	public static final String PREF_LOGIN_STATE = "login_def";
	public static final String KEY_AUTO_LOGIN = "auto_login";
	public static final String KEY_LOGIN_STATE = "login_state";
	public static final String ACCESS_TOKEN = "access_token";

	public static String getAppUserID(Context context, boolean isTempID) {

		SharedPreferences preference;
		
		if(isTempID) {
			preference = context.getSharedPreferences("APP_USER_ID_TEMP", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		} else {
			preference = context.getSharedPreferences("APP_USER_ID", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		}
		
		
		return preference.getString("EXTRA_APP_USER_ID", null);
	}

	public static void setAppUserID(Context context, String strAppUserID, boolean isTempID) {
		
		SharedPreferences preference;
		
		if(isTempID) {
			preference = context.getSharedPreferences("APP_USER_ID_TEMP", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		} else {
			 preference = context.getSharedPreferences("APP_USER_ID", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		}
		
		Editor editor = preference.edit();
		editor.putString("EXTRA_APP_USER_ID", strAppUserID);
		editor.commit();
	}
	
	public static void setAutoLogin(Context context, boolean isAutoLogin) {

		Editor editor = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_AUTO_LOGIN, isAutoLogin);
		editor.commit();
	}

	public static boolean getAutoLogin(Context context) {
		return context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE).getBoolean(KEY_AUTO_LOGIN, false);
	}

	public static void setNateOnLoginState(Context context, boolean isLogin) {
		Editor editor = context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_LOGIN_STATE, isLogin);
		editor.commit();
	}

	public static boolean getNateOnLoginState(Context context) {
		return context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).getBoolean(KEY_LOGIN_STATE, false);
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

	public static void setGoogleLoginState(Context context, boolean isLogin) {
		Editor editor = context.getSharedPreferences(PREF_LOGIN_STATE, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_LOGIN_STATE, isLogin);
		editor.commit();
	}

	public static boolean getGoogleLoginState(Context context) {
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
