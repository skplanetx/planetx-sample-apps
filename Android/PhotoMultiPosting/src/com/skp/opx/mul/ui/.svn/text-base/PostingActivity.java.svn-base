package com.skp.opx.mul.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.AuthorInfo;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.mul.R;
import com.skp.opx.mul.database.DaoPosting;
import com.skp.opx.mul.database.EntityPostingDB;
import com.skp.opx.mul.entity.EntityPosting;
import com.skp.opx.mul.util.BitmapUtil;
import com.skp.opx.mul.util.PreferenceUtil;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sdk.PopupDialogUtil;
import com.skp.opx.sns.sl.SnsManager;

/**
 * @설명 : 게시글(이미지) 등록 Activity
 * @클래스명 : PostingActivity
 * 
 */
public class PostingActivity extends Activity implements OnClickListener, TextWatcher, OnCheckedChangeListener {

	private static final int MAX_INPUT_LENGTH = 140;
	private static final String TEMP_FILE_PATH = "/data/data/com.skp.opx.mul/tempfile/";

	private Button mBtnSend, mBtnHome, mBtnAccount;
	private LinearLayout mFBLayout;
	private LinearLayout mTWLayout;
	private LinearLayout mALLLayout;
	private LinearLayout mCYLayout;
	private String mPhotoURL = "";
	private String mDownloadedFilePath = "";
	private String mVerifier="";
	private EditText mContent;
	private ImageView mImageView;
	private TextView mTvCount;

	private final int IMAGEDOWN = 0;
	private final int SHOWIMAGE = 1;
	private final int DISMISSPROGRESS = 2;
	private CheckBox mAllCheck;
	private CheckBox mFbCheck;
	private CheckBox mCyCheck;
	private CheckBox mTwCheck;

	private String mStrInvalidImageMessage = "";
	private ReqeustHandler mReqeustHandler = new ReqeustHandler();

	enum SOCIAL {FACEBOOK, TWITTER, CYWORLD}

	@Override
	protected void onResume() {

		setAccountVisible();
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_posting);

		getIntentData();
		initWidgets();

	}

	private void getIntentData(){

		mPhotoURL =  getIntent().getStringExtra("url");
		mVerifier = getIntent().getStringExtra("verifier");

		if(mVerifier.equals("album")){
			mDownloadedFilePath=mPhotoURL;
			mHandler.sendEmptyMessage(SHOWIMAGE);
		}
		else
			mHandler.sendEmptyMessage(IMAGEDOWN);

	}

	/**
	 * Init View
	 * */
	private void initWidgets() {

		mBtnSend		= (Button)findViewById(R.id.send_bt);
		mBtnHome		= (Button)findViewById(R.id.home_bt);
		mBtnAccount     = (Button)findViewById(R.id.account_bt);

		mContent  =(EditText)findViewById(R.id.posting_content_et);	
		mImageView = (ImageView)findViewById(R.id.posting_iv);   
		mTvCount = (TextView) findViewById(R.id.input_count_tv);
		mTvCount.setText(getString(R.string.input_count, mContent.getText().length()));

		mALLLayout = (LinearLayout)findViewById(R.id.all_ly);
		mCYLayout = (LinearLayout)findViewById(R.id.cy_layout);
		mFBLayout = (LinearLayout)findViewById(R.id.fb_layout);
		mTWLayout = (LinearLayout)findViewById(R.id.tw_layout);

		mAllCheck = (CheckBox)findViewById(R.id.posting_all_cb);
		mFbCheck = (CheckBox)findViewById(R.id.posting_fb_cb);
		mCyCheck = (CheckBox)findViewById(R.id.posting_cy_cb);
		mTwCheck = (CheckBox)findViewById(R.id.posting_tw_cb);

		mAllCheck.setOnCheckedChangeListener(this);
		mFbCheck.setOnCheckedChangeListener(this);
		mCyCheck.setOnCheckedChangeListener(this);
		mTwCheck.setOnCheckedChangeListener(this);

		mContent.addTextChangedListener(this);

		mBtnHome.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);
		mBtnAccount.setOnClickListener(this);
	}

	private void setAccountVisible(){

		if(PreferenceUtil.getOneIDAccessToken(this) != null && PreferenceUtil.getOneIDAccessToken(this).length() > 0) {
			mCYLayout.setVisibility(View.VISIBLE);
		} else {
			mCYLayout.setVisibility(View.GONE);
		}

		if(SnsManager.getInstance().isEnableFacebook()) {
			mFBLayout.setVisibility(View.VISIBLE);
		} else {
			mFBLayout.setVisibility(View.GONE);
		}

		if(SnsManager.getInstance().isEnableTwitter()) {
			mTWLayout.setVisibility(View.VISIBLE);
		} else {
			mTWLayout.setVisibility(View.GONE);
		}

		if(mCYLayout.getVisibility() == View.GONE && mFBLayout.getVisibility() == View.GONE && mTWLayout.getVisibility() == View.GONE) {
			mALLLayout.setVisibility(View.GONE);
		} else {
			mALLLayout.setVisibility(View.VISIBLE);
		}
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case IMAGEDOWN: //파일 다운로드
				PopupDialogUtil.showProgressDialog(PostingActivity.this, getResources().getString(R.string.loading));
				startImageRequest();
				break;

			case SHOWIMAGE: //파일 보여주기
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory.decodeFile(mDownloadedFilePath, options);
				mImageView.setImageBitmap(bitmap);
				PopupDialogUtil.dismissProgressDialog();
				break;

			case DISMISSPROGRESS:
				PopupDialogUtil.dismissProgressDialog();
				break;

			default:
				break;
			}
		}
	};

	private void startImageRequest(){

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				mDownloadedFilePath = BitmapUtil.requestImageFromURL(mPhotoURL, TEMP_FILE_PATH);

				if(mDownloadedFilePath != null){
					mHandler.sendEmptyMessage(SHOWIMAGE);
				}else{
					Toast.makeText(PostingActivity.this, "download failed", Toast.LENGTH_SHORT).show();
					mHandler.sendEmptyMessage(DISMISSPROGRESS);
				}
			}
		});
		t.start();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if(buttonView.getId() == mAllCheck.getId() && mAllCheck.isChecked() == false) {
			mFbCheck.setChecked(false);
			mCyCheck.setChecked(false);
			mTwCheck.setChecked(false);
		}

		if(isChecked == false) {
			return;
		}

		if(buttonView.getId() == mAllCheck.getId() && mAllCheck.isChecked() == true){ 
			if(mFBLayout.getVisibility() == View.VISIBLE) {
				mFbCheck.setChecked(isValidSocialImageFile(SOCIAL.FACEBOOK));
			} 

			if(mCYLayout.getVisibility() == View.VISIBLE) {
				mCyCheck.setChecked(isValidSocialImageFile(SOCIAL.CYWORLD));
			}

			if(mTWLayout.getVisibility() == View.VISIBLE) {
				mTwCheck.setChecked(isValidSocialImageFile(SOCIAL.TWITTER));
			}
		} else {
			if(mFbCheck.getId() == buttonView.getId()) {
				buttonView.setChecked(isValidSocialImageFile(SOCIAL.FACEBOOK));
			} else if(mCyCheck.getId() == buttonView.getId()) {
				buttonView.setChecked(isValidSocialImageFile(SOCIAL.CYWORLD));
			} else if(mTwCheck.getId() == buttonView.getId()) {
				buttonView.setChecked(isValidSocialImageFile(SOCIAL.TWITTER));
			}
		}

		if(mStrInvalidImageMessage != null && mStrInvalidImageMessage.length() > 0) {
			PopupDialogUtil.showConfirmDialog(this, getString(R.string.app_name), mStrInvalidImageMessage, null);
			mStrInvalidImageMessage = "";
		}
	}

	private boolean isValidSocialImageFile(SOCIAL social) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mDownloadedFilePath, options);

		long fileSize = new File(mDownloadedFilePath).length();
		String strFileExtention = FilenameUtils.getExtension(mDownloadedFilePath);

		switch(social) {
		//		case FACEBOOK : //최대 해상도 960px
		//			if(options.outWidth > 960 * options.outHeight > 960) {
		//				mStrInvalidImageMessage = getString(R.string.invalid_image_facebook);
		//				return false;
		//			}
		//			break;

		case CYWORLD : //jpg, png, gif, jpeg만 가능, 최대 사이즈 2 Mbyte(2097152 byte)
			if(strFileExtention.equalsIgnoreCase("jpg") == false && strFileExtention.equalsIgnoreCase("png") == false &&
			strFileExtention.equalsIgnoreCase("gif") == false && strFileExtention.equalsIgnoreCase("jpeg") == false) {
				mStrInvalidImageMessage = getString(R.string.invalid_image_cyworld_extention);
				return false;
			}

			if(fileSize > 2097152) {
				mStrInvalidImageMessage = getString(R.string.invalid_image_cyworld_size);
				return false;
			}
			break;

		case TWITTER : //jpg, png, gif만 가능, 최대 사이즈 3145728 byte
			if(strFileExtention.equalsIgnoreCase("jpg") == false && strFileExtention.equalsIgnoreCase("png") == false &&
			strFileExtention.equalsIgnoreCase("gif") == false) {
				mStrInvalidImageMessage = getString(R.string.invalid_image_cyworld_extention);
				return false;
			}

			if(fileSize > 3145728) {
				mStrInvalidImageMessage = getString(R.string.invalid_image_twitter_size);
				return false;
			}

			break;
		}

		return true;
	}

	private class ReqeustHandler extends Handler implements OnEntityParseComplete {

		public static final int START_REQUEST = 0;
		public static final int END_REQUEST = 3;

		public int mCurrentMsgWhat = 0;

		@Override
		public void handleMessage(Message msg) {

			mCurrentMsgWhat = msg.what;

			try {
				switch(msg.what) {
				case 0 :  
					if(mFbCheck.isChecked()) requestPosting(SOCIAL.FACEBOOK);
					else sendEmptyMessage(msg.what +1);
					break;

				case 1: 
					if(mTwCheck.isChecked()) 	requestPosting(SOCIAL.TWITTER);
					else sendEmptyMessage(msg.what +1);
					break;

				case 2: 
					if(mCyCheck.isChecked()) requestPosting(SOCIAL.CYWORLD);
					else sendEmptyMessage(msg.what +1);
					break;

				case END_REQUEST :
					mCurrentMsgWhat = START_REQUEST;
					Toast.makeText(PostingActivity.this, R.string.success_posting, Toast.LENGTH_SHORT).show();
					finish();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				sendEmptyMessage(msg.what +1);
			}
		}

		public void request() {

			mCurrentMsgWhat = START_REQUEST;
			sendEmptyMessage(START_REQUEST);
		}

		@Override
		public void onParsingComplete(Object entityArray) {

			if(mCurrentMsgWhat < END_REQUEST) {
				sendEmptyMessage(mCurrentMsgWhat +1);
				insertPostId_DB((ArrayList<EntityPosting>)entityArray, mCurrentMsgWhat);
			}
		}
	}

	private void insertPostId_DB(ArrayList<EntityPosting> array, int social) {

		if(array.size() <= 0) {
			return;
		}

		String strSocialName;

		switch(social) {
		case 0 : strSocialName = "facebook"; break;
		case 1 : strSocialName = "Twitter"; break;
		case 2 : strSocialName = "Cyworld"; break;
		default :  return;
		}

		EntityPostingDB mEntity = new EntityPostingDB();

		mEntity.postId = array.get(0).postId;
		mEntity.social = strSocialName;

		DaoPosting mDao = DaoPosting.getInstance();

		try {
			mDao.insertPostingInfo(PostingActivity.this, mEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @설명 : 소셜 커넥트 게시글(이미지) 등록
	 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/post/image
	 * @RequestPathParam : 
	 * {socialName} 게시글을 등록할 소셜 프로바이더 이름을 지정합니다
	 * {linkId} 사용자 ID입니다
	 */
	private void requestPosting(SOCIAL social) {

		String strSocial = null;
		String strLinkID = null, strAccessToken = null, strAccessTokenSecret = null, strCategory = null;

		if(social == SOCIAL.FACEBOOK) {
			strSocial = "facebook";
			strLinkID = "me";
			strAccessToken = SnsManager.getInstance().getFacebookAccessToken();
		} else if(social == SOCIAL.TWITTER) {
			strSocial = "twitter";
			strLinkID = Define.Twitter_Key.TWITTER_LINK_ID;
			strAccessToken = SnsManager.getInstance().getTwitterAccessToken();
			strAccessTokenSecret = SnsManager.getInstance().getTwitterAccessTokenSecret();
		} else if(social == SOCIAL.CYWORLD) {
			strSocial = "cyworld"; 
			strLinkID = "me";
			strAccessToken = AuthorInfo.accessToken;
			strCategory = "0";
		} else {
			return;
		}

		String strURl = "https://apis.skplanetx.com/social/providers/" + strSocial + "/users/" + strLinkID + "/post/image";

		Map<String, Object> parameters =new HashMap<String, Object>();
		parameters.put("socialAccessToken", strAccessToken);
		parameters.put("socialAccessTokenSecret", strAccessTokenSecret);
		parameters.put("content", mContent.getText().toString());

		if(strCategory != null) {
			parameters.put("category", "0");
		}

		File file = new File(mDownloadedFilePath);

		try {
			RequestBundle bundle = AsyncRequester.make_PUT_POST_bundle(this, strURl, parameters, null, file);
			AsyncRequester.request(PostingActivity.this, bundle, HttpMethod.POST, new EntityParserHandler(new EntityPosting(), mReqeustHandler));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.send_bt :
			if(mALLLayout.getVisibility() == View.GONE){
				PopupDialogUtil.showConfirmDialog(this, R.string.app_name, R.string.not_exist_sns, null);
			} else{
				if(mContent.getText().length()<=0){
					PopupDialogUtil.showConfirmDialog(this, R.string.app_name, R.string.edit_hint, null);
					return;
				}
				if(mFbCheck.isChecked() == false && mCyCheck.isChecked() == false && mTwCheck.isChecked() == false) {
					PopupDialogUtil.showConfirmDialog(this, R.string.app_name, R.string.not_seleted_sns, null);
					return;
				}

				mReqeustHandler.request();   
			}
			break;
		case R.id.home_bt :
			startActivity(new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP ));
			break;

		case R.id.account_bt : 
			startActivity(new Intent(this, AccountSettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP ));
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void afterTextChanged(Editable s) {

		if (s.length() > MAX_INPUT_LENGTH) {
			Toast.makeText(this, R.string.exceed_char, Toast.LENGTH_SHORT).show();
			String tempStr = s.toString().substring(0, MAX_INPUT_LENGTH);
			s.clear();
			s.append(tempStr);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (s.length() > MAX_INPUT_LENGTH) {
			return;
		}

		if(mTvCount != null) 
			mTvCount.setText(getString(R.string.input_count, s.length()));
	}
}