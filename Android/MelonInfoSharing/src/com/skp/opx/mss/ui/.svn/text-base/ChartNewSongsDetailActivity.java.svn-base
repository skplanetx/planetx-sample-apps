package com.skp.opx.mss.ui;

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
import com.skp.opx.mss.entity.EntityMelonNewSongChart;
import com.skp.opx.mss.ui.adapter.Adapter_Flickr;
import com.skp.opx.mss.util.FlickrUtil;
import com.skp.opx.sdk.PopupDialogUtil;

/**
 * @설명 : 최신곡 차트 상세 Activity
 * @클래스명 : ChartNewSongsDetailActivity
 * 
 */
public class ChartNewSongsDetailActivity extends Activity implements OnClickListener {

	private Button 				mBtnPlay, mBtnMelonInfo, mBtnYoutube, mBtnShare;
	private TextView			mTvSubTitle, mTvInfo1, mTvInfo2;
	private GridView 			mGridview;
	private String 				mUrlValue, mStrSongName; 
	private int					mStrContentId, mStrMenuId;

	EntityMelonNewSongChart 	mMelonEntity;
	
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
				
				Intent intent = new Intent(ChartNewSongsDetailActivity.this, ShareActivity.class);
				intent.putExtra("IS_SHARE_YOUTUBE", true);
				intent.putExtra("TAG_SONGNAME", mMelonEntity.songName);
				intent.putExtra("TAG_YOUTUBE_THUMBNAIL_URI", (String)FlickrUtil.thumbnailUrl.get(position));
				intent.putExtra("TAG_YOUTUBE_VIDEO_URI", (String)FlickrUtil.videoUrl.get(position));
				startActivity(intent);
			}
		});
		
		new youTubeTask().execute();		
		PopupDialogUtil.showProgressDialog(ChartNewSongsDetailActivity.this, getResources().getString(R.string.loading));
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mGridview		= (GridView) findViewById(R.id.gridview);
		mBtnShare		= (Button) findViewById(R.id.share_bt);
		mBtnPlay		= (Button) findViewById(R.id.play_bt);
		mBtnMelonInfo	= (Button) findViewById(R.id.melon_info_bt);
		mBtnYoutube		= (Button) findViewById(R.id.youtube_bt);

		mMelonEntity = (EntityMelonNewSongChart)getIntent().getSerializableExtra("ENTITY");
		
		mTvSubTitle		= (TextView) findViewById(R.id.sub_title_tv);
		mTvSubTitle.setText(mMelonEntity.songName);

		mTvInfo1		= (TextView) findViewById(R.id.info_1_tv);
		mTvInfo1.setText(mMelonEntity.artistName);

		mTvInfo2		= (TextView) findViewById(R.id.info_2_tv);
		mTvInfo2.setText(mMelonEntity.issueDate);

		mBtnShare.setOnClickListener(this);
		mBtnPlay.setOnClickListener(this);
		mBtnMelonInfo.setOnClickListener(this);
		mBtnYoutube.setOnClickListener(this);
		((Button)findViewById(R.id.home_bt)).setOnClickListener(this);

		mStrMenuId		= mMelonEntity.menuId;
		mStrContentId	= mMelonEntity.songId;
		mStrSongName 	= mMelonEntity.songName;

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
			// 공유하기 Activity로 이동
			// default tag 값으로 곡이름, 가수이름 넘겨줌
			Intent share_intent = new Intent(this, ShareActivity.class);
			mUrlValue="http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=song&cid="+mStrContentId+"&menuId="+mStrMenuId;
			share_intent.putExtra("TAG_SONGURI", mUrlValue);
			share_intent.putExtra("TAG_SONGNAME", mTvSubTitle.getText().toString());
			startActivity(share_intent);
			break;
		}
		case R.id.melon_info_bt :
		{
			// 해당 곡의 멜론 상세 웹페이지로 이동 
			mUrlValue="http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=song&cid="+mStrContentId+"&menuId="+mStrMenuId;
			Intent melonInfo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlValue));
			startActivity(melonInfo_intent);
			break;
		}
		case R.id.youtube_bt :
		{
			// 해당 곡의 유투브 검색 웹페이지로 이동
			mUrlValue = "http://m.youtube.com/results?q="+mStrSongName;
			Intent melonInfo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlValue));
			startActivity(melonInfo_intent);
			break;
		}
		case R.id.home_bt :
		{
			startActivity(new Intent(ChartNewSongsDetailActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
			break;
		}
		}
	}
}
