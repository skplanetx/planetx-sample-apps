package com.skp.opx.rpn.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.database.DaoSearchedPathBox;
import com.skp.opx.rpn.database.EntitySearchdPathBox;
import com.skp.opx.rpn.entity.EntityMovePathDetail;
import com.skp.opx.rpn.ui.adapter.Adapter_MovePathDetail;

/**
 * @설명 : 경로 상세 Activity
 * @클래스명 : MovePathDetailBoxActivity 
 *
 */
public class MovePathDetailBoxActivity extends ListActivity {
	//출발지, 목적지, 거리, 시간, 경로 
	private Adapter_MovePathDetail mMovePathDetailAdapter;
	private Button mMapViewButton;
	private ProgressDialog mProgressDialog;
	private TextView mStartLocation;
	private TextView mDestination;
	private TextView mDistance;
	private TextView mTime;
	private ArrayList<EntityMovePathDetail> mMovePathDetailList = new ArrayList<EntityMovePathDetail>();
	private long mSearchedTime;
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);		
		
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();        
        
		setContentView(R.layout.activity_move_path_detail);
		mStartLocation = (TextView)findViewById(R.id.start_location);
		mDestination = (TextView)findViewById(R.id.dest_location);
		mDistance = (TextView)findViewById(R.id.distance);
		mTime = (TextView)findViewById(R.id.arrived_time);
		getIntentData();
		
	
		  mMapViewButton = (Button)findViewById(R.id.mapView_bt);
		  mMapViewButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		 mHandler.sendEmptyMessage(0); 
		  
	}
	  private Handler mHandler = new Handler()
		{
			public void handleMessage(Message msg) {
			
			//	if(msg.what ==0) {
				DaoSearchedPathBox dao = DaoSearchedPathBox.getInstance(); //경로
				ArrayList<EntitySearchdPathBox> list =dao.getSearchedPathInfoListById(getApplicationContext(), mSearchedTime);
					EntityMovePathDetail entity = null;
					mStartLocation.setText(list.get(0).mStartLocation);
					mDestination.setText(list.get(0).mDestination);
					mDistance.setText(list.get(0).mTotalDistance);
					mTime.setText(list.get(0).mTotalTime);
					
					for (int i = 0; i < list.size(); i++) {
						entity = new EntityMovePathDetail();
						entity.mName = list.get(i).mName;
						entity.mTurnType = list.get(i).mTurnType;
						entity.mDistance = list.get(i).mDistance;
						entity.mTime = list.get(i).mTime;
						entity.mSpeed = list.get(i).mSpeed;
						mMovePathDetailList.add(entity);
					}		
				
				mMovePathDetailAdapter  = new Adapter_MovePathDetail(getApplicationContext());
				mMovePathDetailAdapter.setMovePathDetailList(mMovePathDetailList);
				setListAdapter(mMovePathDetailAdapter);
				mProgressDialog.dismiss();
				super.handleMessage(msg);
			}
		};
	private void getIntentData(){
		Intent intent = getIntent();	
		mSearchedTime = intent.getLongExtra("searchedTime", 0);
		
	}
 

}
