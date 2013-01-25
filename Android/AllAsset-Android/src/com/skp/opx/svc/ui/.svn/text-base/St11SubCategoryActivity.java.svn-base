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
import com.skp.opx.svc.entity.Entity11stCategorySub;
import com.skp.opx.svc.ui.adapter.Adapter_11stSubCategory;

/**
 * @설명 : 11번가 서브 카테고리 Activity
 * @클래스명 : St11SubCategoryActivity
 * 
 */
public class St11SubCategoryActivity extends ListActivity implements OnClickListener, OnEditorActionListener{

	private InputMethodManager mInputMethodManager;
	private EditText mSearchEt;

	private Adapter_11stSubCategory mCategoryAdapter;
	private ArrayList<Entity11stCategorySub> mSubCategoryArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eleven_st_sub_category);

		Entity11stCategory categoryEntity = (Entity11stCategory)getIntent().getSerializableExtra("ENTITY");

		initWidgets();
		initAdapterList(categoryEntity.CategoryCode);
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		mSearchEt = (EditText)findViewById(R.id.product_search_et);
		mSearchEt.setOnEditorActionListener(this);
		findViewById(R.id.back_bt).setOnClickListener(this);
		findViewById(R.id.edit_bt).setOnClickListener(this);
		
		mCategoryAdapter = new Adapter_11stSubCategory(this);
	}

	/**
	 * @설명 : 11번가  카테고리 조회
	 * @RequestURI : http://apis.skplanetx.com/11st/common/categories/{categoryCode}
	 * @RequestPathParam :
	 * {categoryCode} 카테고리 코드 정보입니다
	 */
	private void initAdapterList(int categoryCode) {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("option", "Children");  //부가 정보를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.ELEVEN_ST_CATEGORY_URI + "/" + String.valueOf(categoryCode) , map);

		try {
			//API 호출
			AsyncRequester.request(St11SubCategoryActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stCategorySub(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mSubCategoryArray = (ArrayList<Entity11stCategorySub>)entityArray;
					mCategoryAdapter.set11stSubCategoryList(mSubCategoryArray);
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

		Intent intent_search = new Intent(St11SubCategoryActivity.this, St11SearchResultListActivity.class );
		intent_search.putExtra("KEYWORD", mSubCategoryArray.get(position).CategoryName);
		startActivity( intent_search );
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			if( mSearchEt.length() == 0 ) {
				Toast.makeText(St11SubCategoryActivity.this, R.string.search_keyword_null, Toast.LENGTH_SHORT).show();
				return true;
			}else{ //검색결과 페이지 이동
				Intent intent_search = new Intent(St11SubCategoryActivity.this, St11SearchResultListActivity.class );
				intent_search.putExtra("KEYWORD", mSearchEt.getText().toString());
				startActivity( intent_search );

				mInputMethodManager.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	}

}
