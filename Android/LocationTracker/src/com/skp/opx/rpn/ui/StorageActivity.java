package com.skp.opx.rpn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Window;

import com.skp.opx.rpn.R;

/**
 * @설명 : 보관함 Activity
 * @클래스명 : StorageActivity 
 *
 */
public class StorageActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState); 
        
        addPreferencesFromResource(R.xml.activity_storage);
    }
    
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		
		if(preference.getKey().equals(getString(R.string.favorite_management))) {
			Intent intent = new Intent(this, FavoriteActivity.class);
			startActivity(intent);
		} else if(preference.getKey().equals(getString(R.string.direct_address_management))) {
			Intent intent = new Intent(this, DesignatedContactActivity.class);
			startActivity(intent);
		} else if(preference.getKey().equals(getString(R.string.message_send_management))) {
			Intent intent = new Intent(this, SendMessageBoxActivity.class);
			startActivity(intent);
		} else if(preference.getKey().equals(getString(R.string.path_management))) {
			Intent intent = new Intent(this, RealTimeMovePathActivity.class); // 실시간 이동 경로
			startActivity(intent);
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}
