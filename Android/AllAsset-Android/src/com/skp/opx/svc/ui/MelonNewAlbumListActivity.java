package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.EntityMelonNewAlbum;
import com.skp.opx.svc.ui.adapter.Adapter_MelonNewAlbum;

/**
 * @설명 : Melon 최신앨범 Activity
 * @클래스명 : MelonNewAlbumListActivity
 * 
 */
public class MelonNewAlbumListActivity extends ListActivity implements OnClickListener{

	private Adapter_MelonNewAlbum mNewAdapter;
	private ArrayList<EntityMelonNewAlbum> mAlbumarray; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_melon_new_list);

		initWidgets();
		initNewAlbumList() ;
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.home_bt).setOnClickListener(this);
		TextView mSubTitle = (TextView)findViewById(R.id.sub_header_tv);
		mSubTitle.setText(getString(R.string.current_album));
	
		mNewAdapter = new Adapter_MelonNewAlbum(this);
	}

	/**
	 * @설명 : Melon 최신앨범
	 * @RequestURI : http://apis.skplanetx.com/melon/newreleases/albums
	 */
	private void initNewAlbumList() {

		//Querystring Parameters		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_NEW_ALBUMS_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MelonNewAlbumListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonNewAlbum(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mAlbumarray = (ArrayList<EntityMelonNewAlbum>)entityArray;
					
					mNewAdapter.setMelonNewList(mAlbumarray);
					setListAdapter(mNewAdapter);
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
			Intent intent_home = new Intent(MelonNewAlbumListActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;

		}
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_melon, menu);
		return result;
	} 

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String detailURL = "http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=album&cid=" + mAlbumarray.get(position).albumId + "&menuId=" +  mAlbumarray.get(position).menuId;
		Intent melonInfo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailURL));
		startActivity(melonInfo_intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.current_music:
			startActivity(new Intent(this, MelonNewSongsListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.current_album:
			return true;
		case R.id.music_chart:
			startActivity(new Intent(this, MelonChartListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.music_search:
			startActivity(new Intent(this, MelonSearchListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 


}
