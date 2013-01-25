package com.skp.opx.mss.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.mss.entity.EntityMelonDJCategory;
import com.skp.opx.mss.entity.EntityMelonDJDetail;
import com.skp.opx.mss.ui.adapter.Adapter_MelonDJDetail;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @설명 : MelOn DJ 상세정보 Activity
 * @클래스명 : MelonDJDetailActivity
 * 
 */
public class MelonDJDetailActivity extends ListActivity implements OnClickListener{

	private EntityMelonDJCategory mCateogoryInfo;
	private Adapter_MelonDJDetail mDJDetailAdapter;
	ArrayList<EntityMelonDJDetail> mDJDetailArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dj_detail_list);

		mCateogoryInfo = (EntityMelonDJCategory)getIntent().getSerializableExtra("CATEGORYENTITY");

		initWidgets();
		initDJDetailList();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.back_bt).setOnClickListener(this);
		TextView subTitle = (TextView)findViewById(R.id.sub_title_tv);
		subTitle.setText(mCateogoryInfo.offeringTitle);
		
		mDJDetailAdapter = new Adapter_MelonDJDetail(this);
	}

	/**
	 * @설명 : Melon DJ 상세정보
	 * @RequestURI : http://apis.skplanetx.com/melon/melondj/categories/{categoryId}/offerings/{offeringId}
	 * @RequestPathParam : 
	 * {categoryId} 카테고리 ID입니다
	 * {offeringId} 멜론 DJ에서 임의로 부여한 가상 음원 ID입니다
	 */
	private void initDJDetailList() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 1); //조회할 목록의 페이지를 지정합니다
		map.put("count", 50);  //페이지당 출력되는 곡 수를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_DJ_CATEGORY_URI + mCateogoryInfo.categoryId + "/offerings/"  + mCateogoryInfo.offeringId , map);

		try {
			//API 호출
			AsyncRequester.request(MelonDJDetailActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonDJDetail(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mDJDetailArray = (ArrayList<EntityMelonDJDetail>)entityArray;
					mDJDetailAdapter.setMelonDJDetailList(mDJDetailArray);
					setListAdapter(mDJDetailAdapter);
				}
			}, null, "menuId"));
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
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// 상세페이지로 이동 - 해당 position의 arr 정보 넘겨줌
		Intent intent = new Intent(this, MelonDJRecommendDetailActivity.class);
		intent.putExtra("ENTITY", mDJDetailArray.get(position));
		Log.d("TEST", "menuId : " + mDJDetailArray.get(position).menuId);
		startActivity(intent);
	}

}













