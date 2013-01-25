package com.skp.opx.sns.sl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

/**
 * @설명 : SNS 자동로그인을 위한 Handler 클래스
 * @클래스명 : SnsLoginHandler
 */
public abstract class SnsLoginHandler extends Handler implements OnReceiveAccessToken {

	private Activity mActivity;
	private String mTwitterConsumerKey="";
	private String mTwitterConsumerSecret="";
	private ProgressDialog mProgressDialog;
	
	private static final int NOT_MULTI_ACCESS = -1;
	public static final int FACEBOOK = 0, TWITTER = 1, GOOGLE_PLUS = 2;
	private boolean mIsSnsAccess[] = new boolean[3];
	private int mMsgWhat = NOT_MULTI_ACCESS;
	
	public SnsLoginHandler(Activity activity,String TWITTER_CONSUMER_KEY, String TWITTER_CONSUMER_SECRET) {

		mActivity = activity;
		mTwitterConsumerKey = TWITTER_CONSUMER_KEY;
		mTwitterConsumerSecret= TWITTER_CONSUMER_SECRET; 
		mProgressDialog = new ProgressDialog(activity);
	}

	/** 
	 * SNS 순차 로그인을 위한 오버라이드 함수
	 * */
	@Override
	public void handleMessage(Message msg) {

		mMsgWhat = msg.what;
		
		if(msg.what >= mIsSnsAccess.length) {
			mProgressDialog.dismiss();
			mMsgWhat = NOT_MULTI_ACCESS;
			return;
		}
		
		if(mIsSnsAccess[msg.what]) {
			try {
				login(msg.what);
			} catch (Exception e) {
				e.printStackTrace();
				sendEmptyMessage(msg.what +1); 
			}
		} else {
			sendEmptyMessage(msg.what +1);
		}
	}
	
	/** 
	 * 복수의 SNS 로그인 시작함수
	 * */
	public void loginMultiSnsAccess(final boolean isFacebookAccess, final boolean isTwitterAccess, final boolean isGooglePlusAccess) {

		mIsSnsAccess[FACEBOOK] = isFacebookAccess;
		mIsSnsAccess[TWITTER] = isTwitterAccess;
		mIsSnsAccess[GOOGLE_PLUS] = isGooglePlusAccess;

		sendEmptyMessage(FACEBOOK);
		
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.show();
	}

	/** 
	 * 로그인 클래스의 로그인 함수를 호출한다.
	 * */
	public void login(int sns) throws Exception {
		
		switch(sns) {
		case FACEBOOK : SnsManager.startFacebookAccess(mActivity, this); break;
		case TWITTER : SnsManager.startTwitterAccess(mActivity,  mTwitterConsumerKey, mTwitterConsumerSecret, this); break;
		case GOOGLE_PLUS : SnsManager.startGooglePlusAccess(mActivity, SnsLoginHandler.this); break;
		}
	}
	
	/** 
	 * SNS 로그아웃 함수
	 * */
	public void logout(int sns) {
		
		switch(sns) {
		case FACEBOOK : SnsManager.getInstance().setFacebookAccessToken(null); break;
		case TWITTER : SnsManager.getInstance().setTwitterAccessToken(null, null); break;
		case GOOGLE_PLUS : SnsManager.getInstance().setGooglePlusAccessToken(null); break;
		}
	}
	
	/** 
	 * 페이스북 AccessToken 수신시 토큰을 저장하며, onCompleteFacebook함수를 호출한다.
	 * */
	@Override
	public void onReceiveFacebookAccessToken(String strAccessToken) {

		if(mMsgWhat != NOT_MULTI_ACCESS) {
			sendEmptyMessage(mMsgWhat+1);
		}
		
		SnsManager.getInstance().setFacebookAccessToken(strAccessToken);
		onCompleteFacebook();
	}

	/** 
	 * 트위터 AccessToken 수신시 토큰을 저장하며, onCompleteTwitter함수를 호출한다.
	 * */
	@Override
	public void onReceiveTwitterAccessToken(String strAccessToken, String strAccessVerifier) {

		if(mMsgWhat != NOT_MULTI_ACCESS) {
			sendEmptyMessage(mMsgWhat+1);
		}
		
		SnsManager.getInstance().setTwitterAccessToken(strAccessToken, strAccessVerifier);
		onCompleteTwitter();
	}

	/** 
	 * 구글플러스 AccessToken 수신시 토큰을 저장하며, onCompleteGogglePlus함수를 호출한다.
	 * */
	@Override
	public void onReceiveGogglePlusAccessToken(String strAccessToken) {

		if(mMsgWhat != NOT_MULTI_ACCESS) {
			sendEmptyMessage(mMsgWhat+1);
		}
		
		SnsManager.getInstance().setGooglePlusAccessToken(strAccessToken);
		onCompleteGogglePlus();
	}

	/** 
	 * 복수의 SNS 로그인이 모두 끝났을 때 호출되는 함수
	 * */
	protected abstract void onMultiLoginComplete();
	
	/**
	 * 페이스북 로그인이 끝났을 때 호출되는 함수
	 * */
	protected abstract void onCompleteFacebook();
	
	/** 
	 * 트위터 로그인이 끝났을 때 호출되는 함수
	 * */
	protected abstract void onCompleteTwitter();
	
	/** 
	 * 구글플러스 로그인이 끝났을 때 호출되는 함수
	 * */
	protected abstract void onCompleteGogglePlus();
}
