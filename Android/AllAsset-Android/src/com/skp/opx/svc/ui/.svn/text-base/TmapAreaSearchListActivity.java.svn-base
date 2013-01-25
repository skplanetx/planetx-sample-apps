package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.EntityTmapAreaSearch;
import com.skp.opx.svc.ui.adapter.Adapter_TmapAreaSearch_Result;

/**
 * @설명 : Tmap 주변검색 결과 Activity
 * @클래스명 : TmapAreaSearchListActivity
 * 
 */
public class TmapAreaSearchListActivity extends ListActivity implements OnClickListener {

	private String classLCode= "";
	private String classMCode= "";
	private String noorLon= "";
	private String noorLat= "";
	private String name;
	private ArrayList<EntityTmapAreaSearch> mSearchedList = new ArrayList<EntityTmapAreaSearch>();
	private Adapter_TmapAreaSearch_Result mAreaSearchAdapter= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tmap_area_search_list);

		getIntentData();	
		initWidgets();
		mHandler.sendEmptyMessage(0);
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.back_bt).setOnClickListener(this);
		TextView nameText = (TextView)findViewById(R.id.name);
		nameText.setText(name);
	}

	private void getIntentData(){

		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		classLCode =intent.getStringExtra("classLCode");
		classMCode = intent.getStringExtra("classMCode");
		noorLon = intent.getStringExtra("noorLon");
		noorLat =intent.getStringExtra("noorLat");
	}

	/**
	 * @설명 : T map POI 주변 검색
	 * @RequestURI : https://apis.skplanetx.com/tmap/pois/around
	 */
	private void getAreaInfo(){

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classLCode", classLCode);  //업종 대분류 코드입니다
		map.put("classMCode", classMCode);  //업종 중분류 코드입니다
		map.put("noorLat", noorLat);  //위도 좌표값입니다
		map.put("noorLon", noorLon);  //경도 좌표값입니다
		map.put("searchType", "category");	//검색 종류입니다
		map.put("reqCoordType", "WGS84GEO"); //요청 좌표계 유형을 지정합니다
		map.put("count", "30"); //요청 시 받을 검색 리스트 개수입니다 
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.TMAP_AREA_SEARCH_URI, map);

		try{
			//API 호출
			AsyncRequester.request(TmapAreaSearchListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityTmapAreaSearch(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					ArrayList<EntityTmapAreaSearch> array = (ArrayList<EntityTmapAreaSearch>)entityArray;
					EntityTmapAreaSearch entity = null;
					for (int i = 0; i < array.size(); i++) {
						entity = new EntityTmapAreaSearch();
						entity.name = array.get(i).name;
						entity.desc = array.get(i).desc;
						entity.telNo= array.get(i).telNo;
						entity.upperAddrName = array.get(i).upperAddrName;
						entity.middleAddrName = array.get(i).middleAddrName;
						entity.lowerAddrName = array.get(i).lowerAddrName;
						entity.firstNo = array.get(i).firstNo;
						entity.secondNo = array.get(i).secondNo;
						mSearchedList.add(entity);
					}
					mAreaSearchAdapter = new Adapter_TmapAreaSearch_Result(getApplicationContext());
					mAreaSearchAdapter.setAreaSearchList(mSearchedList);
					setListAdapter(mAreaSearchAdapter);   
				}

			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();

		}

	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {

			getAreaInfo();
			super.handleMessage(msg);
		}
	};	
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.back_bt :
			finish();
			break;
		}
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_tmap, menu);
		return result;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.path_search:
			startActivity(new Intent(this, TmapPathSearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.area_search:
			startActivity(new Intent(this, TmapAreaSearchCategoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
			
		case R.id.traffic_info : 
			startActivity(new Intent(this, TmapTrafficInfoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
}
