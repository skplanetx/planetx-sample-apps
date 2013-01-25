package com.skp.opx.sns.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skp.opx.sns.R;
import com.skp.opx.core.client.RequestGenerator;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sns.util.IntentUtil;
import com.skp.opx.sns.util.PreferenceUtil;

/**
 * @설명 : 메인 Preference Activity
 * @클래스명 : MainPreferenceActivity 
 *
 */
public class MainPreferenceActivity extends PreferenceActivity implements OnPreferenceClickListener {
	private Preference mPrefShowAllSNS;
	private Preference mPrefRecommendFriend;
	private Preference mPrefMakeFriend;
	private Preference mPrefAccountSetting;
	
	private Preference mPrefUploadPhonebook;
	private Preference mPrefFriendList; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.menu_preference);	

		initPreferences();

	}

	@Override
	public boolean onPreferenceClick(final Preference preference) {

		if((preference == mPrefFriendList || preference == mPrefUploadPhonebook) && PreferenceUtil.getAppUserID(this, false) == null) {

			LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout mLayout=(LinearLayout)mInflater.inflate(R.layout.dialog_set_user_info, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(MainPreferenceActivity.this);
			builder.setTitle(R.string.set_AppUserInfo);
			builder.setView(mLayout);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which){
					
					EditText mEtName = (EditText) mLayout.findViewById(R.id.name_et);
					EditText mEtPhone = (EditText) mLayout.findViewById(R.id.phone_et);
					
					
					RequestGenerator.getAppUserId(mEtName.getText().toString(), mEtPhone.getText().toString(), MainPreferenceActivity.this, new OnEntityParseComplete() {
						@Override
						public void onParsingComplete(Object entityArray) {

							PreferenceUtil.setAppUserID(MainPreferenceActivity.this, PreferenceUtil.getAppUserID(MainPreferenceActivity.this, true), false);

							if(preference == mPrefFriendList) {
								IntentUtil.changeActivityWithAnim(MainPreferenceActivity.this, new Intent(MainPreferenceActivity.this, RecommendToFriendListActivity.class), 0);
							} else {
								IntentUtil.changeActivityWithAnim(MainPreferenceActivity.this, new Intent(MainPreferenceActivity.this, UploadPhoneBookActivity.class), 0);
							}
						}
					});
				}
			});
			builder.setNegativeButton(R.string.cancel, null);
			builder.create().show();
			return true;
		}

		Class<?> cls = null;
		if (preference == mPrefShowAllSNS) {				
			cls = MainHomeListActivity.class;
		} else if (preference == mPrefFriendList) {	
			cls = RecommendToFriendListActivity.class;
		} else if (preference == mPrefUploadPhonebook) {			
			cls = UploadPhoneBookActivity.class;
		} else if (preference == mPrefAccountSetting) {		
			cls = AccountSettingActivity.class;
		}

		IntentUtil.changeActivityWithAnim(MainPreferenceActivity.this, new Intent(MainPreferenceActivity.this, cls), 0);
		return true;
	}

	/** 
	 *  Preference 초기화 Method
	 * */
	protected void initPreferences() {

		mPrefShowAllSNS 		= (Preference) findPreference(getString(R.string.key_all_sns));
		mPrefFriendList	= (Preference) findPreference(getString(R.string.key_friend_list));
		mPrefUploadPhonebook			= (Preference) findPreference(getString(R.string.key_upload_phonebook));
		mPrefAccountSetting		= (Preference) findPreference(getString(R.string.key_account));

		mPrefShowAllSNS.setOnPreferenceClickListener(this);
		mPrefFriendList.setOnPreferenceClickListener(this);
		mPrefUploadPhonebook.setOnPreferenceClickListener(this);
		mPrefAccountSetting.setOnPreferenceClickListener(this);
	}
}
