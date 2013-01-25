package com.skp.opx.rpn.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;
import com.skp.opx.core.client.Define;
import com.skp.opx.rpn.R;
import com.skp.opx.rpn.database.DaoRealTImePathBox;
import com.skp.opx.rpn.database.EntityRealTimePathBox;
import com.skp.opx.rpn.entity.EntityRealTimeMovePathDetail;
import com.skp.opx.rpn.ui.adapter.Adapter_RealTimeMovePathDetail;

/**
 * @설명 : 실시간 이동 경로 상세 Activity
 * @클래스명 : RealTimeMovePathDetailBoxActivity 
 *
 */
public class RealTimeMovePathDetailBoxActivity extends ListActivity {
	//출발지, 목적지, 거리, 시간, 경로 
	private Adapter_RealTimeMovePathDetail mMovePathDetailAdapter;
	private ProgressDialog mProgressDialog;
	private TextView mStartLocation;
	private TextView mDestination;
	private TextView mDistance;
	private TMapView mTmapview; //TMap
	private ArrayList<EntityRealTimeMovePathDetail> mMovePathDetailList = new ArrayList<EntityRealTimeMovePathDetail>();
	private long mSearchedTime;
	//실시간 리스트뷰 업데이트 되도록
	private BroadcastReceiver mReceiver;

	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());       
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);		

		initWidget();

		Intent intent = getIntent();		
		mSearchedTime = intent.getLongExtra("searchedTime", 0);
		findPath(intent.getStringExtra("startLat"), intent.getStringExtra("startLon"), intent.getStringExtra("destinationLat"), intent.getStringExtra("destinationLon"));
		mHandler.sendEmptyMessage(0); 
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mHandler.sendEmptyMessage(0); 
			}
		};
	}

	/**
	 * TMap을 초기화 시킨다.
	 */
	private void makeTMapWidget() {

		LinearLayout linear =(LinearLayout)findViewById(R.id.map_ly);
		mTmapview = new TMapView(this);
		mTmapview.setSKPMapApiKey(Define.OAuth.KEY);
		mTmapview.setLanguage(mTmapview.LANGUAGE_KOREAN);
		mTmapview.setZoomLevel(15);
		mTmapview.setMapType(mTmapview.MAPTYPE_STANDARD);
		mTmapview.setTrackingMode(true);
		mTmapview.setIconVisibility(true);
		linear.addView(mTmapview);
	}

	/**
	 * TMap 경로탐색 및 표시한다.
	 */
	private void findPath(String strRsdStartXPos, String strRsdStartYPos, String strRsdEndXPos, String strRsdEndYPos) {

		TMapData tmapdata = new TMapData();
		mTmapview.setLocationPoint(Double.parseDouble(strRsdStartYPos), Double.parseDouble(strRsdStartXPos));
		mTmapview.setCenterPoint(Double.parseDouble(strRsdStartYPos), Double.parseDouble(strRsdStartXPos));
		TMapPoint startpoint = new TMapPoint(Double.parseDouble(strRsdStartXPos), Double.parseDouble(strRsdStartYPos));
		TMapPoint endpoint = new TMapPoint(Double.parseDouble(strRsdEndXPos), Double.parseDouble(strRsdEndYPos));
		
		try {
			TMapPolyLine polyline = tmapdata.findPathData(startpoint, endpoint);
			mTmapview.addTMapPolyLine("TestID", polyline);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initWidget() {

		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage(getString(R.string.loading));
		mProgressDialog.show();        

		setContentView(R.layout.activity_move_path_detail);
		mStartLocation = (TextView)findViewById(R.id.start_location);
		mDestination = (TextView)findViewById(R.id.dest_location);
		mDistance = (TextView)findViewById(R.id.distance);

		RadioGroup rg = (RadioGroup)findViewById(R.id.appear_type_rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch(checkedId) {
				case R.id.map_rb :
					findViewById(R.id.context_ly).setVisibility(View.GONE);
					findViewById(R.id.map_ly).setVisibility(View.VISIBLE);
					break;

				case R.id.context_rb :
					findViewById(R.id.context_ly).setVisibility(View.VISIBLE);
					findViewById(R.id.map_ly).setVisibility(View.GONE);
					break;
				}
			}
		});

		makeTMapWidget();
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerEvent();
	}
	@Override
	protected void onStop() {
		super.onStop();
		if ( null != mReceiver ) {
			unregisterReceiver(mReceiver);
		}
	}

	/** 
	 * Receiver 등록 Method  
	 * */
	private void registerEvent() {

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.skp.opx.rpn.update");

		if ( null != mReceiver ) {
			registerReceiver(mReceiver, intentFilter);
		}

	}
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			DaoRealTImePathBox dao = DaoRealTImePathBox.getInstance(); //경로
			ArrayList<EntityRealTimePathBox> list =dao.getRealTimePathInfoListById(getApplicationContext(), mSearchedTime);
			EntityRealTimeMovePathDetail entity = null;
			mStartLocation.setText(list.get(0).mStartLocation);
			mDestination.setText(list.get(0).mDestination);
			mDistance.setText(list.get(0).mTotalDistance);

			if(mMovePathDetailList.size() > 0){
				mMovePathDetailList.clear();
			}

			for (int i = 0; i < list.size(); i++) {
				entity = new EntityRealTimeMovePathDetail();
				entity.mName = list.get(i).mName;
				entity.mTime = list.get(i).mTime;
				entity.mCurrentLat = list.get(i).mCurrentLat;
				entity.mCurrentLon = list.get(i).mCurrentLon;
				
				if(entity != null && entity.mCurrentLat != null && entity.mCurrentLon != null) {
					TMapPoint tpoint = new TMapPoint(Double.parseDouble(entity.mCurrentLat), Double.parseDouble(entity.mCurrentLon));
					TMapMarkerItem tItem = new TMapMarkerItem();
					tItem.setName("" + list.get(i).mTime);
					tItem.setTMapPoint(tpoint);
					mTmapview.addMarkerItem("" + i, tItem);
				}
				
				mMovePathDetailList.add(entity);
			}		

			mMovePathDetailAdapter  = new Adapter_RealTimeMovePathDetail(getApplicationContext());
			mMovePathDetailAdapter.setMovePathDetailList(mMovePathDetailList);
			setListAdapter(mMovePathDetailAdapter);
			mProgressDialog.dismiss();
			super.handleMessage(msg);
		}
	};
}
