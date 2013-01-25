package com.skp.opx.sns.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sns.R;
import com.skp.opx.sns.entity.EntityHomePostsViews;
import com.skp.opx.sns.sl.SnsManager;
import com.skp.opx.sns.ui.adapater.SnsAdapter;
import com.skp.opx.sns.ui.adapater.SnsAdapter.DISPLAY_MODE;
import com.skp.opx.sns.util.IntentUtil;

/**
 * @설명 : SNS 모아보기 메인 Activity
 * @클래스명 : MainHomeListActivity 
 *
 */
public class MainHomeListActivity extends ListActivity implements OnClickListener, OnItemClickListener {

	private static final int MENU_REFRESH 		= 1;
	private static final int MENU_ACCOUNTSET 	= 2;

	private RadioGroup mSnsRadioGroup;
	private RadioButton mRbSimpleView, mRbDetailView;
	private Button mBtnUpdate, mBtnAccountAdd, mBtnShowMenu;
	private SnsAdapter mSnsAdapter = null;
	private ListView mListView = null;
	private ArrayList<EntityHomePostsViews> mEntityList = null;

	private enum SNS_KIND {FACEBOOK, TWITTER, GOOGLEPLUS};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_home_list);

		initView();
	}

	/** 
	 *  Widget 초기화 Method
	 * */
	private void initView() {

		mSnsRadioGroup = (RadioGroup)findViewById(R.id.group_menu_rg);

		mSnsRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				mSnsAdapter.clearEntityArray();
				updateContents();
			}
		});

		mRbSimpleView 	= (RadioButton) findViewById(R.id.simple_view_rb);
		mRbDetailView	= (RadioButton) findViewById(R.id.detail_view_rb);

		mBtnUpdate		= (Button) findViewById(R.id.update_bt);
		mBtnAccountAdd 	= (Button) findViewById(R.id.account_add_bt);
		mBtnShowMenu	= (Button) findViewById(R.id.show_menu_bt);

		mSnsAdapter	= new SnsAdapter(this);	

		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mSnsAdapter);

		mRbSimpleView.setOnClickListener(radioClickListener);
		mRbDetailView.setOnClickListener(radioClickListener);

		mBtnUpdate.setOnClickListener(this);
		mBtnAccountAdd.setOnClickListener(this);
		mBtnShowMenu.setOnClickListener(this);

		updateContents();

		for(int index = 0, maxIndex = mSnsRadioGroup.getChildCount(); index < maxIndex; index++) {
			RadioButton radio = (RadioButton)mSnsRadioGroup.getChildAt(index);

			if(radio.getVisibility() == View.VISIBLE) {
				radio.setChecked(true);
				break;
			}
		}

	}

	/** 
	 *  소셜 프로바이더 Visible/Invisible 처리 Method
	 * */
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

		if(SnsManager.getInstance().isEnableGooglePlus()){
			findViewById(R.id.google_menu_rb).setVisibility(View.VISIBLE);
		}
		else{
			findViewById(R.id.google_menu_rb).setVisibility(View.GONE);
		}

		if(findViewById(R.id.facebook_menu_rb).getVisibility()==View.GONE &&findViewById(R.id.twitter_menu_rb).getVisibility()==View.GONE && findViewById(R.id.google_menu_rb).getVisibility()==View.GONE){
			((TextView)findViewById(android.R.id.empty)).setText(R.string.not_exist_sns);
			findViewById(R.id.group_menu_rg).setVisibility(View.GONE);
			findViewById(R.id.view_rg).setVisibility(View.GONE);
		}

	}

	/** 
	 *  URI 조립 Method
	 * */
	private String makeSelectedSnsURI(SNS_KIND sns) {

		String strURI = "https://apis.skplanetx.com/social/providers/";
		String strAccessToken = "socialAccessToken=";
		String strLinkID = "";

		switch(sns) {
		case FACEBOOK : 
			strURI += "facebook";
			strAccessToken += SnsManager.getInstance().getFacebookAccessToken();
			strLinkID = "me";
			strURI += "/users/" + strLinkID + "/feeds/home?version=1&" + strAccessToken;
			break;

		case TWITTER : 
			strURI += "twitter";
			strAccessToken += SnsManager.getInstance().getTwitterAccessToken() + "&socialAccessTokenSecret=" + SnsManager.getInstance().getTwitterAccessTokenSecret();
			strLinkID = Define.Twitter_Key.TWITTER_LINK_ID;
			strURI += "/users/" + strLinkID + "/feeds/home?version=1&" + strAccessToken;
			break;

		case GOOGLEPLUS : 
			strURI += "googleplus";
			strAccessToken += SnsManager.getInstance().getGooglePlusAccessToken();
			strLinkID = "me";
			strURI += "/users/" + strLinkID + "/feeds?version=1&" + strAccessToken;
			break;

		}

		return strURI;
	}

	/** 
	 *  컨텐트 업데이트 Method
	 * */
	private void updateContents() {

		updateVisibilitySns();

		try {
			switch(mSnsRadioGroup.getCheckedRadioButtonId()) {
			case R.id.facebook_menu_rb : 
				request(SNS_KIND.FACEBOOK); 
				break;
			case R.id.twitter_menu_rb : 
				request(SNS_KIND.TWITTER); 
				break;
			case R.id.google_menu_rb :
				request(SNS_KIND.GOOGLEPLUS);
				break;
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @설명 : 소셜 컴포넌트 소셜 커넥트 지인목록 조회 및 검색
	 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/buddies
	 * @RequestPathParam : 
	 * {socialName} 댓글을 조회할 소셜 프로바이더 이름입니다
	 * {linkId} 댓글을 조회하는 사용자 ID입니다
	 */
	private void request(SNS_KIND sns) throws CloneNotSupportedException {

		switch(sns) {
		case FACEBOOK : if(SnsManager.getInstance().isEnableFacebook() == false) {return;} break;
		case TWITTER : if(SnsManager.getInstance().isEnableTwitter() == false) {return;} break;
		case GOOGLEPLUS : if(SnsManager.getInstance().isEnableGooglePlus() == false) {return;} break;
		}
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(MainHomeListActivity.this, makeSelectedSnsURI(sns) , null);
		//API 호출
		AsyncRequester.request(MainHomeListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityHomePostsViews(),new OnEntityParseComplete() {

			@Override
			public void onParsingComplete(Object entityArray) {

				mEntityList = (ArrayList<EntityHomePostsViews>) entityArray;
				mSnsAdapter.setEntityArray(mEntityList);
				mSnsAdapter.notifyDataSetChanged();
			}
		}, "comments", true));
	}

	private OnClickListener radioClickListener = new OnClickListener() {

		public void onClick(View v) {

			switch(v.getId()) {
			case R.id.simple_view_rb:
				mSnsAdapter.setDisplayMode(DISPLAY_MODE.SIMPLE);
				break;

			case R.id.detail_view_rb:
				mSnsAdapter.setDisplayMode(DISPLAY_MODE.DETAIL);
				break;
			}

			mSnsAdapter.notifyDataSetChanged();
		}
	};

	public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {

		Intent intent = new Intent(this, ContentsDetailListActivity.class);

		switch(mSnsRadioGroup.getCheckedRadioButtonId()) {
		case R.id.facebook_menu_rb : 
			intent.putExtra(Define.INTENT_EXTRAS.KEY_ENTITY_ALL, mEntityList.get(position));
			intent.putExtra(Define.SNS_KV.KEY_SNS_NAME, "facebook" );	
			break;

		case R.id.twitter_menu_rb : 
			intent.putExtra(Define.INTENT_EXTRAS.KEY_ENTITY_ALL, mEntityList.get(position));
			intent.putExtra(Define.SNS_KV.KEY_SNS_NAME, "twitter" );
			break;
		case R.id.google_menu_rb :
			intent.putExtra(Define.INTENT_EXTRAS.KEY_ENTITY_ALL, mEntityList.get(position));
			intent.putExtra(Define.SNS_KV.KEY_SNS_NAME, "googleplus");
			break;
		}

		startActivity(intent);
	}


	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.account_add_bt:
			IntentUtil.moveIntent(MainHomeListActivity.this, AccountSettingActivity.class);
			break;

		case R.id.update_bt:
			updateContents();
			break;

		case R.id.show_menu_bt:
			IntentUtil.changeActivityWithAnim(MainHomeListActivity.this, new Intent(MainHomeListActivity.this, MainPreferenceActivity.class), 1);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		setMenuItem(menu);
		return true;
	}

	private void setMenuItem(Menu menu) {
		menu.add(0,MENU_REFRESH,0,getString(R.string.refresh));
		menu.add(0,MENU_ACCOUNTSET,0,getString(R.string.account_setting));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {
		case MENU_REFRESH: 
			updateContents(); 
			return true;

		case MENU_ACCOUNTSET:
			IntentUtil.moveIntent(MainHomeListActivity.this, AccountSettingActivity.class);
			return true;
		}
		return false;
	}
}
