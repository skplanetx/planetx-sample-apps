package com.skp.opx.rpn.util;

import com.skp.opx.rpn.service.MsgSendService;

import android.content.Context;
import android.content.Intent;

/**
 * @설명 : 메시지 전송 관련 Util
 * @클래스명 : ServiceUtil 
 *
 */
public class ServiceUtil {

	/**
	 * 메시지 전송 중지시 알람 종료, 발신 서비스 중지 Method
	 */
	public static void stopMsgService(Context context){
		
		try {
			//즉각 전송 중시
			PreferenceUtil.setSendingMsgMode(context, false);
			//목적지 도착시 알람 종료
			RpnAlarmManager am = RpnAlarmManager.getInstance();
			am.stopAlarm();
			am.release();
			//도착지 알람 종료
			ApproximaAlert a = ApproximaAlert.getInstance();
			a.removeLocation();
			a.release();
			//location manager 반환
			CoordinateUtil.release();		
			//발신 서비스 정지
			Intent i = new Intent();
			i.setClass( context, MsgSendService.class );
			context.stopService(i);
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
}
