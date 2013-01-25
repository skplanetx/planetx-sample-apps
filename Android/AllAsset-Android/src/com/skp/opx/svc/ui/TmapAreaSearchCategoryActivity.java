package com.skp.opx.svc.ui;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityTmapAreaSearchCategory;
import com.skp.opx.svc.ui.adapter.Adapter_TmapAreaSearch_Category;
import com.skp.opx.svc.ui.dialog.GeneralAlertDialog;
import com.skp.opx.svc.utils.CoordinateUtil;
import com.skp.opx.svc.utils.PreferenceUtil;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @설명 : Tmap 주변 검색 카테고리 Activity
 * @클래스명 : TmapAreaSearchCategoryActivity
 * 
 */
public class TmapAreaSearchCategoryActivity extends ListActivity implements OnClickListener{

	private String mCurrentLon;
	private String mCurrentLat;
	private Adapter_TmapAreaSearch_Category mCategoryAdapter;
	private ArrayList<EntityTmapAreaSearchCategory> mCategoryList = new ArrayList<EntityTmapAreaSearchCategory>();
	private LocationManager mLocationManager;
	
	@Override
	protected void onStart() {
		
		super.onStart();
		
		if(!PreferenceUtil.isGPSDialogShown(this)){
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			if (!gpsEnabled) {
				GeneralAlertDialog dialog = new  GeneralAlertDialog(TmapAreaSearchCategoryActivity.this, 
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
		setup();	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tmap_area_search_list);
	
		initWidgets();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mCategoryList.add(new EntityTmapAreaSearchCategory(getString(R.string.diner), "01", "01"));
		mCategoryList.add(new EntityTmapAreaSearchCategory(getString(R.string.entertainment), "02", "01"));
		mCategoryList.add(new EntityTmapAreaSearchCategory(getString(R.string.hospital), "03", "01"));
		mCategoryList.add(new EntityTmapAreaSearchCategory(getString(R.string.transportation), "04", "01"));
		mCategoryAdapter = new Adapter_TmapAreaSearch_Category(this);
		mCategoryAdapter.setAreaSearchCategoryList(mCategoryList);
	   setListAdapter(mCategoryAdapter);
	   
	}
	
	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.back_bt).setOnClickListener(this);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		EntityTmapAreaSearchCategory entity = mCategoryList.get(position);
	    Intent intent = new Intent(this, TmapAreaSearchListActivity.class);
	    intent.putExtra("name",entity.name);
	    intent.putExtra("classLCode", entity.classLcode);
	    intent.putExtra("classMCode", entity.classMcode);
	    intent.putExtra("noorLon", mCurrentLon);
	    intent.putExtra("noorLat", mCurrentLat);
	    startActivity(intent);
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
			return true;
			
		case R.id.traffic_info : 
			startActivity(new Intent(this, TmapTrafficInfoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
	
}
