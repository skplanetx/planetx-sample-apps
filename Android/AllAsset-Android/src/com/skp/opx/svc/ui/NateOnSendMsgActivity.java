package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sdk.PopupDialogUtil;
import com.skp.opx.svc.entity.EntityNateOnBuddiesInfo;
import com.skp.opx.svc.entity.EntityNateOnMsgQuery;
import com.skp.opx.svc.entity.EntityNateOnMsgSend;
import com.skp.opx.svc.ui.adapter.Adapter_NateOnNotes;
/**
 * @설명 : NateOn 메시지 발송
 * @클래스명 : NateOnSendMsgActivity
 * 
 */
public class NateOnSendMsgActivity extends ListActivity implements android.view.View.OnClickListener{

	private EditText mMsgEt;
	private EntityNateOnBuddiesInfo mBuddiesInfo;
	private ArrayList<EntityNateOnMsgQuery> mInfoArray;
	private Adapter_NateOnNotes mNotesAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nateon_msg_send);

		mBuddiesInfo = (EntityNateOnBuddiesInfo)getIntent().getSerializableExtra("ENTITY");

		initWidgets();
		nateMsgSearch();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mMsgEt = (EditText)findViewById(R.id.msg_et);

		TextView msgDesTv = (TextView)findViewById(R.id.msg_des_tv);
		msgDesTv.setText(getString(R.string.nate_msg_send, mBuddiesInfo.name));
		findViewById(R.id.back_bt).setOnClickListener(this);
		findViewById(R.id.send_bt).setOnClickListener(this);
		findViewById(R.id.msg_bt).setOnClickListener(this);
		
		mNotesAdapter = new Adapter_NateOnNotes(this);
	}

	/**
	 * @설명 : NateOn 쪽지 발송
	 * @RequestURI : https://apis.skplanetx.com/nateon/notes 
	 */
	private void nateSendMsg(){

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receivers", mBuddiesInfo.nateId);   //쪽지를 수신할 사용자의 ID를 입력합니다
		map.put("message", mMsgEt.getText().toString()); //쪽지 내용입니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_PUT_POST_bundle(this, Define.NATEON_SEND_NOTE_URI, map, null, null);

		try {
			//API 호출
			AsyncRequester.request(NateOnSendMsgActivity.this, bundle, HttpMethod.POST, new EntityParserHandler(new EntityNateOnMsgSend(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					ArrayList<EntityNateOnMsgSend> array = (ArrayList<EntityNateOnMsgSend>)entityArray;

					if(array.get(0).receiver !=null)
					{

						PopupDialogUtil.showConfirmDialog(NateOnSendMsgActivity.this, R.string.nate_on , R.string.send_success, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						});

					}else{
						PopupDialogUtil.showConfirmDialog(NateOnSendMsgActivity.this, R.string.nate_on, R.string.send_failed, null);
					}


				}

			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			PopupDialogUtil.showConfirmDialog(NateOnSendMsgActivity.this, R.string.nate_on, R.string.send_failed, null);
		}
	}

	private void nateMsgSearch(){

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", "1");
		map.put("searchType", "receiver");
		map.put("searchKeyword", mBuddiesInfo.name);
		map.put("page", "1");
		map.put("count", "50");
		map.put("boxType", "R");
		

		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.NATEON_SEND_NOTE_URI, map);

		try {

			AsyncRequester.request(NateOnSendMsgActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityNateOnMsgQuery(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					
					mInfoArray = (ArrayList<EntityNateOnMsgQuery>)entityArray;
					
					ArrayList<EntityNateOnMsgQuery> array = new ArrayList<EntityNateOnMsgQuery>();


					for(int i = 0; i < mInfoArray.size(); i++){
						array.add(mInfoArray.get(i));
					}
					
					mNotesAdapter.setNateOnBuddiesList(array);
					setListAdapter(mNotesAdapter);   
				}

			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			PopupDialogUtil.showConfirmDialog(NateOnSendMsgActivity.this, R.string.nate_on, R.string.send_failed, null);
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.back_bt :
			finish();
			break;
		case R.id.send_bt :
			nateSendMsg();
			break;
		case R.id.msg_bt:
			findViewById(R.id.sub_msg_header_ly).setVisibility(View.VISIBLE);
			findViewById(R.id.list_ly).setVisibility(View.GONE);
			findViewById(R.id.msg_bt).setVisibility(View.GONE);
			break;
		}
	}
	
}
