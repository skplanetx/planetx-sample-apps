package com.skp.opx.mss.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.oauth.OAuthInfoManager;
import com.skp.openplatform.android.sdk.oauth.OAuthListener;
import com.skp.opx.core.client.Define;
import com.skp.opx.mss.database.SoundDAO;
import com.skp.opx.mss.entity.EntityMainList;
import com.skp.opx.mss.entity.EntityMainMyList;
import com.skp.opx.mss.ui.adapter.Adapter_MelonMainList_Default;
import com.skp.opx.mss.ui.adapter.Adapter_MelonMainList_Myplay;
import com.skp.opx.mss.util.PreferenceUtil;
import com.skp.opx.mss.util.SystemUtil;
import com.skp.opx.mss.util.ToastUtil;
import com.skp.opx.sdk.ErrorMessage;
import com.skp.opx.sdk.OAuthenticate;
import com.skp.opx.sns.sl.SnsLoginHandler;
import com.skp.opx.sns.sl.SnsManager;

/**
 * @설명 : 음악공유서비스 MainActivity 
 * @클래스명 : MainActivity
 * 
 */
public class MainActivity extends ListActivity implements RadioGroup.OnCheckedChangeListener, OnItemClickListener, OnClickListener {

	private	ImageButton 	mImgBtnSearch;
	private ListView		mLvMain;
	private RadioGroup 		mTabRadioGroup;
	private	RadioButton		mRbMusicChart;

	Adapter_MelonMainList_Default mMainListDefaultAdapter;
	Adapter_MelonMainList_Myplay  mMainListMyplayAdapter;

	ArrayList<EntityMainMyList> mMyplayList;
	ArrayList<EntityMainList> mMainList;

	EntityMainMyList play;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build()); 

		getAuth();
		initWidgets();
		initChartList();

		new AccessHandler(this).loginMultiSnsAccess(SnsManager.isAutoLoginFacebook(this), SnsManager.isAutoLoginTwitter(this), SnsManager.isAutoLoginGooglePlus(this));
	}

	/**
	 * SNS Login Access Handler
	 * */
	private class AccessHandler extends SnsLoginHandler {

		public AccessHandler(Activity activity) { super(activity, Define.Twitter_Key.TWITTER_CONSUMER_KEY, Define.Twitter_Key.TWITTER_CONSUMER_SECRET); }

		@Override
		protected void onMultiLoginComplete() {	}

		@Override
		protected void onCompleteFacebook() { }

		@Override
		protected void onCompleteTwitter() { }

		@Override
		protected void onCompleteGogglePlus() {	}
	}

	/**
	 * Auth 인증
	 * */
	private void getAuth(){

		OAuthenticate.privateAuthenticate(MainActivity.this, "tcloud,cyworld,user/profile", Define.OAuth.KEY, Define.OAuth.CLIENT_ID, Define.OAuth.SECRET, new OAuthListener() {

			@Override
			public void onError(String errorMessage) {

				ErrorMessage.showErrorDialog(MainActivity.this, errorMessage);
			}

			@Override
			public void onComplete(String result) {
				PreferenceUtil.setOneIDAccessToken(MainActivity.this, OAuthInfoManager.authorInfo.accessToken);
			}
		});
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mImgBtnSearch		= (ImageButton)findViewById(R.id.search_bt);
		mImgBtnSearch.setOnClickListener(this);

		mTabRadioGroup = (RadioGroup)findViewById(R.id.chart_rg);
		mTabRadioGroup.setOnCheckedChangeListener(this);

		mLvMain	= (ListView) findViewById(android.R.id.list);
		mLvMain.setOnItemClickListener(this);

		mRbMusicChart	= (RadioButton)findViewById(R.id.music_chart_rb);

	}

	/**
	 * Main page - 음악차트 (차트종류)
	 * */
	public void initChartList()
	{
		mMainListDefaultAdapter = new Adapter_MelonMainList_Default(this);

		mMainList = new ArrayList<EntityMainList>();

		mMainList.add(new EntityMainList(getString(R.string.realtime_chart)));
		mMainList.add(new EntityMainList(getString(R.string.album_top20)));
		mMainList.add(new EntityMainList(getString(R.string.recent_music)));
		mMainList.add(new EntityMainList(getString(R.string.recent_album)));
		mMainList.add(new EntityMainList(getString(R.string.recommend_dj)));

		mMainListDefaultAdapter.setMainlist(mMainList);

		setListAdapter(mMainListDefaultAdapter);
	}

	/**
	 * Main page - 내 플레이리스트 (로컬에서 음악 List 불러오기)
	 * */
	public void initMyplayList()
	{
		mMainListMyplayAdapter = new Adapter_MelonMainList_Myplay(this);

		mMyplayList =  SoundDAO.getSoundInfo(this);

		mMainListMyplayAdapter.setMyplayInfolist(mMyplayList);

		if(mMyplayList.size()>0) {
			setListAdapter(mMainListMyplayAdapter);
		}
		else {
			((TextView) findViewById(android.R.id.empty)).setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v)
	{
		if(v.getId()==R.id.search_bt)
		{
			startActivity(new Intent(this, SearchListActivity.class));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if(mRbMusicChart.isChecked())
		{
			if(position==0){
				Intent realtime_intent = new Intent(this, ChartRealtimeActivity.class);
				startActivity(realtime_intent);
			}
			if(position==1){
				Intent top_intent = new Intent(this, ChartTopAlbumActivity.class);
				startActivity(top_intent);
			}
			if(position==2){
				Intent new_song_intent = new Intent(this, ChartNewSongsActivity.class);
				startActivity(new_song_intent);
			}
			if(position==3){
				Intent new_album_intent = new Intent(this, ChartNewAlbumActivity.class);
				startActivity(new_album_intent);
			}
			if(position==4){
				Intent dj_intent = new Intent(this, MelonDJMainActivity.class);
				startActivity(dj_intent);
			}
		} else {	
			try {
				Intent myplay_intent = new Intent(this, ChartMyplayDetailActivity.class);
				myplay_intent.putExtra("songName",  mMyplayList.get(position).songName.toString());
				myplay_intent.putExtra("artistName", mMyplayList.get(position).artistName.toString());
				myplay_intent.putExtra("uriPath",  mMyplayList.get(position).uriPath.toString());
				myplay_intent.putExtra("mimeType", mMyplayList.get(position).mimeType.toString());
				startActivity(myplay_intent);
			} catch (Exception e) {
				Toast.makeText(this, R.string.format_warn_msg, Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.music_chart_rb: 
			initChartList();
			mMainListDefaultAdapter.notifyDataSetChanged();
			break;
		case R.id.myplay_chart_rb: 
			//내장 메모리 혹은 sd 카드 마운트 상태 체크
			if(!SystemUtil.isMediaMounted()){
				ToastUtil.showToastByString(this, getString(R.string.sd_unmount));
				return;
			}

			initMyplayList();
			mMainListMyplayAdapter.notifyDataSetChanged();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 1, 0, getString(R.string.account_manage));
		menu.add(0, 2, 0, getString(R.string.finish));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {
		case 1:
			startActivity(new Intent(MainActivity.this, AccountSettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
			return true;
		case 2:
			finish();
			return true;
		}
		return false;
	}
}
