package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.skp.opx.svc.entity.EntityCyBesties;
import com.skp.opx.svc.ui.adapter.Adapter_CyworldFriend;

/**
 * @설명 : 싸이월드 친구 목록 Activity
 * @클래스명 : CyworldFriendListActivity
 * 
 */
public class CyworldFriendListActivity extends ListActivity implements OnClickListener, OnEditorActionListener{

	private InputMethodManager mInputMethodManager;
	private EditText mSearchEt;

	private Adapter_CyworldFriend mFriendAdapter;
	private ArrayList<EntityCyBesties> mCyBestiesArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_cyworld_friend_list);

		initWidgets(); 
		initCyBestiesList("");
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
		
		mFriendAdapter = new Adapter_CyworldFriend(this);
	}
	
	/**
	 * @설명 : Cyworld 관심일촌 목록보기 
	 * @RequestURI : https://apis.skplanetx.com/cyworld/cys/besties
	 */
	private void initCyBestiesList(final String keyword) {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.CYWORLD_BESTIES_URI, map);

		try {
			//API 호출
			AsyncRequester.request(CyworldFriendListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityCyBesties(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mCyBestiesArray = (ArrayList<EntityCyBesties>)entityArray;

					ArrayList<EntityCyBesties> sortBestiesArray = new ArrayList<EntityCyBesties>();

					for(int i = 0; i < mCyBestiesArray.size(); i++){
						if(mCyBestiesArray.get(i).cyName.contains(keyword)){
							sortBestiesArray.add(mCyBestiesArray.get(i));
						}
					}
					
					mFriendAdapter.setCyworldFriendList(sortBestiesArray);
					setListAdapter(mFriendAdapter);
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
			Intent intent_home = new Intent(CyworldFriendListActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;
		case R.id.edit_bt :
			mSearchEt.setText("");
			initCyBestiesList("");
			break;			
		}
	} 
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_search = new Intent(CyworldFriendListActivity.this, CyworldPhotoListActivity.class );
		intent_search.putExtra("ENTITY", mCyBestiesArray.get(position));
		startActivity( intent_search );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_my_cyworld, menu);
		return result;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.friend_list:
			return true;
		case R.id.my_photo_album:
			startActivity(new Intent(this, CyworldMyPhotoListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.my_guest_book:
			startActivity(new Intent(this, CyworldMyVisitbookListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			if( mSearchEt.length() == 0 ) {
				Toast.makeText(CyworldFriendListActivity.this, R.string.search_keyword_null, Toast.LENGTH_SHORT).show();
				return true;
			}else{ //검색결과 setting
				initCyBestiesList(mSearchEt.getText().toString());
				mInputMethodManager.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	}

}
