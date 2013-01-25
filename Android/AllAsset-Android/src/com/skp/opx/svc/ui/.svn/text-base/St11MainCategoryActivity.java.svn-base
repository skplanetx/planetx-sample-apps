package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.skp.opx.svc.entity.Entity11stCategory;
import com.skp.opx.svc.ui.adapter.Adapter_11stMainCategory;

/**
 * @설명 : 11번가 메인 카테고리 Activity
 * @클래스명 : St11MainCategoryActivity
 * 
 */
public class St11MainCategoryActivity extends ListActivity implements OnClickListener, OnEditorActionListener{

	private InputMethodManager mInputMethodManager;
	private EditText mSearchEt;
	
	private Adapter_11stMainCategory mCategoryAdapter;
	private ArrayList<Entity11stCategory> mCategoryArray; 

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eleven_st_main_category);

		initWidgets();
		initCategoryList();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		mSearchEt = (EditText)findViewById(R.id.product_search_et);
		mSearchEt.setOnEditorActionListener(this);
		findViewById(R.id.home_bt).setOnClickListener(this);
		findViewById(R.id.edit_bt).setOnClickListener(this);
		
		mCategoryAdapter = new Adapter_11stMainCategory(this);
	}

	/**
	 * @설명 : 11번가  카테고리 전체조회
	 * @RequestURI : http://apis.skplanetx.com/11st/common/categories
	 */
	private void initCategoryList() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.ELEVEN_ST_CATEGORY_URI, map);

		try {
			//API 호출
			AsyncRequester.request(St11MainCategoryActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stCategory(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mCategoryArray = (ArrayList<Entity11stCategory>)entityArray;
					mCategoryAdapter.set11stMainCategoryList(mCategoryArray);
					setListAdapter(mCategoryAdapter);
				}
			},"RootCategory"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.home_bt :
			Intent intent_home = new Intent(St11MainCategoryActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;
		case R.id.edit_bt :
			mSearchEt.setText("");
			break;

		}
	} 

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_search = new Intent(St11MainCategoryActivity.this, St11SubCategoryActivity.class );
		intent_search.putExtra("ENTITY", mCategoryArray.get(position));
		startActivity( intent_search );
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			if( mSearchEt.length() == 0 ) {
				Toast.makeText(St11MainCategoryActivity.this, R.string.search_keyword_null, Toast.LENGTH_SHORT).show();
				return true;
			}else{ //검색결과 페이지 이동
				Intent intent_search = new Intent(St11MainCategoryActivity.this, St11SearchResultListActivity.class );
				intent_search.putExtra("KEYWORD", mSearchEt.getText().toString());
				startActivity( intent_search );
				
				mInputMethodManager.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	}

}
