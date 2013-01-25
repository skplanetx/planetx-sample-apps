package com.skp.opx.sns.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.skp.opx.core.client.Define;
import com.skp.opx.core.client.Define.SNS_KV;
import com.skp.opx.sdk.OAuthenticate;
import com.skp.opx.sns.R;
import com.skp.opx.sns.sl.SnsLoginHandler;
import com.skp.opx.sns.sl.SnsManager;
import com.skp.opx.sns.util.IntentUtil;
import com.skp.opx.sns.util.PopupDialogUtil;
import com.skp.opx.sns.util.PreferenceUtil;

/**
 * @설명 : 계정 로그인 Activity
 * @클래스명 : AccountSettingActivity 
 *
 */
public class AccountSettingActivity extends CommonActivity {

	private Button 	mBtnCyworldLogin,mBtnFacebookLogin,mBtnTwitterLogin,mBtnGoogleLogin;
	private Button 	mBtnShowMenu;
	private Button 	mBtnSNS;

	private AccessHandler mAccessHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_setting);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
		
		mAccessHandler = new AccessHandler(this);
		OAuthenticate.privateAuthenticate(this, Define.OAuth.SCOPE, Define.OAuth.KEY, Define.OAuth.CLIENT_ID, Define.OAuth.SECRET);
		initView();
		startAutoLogin();
	}

	/** 
	 *  자동 로그인 요청 Method
	 * */
	private void startAutoLogin() {

		mAccessHandler.loginMultiSnsAccess(SnsManager.isAutoLoginFacebook(this), SnsManager.isAutoLoginTwitter(this), SnsManager.isAutoLoginGooglePlus(this));
	}

	/** 
	 *  로그인 업데이트 Method
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

				if(SnsManager.getInstance().isEnableGooglePlus()) {
					mBtnGoogleLogin.setText(R.string.logout);
					mBtnGoogleLogin.setBackgroundResource(R.drawable.btn_red);
					isEnable = true;
				} else {
					mBtnGoogleLogin.setText(R.string.login);
					mBtnGoogleLogin.setBackgroundResource(R.drawable.btn_def);
					SnsManager.getInstance().clearGooglePlusAccessToken(AccountSettingActivity.this);
				}

				if(isEnable) {
					mBtnSNS.setVisibility(Button.VISIBLE);
				} else {
					mBtnSNS.setVisibility(Button.GONE);
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
			IntentUtil.moveIntent(AccountSettingActivity.this, MainHomeListActivity.class);
			finish();
			return true;
		case MENU_QUIT:
			PopupDialogUtil.showYesNoDialog(
					AccountSettingActivity.this, 
					R.string.app_name, 
					R.string.want_to_quit, 
					android.R.string.ok, 
					android.R.string.cancel, 
					true, false, null, 
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					}, 
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			return true;
		}
		return false;
	}

	@Override
	protected void setMenuItem(Menu menu) {

		menu.add(0,MENU_HOME,0,getString(R.string.home));
		menu.add(0,MENU_QUIT,0,getString(R.string.finish));
		super.setMenuItem(menu);
	}

	/** 
	 *  뷰 초기화 Method
	 * */
	private void initView() {

		mBtnCyworldLogin 	= (Button) findViewById(R.id.cyworld_login_bt);
		mBtnFacebookLogin	= (Button) findViewById(R.id.facebook_login_bt);
		mBtnTwitterLogin		= (Button) findViewById(R.id.twitter_login_bt);
		mBtnGoogleLogin		= (Button) findViewById(R.id.google_login_bt);
		mBtnShowMenu		= (Button) findViewById(R.id.show_menu_bt);
		mBtnSNS 				= (Button)findViewById(R.id.sns_bt);

		mBtnCyworldLogin.setOnClickListener(new OnLoginClickListener());
		mBtnFacebookLogin.setOnClickListener(new OnLoginClickListener());
		mBtnTwitterLogin.setOnClickListener(new OnLoginClickListener());
		mBtnGoogleLogin.setOnClickListener(new OnLoginClickListener());

		mBtnShowMenu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				IntentUtil.changeActivityWithAnim(AccountSettingActivity.this, new Intent(AccountSettingActivity.this, MainPreferenceActivity.class), 1);
			}
		});

		mBtnSNS.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				IntentUtil.changeActivityWithAnim(AccountSettingActivity.this, new Intent(AccountSettingActivity.this, MainHomeListActivity.class), 1);
			}
		});

		((CheckBox)findViewById(R.id.facebook_autoLogin)).setChecked(SnsManager.isAutoLoginFacebook(this));
		((CheckBox)findViewById(R.id.twitter_autoLogin)).setChecked(SnsManager.isAutoLoginTwitter(this));
		((CheckBox)findViewById(R.id.google_autoLogin)).setChecked(SnsManager.isAutoLoginGooglePlus(this));

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

		((CheckBox)findViewById(R.id.google_autoLogin)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				SnsManager.setAutoLoginGooglePlus(AccountSettingActivity.this, isChecked);
			}
		});
	}

	/** 
	 *  로그인 버튼 텍스트 세팅 Method
	 * */
	private void setLoginButtonText(String receivedAccount) {

		if (getString(R.string.cyworld).equals(receivedAccount)) {
			mBtnCyworldLogin.setText(R.string.logout);
		}  else if (getString(R.string.facebook).equals(receivedAccount)) {
			mBtnFacebookLogin.setText(R.string.logout);
		} else if (getString(R.string.twitter).equals(receivedAccount)) {
			mBtnTwitterLogin.setText(R.string.logout);
		} else if (getString(R.string.google).equals(receivedAccount)) {
			mBtnGoogleLogin.setText(R.string.logout);
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
				case R.id.cyworld_login_bt : 	
					break;

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

				case R.id.google_login_bt : 	
					if(SnsManager.getInstance().isEnableGooglePlus()) {
						mAccessHandler.logout(SnsLoginHandler.GOOGLE_PLUS);
					} else {
						mAccessHandler.login(SnsLoginHandler.GOOGLE_PLUS);
					}
					break;
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				updateLoginState();
			}
		}
	}
}
