package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OAuthenticate;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sdk.PopupDialogUtil;
import com.skp.opx.svc.constants.Defines;
import com.skp.opx.svc.entity.Entity11stSearchResult;
import com.skp.opx.svc.entity.EntityCyVisitBook;
import com.skp.opx.svc.entity.EntityMainMenu;
import com.skp.opx.svc.entity.EntityMelonChart;
import com.skp.opx.svc.entity.EntityNateOnMsgQuery;
import com.skp.opx.svc.ui.adapter.Adapter_MainMenu;
import com.skp.opx.svc.utils.CoordinateUtil;
import com.skp.opx.svc.utils.PreferenceUtil;

/**
 * @설명 : 서비스 모아보기 Main Activity
 * @클래스명 : MainActivity
 * 
 */
public class MainActivity extends ListActivity implements OnClickListener{

	private LinearLayout  mNateLy, mCyLy, m11stLy, mMelonLy, mTmapLy;
	private TextView      mNateTv, mNateCntTv, mCyTv, mCyCntTv, m11stTv, mMelonTv, mTmapTv, mMenuSetDesTv;

	private String strSong, strRank, strArtist, strProductName;
	private String m11stkeyword;
	private String mTmapLocation;
	private String mTmapDistance;
	private String mTmapTime;
	private String mCyMsgCount;

	private LocationManager mLocationManager;
	private String mCurrentLon;
	private String mCurrentLat;

	@Override
	protected void onResume() {
		super.onResume();

		initPlanetLiveView();
		initMainView();
		setup(); //현재 위치 설정
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build()); 
		OAuthenticate.publicAuthenticate(this, Define.OAuth.KEY);
		OAuthenticate.privateAuthenticate(MainActivity.this, "user/profile, nateon, cyworld", Define.OAuth.KEY, Define.OAuth.CLIENT_ID, Define.OAuth.SECRET);

		initWidgets();
		initPlanetLiveView();
		initMainView();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mNateLy       = (LinearLayout)findViewById(R.id.nateon_menu);
		mNateTv       = (TextView)findViewById(R.id.nateon_tv);
		mNateCntTv    = (TextView)findViewById(R.id.nateon_cnt_tv);
		mCyLy         = (LinearLayout)findViewById(R.id.cyworld_menu);
		mCyTv         = (TextView)findViewById(R.id.cyworld_tv);
		mCyCntTv      = (TextView)findViewById(R.id.cyworld_cnt_tv);
		m11stLy       = (LinearLayout)findViewById(R.id.eleven_menu);
		m11stTv       = (TextView)findViewById(R.id.eleven_st_tv);
		mMelonLy      = (LinearLayout)findViewById(R.id.melon_menu);
		mMelonTv      = (TextView)findViewById(R.id.melon_tv);
		mTmapLy       = (LinearLayout)findViewById(R.id.tmap_menu);
		mTmapTv       = (TextView)findViewById(R.id.tmap_tv);
		mMenuSetDesTv = (TextView)findViewById(R.id.menu_setting_des);
		//marquee 적용
		mNateTv.setSelected(true);
		mCyTv.setSelected(true);
		m11stTv.setSelected(true);
		mMelonTv.setSelected(true); 
		mTmapTv.setSelected(true);

		findViewById(R.id.nateon_tv).setOnClickListener(this);
		findViewById(R.id.cyworld_tv).setOnClickListener(this);
		findViewById(R.id.eleven_st_tv).setOnClickListener(this);
		findViewById(R.id.melon_tv).setOnClickListener(this);
		findViewById(R.id.tmap_tv).setOnClickListener(this);
	}

	/**
	 * My 플래닛 Live 노출  set
	 */
	private void initPlanetLiveView(){

		initMelonMain(); 
		mTmapLocation = PreferenceUtil.getSharedPreference(getApplicationContext(), Defines.TMAP_SETTING_DESN_KEYWORD);
		mTmapDistance = PreferenceUtil.getSharedPreference(getApplicationContext(), "mDistanceTv");
		mTmapTime = PreferenceUtil.getSharedPreference(getApplicationContext(), "mTimeTv");
		m11stkeyword = PreferenceUtil.getSharedPreference(getApplicationContext(), Defines.ST11_SETTING_KEYWORD);
		if(mTmapLocation != null){
			mTmapTv.setText(getString(R.string.current_location) + " - " + mTmapLocation+ " - " + mTmapDistance+ " - " + mTmapTime);
		}

		PreferenceManager.setDefaultValues(this, R.xml.menu_preferences, false);  //Preference Setting

		SharedPreferences pref      = PreferenceManager.getDefaultSharedPreferences(this); //PreferenceActivity Setting 
		boolean isNateLiveVisible   =  pref.getBoolean(getString(R.string.nate_live_key), false);
		boolean isCyLiveVisible     =  pref.getBoolean(getString(R.string.cy_live_key), false);
		boolean isMelonLiveVisible  =  pref.getBoolean(getString(R.string.melon_live_key), false);
		boolean isElevenLiveVisible =  pref.getBoolean(getString(R.string.eleven_live_key), false);
		boolean isTmapLiveVisible   =  pref.getBoolean(getString(R.string.tmap_live_key), false);
		
		mCyMsgCount = pref.getString(getString(R.string.cyworld_settings), "화면에 표시될 방명록 수 : 5").substring(16);

		if(isNateLiveVisible){ 
			mNateLy.setVisibility(View.VISIBLE);
		}else{
			mNateLy.setVisibility(View.GONE); 
		}

		if(isCyLiveVisible){
			mCyLy.setVisibility(View.VISIBLE); 
		}else{
			mCyLy.setVisibility(View.GONE); 
		}

		if(isElevenLiveVisible){ 
			m11stLy.setVisibility(View.VISIBLE); 
		}else{
			m11stLy.setVisibility(View.GONE); 
		}

		if(isMelonLiveVisible){ 
			mMelonLy.setVisibility(View.VISIBLE); 
		}else{
			mMelonLy.setVisibility(View.GONE); 
		}

		if(isTmapLiveVisible){ 
			mTmapLy.setVisibility(View.VISIBLE); 
		}else{
			mTmapLy.setVisibility(View.GONE); 
		}

	}

	/**
	 * Main 노출  set
	 */
	private void initMainView() {

		Adapter_MainMenu mainAdpater = new Adapter_MainMenu(this);

		ArrayList<EntityMainMenu> menuList = new ArrayList<EntityMainMenu>();

		SharedPreferences pref      = PreferenceManager.getDefaultSharedPreferences(this); //PreferenceActivity Setting 
		boolean isNateMainVisible   =  pref.getBoolean(getString(R.string.nate_main_key), false);
		boolean isCyMainVisible     =  pref.getBoolean(getString(R.string.cy_main_key), false);
		boolean isMelonMainVisible  =  pref.getBoolean(getString(R.string.melon_main_key), false);
		boolean isElevenMainVisible =  pref.getBoolean(getString(R.string.eleven_main_key), false);
		boolean isTmapMainVisible   =  pref.getBoolean(getString(R.string.tmap_main_key), false);

		if(isNateMainVisible){menuList.add(new EntityMainMenu(getString(R.string.nate_on)));}
		if(isCyMainVisible){menuList.add(new EntityMainMenu(getString(R.string.cyworld)));}
		if(isMelonMainVisible){menuList.add(new EntityMainMenu(getString(R.string.melon)));}
		if(isElevenMainVisible){menuList.add(new EntityMainMenu(getString(R.string.eleven_st)));}
		if(isTmapMainVisible){menuList.add(new EntityMainMenu(getString(R.string.tmap)));}

		mainAdpater.setMainlist(menuList);
		setListAdapter(mainAdpater);
	}

	/**
	 * @설명 : Melon 실시간 차트 
	 * @RequestURI : http://apis.skplanetx.com/melon/charts/realtime
	 */
	private void initMelonMain() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 1);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		
		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_CHARTS_REALTIME_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MainActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonChart(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {

					ArrayList<EntityMelonChart> melonArray = (ArrayList<EntityMelonChart>)entityArray;

					StringBuilder sb = new StringBuilder();

					for(int i = 0; i < melonArray.size() ; i++)
					{
						strRank = Integer.toString(melonArray.get(i).currentRank) + "위  ";
						strSong = melonArray.get(i).songName + "-";
						strArtist = melonArray.get(i).artistName + "   ";
						sb.append(strRank).append(strSong).append(strArtist);
					}
					mMelonTv.setText(sb.toString());
					init11stMain(m11stkeyword);

				}
			},null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @설명 : 네이트온 읽지 않은 쪽지 수
	 * @RequestURI : https://apis.skplanetx.com/nateon/notes/unread/counts
	 */
	private void initNateOnMain() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();

		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.NATEON_SEND_NOTE_UNREAD_CONUTS, map);

		try {

			AsyncRequester.request(MainActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityNateOnMsgQuery(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					
					ArrayList<EntityNateOnMsgQuery> array = new ArrayList<EntityNateOnMsgQuery>();

				}

			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			PopupDialogUtil.showConfirmDialog(MainActivity.this, R.string.nate_on, R.string.send_failed, null);
		}
	}
	
	/**
	 * @설명 : 싸이월드 최근 방명록 조회
	 * @RequestURI : https://apis.skplanetx.com/cyworld/minihome
	 */
	private void initCyworldMain() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("count", mCyMsgCount);
		
		Calendar calender = Calendar.getInstance();
		String year = String.valueOf(calender.get(calender.YEAR));
		
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.CYWORLD_ME_VISIT_URI + year +"/items", map);

		try {

			AsyncRequester.request(MainActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityCyVisitBook(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					
					ArrayList<EntityCyVisitBook> array = (ArrayList<EntityCyVisitBook>)entityArray;
					StringBuffer sb = new StringBuffer();
					String date;
					for (int i = 0; i < array.size(); i++) {
						sb.append("[   ");//방명록 구분
						sb.append(array.get(i).content.toString());//방명록 내용
						sb.append("  -  ");//글과 시간 구분
						date = array.get(i).writeDate.substring(0, array.get(i).writeDate.indexOf("T"));
						sb.append(date);//방명록 쓴 시간
						sb.append("   ]");//방명록 구분 마침
						sb.append("\t\t\t");
					}
					
					mCyTv.setText(sb);
					initNateOnMain();
				}

			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			PopupDialogUtil.showConfirmDialog(MainActivity.this, R.string.nate_on, R.string.send_failed, null);
		}
	}
	
	/**
	 * @설명 : 11번가 상품검색
	 * @RequestURI : http://apis.skplanetx.com/11st/common/products
	 */
	private void init11stMain(String keyword) {

		if(keyword != null && !"".equals(keyword) ){
			//Querystring Parameters
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", 1);   //조회할 목록의 페이지를 지정합니다
			map.put("count", 50); //페이지당 출력되는 상품 수를 지정합니다
			map.put("searchKeyword", keyword); //검색을 위한 키워드를 입력합니다.
			map.put("option", "Products"); //부가 정보를 지정합니다
			map.put("sortCode", Define.getSortCode(0)); //상품의 정렬 방식을 지정합니다.

			//Bundle 생성
			RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.ELEVEN_ST_PRODUCT_SEARHC_URI, map);

			try {
				//API 호출
				AsyncRequester.request(MainActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stSearchResult(), new OnEntityParseComplete() {

					@Override
					public void onParsingComplete(Object entityArray) {

						ArrayList<Entity11stSearchResult> productArray = (ArrayList<Entity11stSearchResult>)entityArray;

						StringBuilder sb = new StringBuilder();

						for(int i = 0; i < productArray.size() ; i++)
						{
							strProductName =  productArray.get(i).ProductName;
							sb.append(strProductName);
						}
						m11stTv.setText(sb.toString());
						initCyworldMain();
					}
				}));
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}	
		}else{
			initCyworldMain();
		}

	}
	
	private void setup(){    	

		Location gpsLocation = null;
		Location networkLocation = null;
		Location passiveLocation = null;

		gpsLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER, R.string.not_support_gps, false); 
		networkLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER, R.string.not_support_network, true);	
		passiveLocation = requestUpdatesFromProvider(LocationManager.PASSIVE_PROVIDER, R.string.canot_know_location, true);	

		if (gpsLocation != null && networkLocation != null) {
			updateCooridiate(CoordinateUtil.getBetterLocation(gpsLocation, networkLocation));
		} else if (gpsLocation != null) {
			updateCooridiate(gpsLocation);
		} else if (networkLocation != null) {
			updateCooridiate(networkLocation);
		}else if(passiveLocation != null) {
			updateCooridiate(passiveLocation);
		}

	}
	
	private Location requestUpdatesFromProvider(final String provider, final int errorResId, boolean showToast) {
		Location location = null;
		if (mLocationManager.isProviderEnabled(provider)) {
			mLocationManager.requestLocationUpdates(provider, 0, 0, listener); 
			location = mLocationManager.getLastKnownLocation(provider);
			if(location !=null){
				updateCooridiate(location);
			}
		} else {
			if(showToast)
				Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
		}
		return location;
	}

	private void updateCooridiate(Location location){  
		mCurrentLat = String.valueOf(location.getLatitude());
		mCurrentLon = String.valueOf(location.getLongitude());	
	}
	
	private final LocationListener listener = new LocationListener() {

		public void onLocationChanged(Location location) {
			updateCooridiate(location);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.nateon_tv :
			Intent intent_nateon = new Intent(getApplicationContext(), NateOnBuddiesListActivity.class);
			startActivity(intent_nateon);
			break;
		case R.id.cyworld_tv :
			Intent intent_cyworld = new Intent(getApplicationContext(), CyworldFriendListActivity.class);
			startActivity(intent_cyworld);
			break;
		case R.id.eleven_st_tv :
			if(m11stkeyword != null){
				Intent intent_11st = new Intent(getApplicationContext(), St11SearchResultListActivity.class);
				intent_11st.putExtra("KEYWORD", m11stkeyword);
				startActivity(intent_11st);
			}else{
				Intent intent_11st = new Intent(getApplicationContext(), MainMenuSettingActivity.class);
				startActivity(intent_11st);
			}
			break;
		case R.id.melon_tv :
			Intent intent_melon = new Intent(getApplicationContext(), MelonChartListActivity.class);
			startActivity(intent_melon);
			break;
		case R.id.tmap_tv :
			if(mTmapLocation != null){
				if(mCurrentLon == null || mCurrentLat == null) {
					Toast.makeText(this, R.string.canot_know_location, Toast.LENGTH_SHORT).show();
					return;
				}
				
				Intent intent_tmap = new Intent(getApplicationContext(), TmapPathDetailActivity.class);
				intent_tmap.putExtra("location", mTmapLocation);
				intent_tmap.putExtra("rsdEndXPos",PreferenceUtil.getSharedPreference(getApplicationContext(), "rsdEndXPos"));
				intent_tmap.putExtra("rsdEndYPos", PreferenceUtil.getSharedPreference(getApplicationContext(), "rsdEndYPos"));
				intent_tmap.putExtra("rsdStartXPos", mCurrentLon);
				intent_tmap.putExtra("rsdStartYPos", mCurrentLat);
				startActivity(intent_tmap);
			}else{
				Intent intent_tmap = new Intent(getApplicationContext(), MainMenuSettingActivity.class);
				startActivity(intent_tmap);
			}
			break;
		}
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.home:
			return true;
		case R.id.menu_setting:
			startActivity(new Intent(this, MainMenuSettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 

}
