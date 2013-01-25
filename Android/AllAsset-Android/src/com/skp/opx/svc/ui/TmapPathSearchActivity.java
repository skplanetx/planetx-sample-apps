package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import com.skp.opx.svc.constants.Defines;
import com.skp.opx.svc.database.DaoSearchedPathBox;
import com.skp.opx.svc.database.EntitySearchdPathBox;
import com.skp.opx.svc.entity.EntityTmapTotalSearch;
import com.skp.opx.svc.ui.adapter.Adapter_TmapPathCurrent;
import com.skp.opx.svc.ui.adapter.Adapter_TmapPathSearch;
import com.skp.opx.svc.ui.dialog.GeneralAlertDialog;
import com.skp.opx.svc.utils.CoordinateUtil;
import com.skp.opx.svc.utils.PreferenceUtil;

/**
 * @설명 : Tmap 경로 검색 및 최근 검색결과 Activity
 * @클래스명 : TmapPathSearchActivity
 * 
 */
public class TmapPathSearchActivity extends ListActivity implements OnClickListener, OnEditorActionListener{

	private InputMethodManager mInputMethodManager;
	private LocationManager mLocationManager;
	private EditText mSearchEt;
	private TextView mSearchResultTv;
	private String mCurrentLon;
	private String mCurrentLat;
	private Adapter_TmapPathSearch mTotalSearchAdapter;
	private ArrayList<EntityTmapTotalSearch> mSearchArray;
	private Adapter_TmapPathCurrent mCurrentAdapter;
	private ArrayList<EntitySearchdPathBox> mCurrentArray;;

	@Override
	protected void onResume() {
		super.onResume();		
		setup();
		initCurrenPathAdpaterList();
	}  
	
	@Override
	protected void onStart() {
	super.onStart();
		
		if(!PreferenceUtil.isGPSDialogShown(this)){
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			if (!gpsEnabled) {
				GeneralAlertDialog dialog = new  GeneralAlertDialog(TmapPathSearchActivity.this, 
						getResources().getString(R.string.enable_gps),
						getResources().getString(R.string.enable_gps_dialog));    			
				dialog.setPostiveButton(new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						PreferenceUtil.setGPSDialogShown(getApplicationContext());
						enableLocationSettings();
					}
				});			
				dialog.show();	
			}
		}	
			
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tmap_path_search);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		initWidgets(); 
		initCurrenPathAdpaterList();
	}
	
	/**
	 * 현재 위치 잡기    
	 */
    private void setup(){    	
    
    	Location gpsLocation = null;
    	Location networkLocation = null;
    	Location passiveLocation = null;
    	
    	//프로바이더 두개를 사용하는 것이 가장 정확도가 높다. gps 가 항상 정확하지 않음 으로     		
		gpsLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER, R.string.not_support_gps, true); 
	    networkLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER, R.string.not_support_network, true);	
	    passiveLocation = requestUpdatesFromProvider(LocationManager.PASSIVE_PROVIDER, R.string.canot_know_location, true);	
	    
 		if (gpsLocation != null && networkLocation != null) {
            updateCooridiate(CoordinateUtil.getBetterLocation(gpsLocation, networkLocation));
        } else if (gpsLocation != null) {
            updateCooridiate(gpsLocation);
        } else if (networkLocation != null) {
        	updateCooridiate(networkLocation);
        }else{
        	updateCooridiate(passiveLocation);
        }
    	
    }
    private Location requestUpdatesFromProvider(final String provider, final int errorResId, boolean showToast) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, 0, 0, listener); //시간 , 거리
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
	private void enableLocationSettings() {

		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
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
    
    /** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);		
		findViewById(R.id.edit_bt).setOnClickListener(this);
		findViewById(R.id.start_to_desn_bt).setOnClickListener(this);
		findViewById(R.id.home_bt).setOnClickListener(this);
		mSearchResultTv = (TextView)findViewById(R.id.sub_header_tv);
		mSearchEt = (EditText)findViewById(R.id.location_search_et);
		mSearchEt.setOnEditorActionListener(this);
		
	}

	/** 
	 * Init Current Path Adapter List (DB에 저장된 최근 검색 경로 List)
	 * */
	private void initCurrenPathAdpaterList() {

		mTotalSearchAdapter = new Adapter_TmapPathSearch(this);
		mSearchResultTv.setText(getString(R.string.current_path_view));

		DaoSearchedPathBox dao = DaoSearchedPathBox.getInstance();
		mCurrentArray = dao.getAllSearchedPathInfoList(getApplicationContext());
		mCurrentAdapter = new Adapter_TmapPathCurrent(getApplicationContext());
		mCurrentAdapter.setTmapPathCurrentList(mCurrentArray);
		setListAdapter(mCurrentAdapter);
		if(mSearchArray != null){ mSearchArray = null; }
		mCurrentAdapter.notifyDataSetChanged();
	}

	/**
	 * @설명 : T map  POI 통합검색
	 * @RequestURI : https://apis.skplanetx.com/tmap/pois
	 */
	private void initSearchPathAdpaterList(final String keyword) {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchKeyword", keyword);   //검색어입니다
		map.put("resCoordType", "WGS84GEO"); //받고자 하는 응답 좌표계 유형을 지정합니다

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.TMAP_TOTAL_SEARCH_URI, map);

		try {
			//API 호출
			AsyncRequester.request(TmapPathSearchActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityTmapTotalSearch(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mSearchArray = (ArrayList<EntityTmapTotalSearch>)entityArray;
					mTotalSearchAdapter.setTotalSearchList(mSearchArray);
					setListAdapter(mTotalSearchAdapter);
					mSearchResultTv.setText(getString(R.string.search_result) + mSearchArray.size() + getString(R.string.cnt));
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent_search = new Intent(TmapPathSearchActivity.this, TmapPathDetailActivity.class );

		String rsdEndXPos = "";
		String rsdEndYPos = "";
		String name= "";

		DaoSearchedPathBox daoSearchedPathfBox = DaoSearchedPathBox.getInstance();	
		EntitySearchdPathBox entity= null;

		DaoSearchedPathBox dao = DaoSearchedPathBox.getInstance();
		mCurrentArray = dao.getAllSearchedPathInfoList(getApplicationContext());

		if(mSearchArray != null){

			entity = new EntitySearchdPathBox();
			entity.mDestination = mSearchArray.get(position).name;
			entity.mDestinatioLon = mSearchArray.get(position).frontLon;
			entity.mDestinationLat = mSearchArray.get(position).frontLat;

			try {
				daoSearchedPathfBox.insertSearchedPathInfo(getApplicationContext(), entity);
			} catch (Exception e) {
				e.printStackTrace();
			}		
			rsdEndXPos = mSearchArray.get(position).frontLon;
			rsdEndYPos = mSearchArray.get(position).frontLat;
			name = entity.mDestination = mSearchArray.get(position).name;
		} else {

			name = mCurrentArray.get(position).mDestination;
			rsdEndXPos = mCurrentArray.get(position).mDestinatioLon;
			rsdEndYPos = mCurrentArray.get(position).mDestinationLat;
		}

		mSearchEt.setText("");
		
		intent_search.putExtra("location", name);
		intent_search.putExtra("rsdEndXPos", rsdEndXPos);
		intent_search.putExtra("rsdEndYPos", rsdEndYPos);
		intent_search.putExtra("rsdStartXPos", mCurrentLon);
		intent_search.putExtra("rsdStartYPos", mCurrentLat);
		startActivity( intent_search );
	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.home_bt :
			Intent intent_home = new Intent(TmapPathSearchActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;
		case R.id.edit_bt :
			mSearchEt.setText("");
			initCurrenPathAdpaterList();
			break;
		case R.id.start_to_desn_bt:
			
			Intent intent_search = new Intent(TmapPathSearchActivity.this, TmapPathDetailActivity.class );			
			
			if(PreferenceUtil.getSharedPreference(getApplicationContext(), "rsdStartXPos") == null){
				Toast.makeText(TmapPathSearchActivity.this, R.string.tmap_not_start_to_desn, Toast.LENGTH_SHORT).show();
				return;
			}
			
//			intent_search.putExtra(Defines.TMAP_SETTING__START_KEYWORD, 
//					PreferenceUtil.getSharedPreference(getApplicationContext(), Defines.TMAP_SETTING__START_KEYWORD));
			intent_search.putExtra("location", 	PreferenceUtil.getSharedPreference(getApplicationContext(), Defines.TMAP_SETTING_DESN_KEYWORD));
			
			intent_search.putExtra("rsdStartXPos",PreferenceUtil.getSharedPreference(getApplicationContext(), "rsdStartXPos"));
			intent_search.putExtra("rsdStartYPos",PreferenceUtil.getSharedPreference(getApplicationContext(), "rsdStartYPos"));
			
			intent_search.putExtra("rsdEndXPos",PreferenceUtil.getSharedPreference(getApplicationContext(), "rsdEndXPos"));
			intent_search.putExtra("rsdEndYPos",PreferenceUtil.getSharedPreference(getApplicationContext(), "rsdEndYPos"));
			
			intent_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_search );
			
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

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			if( mSearchEt.length() == 0 ) {
				Toast.makeText(TmapPathSearchActivity.this, R.string.search_keyword_null, Toast.LENGTH_SHORT).show();
				return true;
			}else{ 
				initSearchPathAdpaterList(mSearchEt.getText().toString());
				mInputMethodManager.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	} 
}
