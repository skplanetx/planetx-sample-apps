package com.skp.opx.sns.sl;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.skp.opx.core.client.Define.Facebook_Key;
import com.skp.opx.core.client.Define.Twitter_Key;
import com.skp.opx.sns.sl.facebook.DialogError;
import com.skp.opx.sns.sl.facebook.Facebook;
import com.skp.opx.sns.sl.facebook.Facebook.DialogListener;
import com.skp.opx.sns.sl.facebook.FacebookError;
import com.skp.opx.sns.sl.googleplus.GetNameTask;

/**
 * @설명 : SNS 로그인 manager 클래스
 * @클래스명 : SnsManager
 */
public final class SnsManager {

	private String mFacebookAccessToken = null;
	private String mTwitterAccessToken = null;
	private String mTwitterAccessTokenSecret = null;
	private String mGooglePlusAccessToken = null;

	private static SnsManager mSnsManager = null;

	private SnsManager() {}

	public static SnsManager getInstance() {

		if(mSnsManager == null) {
			mSnsManager = new SnsManager();
		}

		return mSnsManager;
	}
	
	/** 
	 * 저장된 페이스북 AccessToken을 반환한다.
	 * */
	public String getFacebookAccessToken() 							 						{ return mFacebookAccessToken; }
	
	/** 
	 * 저장된 트위터 AccessToken을 반환한다.
	 * */
	public String getTwitterAccessToken() 								 						{ return mTwitterAccessToken; }
	
	/** 
	 * 저장된 트위터 Secret AccessToken을토큰을 반환한다.
	 * */
	public String getTwitterAccessTokenSecret() 								 				{ return mTwitterAccessTokenSecret; }
	
	/** 
	 * 저장된 구글플러스 AccessToken을 반환한다.
	 * */
	public String getGooglePlusAccessToken() 							 						{ return mGooglePlusAccessToken; }
	
	/** 
	 * 페이스북 AccessToken을 저장한다.
	 * */
	public void setFacebookAccessToken(String strAccessToken)  						{ mFacebookAccessToken = strAccessToken; }
	
	/** 
	 * 트위터 AccessToken을 저장한다.
	 * */
	public void setTwitterAccessToken(String strAccessToken, String strSecretAccessToken) 	{ mTwitterAccessToken = strAccessToken; mTwitterAccessTokenSecret = strSecretAccessToken;}
	
	/** 
	 * 구글플러스 AccessToken을 저장한다.
	 * */
	public void setGooglePlusAccessToken(String strAccessToken) 						{ mGooglePlusAccessToken = strAccessToken; }
	
	/** 
	 * 페이스북 사용가능한지 여부
	 * */
	public boolean isEnableFacebook() 															{ return mFacebookAccessToken != null && mFacebookAccessToken.length() > 0; }
	
	/** 
	 * 트위터 사용가능한지 여부
	 * */
	public boolean isEnableTwitter() 																{ return mTwitterAccessToken != null && mTwitterAccessToken.length() > 0 && mTwitterAccessTokenSecret != null && mTwitterAccessTokenSecret.length() > 0; }
	
	/** 
	 * 구글플러스 사용가능한지 여부
	 * */
	public boolean isEnableGooglePlus()															{ return mGooglePlusAccessToken != null && mGooglePlusAccessToken.length() > 0; }
	
	/** 
	 * 페이스북 AccessToken 수신을 위한 절차를 시작한다.
	 * */
	public static void startFacebookAccess(Activity activity, final OnReceiveAccessToken tokenInterface) throws Exception {

		Facebook facebook = new Facebook(Facebook_Key.FACEBOOK_APP_ID);
		facebook.authorize_def(activity, new String[] {"email,read_stream,read_friendlists,publish_stream,user_photos,friends_photos,user_videos,read_mailbox,user_actions.video,manage_pages,offline_access,friends_videos,friends_birthday,user_birthday"}, new DialogListener() {

			@Override
			public void onComplete(Bundle values) {

				tokenInterface.onReceiveFacebookAccessToken(values.getString("access_token"));
			}

			@Override
			public void onFacebookError(FacebookError e) {

				tokenInterface.onReceiveFacebookAccessToken(null);
			}

			@Override
			public void onError(DialogError e) {	

				tokenInterface.onReceiveFacebookAccessToken(null);
			}

			@Override
			public void onCancel() {

				tokenInterface.onReceiveFacebookAccessToken(null);
			}
		} );
	}

	/** 
	 * 트위터 AccessToken 수신을 위한 절차를 시작한다.
	 * */
	public static void startTwitterAccess(Context context, String strTwitterConsumerKey, String strTwitterConsumerSecret, final OnReceiveAccessToken tokenInterface) throws Exception {

		TwitterFactory factory = new TwitterFactory();
		Twitter twitter = factory.getOAuthAuthorizedInstance(strTwitterConsumerKey, strTwitterConsumerSecret);
		RequestToken requestToken = twitter.getOAuthRequestToken(Twitter_Key.TWITTER_CALLBACK_URL);
		startTwitterWebView(context, twitter, requestToken, tokenInterface);
	}

	/** 
	 * 구글플러스 AccessToken 수신을 위한 절차를 시작한다.
	 * */
	public static void startGooglePlusAccess(Activity activity, final OnReceiveAccessToken tokenInterface)  throws Exception {

		new GetNameTask(activity, 0, tokenInterface).execute();
	}	

	/** 
	 * 트위터 AccessToken 수신을 위하여 웹뷰를 호출하며, Preference 저장값이 있으면 Preference에 저장된 값을 사용한다.
	 * */
	private static void startTwitterWebView(final Context context, final Twitter twitter, final RequestToken requestToken, final OnReceiveAccessToken tokenInterface) throws Exception {

		if(getTwitterAccessToken(context) != null && getTwitterTokenSecret(context) != null) {
			tokenInterface.onReceiveTwitterAccessToken(getTwitterAccessToken(context), getTwitterTokenSecret(context));
			return;
		}

		WebView webView = new WebView(context);

		final Dialog dialog = new Dialog(context);
		dialog.setTitle("TWITTER");
		dialog.setContentView(webView, new LayoutParams(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels));
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				tokenInterface.onReceiveTwitterAccessToken(null, null);
			}
		});
		dialog.show();

		webView.loadUrl(requestToken.getAuthorizationURL());
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if (url != null && url.equals("http://mobile.twitter.com/")) {
					dialog.dismiss();
					tokenInterface.onReceiveTwitterAccessToken(null, null);
				} else if (url != null && url.startsWith("https://developers.skplanetx.com")) {
					String[] urlParameters = url.split("\\?")[1].split("&");
					String oauthToken = "";
					String oauthVerifier = "";

					if (urlParameters[0].startsWith("oauth_token")) {
						oauthToken = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_token")) {
						oauthToken = urlParameters[1].split("=")[1];
					}

					if (urlParameters[0].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[1].split("=")[1];
					}

					try {
						AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
						setTwitterAccessToken(context, accessToken.getToken(), accessToken.getTokenSecret());
						tokenInterface.onReceiveTwitterAccessToken(accessToken.getToken(), accessToken.getTokenSecret());
					} catch (TwitterException e) {
						tokenInterface.onReceiveTwitterAccessToken(null, null);
					} finally {
						dialog.dismiss();
					}
				}
			}
		});
	}

	/** 
	 * 저장된 페이스북 AccessToken을 삭제한다. 
	 * */
	public void clearFacebookAccessToken(Context context) {

		setFacebookAccessToken(null);
	}

	/** 
	 * 저장된 트위터 AccessToken을 삭제한다. 
	 * */
	public void clearTwitterAccessToken(Context context) {

		setTwitterAccessToken(null, null);
	}

	/** 
	 * 저장된 구글플러스 AccessToken을 삭제한다. 
	 * */
	public void clearGooglePlusAccessToken(Context context) {

		setGooglePlusAccessToken(null);
	}

	/** 
	 * 트위터 AccessToken을 Preference에 저장한다. 
	 * */
	private static void setTwitterAccessToken(Context context, String strOauthToken, String strOauthTokenSecret) {

		Editor editor = context.getSharedPreferences("TWITTER", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).edit();
		editor.putString("TOKEN", strOauthToken);
		editor.putString("SECRET", strOauthTokenSecret);
		editor.commit();
	}

	/** 
	 * 저장된 트위터 AccessToken을 반환한다. 
	 * */
	private static String getTwitterAccessToken(Context context) {

		SharedPreferences pre = context.getSharedPreferences("TWITTER", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		return pre.getString("TOKEN", null);
	}

	/** 
	 * 저장된 트위터 Secret AccessToken을 반환한다. 
	 * */
	private static String getTwitterTokenSecret(Context context) {

		SharedPreferences pre = context.getSharedPreferences("TWITTER", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		return pre.getString("SECRET", null);
	}
	
	/** 
	 * 페이스북 자동 로그인 여부를 저장한다.
	 * */
	public static void setAutoLoginFacebook(Context context, boolean isAuto) {

		Editor editor = context.getSharedPreferences("AUTO", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).edit();
		editor.putBoolean("Facebook", isAuto);
		editor.commit();
	}

	/** 
	 * 페이스북 자동 로그인 여부
	 * */
	public static boolean isAutoLoginFacebook(Context context) {

		SharedPreferences pre = context.getSharedPreferences("AUTO", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		return pre.getBoolean("Facebook", false);
	}
	
	/** 
	 * 트위터 자동 로그인 여부를 저장한다.
	 * */
	public static void setAutoLoginTwitter(Context context, boolean isAuto) {

		Editor editor = context.getSharedPreferences("AUTO", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).edit();
		editor.putBoolean("Twitter", isAuto);
		editor.commit();
	}

	/** 
	 * 트위터 자동 로그인 여부
	 * */
	public static boolean isAutoLoginTwitter(Context context) {

		SharedPreferences pre = context.getSharedPreferences("AUTO", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		return pre.getBoolean("Twitter", false);
	}
	
	/** 
	 * 구글플러스 자동 로그인 여부를 저장한다.
	 * */
	public static void setAutoLoginGooglePlus(Context context, boolean isAuto) {

		Editor editor = context.getSharedPreferences("AUTO", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).edit();
		editor.putBoolean("GooglePlus", isAuto);
		editor.commit();
	}

	/** 
	 * 구글플러스 자동 로그인 여부
	 * */
	public static boolean isAutoLoginGooglePlus(Context context) {

		SharedPreferences pre = context.getSharedPreferences("AUTO", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		return pre.getBoolean("GooglePlus", false);
	}
}
