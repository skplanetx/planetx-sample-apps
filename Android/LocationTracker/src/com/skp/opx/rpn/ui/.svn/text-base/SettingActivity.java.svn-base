package com.skp.opx.rpn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Window;

import com.skp.opx.rpn.R;

/**
 * @설명 : 설정 Activity
 * @클래스명 : SettingActivity 
 *
 */
public class SettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        super.onCreate(savedInstanceState); 
        addPreferencesFromResource(R.xml.activity_setting);
    }
    
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		
		if(preference.getKey().equals(getString(R.string.shortcuts_setting))) { //바로가기 설정
			Intent intent = new Intent(this, ShortcutsSettingActivity.class);
			startActivity(intent);
		} else if(preference.getKey().equals(getString(R.string.message_setting))) { //메시지 설정
			Intent intent = new Intent(this, MessageSettingActivity.class); 
			startActivity(intent);
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}
