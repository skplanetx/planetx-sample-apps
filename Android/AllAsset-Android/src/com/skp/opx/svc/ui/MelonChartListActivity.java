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
import android.widget.RadioGroup;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.EntityMelonChart;
import com.skp.opx.svc.ui.adapter.Adapter_MelonChartAlbum;
import com.skp.opx.svc.ui.adapter.Adapter_MelonChartSong;

/**
 * @설명 : Melon 음악차트 Activity ( 실시간차트 / 주간TOP100 / 앨범TOP20 )
 * @클래스명 : MelonChartListActivity
 * 
 */
public class MelonChartListActivity extends ListActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener{

	private Adapter_MelonChartSong mChartSongAdapter;
	private Adapter_MelonChartAlbum mChartAlbumAdapter;
	private ArrayList<EntityMelonChart> mInfoArray;
	private String mServiceType;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_melon_chart_list);

		initWidgets();
		mServiceType = "song";
		initRealTimeList();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		RadioGroup tabRadioGroup = (RadioGroup)findViewById(R.id.chart_rg);
		tabRadioGroup.setOnCheckedChangeListener(this);

		findViewById(R.id.home_bt).setOnClickListener(this);
		
		mChartSongAdapter = new Adapter_MelonChartSong(this);
		mChartAlbumAdapter = new Adapter_MelonChartAlbum(this);
	}

	/**
	 * @설명 : Melon 실시간 차트
	 * @RequestURI : http://apis.skplanetx.com/melon/charts/realtime
	 */
	private void initRealTimeList() {

		mServiceType = "song";
		
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_CHARTS_REALTIME_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MelonChartListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonChart(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mInfoArray = (ArrayList<EntityMelonChart>)entityArray;
					
					mChartSongAdapter.setMelonChartList(mInfoArray);
					setListAdapter(mChartSongAdapter);
				}
			},null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @설명 : Melon 장르차트(가요) (음악 장르 구분 없이 Top 100 곡 목록을 조회합니다.)
	 * @RequestURI : http://apis.skplanetx.com/melon/charts/topgenres
	 */
	private void initWeeklyList() {
		
		mServiceType = "song";
		
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_CHARTS_TOP_GENRES_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MelonChartListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonChart(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mInfoArray = (ArrayList<EntityMelonChart>)entityArray;
					
					mChartSongAdapter.setMelonChartList(mInfoArray);
					setListAdapter(mChartSongAdapter);
				}
			},null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @설명 : Melon 앨범차트
	 * @RequestURI :http://apis.skplanetx.com/melon/charts/topalbums
	 */
	private void initTopAlbumsList() {

		mServiceType = "album";
		
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_CHARTS_TOP_ALBUMS_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MelonChartListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonChart(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mInfoArray = (ArrayList<EntityMelonChart>)entityArray;
					
					
					mChartAlbumAdapter.setMelonChartList(mInfoArray);
					setListAdapter(mChartAlbumAdapter);
				}
			},null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.home_bt :
			Intent intent_home = new Intent(MelonChartListActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;
		}
	} 

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		String detailURL= "";

		if(mServiceType.equals("song")){
			detailURL = "http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=song&cid=" + mInfoArray.get(position).songId + "&menuId=" + mInfoArray.get(position).menuId;
		}else{
			detailURL = "http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=album&cid=" + mInfoArray.get(position).albumId + "&menuId=" + mInfoArray.get(position).menuId;
		}
		Intent melonInfo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailURL));
		startActivity(melonInfo_intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_melon, menu);
		return result;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.current_music:
			startActivity(new Intent(this, MelonNewSongsListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.current_album:
			startActivity(new Intent(this, MelonNewAlbumListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.music_chart:
			return true;
		case R.id.music_search:
			startActivity(new Intent(this, MelonSearchListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.current_rb: //실시간 차트
			initRealTimeList();
			mChartSongAdapter.notifyDataSetChanged();
			break;
		case R.id.weekly_top_rb: //주간 차트
			initWeeklyList();
			mChartSongAdapter.notifyDataSetChanged();
			break;
		case R.id.album_top_rb: //앨범 차트
			initTopAlbumsList();
			mChartAlbumAdapter.notifyDataSetChanged();
			break;
		}
	} 
}
