package com.skp.opx.rpn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Window;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.util.ServiceUtil;

/**
 * @설명 : 메시지 설정 Activity
 * @클래스명 : MessageSettingActivity 
 *
 */
public class MessageSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        super.onCreate(savedInstanceState); 
        addPreferencesFromResource(R.xml.activity_message_setting);
        //메시지 전송 주기 설정 : 설정된 값 텍스트로 보여줘야 함.
    }
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		
		if(preference.getKey().equals(getString(R.string.message_send_tick))) {
			//메시지 전송 주기 설정 : 리스트에서 선택했을 시 코딩해야됨... 글자 갱신
			//해제시 알람 매니저 종료
			findPreference(getString(R.string.message_send_tick)).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				//매뉴얼로 preference setting 해야함 
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					// TODO Auto-generated method stub
					String val = (String)newValue;			
					
					if(val.equals("0")){
						//전송 중단	
						ServiceUtil.stopMsgService(getApplicationContext());							
					}
					return true;
				}
			});
		} else if(preference.getKey().equals(getString(R.string.message_text_setting))) {
			Intent intent = new Intent(this, MessageTextSettingActivity.class);
			startActivity(intent);
		} 
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}
