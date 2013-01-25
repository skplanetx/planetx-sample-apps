package com.skp.opx.svc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.skp.Tmap.TMapView;
import com.skp.opx.core.client.Define;
import com.skp.opx.svc.R;

/**
 * @설명 : Tmap 교통정보 Activity
 * @클래스명 : TmapTrafficInfoActivity
 * 
 */
public class TmapTrafficInfoActivity extends Activity implements OnClickListener {

	private TMapView mTmapview; //TMap

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());       
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tmap_traffic_info);
	
		initWidgets();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.back_bt).setOnClickListener(this);
        makeTMapWidget();
	}

    /**
     * TMap을 생성한다.
     */
    private void makeTMapWidget() {
    	
    	LinearLayout linear =(LinearLayout)findViewById(R.id.map_ly);
		mTmapview = new TMapView(this);
		linear.addView(mTmapview);
    }
	
    /**
     * TMap을 초기화 시킨다.
     */
    private void tmapSet() {
    	
		mTmapview.setSKPMapApiKey(Define.OAuth.KEY);
		mTmapview.setLanguage(mTmapview.LANGUAGE_KOREAN);
		mTmapview.setLocationPoint(127.55963904390588, 36.411576147592186);
		mTmapview.setCenterPoint(127.5963904390588, 36.411576147592186);
		mTmapview.setZoomLevel(7);
		mTmapview.setMapType(mTmapview.MAPTYPE_TRAFFIC);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		
		tmapSet();
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
