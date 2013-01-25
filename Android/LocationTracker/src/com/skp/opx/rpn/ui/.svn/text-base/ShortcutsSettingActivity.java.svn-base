package com.skp.opx.rpn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Window;

import com.skp.opx.rpn.R;

/**
 * @설명 : 바로가기 설정 Activity
 * @클래스명 : ShortcutsSettingActivity 
 *
 */
public class ShortcutsSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_shortcut_setting);
    }
    
    @Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		
		if(preference.getKey().equals(getString(R.string.go_home))) { // 집으로
			Intent intent = new Intent(this, ShortcutsSettingHomeActivity.class);
			startActivity(intent);
		} else if(preference.getKey().equals(getString(R.string.go_company))) { //회사로
			Intent intent = new Intent(this, ShortcutsSettingCompanyActivity.class);
			startActivity(intent);
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}

