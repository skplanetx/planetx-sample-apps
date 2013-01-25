package com.skp.opx.sns.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.skp.opx.core.client.Define;
import com.skp.opx.sns.R;

/**
 * @설명 : 공통 인텐트 Util
 * @클래스명 : IntentUtil 
 *
 */
public class IntentUtil {
	
	public static void moveIntent(Context context, Class<?> cls) {
		
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
	
	public static void moveExtraIntent(Context context, Class<?> cls, String key, String value) {
		
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		if (key != null) {
			intent.putExtra(key, value);
		}
		
		context.startActivity(intent);
	}
	
	public static void moveExtraIntentForResult(Context context, Class<?> cls, String key, String value) {
		
		Intent intent = new Intent(context, cls);
		
		if (key != null) {
			intent.putExtra(key, value);
		}
		
		((Activity)context).startActivityForResult(intent, Define.REQUEST_CODE);
	}
	

	
	public static final void changeActivityWithAnim(Context context, Intent intent, int slideDirection) {
		
		context.startActivity(intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP));
		((Activity)context).overridePendingTransition(slideDirection == 0 ? R.anim.slide_left_in : R.anim.slide_right_in, slideDirection == 0 ? R.anim.slide_left_out : R.anim.slide_right_out );
	}
}
