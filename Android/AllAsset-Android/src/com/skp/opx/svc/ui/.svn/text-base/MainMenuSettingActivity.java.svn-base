package com.skp.opx.svc.ui;

import com.skp.opx.svc.R;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * @설명 : Setting Activity
 * @클래스명 : MainMenuSettingActivity
 * 
 */
public class MainMenuSettingActivity extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener{

	private CheckBoxPreference mPreNateMainCk;
	private CheckBoxPreference mPreNateLiveCk;
	private CheckBoxPreference mPreCyMainCk;
	private CheckBoxPreference mPreCyLiveCk;
	private CheckBoxPreference mPreMelonMainCk;
	private CheckBoxPreference mPreMelonLiveCk;
	private CheckBoxPreference mPreTmapMainCk;
	private CheckBoxPreference mPreTmapLiveCk;
	private Preference         mPreTmapSet;
	private CheckBoxPreference mPre11stMainCk;
	private CheckBoxPreference mPre11stLiveCk;
	private Preference         mPre11stSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.menu_preferences);
	}

	/**
	 * Preference 초기화
	 * */
	protected void initPreferences() {

		mPreNateMainCk  = (CheckBoxPreference) findPreference(getString(R.string.nate_main_key));
		mPreNateLiveCk  = (CheckBoxPreference) findPreference(getString(R.string.nate_live_key));
		mPreCyMainCk    = (CheckBoxPreference) findPreference(getString(R.string.cy_main_key));
		mPreCyLiveCk    = (CheckBoxPreference) findPreference(getString(R.string.cy_live_key));
		mPreMelonMainCk = (CheckBoxPreference) findPreference(getString(R.string.melon_main_key));
		mPreMelonLiveCk = (CheckBoxPreference) findPreference(getString(R.string.melon_live_key));
		mPreTmapMainCk  = (CheckBoxPreference) findPreference(getString(R.string.tmap_main_key));
		mPreTmapLiveCk  = (CheckBoxPreference) findPreference(getString(R.string.tmap_live_key));
		mPreTmapSet     = (Preference) findPreference(getString(R.string.tmap_settings));
		mPre11stMainCk  = (CheckBoxPreference) findPreference(getString(R.string.eleven_main_key));
		mPre11stLiveCk  = (CheckBoxPreference) findPreference(getString(R.string.eleven_live_key));
		mPre11stSet     = (Preference) findPreference(getString(R.string.eleven_settings));
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		if(preference.getKey().equals(getString(R.string.tmap_settings))){
			Intent intent_tmap = new Intent(getApplicationContext(), MenuSettingTmapActivity.class);
			startActivity(intent_tmap);
		}
		if(preference.getKey().equals(getString(R.string.eleven_settings))){
			Intent intent_11st = new Intent(getApplicationContext(), MenuSetting11StMainCategoryActivity.class);
			startActivity(intent_11st);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return result;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.home:
			startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.menu_setting:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
