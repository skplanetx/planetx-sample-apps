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
import com.skp.opx.mss.entity.EntityMelonDJDetail;
import com.skp.opx.mss.ui.adapter.Adapter_Flickr;
import com.skp.opx.mss.util.FlickrUtil;
import com.skp.opx.sdk.PopupDialogUtil;

/**
 * @설명 : Melon DJ SNS 포스팅을 위한 상세정보 Activity
 * @클래스명 : MelonDJRecommendDetailActivity
 * 
 */
public class MelonDJRecommendDetailActivity extends Activity implements OnClickListener {

	private Button 				mBtnPlay, mBtnMelonInfo, mBtnYoutube, mBtnShare;
	private TextView			mTvSubTitle;
	private TextView			mTvInfo1, mTvInfo2;
	private GridView 			mGridview;
	private String 				mUrlValue, mStrSongName; 
	private int					mStrContentId, mStrMenuId;

	private EntityMelonDJDetail 	mDJDetailEntity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dj_recommend_detail);

		FlickrUtil.imageCacheIn(getApplicationContext());

		initWidgets();

		mGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(MelonDJRecommendDetailActivity.this, ShareActivity.class);
				intent.putExtra("IS_SHARE_YOUTUBE", true);
				intent.putExtra("TAG_SONGNAME", mDJDetailEntity.songName);
				intent.putExtra("TAG_YOUTUBE_THUMBNAIL_URI", (String)FlickrUtil.thumbnailUrl.get(position));
				intent.putExtra("TAG_YOUTUBE_VIDEO_URI", (String)FlickrUtil.videoUrl.get(position));
				startActivity(intent);
			}
		});

		new youTubeTask().execute();
		PopupDialogUtil.showProgressDialog(MelonDJRecommendDetailActivity.this, getResources().getString(R.string.loading));
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

		mDJDetailEntity 	= (EntityMelonDJDetail)getIntent().getSerializableExtra("ENTITY");

		mTvSubTitle		= (TextView)findViewById(R.id.sub_title_tv);
		mTvSubTitle.setText(mDJDetailEntity.songName);

		mTvInfo1		= (TextView)findViewById(R.id.info_1_tv);
		mTvInfo1.setText(mDJDetailEntity.artistName);

		mTvInfo2		= (TextView)findViewById(R.id.info_2_tv);
		mTvInfo2.setText(mDJDetailEntity.albumName);

		mBtnShare.setOnClickListener(this);
		mBtnPlay.setOnClickListener(this);
		mBtnMelonInfo.setOnClickListener(this);
		mBtnYoutube.setOnClickListener(this);
		((Button)findViewById(R.id.home_bt)).setOnClickListener(this);

		mStrMenuId		= mDJDetailEntity.menuId;
		mStrContentId	= mDJDetailEntity.songId;
		mStrSongName 	= mDJDetailEntity.songName;

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
			mUrlValue="http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=song&cid="+mStrContentId+"&menuId="+mStrMenuId;

			Intent share_intent = new Intent(this, ShareActivity.class);
			share_intent.putExtra("TAG_SONGNAME", mTvSubTitle.getText().toString());
			share_intent.putExtra("TAG_SONGURI", mUrlValue);
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
			startActivity(new Intent(MelonDJRecommendDetailActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
			break;
		}
		}
	}
}
