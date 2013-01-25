package com.skp.opx.rpn.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.receiver.AlarmReceiver;

/**
 * @설명 : 경로 알리미용 알람 매니저
 * @클래스명 : RpnAlarmManager 
 *
 */
public class RpnAlarmManager {

	private static RpnAlarmManager mTheInstance;
	private AlarmManager am;
	private PendingIntent pendingIntent;
	private long alarmInterval; //알림 간격
	
	public static synchronized RpnAlarmManager getInstance() {
		if (mTheInstance == null) {
			mTheInstance = new RpnAlarmManager();
		}
		return mTheInstance;
	}

	/** 
	 *  알림 시작 Method
	 * */
	public synchronized void startAlarm(Context context) {
		
		am = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		stopAlarm();
		readAlarmCycle(context);
		Intent amIntent= null;
		PendingIntent sender = null;
		
		amIntent = new Intent(context, AlarmReceiver.class);
		pendingIntent = sender = PendingIntent.getBroadcast(context, 0, amIntent,0);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), alarmInterval, sender);

	}	
	
	/** 
	 * 알림 종료 Method  
	 * */
	public synchronized void stopAlarm() {
		if (pendingIntent != null) {
			am.cancel(pendingIntent);
			
		}
	}
	/**
	 * 기존 실행 되는 알림이 있는지 확인 Method
	 */
	public synchronized boolean isPendingIntentExist(){
		
		if(pendingIntent != null){
			return true;
		}else{
			return false;
		}
	}
	
	/** 
	 *  알림 Cycle 반환 Method
	 * */
	private void readAlarmCycle(Context context){
	
		String cycle = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.message_send_tick), "120000");

		alarmInterval = Long.parseLong(cycle);
	}	
	
	/** 
	 * 자원반납 Method  
	 * */
	public synchronized void release(){
		if(mTheInstance != null){
			mTheInstance = null;
		}
	}
}
