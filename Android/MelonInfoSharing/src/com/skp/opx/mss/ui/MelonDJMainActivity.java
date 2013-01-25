package com.skp.opx.mss.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.mss.entity.EntityMelonDJMain;
import com.skp.opx.mss.ui.adapter.Adapter_MelonDJMain;
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
 * @설명 : Melon DJ 메인정보  Activity
 * @클래스명 : MelonDJMainActivity
 * 
 */
public class MelonDJMainActivity extends ListActivity implements OnClickListener{

	private Adapter_MelonDJMain mDJMainAdapter;
	private ArrayList<EntityMelonDJMain> mDJMainArray;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dj_main_list);
		
		initWidgets();
		initAdapterList();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {
		
		findViewById(R.id.home_bt).setOnClickListener(this);
		
		mDJMainAdapter = new Adapter_MelonDJMain(this);
	}

	/**
	 * @설명 : 멜론 DJ 메인 정보를 조회합니다.
	 * @RequestURI : http://apis.skplanetx.com/melon/melondj
	 */
	private void initAdapterList() {
		
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_DJ_MAIN_URI , map);

		try {
			//API 호출
			AsyncRequester.request(MelonDJMainActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonDJMain(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mDJMainArray = (ArrayList<EntityMelonDJMain>)entityArray;
					mDJMainAdapter.setMelonDJMainList(mDJMainArray);
					setListAdapter(mDJMainAdapter);
				}
			}, null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.home_bt :
			Intent intent_home = new Intent(MelonDJMainActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;

		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_cateogory = new Intent(MelonDJMainActivity.this, MelonDJSubCategoryActivity.class );
		intent_cateogory.putExtra("MAINENTITY", mDJMainArray.get(position));
		startActivity( intent_cateogory );
	}
}

