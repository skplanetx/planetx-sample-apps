package com.skp.opx.mul.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.AuthorInfo;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.mul.R;
import com.skp.opx.mul.entity.EntityFeedInfo;
import com.skp.opx.mul.ui.adapter.Adapter_MyPosting;
import com.skp.opx.mul.util.IntentUtil;
import com.skp.opx.mul.util.PreferenceUtil;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sdk.PopupDialogUtil;
import com.skp.opx.sns.sl.SnsManager;

/**
 * @설명 : 사용자 게시글 조회 Activity
 * @클래스명 : MypostingListActivity
 * 
 */
public class MypostingListActivity extends ListActivity implements OnItemClickListener {

	private ListView						mMypostingListView =null;
	private Adapter_MyPosting				mMypostingAdapter  =null;
	private ArrayList<EntityFeedInfo>		mEntityAllFeedList =null;	//전체Posting request한 List
	private ArrayList<EntityFeedInfo> 		mEntityFilterList  =null;	//Filtering한 List
	private RadioGroup mSnsRadioGroup;

	private enum SNS_KIND {FACEBOOK, TWITTER, CYWORLD};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_posting_list);

		initWidgets();
		
	}
	
	/**
	 * Init View
	 * */
	private void initWidgets() {

		mSnsRadioGroup = (RadioGroup)findViewById(R.id.group_menu_rg);

		mSnsRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				mMypostingAdapter.clearEntityArray();
				updateContents();
			}
		});

		mMypostingAdapter	= new Adapter_MyPosting(this);	
		mMypostingListView	= (ListView) findViewById(android.R.id.list);
		mMypostingListView.setAdapter(mMypostingAdapter);
		mMypostingListView.setOnItemClickListener(this);

		updateContents();
		
		for(int index = 0, maxIndex = mSnsRadioGroup.getChildCount(); index < maxIndex; index++) {
			RadioButton radio = (RadioButton)mSnsRadioGroup.getChildAt(index);

			if(radio.getVisibility() == View.VISIBLE) {
				radio.setChecked(true);
				break;
			}
		}
	}

	private void updateVisibilitySns() {

		if(SnsManager.getInstance().isEnableFacebook()) {
			findViewById(R.id.facebook_menu_rb).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.facebook_menu_rb).setVisibility(View.GONE);
		}
		if(SnsManager.getInstance().isEnableTwitter()) {
			findViewById(R.id.twitter_menu_rb).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.twitter_menu_rb).setVisibility(View.GONE);
		}
		if(PreferenceUtil.getOneIDAccessToken(this) != null && PreferenceUtil.getOneIDAccessToken(this).length() > 0){
			findViewById(R.id.cyworld_menu_rb).setVisibility(View.VISIBLE);
		} else{
			findViewById(R.id.cyworld_menu_rb).setVisibility(View.GONE);
		}

		if(findViewById(R.id.facebook_menu_rb).getVisibility()==View.GONE && findViewById(R.id.twitter_menu_rb).getVisibility() ==View.GONE && findViewById(R.id.cyworld_menu_rb).getVisibility() ==View.GONE)
		{
			((TextView) findViewById(android.R.id.empty)).setText(R.string.not_exist_sns);
		} 
	}

	
	private String makeSelectedSnsURI(SNS_KIND sns) {

		String strURI = "https://apis.skplanetx.com/social/providers/";
		String strAccessToken = "socialAccessToken=";
		String strLinkID 	=null;

		switch(sns) {
		case FACEBOOK : 
			strURI += "facebook";
			strAccessToken += SnsManager.getInstance().getFacebookAccessToken();
			strLinkID = "me";
			break;

		case TWITTER : 
			strURI += "twitter";
			strAccessToken += SnsManager.getInstance().getTwitterAccessToken() + "&socialAccessTokenSecret=" + SnsManager.getInstance().getTwitterAccessTokenSecret();
			strLinkID = Define.Twitter_Key.TWITTER_LINK_ID;
			break;
		case CYWORLD : 
			strURI += "cyworld";
			strLinkID = "me";
			strAccessToken += AuthorInfo.accessToken;
			break;
		}

		strURI += "/users/" + strLinkID + "/feeds?version=1&" + strAccessToken;
		return strURI;
	}

	private void updateContents() {

		updateVisibilitySns();

		try {
			switch(mSnsRadioGroup.getCheckedRadioButtonId()) {
			case R.id.facebook_menu_rb : 
				mReqeustHandler.request(SNS_KIND.FACEBOOK); 
				break;
			case R.id.twitter_menu_rb : 
				mReqeustHandler.request(SNS_KIND.TWITTER); 
				break;
			case R.id.cyworld_menu_rb :
				mReqeustHandler.request(SNS_KIND.CYWORLD); 
				break;
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private ReqeustHandler mReqeustHandler = new ReqeustHandler();

	/**
	 * @설명 : 소셜 커넥트 사용자 게시글 조회
	 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds
	 * {socialName} 댓글을 조회할 소셜 프로바이더 이름입니다
	 * {feedId} 게시글 식별 ID입니다
	 */
	private class ReqeustHandler extends Handler implements OnEntityParseComplete {

		private SNS_KIND	mCurrentSNS_KIND;
		private String 		strSocial;

		private void request(SNS_KIND sns) throws CloneNotSupportedException {

			mCurrentSNS_KIND = sns;
			String strURI = makeSelectedSnsURI(sns);

			switch(sns) {
			case FACEBOOK : 
				strSocial="facebook";
				if(SnsManager.getInstance().isEnableFacebook() == false) {
					return;
				} 
				break;
			case TWITTER : 
				strSocial="Twitter";
				if(SnsManager.getInstance().isEnableTwitter() == false) {
					return;
				}
				break;
			case CYWORLD :
				strSocial="Cyworld";
				strURI += "&category=0";
				if(PreferenceUtil.getOneIDAccessToken(MypostingListActivity.this) == null && PreferenceUtil.getOneIDAccessToken(MypostingListActivity.this).length() <= 0){
					return;
				}
				break;
			}

			//Bundle 설정
			RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(MypostingListActivity.this, strURI, null);
			//API 호출
			AsyncRequester.request(MypostingListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityFeedInfo(),this, "comments"));
		}

		@Override
		public void handleMessage(Message msg) {

			try {
				request(SNS_KIND.TWITTER);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onParsingComplete(Object entityArray) {

			PopupDialogUtil.showProgressDialog(MypostingListActivity.this, getString(R.string.loading));

//			DaoPosting dao = DaoPosting.getInstance();
//
			mEntityAllFeedList 	= (ArrayList<EntityFeedInfo>) entityArray;
			if(mEntityAllFeedList.size()<=0){
				return;
			}
//			mEntityFilterList	= new ArrayList<EntityFeedInfo>();	
//			try {
//				/** DB저장시 */
//				ArrayList<EntityPostingDB> dbList = dao.getPostingInfoListSocialType(getApplicationContext(), strSocial);
//				for(int index =0; index < dbList.size(); index++){
//					for(int index2=0; index2<mEntityAllFeedList.size(); index2++){
//						if(mEntityAllFeedList.get(index2).link.contains(dbList.get(index).postId)) {
//							mEntityFilterList.add(mEntityAllFeedList.get(index2));
//						}
//					}
//				}
//			
//			} catch (Exception e) {
//				e.printStackTrace();
//				return;
//			} finally {
//				PopupDialogUtil.dismissProgressDialog();
//			}

			mMypostingAdapter.setEntityArray(mEntityAllFeedList);
			mMypostingAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

		Intent intent = new Intent(this, MypostingDetailActivity.class);
		intent.putExtra(Define.INTENT_EXTRAS.KEY_ENTITY_ALL, mEntityAllFeedList.get(position));
		
		switch(mSnsRadioGroup.getCheckedRadioButtonId()) {
		case R.id.facebook_menu_rb : intent.putExtra(Define.SNS_KV.KEY_SNS_NAME, "facebook" ); break;
		case R.id.twitter_menu_rb : intent.putExtra(Define.SNS_KV.KEY_SNS_NAME, "twitter" ); break;
		case R.id.cyworld_menu_rb : intent.putExtra(Define.SNS_KV.KEY_SNS_NAME, "cyworld" ); break;
		}

		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0,0,0,getString(R.string.tcloud));
		menu.add(0,1,0,getString(R.string.account_manage));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {
		case 0:
			IntentUtil.moveIntent(MypostingListActivity.this, HomeActivity.class);
			return true;
		case 1:
			IntentUtil.moveIntent(MypostingListActivity.this, AccountSettingActivity.class);
			return true;
		}
		return false;
	}
}
