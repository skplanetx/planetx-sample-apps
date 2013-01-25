package com.skp.opx.mss.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.core.client.RequestGenerator;
import com.skp.opx.mss.util.BitmapUtil;
import com.skp.opx.mss.util.ImageDownloaderTask;
import com.skp.opx.mss.util.PreferenceUtil;
import com.skp.opx.mss.util.ToastUtil;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityAbstract;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sdk.PopupDialogUtil;
import com.skp.opx.sns.sl.SnsManager;

/**
 * @설명 : Melon 음악정보/Youtube 공유 Activity
 * @클래스명 : ShareActivity
 * 
 * 
 */
public class ShareActivity extends Activity implements OnClickListener, TextWatcher, OnCheckedChangeListener {

	private static final int MAX_INPUT_LENGTH = 140;
	private static final String TEMP_FILE_PATH = "/data/data/com.skp.opx.mss.ui/tempfile/";

	enum SOCIAL {FACEBOOK, TWITTER, CYWORLD}

	private Button 		 mBtnConfirm, mBtnCancel, mBtnAccount;
	private EditText	 	 mEtContent;
	private TextView 	 mTvCount, mPosting_content_tv;
	private LinearLayout mALLLayout, mFbCheckLy, mCyCheckLy, mTwCheckLy;
	private CheckBox     mAllCb, mFbCb, mCyCb, mTwCb;

	private String 		 mStrSongName, mStrSongUri, mStrVidioUri;

	private RequesttHandler mReqeustHandler = new RequesttHandler();

	@Override
	protected void onResume() {
		setAccountVisible();
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);

		initWidgets();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mPosting_content_tv = (TextView)findViewById(R.id.posting_content_tv);
		mALLLayout = (LinearLayout)findViewById(R.id.all_ck_ly);
		mFbCheckLy  = (LinearLayout)findViewById(R.id.fb_ck_ly);
		mCyCheckLy  = (LinearLayout)findViewById(R.id.cy_ck_ly);
		mTwCheckLy  = (LinearLayout)findViewById(R.id.tw_ck_ly);
		mAllCb      = (CheckBox)findViewById(R.id.posting_all_cb);
		mFbCb       = (CheckBox)findViewById(R.id.posting_fb_cb);
		mCyCb       = (CheckBox)findViewById(R.id.posting_cy_cb);
		mTwCb       = (CheckBox)findViewById(R.id.posting_tw_cb);
		mAllCb.setOnCheckedChangeListener(this);
		mFbCb.setOnCheckedChangeListener(this);
		mCyCb.setOnCheckedChangeListener(this);
		mTwCb.setOnCheckedChangeListener(this);

		mBtnConfirm = (Button)findViewById(R.id.confirm_bt);
		mBtnCancel	= (Button)findViewById(R.id.cancel_bt);
		mBtnAccount = (Button)findViewById(R.id.account_bt);

		mBtnConfirm.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		mBtnAccount.setOnClickListener(this);

		mEtContent		= (EditText)findViewById(R.id.posting_content_et);
		mEtContent.addTextChangedListener(this);

		mTvCount = (TextView) findViewById(R.id.input_count_tv);
		mTvCount.setText(getString(R.string.input_count, mEtContent.getText().length()));

		mStrSongName = getIntent().getStringExtra("TAG_SONGNAME");

		if(getIntent().getBooleanExtra("IS_SHARE_YOUTUBE", false)) {
			makeYouTubeContent(mStrSongName);
		} else {
			makeMelonContent(mStrSongName);
		}

	}

	/**
	 * EditText에 표현될 Melon Contents 생성
	 * */
	private void makeMelonContent(String strSongName) {

		mStrSongUri = getIntent().getStringExtra("TAG_SONGURI");

		SpannableString urlSpannableString = new SpannableString(mStrSongUri);
		urlSpannableString.setSpan(new URLSpan(mStrSongUri), 0, mStrSongUri.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		mPosting_content_tv.append(strSongName + "\r\n");
		mPosting_content_tv.append(urlSpannableString);
		mPosting_content_tv.append("\r\n");
	}

	/**
	 * EditText에 표현될 YouTube Contents 생성
	 * */
	private void makeYouTubeContent(String strSongName) {

		String strThumnailUri = getIntent().getStringExtra("TAG_YOUTUBE_THUMBNAIL_URI");
		mStrVidioUri = getIntent().getStringExtra("TAG_YOUTUBE_VIDEO_URI");

		Bitmap picture = ImageDownloaderTask.downloadBitmap(strThumnailUri);
		picture.getHeight();
		picture.getWidth();
		SpannableString imageSpannableString = new SpannableString(strThumnailUri);
		imageSpannableString.setSpan(new ImageSpan(picture), 0, strThumnailUri.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		SpannableString urlSpannableString = new SpannableString(mStrVidioUri);
		urlSpannableString.setSpan(new URLSpan(mStrVidioUri), 0, mStrVidioUri.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		mPosting_content_tv.setText(imageSpannableString);
		mPosting_content_tv.append("\r\n" + strSongName + "\r\n");
		mPosting_content_tv.append(urlSpannableString);
		mPosting_content_tv.append("\r\n");
	}

	/**
	 * SNS Posting Request Handler
	 * */
	private class RequesttHandler extends Handler implements OnEntityParseComplete {

		public static final int START_REQUEST = 0;
		public static final int END_REQUEST = 3;

		public int mCurrentMsgWhat = 0;

		@Override
		public void handleMessage(Message msg) {

			mCurrentMsgWhat = msg.what;

			try {
				switch(msg.what) {
				case 0 :  
					if(mFbCb.isChecked()) requestSnsPosting(SOCIAL.FACEBOOK);
					else sendEmptyMessage(msg.what +1);
					break;

				case 1: 
					if(mTwCb.isChecked()) 	requestSnsPosting(SOCIAL.TWITTER);
					else sendEmptyMessage(msg.what +1);
					break;

				case 2: 
					if(mCyCb.isChecked()) requestSnsPosting(SOCIAL.CYWORLD);
					else sendEmptyMessage(msg.what +1);
					break;

				case END_REQUEST : 
					mCurrentMsgWhat = START_REQUEST;
					Toast.makeText(ShareActivity.this, R.string.posting_success, Toast.LENGTH_SHORT).show();
					finish();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				sendEmptyMessage(msg.what +1);
			}
		}

		public void request() throws CloneNotSupportedException {

			mCurrentMsgWhat = START_REQUEST;
			sendEmptyMessage(START_REQUEST);
		}

		@Override
		public void onParsingComplete(Object entityArray) {

			if(mCurrentMsgWhat <  END_REQUEST) {
				sendEmptyMessage(mCurrentMsgWhat +1);
			}
		}
	}

	/**
	 * SNS Posting
	 * */
	private void requestSnsPosting(SOCIAL social) throws Exception {

		String strSocial = null, strLinkID = null, strAccessToken = null, strAccessTokenSecret = null, strCategory = null;

		switch(social) {
		case FACEBOOK : 
			strSocial = "facebook";
			strLinkID = "me";
			strAccessToken = SnsManager.getInstance().getFacebookAccessToken();
			break;

		case TWITTER :
			strSocial = "twitter";
			strLinkID = Define.Twitter_Key.TWITTER_LINK_ID;
			strAccessToken = SnsManager.getInstance().getTwitterAccessToken();
			strAccessTokenSecret = SnsManager.getInstance().getTwitterAccessTokenSecret();
			break;

		case CYWORLD :
			strSocial = "cyworld";
			strLinkID = "me";
			strAccessToken = PreferenceUtil.getOneIDAccessToken(this);

			if(getIntent().getBooleanExtra("IS_SHARE_YOUTUBE", false)){ 
				strCategory = "0"; 
			} else{ 
				strCategory = "1";
			}
			break;
		}

		RequestBundle bundle = null;

		/**
		 * @설명 : 소셜컴포넌트 게시글(이미지) 등록
		 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/post/image
		 * @RequestPathParam : 
		 * {socialName} 게시글을 등록할 소셜 프로바이더 이름을 지정합니다
		 * {linkId} 사용자 ID입니다
		 */
		if(getIntent().getBooleanExtra("IS_SHARE_YOUTUBE", false)) {
			String strURl = "https://apis.skplanetx.com/social/providers/" + strSocial + "/users/" + strLinkID + "/post/image";

			String strContents = mEtContent.getText().toString() + "\r\n" + mStrVidioUri;

			Map<String, Object> parameters =new HashMap<String, Object>();
			parameters.put("socialAccessToken", strAccessToken);
			parameters.put("socialAccessTokenSecret", strAccessTokenSecret);
			parameters.put("content", strContents);

			if(strCategory != null) {
				parameters.put("category", strCategory);
			}

			String strSavePath = BitmapUtil.requestImageFromURL(getIntent().getStringExtra("TAG_YOUTUBE_THUMBNAIL_URI"), TEMP_FILE_PATH);
			Runtime.getRuntime().exec("chmod 777 " + strSavePath);
			bundle = AsyncRequester.make_PUT_POST_bundle(this, strURl, parameters, null, new File(strSavePath));
		} 
		/**
		 * @설명 : 소셜컴포넌트 게시글(텍스트) 등록
		 * @RequestURI : https://apis.skplanetx.com/social/post
		 */
		else {
			String contents =  mStrSongName + "\r\n" + mEtContent.getText().toString() + "\r\n" + mStrSongUri;	
			String strPayload = RequestGenerator.makePayload_AddPost(contents , strSocial, strLinkID, strAccessToken, strAccessTokenSecret, strCategory);
			bundle = AsyncRequester.make_PUT_POST_bundle(this, Define.SNS_POSTING_URI, null, strPayload, null);
		}

		AsyncRequester.request(this, bundle, HttpMethod.POST, new EntityParserHandler(new EntityAbstract(), mReqeustHandler));
	}

	/**
	 * SNS 계정 Setting
	 * */
	private void setAccountVisible(){

		if(PreferenceUtil.getOneIDAccessToken(this) != null && PreferenceUtil.getOneIDAccessToken(this).length() > 0) {
			mCyCheckLy.setVisibility(View.VISIBLE);
		} else {
			mCyCheckLy.setVisibility(View.GONE);
		}

		if(SnsManager.getInstance().isEnableFacebook()) {
			mFbCheckLy.setVisibility(View.VISIBLE);
		}else{
			mFbCheckLy.setVisibility(View.GONE);
		}

		if(SnsManager.getInstance().isEnableTwitter()) {
			mTwCheckLy.setVisibility(View.VISIBLE);
		}else{
			mTwCheckLy.setVisibility(View.GONE);
		}

		if(mCyCheckLy.getVisibility() == View.GONE && mFbCheckLy.getVisibility() == View.GONE && mTwCheckLy.getVisibility() == View.GONE) {
			mALLLayout.setVisibility(View.GONE);
		} else {
			mALLLayout.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.confirm_bt :
			if(mALLLayout.getVisibility() == View.GONE){
				PopupDialogUtil.showConfirmDialog(this, R.string.app_name, R.string.account_set_msg, null);
			} else {
				if(mEtContent.getText().length()<=0){
					PopupDialogUtil.showConfirmDialog(this, R.string.app_name, R.string.enter_contents, null);
					return;
				}
				if(mFbCb.isChecked() == false && mCyCb.isChecked() == false && mTwCb.isChecked() == false){
					PopupDialogUtil.showConfirmDialog(this, R.string.app_name, R.string.account_check_msg, null);
					return;
				}
				try {
					mReqeustHandler.request();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}    
			}
			break;
		case R.id.cancel_bt :
			finish();
			break;
		case R.id.account_bt : 
			startActivity(new Intent(this, AccountSettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP ));
			break;
		}
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if(buttonView.getId() == mAllCb.getId() && mAllCb.isChecked() == false) {
			mFbCb.setChecked(false);
			mCyCb.setChecked(false);
			mTwCb.setChecked(false);
		}

		if(isChecked == false) {
			return;
		}

		if(buttonView.getId() == mAllCb.getId() && mAllCb.isChecked() == true){ 
			if(mFbCheckLy.getVisibility() == View.VISIBLE) {
				mFbCb.setChecked(true);
			} 

			if(mCyCheckLy.getVisibility() == View.VISIBLE) {
				mCyCb.setChecked(true);
			}

			if(mTwCheckLy.getVisibility() == View.VISIBLE) {
				mTwCb.setChecked(true);
			}
		} else {
			if(mFbCb.getId() == buttonView.getId()) {
				buttonView.setChecked(true);
			} else if(mCyCb.getId() == buttonView.getId()) {
				buttonView.setChecked(true);
			} else if(mTwCb.getId() == buttonView.getId()) {
				buttonView.setChecked(true);
			}
		}

		//		if(mStrInvalidImageMessage != null && mStrInvalidImageMessage.length() > 0) {
		//			PopupDialogUtil.showConfirmDialog(this, getString(R.string.app_name), mStrInvalidImageMessage, null);
		//			mStrInvalidImageMessage = "";
		//		}
	}

	@Override
	public void afterTextChanged(Editable et) {

		if (et.length() > MAX_INPUT_LENGTH) {
			ToastUtil.showToastById(getApplicationContext(), R.string.exceed_char);
			String tempStr = et.toString().substring(0, MAX_INPUT_LENGTH);
			et.clear();
			et.append(tempStr);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int before, int count) {	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		int input_length = s.length();
		if (input_length > MAX_INPUT_LENGTH) {
			return;
		}
		if(mTvCount != null) 
			mTvCount.setText(getString(R.string.input_count, input_length));
	}
}
