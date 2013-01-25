package com.skp.opx.sns.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.core.client.RequestGenerator;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sns.R;
import com.skp.opx.sns.entity.EntityCommentViews;
import com.skp.opx.sns.entity.EntityHomePostsViews;
import com.skp.opx.sns.sl.SnsManager;
import com.skp.opx.sns.ui.adapater.CommentListAdapter;
import com.skp.opx.sns.util.ImageDownloader;
import com.skp.opx.sns.util.ImageDownloaderTask;

/**
 * @설명 : 상세보기 리스트 Activity
 * @클래스명 : ContentsDetailListActivity 
 *
 */
public class ContentsDetailListActivity extends ListActivity implements OnClickListener, OnItemClickListener {

	private static final int 				MENU_REFRESH = 0;

	private Button 							mBtnComment;
	private ListView 							mCommentListView;
	private CommentListAdapter 			mCommentListAdapter;
	private EntityHomePostsViews 		mEntity;
	private ArrayList<EntityCommentViews> mEntityComments = new ArrayList<EntityCommentViews>();
	private String 							mSNSVerifier;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contents_detail_list);

		mSNSVerifier = getIntent().getStringExtra(Define.SNS_KV.KEY_SNS_NAME);
		mEntity = (EntityHomePostsViews)getIntent().getSerializableExtra(Define.INTENT_EXTRAS.KEY_ENTITY_ALL);
		initView();
	}

	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.comment_bt:
			if(mEntity.feedId == null) {
				Toast.makeText(this, R.string.not_exist_feed_id, Toast.LENGTH_SHORT).show();
				return;
			}

			Intent intent = new Intent(this, CommentsActivity.class);
			intent.putExtra(Define.INTENT_EXTRAS.FEED_ID, mEntity.feedId);
			startActivity(intent);
			break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, MENU_REFRESH, 0, getString(R.string.refresh));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	/** 
	 *  상세보기 이미지 다운로드, 리스트 초기화 Method
	 * */
	private void initView() {

		((TextView) findViewById(R.id.comment_count_tv)).setText(String.format(getString(R.string.comment_with_count), 0));

		mBtnComment = (Button) findViewById(R.id.comment_bt);
		mBtnComment.setOnClickListener(this);

		((TextView) findViewById(R.id.owner_title_tv)).setText(mEntity.name); 
		((TextView) findViewById(R.id.username_tv)).setText(mEntity.name);
		((TextView) findViewById(R.id.written_date_tv)).setText(mEntity.publishTime);

		ImageDownloader.download(mEntity.image, ((ImageView)findViewById(R.id.thumbnail_iv)));
		setContentsDiscription();

		mCommentListAdapter = new CommentListAdapter(ContentsDetailListActivity.this, mEntityComments);
		mCommentListView	= (ListView) findViewById(android.R.id.list);
		mCommentListView.setAdapter(mCommentListAdapter);
		mCommentListView.setOnItemClickListener(this);

		if(mSNSVerifier.equalsIgnoreCase("twitter")) {
			mBtnComment.setVisibility(View.GONE);
			findViewById(R.id.comment_count_tv).setVisibility(View.GONE);
			findViewById(R.id.list_ly).setVisibility(View.GONE);
			return;
		}
	}

	/**
	 * @설명 : 소셜 컴포넌트 소셜 커넥트 게시글 관리 댓글 조회
	 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds/{feedId}/comments
	 * @RequestPathParam : 
	 * {socialName} 댓글을 조회할 소셜 프로바이더 이름입니다
	 * {linkId} 댓글을 조회하는 사용자 ID입니다
	 * {feedId} 게시글 식별 ID입니다
	 */
	private void initCommentList(){
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, RequestGenerator.makeURI_CommentInfo("facebook", "me", mEntity.feedId, SnsManager.getInstance().getFacebookAccessToken()), null);

		try {
			//API 호출
			AsyncRequester.request(this,bundle, HttpMethod.GET, new EntityParserHandler(new EntityCommentViews(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {

					mEntityComments = (ArrayList<EntityCommentViews>)entityArray;
					mCommentListAdapter.setEntityList(mEntityComments);
					mCommentListAdapter.notifyDataSetChanged();
					((TextView) findViewById(R.id.comment_count_tv)).setText(String.format(getString(R.string.comment_with_count), mEntityComments.size()));
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/** 
	 *  상세보기 컨텐트 링크처리, 이미지 세팅 Method
	 * */
	private void setContentsDiscription() {

		TextView bodyTv = (TextView) findViewById(R.id.body_tv);
		bodyTv.append(mEntity.title == null ? "" : mEntity.title + "\n");
		bodyTv.append(mEntity.content == null ? "" : mEntity.content + "\n");

		Linkify.addLinks(bodyTv, Linkify.WEB_URLS);

		try {
			Bitmap picture = ImageDownloaderTask.downloadBitmap(mEntity.picture);

			int width = getResources().getDisplayMetrics().widthPixels;
			int height = (width * picture.getHeight()) / picture.getWidth(); 

			Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, width, height, false);
			picture.recycle();

			ImageSpan imageSpan = new ImageSpan(scaledBitmap);
			SpannableString span = new SpannableString(mEntity.picture);
			span.setSpan(imageSpan, 0, mEntity.picture.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			bodyTv.append(span);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onStart() { 
		super.onStart();
		if(mSNSVerifier.equals("facebook")){ initCommentList(); }
	} 

}
