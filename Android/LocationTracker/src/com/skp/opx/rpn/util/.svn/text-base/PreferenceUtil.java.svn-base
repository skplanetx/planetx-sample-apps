package com.skp.opx.rpn.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @설명 : Preference Util
 * @클래스명 : PreferenceUtil 
 *
 */
public class PreferenceUtil {

	/** 
	 *  집을 도착지로 설정 Method
	 * */
	public static void setHomeAsMyDestination(Context context, String info){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("home_as_destination", info);
		editor.commit();
	}
	
	/** 
	 *  도착지로 설정한 집 위치 정보 리턴 Method
	 * */
	public static String getHomeAsMyDestination(Context context){
		
		return PreferenceManager.getDefaultSharedPreferences(context).getString("home_as_destination", "");
	}
	
	/** 
	 *  회사를 도착지로 설정 Method
	 * */
	public static void setWorkPlaceAsMyDestination(Context context, String info){
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("workplace_as_destination", info);
		editor.commit();
	}
	
	/** 
	 *  도착지로 설정한 회사 위치 정보 리턴 Method
	 * */
	public static String getWorkPlaceAsMyDestination(Context context){
		
		return PreferenceManager.getDefaultSharedPreferences(context).getString("workplace_as_destination", "");
	}
	/**
	 * gps 활성화 다이얼로그 한번만 띄우도록 하는 Method
	 */
	public static void setGPSDialogShown(Context context){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("isGPSDialogShown", true);
		editor.commit();
		
	}
    /**
     * gps 활성화 다이얼로그 띄웠는지 여부 확인 Method
     * @return
     */
	public static boolean getGPSDialogShown(Context context){
		
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("isGPSDialogShown", false);
	}

	/** 
	 *  알람 매니저 멈추지 않을 경우,  메시지 전송 상태 설정 Method
	 * */
	public static void setSendingMsgMode(Context context, boolean state){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("sedingMsgMode", true);
		editor.commit();
	}

	/** 
	 *  전송 상태 리턴 Method
	 * */
	public static boolean getSendingMsgMode(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sedingMsgMode", true);
	}
	
}
