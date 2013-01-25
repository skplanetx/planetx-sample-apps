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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.skp.opx.svc.entity.Entity11stSearchResult;
import com.skp.opx.svc.ui.adapter.Adapter_11stSearch;

/**
 * @설명 : 11번가 검색결과 Activity
 * @클래스명 : St11SearchResultListActivity
 * 
 */
public class St11SearchResultListActivity extends ListActivity implements OnClickListener, OnEditorActionListener{

	private InputMethodManager mInputMethodManager;

	private EditText mSearchEt;
	private TextView mSearchResultTv;
	private Spinner mSearchSp;
	
	private String mKeyword;
	private Adapter_11stSearch mSearchResultAdapter;
	private ArrayList<Entity11stSearchResult> mSearchArray;
	private ArrayAdapter<String> mSpinnerAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eleven_st_search_list);

		Bundle rcvBundle = getIntent().getExtras();
		if(rcvBundle != null)
		{
			mKeyword = rcvBundle.getString( "KEYWORD" );
		}
		
		initWidgets();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);		
		findViewById(R.id.edit_bt).setOnClickListener(this);
		findViewById(R.id.back_bt).setOnClickListener(this);

		mSearchEt = (EditText)findViewById(R.id.product_search_et);
		mSearchEt.setOnEditorActionListener(this);
		mSearchEt.setText(mKeyword);
		mSearchEt.setSelection(mKeyword.length());
		mSearchResultTv = (TextView)findViewById(R.id.search_result_tv);

		mSearchSp = (Spinner)findViewById(R.id.search_sp);
		mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				(String[])getResources().getStringArray(R.array.spinner_items_11st_search));
		mSpinnerAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);          
		mSearchSp.setAdapter(mSpinnerAdapter); 
		mSearchSp.setOnItemSelectedListener(mSpinnerSelectedListener);
	}

	/**
	 * @설명 : 11번가  상품검색
	 * @RequestURI : http://apis.skplanetx.com/11st/common/products
	 */
	private void initSearchResultList(final String keyword, String sortCode) {

		mSearchResultAdapter = new Adapter_11stSearch(this);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 1);  //조회할 목록의 페이지를 지정합니다
		map.put("count", 50);  //페이지당 출력되는 상품 수를 지정합니다
		map.put("searchKeyword", keyword);  //검색을 위한 키워드를 입력합니다
		map.put("option", "Products");  //부가 정보를 지정합니다
		map.put("sortCode", sortCode);  //상품의 정렬 방식을 지정합니다.

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.ELEVEN_ST_PRODUCT_SEARHC_URI, map);

		try {
			//API 호출
			AsyncRequester.request(St11SearchResultListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stSearchResult(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mSearchArray = (ArrayList<Entity11stSearchResult>)entityArray;
					mSearchResultAdapter.set11stSearchList(mSearchArray);
					setListAdapter(mSearchResultAdapter);
					if(mSearchArray.size() != 0){
						mSearchResultTv.setText(keyword + getString(R.string.of_search_result) + mSearchArray.size() + getString(R.string.cnt));
					}
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	/** Spinner Selected Listener */
	private OnItemSelectedListener mSpinnerSelectedListener = new OnItemSelectedListener() {

		// OnItemSelectedListener interface to abstract method.
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			initSearchResultList(mKeyword, Define.getSortCode((int) parent.getSelectedItemId()));
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	};

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.back_bt :
			finish();
			break;
		case R.id.edit_bt :
			mSearchEt.setText("");
			break;
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_search = new Intent(St11SearchResultListActivity.this, St11DetailActivity.class );
		intent_search.putExtra("ENTITY", mSearchArray.get(position));
		startActivity( intent_search );
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			if( mSearchEt.length() == 0 ) {
				Toast.makeText(St11SearchResultListActivity.this, R.string.search_keyword_null, Toast.LENGTH_SHORT).show();
				return true;
			}else{ //검색결과 페이지 이동
				initSearchResultList(mSearchEt.getText().toString(),Define.getSortCode(0));
				mInputMethodManager.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	}

}
