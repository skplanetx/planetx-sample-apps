package com.skp.opx.rpn.receiver;

import com.skp.opx.rpn.service.MsgSendService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @설명 : 알람 매니저 Receiver
 * @클래스명 : AlarmReceiver 
 *
 */
public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//브로드 케이스 받으면 전송 수행
		intent.setAction("send");
		intent.setClass( context, MsgSendService.class );
		context.startService( intent );
	}
	
}
