package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.EntityCyBesties;
import com.skp.opx.svc.entity.EntityCyVisitBook;
import com.skp.opx.svc.ui.adapter.Adapter_CyworldVisitBook;

/**
 * @설명 : 싸이월드 일촌 방명록 목록 보기 Activity
 * @클래스명 : CyworldVisitbookListActivity
 * 
 */
public class CyworldVisitbookListActivity extends ListActivity implements OnClickListener{

	private Adapter_CyworldVisitBook mVisitAdapter;
	private ArrayList<EntityCyVisitBook> mCyVisitArray;
	private EntityCyBesties mBestiesInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cyworld_visitbook_list);

		mBestiesInfo  = (EntityCyBesties)getIntent().getSerializableExtra("ENTITY");
		
		initWidgets();
		initAdapterList();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.back_bt).setOnClickListener(this);
		
		mVisitAdapter = new Adapter_CyworldVisitBook(this);
	}

	/**
	 * @설명 : Cyworld 방명록  게시물 목록보기
	 * @RequestURI : https://apis.skplanetx.com/cyworld/minihome/{cyId}/visitbook/{year}/items
	 * @RequestPathParam :
	 * {cyId} 조회할 대상의 싸이월드 ID입니다
	 * {year} 대상 방명록의 연도입니다
	 */
	private void initAdapterList() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.CYWORLD_MINIHOME_URI  + mBestiesInfo.cyId + "/visitbook/2012/items", map);

		try {
			//API 호출
			AsyncRequester.request(CyworldVisitbookListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityCyVisitBook(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mCyVisitArray = (ArrayList<EntityCyVisitBook>)entityArray;
					
					mVisitAdapter.setCyworldVisitList(mCyVisitArray);
					setListAdapter(mVisitAdapter);
				}
			}));
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
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_cyworld, menu);
		return result;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.photo_album:
			startActivity(new Intent(this, CyworldPhotoListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.guest_book:
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
}
