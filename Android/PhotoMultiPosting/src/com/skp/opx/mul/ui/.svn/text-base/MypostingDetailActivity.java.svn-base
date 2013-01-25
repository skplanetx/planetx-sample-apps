package com.skp.opx.mul.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.skp.opx.mul.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.AuthorInfo;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.core.client.RequestGenerator;
import com.skp.opx.mul.entity.EntityComments;
import com.skp.opx.mul.entity.EntityFeedInfo;
import com.skp.opx.mul.ui.adapter.Adapter_Comment;
import com.skp.opx.mul.util.ImageDownloaderTask;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sns.sl.SnsManager;

/**
 * @설명 : 사용자 게시글 조회 상세 Activity
 * @클래스명 : MypostingDetailActivity
 * 
 */
public class MypostingDetailActivity extends Activity implements OnClickListener, OnItemClickListener {

	private Button 				mBtnPrev;
	private String 				mSNSVerifier;
	private EntityFeedInfo		mEntityFeedInfo;
	private ListView 			mCommentListView;
	private Adapter_Comment		mCommentListAdapter;
	private ArrayList<EntityComments> mEntityComments = new ArrayList<EntityComments>();

	@Override
	public void onStart() { 
		super.onStart();
		if(mSNSVerifier.equals("facebook")) {  
			initCommentList(mSNSVerifier, SnsManager.getInstance().getFacebookAccessToken());
		} else if(mSNSVerifier.equals("cyworld")) {
			initCommentList(mSNSVerifier, AuthorInfo.accessToken);
		}
	} 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_posting_detail);

		mSNSVerifier = getIntent().getStringExtra(Define.SNS_KV.KEY_SNS_NAME);
		mEntityFeedInfo = (EntityFeedInfo)getIntent().getSerializableExtra(Define.INTENT_EXTRAS.KEY_ENTITY_ALL);

		initWidgets();
	}

	/**
	 * init View 
	 * */
	private void initWidgets() {

		mBtnPrev		= (Button)findViewById(R.id.prev_bt);
		mBtnPrev.setOnClickListener(this);

		((TextView) findViewById(R.id.comment_count_tv)).setText(String.format(getString(R.string.comment_with_count), 0));
		((TextView) findViewById(R.id.written_date_tv)).setText(mEntityFeedInfo.publishTime.replaceFirst("-", getString(R.string.year)).replaceFirst("-", getString(R.string.month)).replaceFirst(" ",  getString(R.string.day)+ "  "));
		setContentsDiscription();

		mCommentListAdapter = new Adapter_Comment(MypostingDetailActivity.this, mEntityComments);
		mCommentListView	= (ListView) findViewById(android.R.id.list);
		mCommentListView.setAdapter(mCommentListAdapter);
		mCommentListView.setOnItemClickListener(this);

		if(mSNSVerifier.equalsIgnoreCase("twitter")) {
			findViewById(R.id.comment_count_tv).setVisibility(View.GONE);
			findViewById(R.id.list_ly).setVisibility(View.GONE);
			return;
		}
	}

	/**
	 * @설명 : 소셜 커넥트  댓글 조회
	 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds/{feedId}/comments
	 * @RequestPathParam : 
	 * {socialName} 댓글을 조회할 소셜 프로바이더 이름입니다
	 * {feedId} 게시글 식별 ID입니다
	 */
	private void initCommentList(String strSns, String strAccessToken){

		//Bundle 설정
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, RequestGenerator.makeURI_CommentInfo(strSns, "me", mEntityFeedInfo.feedId, strAccessToken, mEntityFeedInfo.folderNo), null);

		try {
			//API 호출
			AsyncRequester.request(this,bundle, HttpMethod.GET, new EntityParserHandler(new EntityComments(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {

					mEntityComments = (ArrayList<EntityComments>)entityArray;
					
					if(mEntityComments.size()<=0){
						return;
					}
					mCommentListAdapter.setEntityList(mEntityComments);
					mCommentListAdapter.notifyDataSetChanged();
					((TextView) findViewById(R.id.comment_count_tv)).setText(String.format(getString(R.string.comment_with_count), mEntityComments.size()));
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private void setContentsDiscription() {

		TextView bodyTv = (TextView) findViewById(R.id.body_tv);
		bodyTv.append(mEntityFeedInfo.content == null ? "" : mEntityFeedInfo.content + "\n");

		Linkify.addLinks(bodyTv, Linkify.WEB_URLS);

		try {
			Bitmap picture = ImageDownloaderTask.downloadBitmap(mEntityFeedInfo.picture);

			int width = getResources().getDisplayMetrics().widthPixels;
			int height = (width * picture.getHeight()) / picture.getWidth(); 

			Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, width, height, false);
			picture.recycle();

			ImageSpan imageSpan = new ImageSpan(scaledBitmap);
			SpannableString span = new SpannableString(mEntityFeedInfo.picture);
			span.setSpan(imageSpan, 0, mEntityFeedInfo.picture.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			bodyTv.append(span);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.prev_bt :
			startActivity(new Intent(this, MypostingListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP ));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

}
