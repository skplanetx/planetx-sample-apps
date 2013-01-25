package com.skp.opx.svc.ui;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.EntityNateOnBuddiesId;
import com.skp.opx.svc.entity.EntityNateOnBuddiesInfo;
import com.skp.opx.svc.ui.adapter.Adapter_NateOnBuddies;

/**
 * @설명 : NateOn 친구 목록 Activity
 * @클래스명 : NateOnBuddiesListActivity
 * 
 */
public class NateOnBuddiesListActivity extends ListActivity implements OnClickListener, OnEditorActionListener{

	private Adapter_NateOnBuddies mBuddiesAdapter;
	private ArrayList<EntityNateOnBuddiesInfo> mInfoArray;
	private ArrayList<EntityNateOnBuddiesInfo> mOrderArray;
	private ArrayList<EntityNateOnBuddiesId>   mIdArray;

	private InputMethodManager mInputMethodManager;
	private EditText mSearchEt;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nateon_buddies_list);

		initWidgets();
		getNateIds();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		mSearchEt = (EditText)findViewById(R.id.friend_search_et);
		mSearchEt.setOnEditorActionListener(this);

		findViewById(R.id.home_bt).setOnClickListener(this);
		findViewById(R.id.edit_bt).setOnClickListener(this);
		
	}

	/**
	 * @설명 : NateOn 친구 목록 조회
	 * @RequestURI : https://apis.skplanetx.com/nateon/buddies 
	 */
	private void getNateIds() {

		final StringBuilder sb = new StringBuilder();

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", "1");
		map.put("count", "50");
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.NATEON_FRIENDS_SEARCH_URI, map);

		try {
			//API 호출
			AsyncRequester.request(NateOnBuddiesListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityNateOnBuddiesId(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {

					mIdArray = (ArrayList<EntityNateOnBuddiesId>)entityArray;

					// NateId Array -> NateId String
					for(Iterator<EntityNateOnBuddiesId> it = mIdArray.iterator() ; it.hasNext() ; ){  
						EntityNateOnBuddiesId value = it.next();             

						if(value.nateId != null){
							sb.append(value.nateId).append(";");
						}
					}

					new Handler().post(new Runnable() {

						@Override
						public void run() {
							initBuddiesList(sb.toString());
						}
					});
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * @설명 : NateOn 친구 정보 조회
	 * @RequestURI : https://apis.skplanetx.com/nateon/buddies/profiles
	 */
	private void initBuddiesList(String nateIds) {
		
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nateIds", nateIds);  //조회할 친구들의 ID를 입력합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(NateOnBuddiesListActivity.this, Define.NATEON_FRIENDS_INFO, map);

		try {
			//API 호출
			AsyncRequester.request(NateOnBuddiesListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityNateOnBuddiesInfo(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {

					mInfoArray = (ArrayList<EntityNateOnBuddiesInfo>)entityArray;

					mOrderArray = new ArrayList<EntityNateOnBuddiesInfo>();
					for(int i = 0 ; i < mInfoArray.size(); i++) {
						mOrderArray.add(mInfoArray.get(i));
					}
					// GroupID Comparator 
					final Comparator<EntityNateOnBuddiesInfo> idComparator= new Comparator<EntityNateOnBuddiesInfo>() {

						private final Collator collator = Collator.getInstance();
						@Override
						public int compare(EntityNateOnBuddiesInfo array1, EntityNateOnBuddiesInfo array2) {
							return collator.compare(array1.groupId, array2.groupId);
						}
					};
					Collections.sort(mOrderArray, idComparator);
					
					mBuddiesAdapter = new Adapter_NateOnBuddies(NateOnBuddiesListActivity.this);
					mBuddiesAdapter.setNateOnBuddiesList(mOrderArray);
					setListAdapter(mBuddiesAdapter);
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.home_bt :
			Intent intent = new Intent(NateOnBuddiesListActivity.this, MainActivity.class );
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent );
			break;
		case R.id.edit_bt :
			mSearchEt.setText("");
			break;
		}
	} 

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_search = new Intent(NateOnBuddiesListActivity.this, NateOnSendMsgActivity.class );
		intent_search.putExtra("ENTITY", mOrderArray.get(position));
		startActivity( intent_search );
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			if( mSearchEt.length() == 0 ) {
				Toast.makeText(NateOnBuddiesListActivity.this, R.string.search_keyword_null, Toast.LENGTH_SHORT).show();
				return true;
			}else{ //친구 검색 결과

				mInputMethodManager.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	}

}
