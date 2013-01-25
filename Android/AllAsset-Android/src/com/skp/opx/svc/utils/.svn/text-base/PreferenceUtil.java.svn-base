package com.skp.opx.svc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @설명 : 공통 Preference 관리 Util
 * @클래스명 : PreferenceUtil 
 *
 */
public class PreferenceUtil {
	
	/**
	 * String 데이터를 저장
	 */
	public static void putSharedPreference(Context context, String key, String value) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Boolean 데이터를 저장
	 */
	public static void putSharedPreference(Context context, String key, boolean value) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * Integer 데이터를 저장
	 */
	public static void putSharedPreference(Context context, String key, int value)	{

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * String 데이터를 읽음
	 */
	public static String getSharedPreference(Context context, String key) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(key, null);
	}

	/**
	 * Boolean 데이터를 읽음
	 */
	public static boolean getBooleanSharedPreference(Context context, String key) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(key, false);
	}

	/**
	 * Int 데이터를 읽음
	 */
	public static int getIntSharedPreference(Context context, String key) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getInt(key, 0);
	}
	
	/**
	 * gps 활성화 다이얼로그 한번만 띄우도록
	 */
	public static void setGPSDialogShown(Context context) {
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("isGPSDialogShown", true);
		editor.commit();
		
	}

	/**
	 *  gps 활성화 다이얼로그 띄워졌었는지 상태 체크
	 */
	public static boolean isGPSDialogShown(Context context) {
		
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("isGPSDialogShown", false);
	}
}
