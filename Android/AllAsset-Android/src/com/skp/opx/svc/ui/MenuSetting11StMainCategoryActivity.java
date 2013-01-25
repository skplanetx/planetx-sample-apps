package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;

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
 * @설명 : 11번가 main category 설정 Activity
 * @클래스명 : MenuSetting11StMainCategoryActivity
 * 
 */
public class MenuSetting11StMainCategoryActivity extends ListActivity implements OnClickListener{

	private Adapter_11stMainCategory mCategoryAdapter;
	private ArrayList<Entity11stCategory> mCategoryArray; 

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_setting_eleven_st_main_category_list);

		initWidgets();
		initCategoryList();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.home_bt).setOnClickListener(this);
		
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
			AsyncRequester.request(MenuSetting11StMainCategoryActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stCategory(), new OnEntityParseComplete() {

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
			Intent intent_home = new Intent(MenuSetting11StMainCategoryActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;
		}
	} 

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_search = new Intent(MenuSetting11StMainCategoryActivity.this, MenuSetting11StSubCategotyListActivity.class );
		intent_search.putExtra("ENTITY", mCategoryArray.get(position));
		startActivity( intent_search );
	}

}
