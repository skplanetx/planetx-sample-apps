package com.skp.opx.sdk;
/**
 * private Open API 사용시, One ID 인증을 위한 클래스
 * @클래스명 : OAuthenticate
 * @작성자 : 박상현
 * @작성일 : 2012. 9. 26.
 * @최종수정일 : 2012. 9. 26.
 * @수정이력 - 수정일, 수정자, 수정 내용
 * TODO
 */


import android.content.Context;

import com.skp.openplatform.android.sdk.oauth.OAuthInfoManager;
import com.skp.openplatform.android.sdk.oauth.OAuthListener;

/**
 * @설명 : OAuth SDK에서 제공되는 API를 사용하여 OneID 로그인하는 클래스
 * @클래스명 : OAuthenticate
 */
public final class OAuthenticate {
	
	private static Context mContext;
	
	/** 
	 * public OAuth 로그인하는 함수
	 * */
	public static void publicAuthenticate(Context context, String strOAuthKey) {
		
		new OAuthInfoManager(strOAuthKey);
	}	
	
	/** 
	 * private OAuth 로그인하는 함수. 결과 리스너가 클래스에 존재한다.
	 * */
	public static void privateAuthenticate(Context context, String strScope, String strOAuthKey, String strClientID, String strSecret) {
		
		privateAuthenticate(context, strScope, strOAuthKey, strClientID, strSecret, oauthlis);
	}
	
	/** 
	 * private OAuth 로그인하는 함수. 결과 리스너를 외부에서 전달 받는다.
	 * */
	public static void privateAuthenticate(Context context, String strScope, String strOAuthKey, String strClientID, String strSecret, OAuthListener listener) {
		
		mContext = context;
		OAuthInfoManager oaic = new OAuthInfoManager(strOAuthKey);
		OAuthInfoManager.clientId = strClientID; //오픈플랫폼웹 사이트에서 발급받은 clientId
		OAuthInfoManager.clientSecret = strSecret; //오픈플랫웹 사이트에서 발급받은 clientSecret
		OAuthInfoManager.scope = strScope; //OAuth의 API 사용범위 scope를 저장한다.
		
		try {
			OAuthInfoManager.login(context, listener); //로그인한다.
			oaic.setContext(context); //OAuthInfoManager에 프로젝트 context를 저장한다.
			OAuthInfoManager.saveOAuthInfo(); //저장된 값들을 OAuthInfoManager에 저장한다.
			OAuthInfoManager.restoreOAuthInfo(); //저장된 OAuthInfo를 불러온다.
		} catch (Exception e) {
		}
	}
	
	/** 
	 * OAuth 로그시 결과 리스너
	 * */
	static OAuthListener oauthlis = new OAuthListener() {
		
		/** 
		 * OAuth 호출시 Error 수신 함수
		 * */
		@Override
		public void onError(String errorMessage) {
			
			//에러 내용을 팝업한다.
			ErrorMessage.showErrorDialog(mContext, errorMessage);
			//프로세서 종료
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
		@Override
		public void onComplete(String message) {
		}
	};
}
