package com.skp.opx.rpn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Window;

import com.skp.opx.rpn.R;

/**
 * @설명 : 메시지 문구 변경 Activity
 * @클래스명 : MessageTextSettingActivity 
 *
 */
public class MessageTextSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        super.onCreate(savedInstanceState); 
        addPreferencesFromResource(R.xml.activity_message_text_setting);
    }
    
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		
		if(preference.getKey().equals(getString(R.string.message_text_setting))) {
	
		} else if(preference.getKey().equals(getString(R.string.message_text_write))) {
			Intent intent = new Intent(this, MessageTextWriteActivity.class);
			startActivity(intent);
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}
