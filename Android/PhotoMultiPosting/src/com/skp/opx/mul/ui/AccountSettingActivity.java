package com.skp.opx.mul.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.skp.opx.mul.R;
import com.skp.opx.sns.sl.SnsLoginHandler;
import com.skp.opx.sns.sl.SnsManager;
import com.skp.opx.core.client.Define;
import com.skp.opx.core.client.Define.SNS_KV;
import com.skp.opx.mul.util.IntentUtil;
import com.skp.opx.mul.util.PreferenceUtil;

/**
 * @설명 : 계정설정 Activity
 * @클래스명 : AccountSettingActivity
 * 
 */
public class AccountSettingActivity extends CommonActivity {

	private Button 	mBtnFacebookLogin,mBtnTwitterLogin;
	private AccessHandler mAccessHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_setting);
	
		mAccessHandler = new AccessHandler(this);
		
		initWidgets();
		startAutoLogin();
	}
	
	/** 
	 * Init View 
	 * */
	private void initWidgets() {

		mBtnFacebookLogin	= (Button) findViewById(R.id.facebook_login_bt);
		mBtnTwitterLogin		= (Button) findViewById(R.id.twitter_login_bt);

		mBtnFacebookLogin.setOnClickListener(new OnLoginClickListener());
		mBtnTwitterLogin.setOnClickListener(new OnLoginClickListener());


		((CheckBox)findViewById(R.id.facebook_autoLogin)).setChecked(SnsManager.isAutoLoginFacebook(this));
		((CheckBox)findViewById(R.id.twitter_autoLogin)).setChecked(SnsManager.isAutoLoginTwitter(this));

		((CheckBox)findViewById(R.id.facebook_autoLogin)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				SnsManager.setAutoLoginFacebook(AccountSettingActivity.this, isChecked);
			}
		});

		((CheckBox)findViewById(R.id.twitter_autoLogin)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				SnsManager.setAutoLoginTwitter(AccountSettingActivity.this, isChecked);
			}
		});

	}
	
	/**
	 * 자동로그인
	 * */
	private void startAutoLogin() {

		mAccessHandler.loginMultiSnsAccess(SnsManager.isAutoLoginFacebook(this), SnsManager.isAutoLoginTwitter(this), SnsManager.isAutoLoginGooglePlus(this));
	}

	/**
	 * 로그인 상태 check
	 * */
	private void updateLoginState() {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				boolean isEnable = false;

				if(SnsManager.getInstance().isEnableFacebook()) {
					mBtnFacebookLogin.setText(R.string.logout);
					mBtnFacebookLogin.setBackgroundResource(R.drawable.btn_red);
					isEnable = true;
				} else {
					mBtnFacebookLogin.setText(R.string.login);
					mBtnFacebookLogin.setBackgroundResource(R.drawable.btn_def);
					SnsManager.getInstance().clearFacebookAccessToken(AccountSettingActivity.this);
				}
				if(SnsManager.getInstance().isEnableTwitter()) {
					mBtnTwitterLogin.setText(R.string.logout);
					mBtnTwitterLogin.setBackgroundResource(R.drawable.btn_red);
					isEnable = true;
				} else {
					mBtnTwitterLogin.setText(R.string.login);
					mBtnTwitterLogin.setBackgroundResource(R.drawable.btn_def);
					SnsManager.getInstance().clearTwitterAccessToken(AccountSettingActivity.this);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case Define.REQUEST_CODE:
			setLoginButtonText(data.getStringExtra(SNS_KV.KEY_ACCOUNT_LOGIN));
			PreferenceUtil.setCyworldLoginState(getApplicationContext(), true);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		setMenuItem(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {
		case MENU_HOME:
			IntentUtil.moveIntent(AccountSettingActivity.this, HomeActivity.class);
			return true;
		case MENU_MYPOSTING:
			IntentUtil.moveIntent(AccountSettingActivity.this, MypostingListActivity.class);
			return true;
		}
		return false;
	}

	@Override
	protected void setMenuItem(Menu menu) {

		menu.add(0,MENU_HOME,0,getString(R.string.tcloud));
		menu.add(0,MENU_MYPOSTING,0,getString(R.string.myposting));
		super.setMenuItem(menu);
	}

	private void setLoginButtonText(String receivedAccount) {
		if (getString(R.string.facebook).equals(receivedAccount)) {
			mBtnFacebookLogin.setText(R.string.logout);
		} else if (getString(R.string.twitter).equals(receivedAccount)) {
			mBtnTwitterLogin.setText(R.string.logout);
		}
	}

	private class AccessHandler extends SnsLoginHandler {

		public AccessHandler(Activity activity) {
			super(activity, Define.Twitter_Key.TWITTER_CONSUMER_KEY, Define.Twitter_Key.TWITTER_CONSUMER_SECRET);
		}

		@Override
		protected void onMultiLoginComplete() {

			updateLoginState();
		}

		@Override
		protected void onCompleteFacebook() {

			updateLoginState();
		}

		@Override
		protected void onCompleteTwitter() {

			updateLoginState();
		}

		@Override
		protected void onCompleteGogglePlus() {

			updateLoginState();
		}
	}

	private class OnLoginClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			try {
				switch(view.getId()) {
				case R.id.facebook_login_bt :	
					if(SnsManager.getInstance().isEnableFacebook()) {
						mAccessHandler.logout(SnsLoginHandler.FACEBOOK);
					} else {
						mAccessHandler.login(SnsLoginHandler.FACEBOOK);
					}
					break;

				case R.id.twitter_login_bt :
					if(SnsManager.getInstance().isEnableTwitter()) {
						mAccessHandler.logout(SnsLoginHandler.TWITTER);
					} else {
						mAccessHandler.login(SnsLoginHandler.TWITTER);
					}
					break;
				} 
			}catch(Exception e) {
				e.printStackTrace();
			} finally {
				updateLoginState();
			}
		}
	}
}
