package com.skp.opx.mss.util;

import com.skp.opx.core.client.Define;
import com.skp.opx.core.client.Define.MSS_KV;
import com.skp.opx.mss.ui.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

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
	
	public static void moveExtraIntent(Context context, Class<?> cls, Bundle bundle) {
		
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	public static void moveExtraIntent(Context context, Class<?> cls, Parcelable parcelable) {
		
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(MSS_KV.KEY_ENTITY_ALL, parcelable);
		context.startActivity(intent);
	}
	
}
