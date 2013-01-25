package com.skp.opx.mss.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.mss.entity.EntityMelonDJCategory;
import com.skp.opx.mss.entity.EntityMelonDJMain;
import com.skp.opx.mss.ui.adapter.Adapter_MelonDJCategory;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * @설명 : Melon 카테고리 정보 Activity
 * @클래스명 : MelonDJSubCategoryActivity
 * 
 */
public class MelonDJSubCategoryActivity extends ListActivity implements OnClickListener{

	private EntityMelonDJMain mMainInfo;
	private Adapter_MelonDJCategory mDJCategoryAdapter;
	private ArrayList<EntityMelonDJCategory> mDJCateogoryArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dj_sub_category_list);

		mMainInfo = (EntityMelonDJMain)getIntent().getSerializableExtra("MAINENTITY");

		initWidgets();
		initAdapterList();
	}


	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {
		
		findViewById(R.id.back_bt).setOnClickListener(this);

		mDJCategoryAdapter = new Adapter_MelonDJCategory(this);
	}

	/**
	 * @설명 : Melon DJ 카테고리 정보
	 * @RequestURI : http://apis.skplanetx.com/melon/melondj/categories/{categoryId}
	 * @RequestPathParam :
	 * {categoryId} 카테고리 ID입니다
	 */
	private void initAdapterList() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 1);  //조회할 목록의 페이지를 지정합니다
		map.put("count", 50);  //페이지당 출력되는 곡 수를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_DJ_CATEGORY_URI +  mMainInfo.categoryId , map);

		try {
			//API 호출
			AsyncRequester.request(MelonDJSubCategoryActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonDJCategory(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mDJCateogoryArray = (ArrayList<EntityMelonDJCategory>)entityArray;
					mDJCategoryAdapter.setMelonDJCategoryList(mDJCateogoryArray);
					setListAdapter(mDJCategoryAdapter);
				}
			},"secondCatetory" ,"menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_detail = new Intent(MelonDJSubCategoryActivity.this, MelonDJDetailActivity.class );
		intent_detail.putExtra("CATEGORYENTITY", mDJCateogoryArray.get(position));
		startActivity( intent_detail );
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.back_bt :
			finish();
			break;
		}
	}
}













