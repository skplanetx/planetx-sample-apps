package com.skp.opx.mss.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.skp.opx.mss.ui.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.mss.entity.EntityMelonSongSearch;
import com.skp.opx.mss.ui.adapter.Adapter_Flickr;
import com.skp.opx.mss.util.FlickrUtil;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sdk.PopupDialogUtil;

/**
 * @설명 : 내 플레이리스트 상세 Activity
 * @클래스명 : ChartMyplayDetailActivity
 * 
 */
public class ChartMyplayDetailActivity extends Activity implements OnClickListener {

	private Button 				mBtnPlay, mBtnMelonInfo, mBtnYoutube, mBtnShare;
	private TextView			mTvSubTitle, mTvInfo1, mTvInfo2;
	private GridView 			mGridview;
	private String 				mUrlValue, mStrSongName, mUriPath, mMimeType ; 
	private int					mStrContentId, mStrMenuId;

	private ArrayList<EntityMelonSongSearch>	mArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chart_detail);
		
		FlickrUtil.imageCacheIn(getApplicationContext());
		
		initWidgets();
		
		mGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Intent intent = new Intent(ChartMyplayDetailActivity.this, ShareActivity.class);
				intent.putExtra("IS_SHARE_YOUTUBE", true);
				intent.putExtra("TAG_SONGNAME", mStrSongName);
				intent.putExtra("TAG_YOUTUBE_THUMBNAIL_URI", (String)FlickrUtil.thumbnailUrl.get(position));
				intent.putExtra("TAG_YOUTUBE_VIDEO_URI", (String)FlickrUtil.videoUrl.get(position));
				startActivity(intent);
			}
		});
		
		new youTubeTask().execute();
		PopupDialogUtil.showProgressDialog(this, getString(R.string.app_name), getString(R.string.loading));
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		((Button) findViewById(R.id.play_bt)).setVisibility(View.VISIBLE);

		Intent intent=getIntent();
		
		mUriPath 		= intent.getStringExtra("uriPath");
		mMimeType		= intent.getStringExtra("mimeType");
		mStrSongName	= intent.getStringExtra("songName");
		
		mGridview		= (GridView) findViewById(R.id.gridview);
		mBtnShare		= (Button)findViewById(R.id.share_bt);
		mBtnPlay		= (Button)findViewById(R.id.play_bt);
		mBtnMelonInfo	= (Button)findViewById(R.id.melon_info_bt);
		mBtnYoutube		= (Button)findViewById(R.id.youtube_bt);

		mTvSubTitle		= (TextView)findViewById(R.id.sub_title_tv);
		mTvSubTitle.setText(mStrSongName);

		mTvInfo1		= (TextView)findViewById(R.id.info_1_tv);
		mTvInfo1.setText(intent.getStringExtra("artistName"));

		mTvInfo2		= (TextView)findViewById(R.id.info_2_tv);
		mTvInfo2.setText("");

		mBtnShare.setOnClickListener(this);
		mBtnPlay.setOnClickListener(this);
		mBtnMelonInfo.setOnClickListener(this);
		mBtnYoutube.setOnClickListener(this);
	}

	/**
	 * @설명 : Melon 곡검색
	 * @RequestURI : http://apis.skplanetx.com/melon/songs
	 */
	private void initSongSearchList(String keyword) {

		//Querystring Parameter
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		map.put("searchKeyword", keyword); //곡 검색 키워드입니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_SEARCH_SONGS_URI, map);

		try {
			//API 호출
			AsyncRequester.request(this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonSongSearch(), new OnEntityParseComplete() {
				@Override
				public void onParsingComplete(Object entityArray) {
					mArray = (ArrayList<EntityMelonSongSearch>)entityArray;
					mStrContentId	= mArray.get(0).songId;
					mStrMenuId		= mArray.get(0).menuId;
				}
			}, null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get Youtube 데이터 AsycTask
	 * */
	private class youTubeTask extends AsyncTask<String, String, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			FlickrUtil.getYoutubeData(mTvSubTitle.getText().toString());		

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mGridview.setAdapter(new Adapter_Flickr(getApplicationContext()));
			PopupDialogUtil.dismissProgressDialog();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId()){
		case R.id.share_bt :
		{
			Intent share_intent = new Intent(this, ShareActivity.class);
			share_intent.putExtra("TAG_SONGNAME", mTvSubTitle.getText().toString());
			mUrlValue="http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=song&cid="+mStrContentId+"&menuId="+mStrMenuId;
			share_intent.putExtra("TAG_SONGURI", mUrlValue);
			startActivity(share_intent);
			break;
		}
		case R.id.play_bt :
		{
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + mUriPath), mMimeType);
			startActivity(intent);
			break;
		}
		case R.id.melon_info_bt :
		{
			initSongSearchList(mTvSubTitle.getText().toString());
			
			mUrlValue="http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=song&cid="+mStrContentId+"&menuId="+mStrMenuId;
			Intent melonInfo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlValue));
			startActivity(melonInfo_intent);
			break;
		}
		case R.id.youtube_bt :
		{
			mUrlValue = "http://m.youtube.com/results?q="+mStrSongName;
			Intent melonInfo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlValue));
			startActivity(melonInfo_intent);
			break;
		}
		}
	}
}
