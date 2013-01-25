package com.skp.opx.rpn.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.skp.opx.rpn.R;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equalsIgnoreCase("SENT_SMS_ACTION")) {
			switch(getResultCode()) {
			case Activity.RESULT_CANCELED : 
				Toast.makeText(context, context.getString(R.string.send_message_fail), Toast.LENGTH_SHORT).show();
				break;

			case Activity.RESULT_OK :
				Toast.makeText(context, context.getString(R.string.send_message_success), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
}
