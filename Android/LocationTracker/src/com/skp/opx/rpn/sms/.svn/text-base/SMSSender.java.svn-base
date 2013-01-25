package com.skp.opx.rpn.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * SMS를 송신한다.
 * @클래스명 : SMSSender
 * @작성자 : 박상현
 * @작성일 : 2012. 9. 27.
 * @최종수정일 : 2012. 9. 27.
 * @수정이력 - 수정일, 수정자, 수정 내용
 * TODO
 */
public final class SMSSender {

	public static final void SendSmsMessage(Context context, String strPhoneNumber, String strMessage) {
		
    	Intent sentIntent = new Intent("SENT_SMS_ACTION");
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
		
		Intent deliverIntent = new Intent("DELIVERED_SMS_ACTION");
		PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
		
		SmsManager.getDefault().sendTextMessage(strPhoneNumber, null, strMessage, sentPI, deliverPI);
	}
}
