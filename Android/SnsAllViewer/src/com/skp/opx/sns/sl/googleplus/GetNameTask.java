package com.skp.opx.sns.sl.googleplus;

import java.io.IOException;

import org.json.JSONException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.skp.opx.sns.sl.OnReceiveAccessToken;

/**
 * @설명 : 구글 플러스 토근 요청 Task
 * @클래스명 : GetNameTask 
 *
 */
public class GetNameTask extends AsyncTask<Void, Void, Void> {

	private final static String G_PLUS_SCOPE = "oauth2:https://www.googleapis.com/auth/plus.me";
	private final static String USERINFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
	private final static String SCOPE = G_PLUS_SCOPE + " " + USERINFO_SCOPE;
	private static final String TAG = "TokenInfoTask";
	protected Activity mActivity;
	private int mRequestCode;
	private OnReceiveAccessToken mOnReceiveAccessToken;

	public GetNameTask(Activity activity, int requestCode, OnReceiveAccessToken onReceiveAccessToken) {
		this.mActivity = activity;
		this.mRequestCode = requestCode;
		this.mOnReceiveAccessToken = onReceiveAccessToken;
	}

	@Override
	protected Void doInBackground(Void... params) {

		try {
			fetchNameFromProfileServer();
		} catch (IOException ex) {
			mOnReceiveAccessToken.onReceiveGogglePlusAccessToken(null);
			onError("Following Error occured, please try again. " + ex.getMessage(), ex);
		} catch (JSONException e) {
			mOnReceiveAccessToken.onReceiveGogglePlusAccessToken(null);
			onError("Bad response: " + e.getMessage(), e);
		}
		return null;
	}

	/** 
	 *  계정 리턴 Method
	 * */
	private String[] getAccountNames() {

		Account[] accounts = AccountManager.get(mActivity).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[accounts.length];

		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}

		return names;
	}

	/** 
	 *  에러 콜백 Method
	 * */
	protected void onError(String msg, Exception e) {

		if (e != null) {
			mOnReceiveAccessToken.onReceiveGogglePlusAccessToken(null);
			Log.e(TAG, "Exception: ", e);
		}
	}

	/** 
	 *  Access Token 요청 Method
	 * */
	private void fetchNameFromProfileServer() throws IOException, JSONException {

		String token = fetchToken();

		if (token != null) {
			mOnReceiveAccessToken.onReceiveGogglePlusAccessToken(token);
		}
	}

	private String fetchToken() throws IOException {

		try {
			return GoogleAuthUtil.getToken(mActivity, getAccountNames()[0], SCOPE);
		} catch (GooglePlayServicesAvailabilityException playEx) {
			mOnReceiveAccessToken.onReceiveGogglePlusAccessToken(null);
		} catch (UserRecoverableAuthException userRecoverableException) {
			mOnReceiveAccessToken.onReceiveGogglePlusAccessToken(null);
			mActivity.startActivityForResult(userRecoverableException.getIntent(), mRequestCode);
		} catch (GoogleAuthException fatalException) {
			mOnReceiveAccessToken.onReceiveGogglePlusAccessToken(null);
			onError("Unrecoverable error " + fatalException.getMessage(), fatalException);
		}
		return null;
	}
}
