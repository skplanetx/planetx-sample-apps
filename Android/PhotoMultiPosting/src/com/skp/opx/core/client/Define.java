package com.skp.opx.core.client;
/**
 * @설명 : 사진멀티포스팅 define data
 * @클래스명 : Define
 * TODO
 */
public final class Define {
	
	/** Authentication */
	public final class OAuth {
		public static final String CLIENT_ID 	= "< your key here >";
		public static final String SECRET 		= "< your key here >";
		public static final String KEY 			= "< your key here >";
	}
	
	/** Twitter Consumer Key & Consumer Secret Key */
	public static final class Twitter_Key{
		public static final String TWITTER_LINK_ID					= "< your key here >";
		public static final String TWITTER_CONSUMER_KEY		="< your key here >";
		public static final String TWITTER_CONSUMER_SECRET	="< your key here >";
		public static final String TWITTER_CALLBACK_URL 		= "< your key here >";
	}
	
	/** Facebook App ID & Auth code */
	public static final class Facebook_Key {
		public static final String FACEBOOK_APP_ID 		= "< your key here >";
	}
	
	/** activity request code */
	public static final int REQUEST_CODE 			= 1;
	public static final int REQUEST_CODE_TWITTER	= 2;
	
	public static final class INTENT_EXTRAS {
		public static final String KEY_ACCOUNT_LOGIN		= "account_login";
		public static final String KEY_ENTITY_ALL			= "entity_all";
		public static final String EXTRA_SOCIAL_NAME		= "EXTRA_SOCIAL_NAME";
		public static final String EXTRA_LINK_ID			= "EXTRA_LINK_ID";
		public static final String FEED_ID					= "FEED_ID";
	}
	
	public static final class SNS_KV {
		public static final String KEY_ACCOUNT_LOGIN	= "account_login";
		public static final String KEY_ENTITY_ALL		= "entity_all";
		public static final String KEY_SNS_NAME			= "sns_name";
	}
	
	public static final class SNS_INTENT {
		public static final String ACTION_LOGIN_OK = "com.skp.opx.sns.LOGIN_OK";
	}

	/** T cloud API Call URI */
	/** 사진 목록 조회*/
	public static final String TCLOUD_IMAGE_SEARCH_URI = "https://apis.skplanetx.com/tcloud/images";
}
