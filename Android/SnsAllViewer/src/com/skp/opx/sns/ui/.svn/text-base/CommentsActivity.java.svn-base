package com.skp.opx.sns.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.core.client.RequestGenerator;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityAbstract;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sns.R;
import com.skp.opx.sns.sl.SnsManager;

/**
 * @설명 : 댓글 Activity
 * @클래스명 : CommentsActivity 
 *
 */
public class CommentsActivity extends Activity implements OnClickListener, TextWatcher {

	private static final int MAX_INPUT_LENGTH = 140;

	private EditText mEtComment;
	private TextView mTvCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);

		initWidgets();
	}

	/** 
	 *  Widget 초기화 Method
	 * */
	private void initWidgets() {

		mEtComment = (EditText) findViewById(R.id.comment_et);
		mEtComment.addTextChangedListener(this);
		mTvCount = (TextView) findViewById(R.id.input_count_tv);
		mTvCount.setText(getString(R.string.input_count, mEtComment.getText().length()));
		((Button) findViewById(R.id.send_bt)).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		switch(view.getId()) {
		case R.id.send_bt : 
			/**
			 * @설명 : 소셜 컴포넌트 소셜 커넥트 게시글 관리 댓글 등록
			 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds/{feedId}/comments
			 * @RequestPathParam : 
			 * {socialName} 소셜 프로바이더 이름입니다
			 * {linkId} 댓글을 등록하는 사용자 ID입니다
			 * {feedId} 게시글 식별 ID입니다
			 */
			try {	
				String strPayload = RequestGenerator.makePayload_AddComment(mEtComment.getText().toString(), SnsManager.getInstance().getFacebookAccessToken());
				String strUri = RequestGenerator.makeURI_AddComment(getIntent().getStringExtra(Define.INTENT_EXTRAS.FEED_ID));
				RequestBundle bundle = AsyncRequester.make_PUT_POST_bundle(this, strUri, null, strPayload, null);

				AsyncRequester.request(this, bundle, HttpMethod.POST, new EntityParserHandler(new EntityAbstract(), new OnEntityParseComplete() {

					@Override
					public void onParsingComplete(Object entityArray) {
						
						Toast.makeText(CommentsActivity.this, R.string.complete_msg, Toast.LENGTH_SHORT).show();
						finish();
					}
				}));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void afterTextChanged(Editable s) {

		if (s.length() > MAX_INPUT_LENGTH) {
			Toast.makeText(this, R.string.exceed_char, Toast.LENGTH_SHORT).show();
			String tempStr = s.toString().substring(0, MAX_INPUT_LENGTH);
			s.clear();
			s.append(tempStr);
		}
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (s.length() > MAX_INPUT_LENGTH) {
			return;
		}

		if(mTvCount != null) mTvCount.setText(getString(R.string.input_count, s.length()));
	}
}
