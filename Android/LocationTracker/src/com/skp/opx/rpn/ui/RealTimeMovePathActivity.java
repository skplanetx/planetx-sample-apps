package com.skp.opx.rpn.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.database.DaoRealTImePathBox;
import com.skp.opx.rpn.database.EntityRealTimePathBox;
import com.skp.opx.rpn.entity.EntityMovePath;
import com.skp.opx.rpn.ui.adapter.Adapter_MovePath_Box;
import com.skp.opx.rpn.ui.dialog.GeneralAlertDialog;

/**
 * @설명 : 실시간 이동 경로 Activity
 * @클래스명 : RealTimeMovePathActivity 
 *
 */
public class RealTimeMovePathActivity extends ListActivity {

	private Adapter_MovePath_Box mMovePathAdapter;
	private ArrayList<EntityMovePath> mMovePathList = new ArrayList<EntityMovePath>();
	private ArrayList<Integer> mCheckedMovePathList =  new ArrayList<Integer>(); // 체크 저장 ID 리스트
	private Button mConfirmButton;	
    private ProgressDialog mProgressDialog= null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading));
		mHandler.sendEmptyMessage(0); // 경로 요청
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_move_path);
		mConfirmButton = (Button)findViewById(R.id.confirm_button);
		mConfirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//삭제 로직				 
				GeneralAlertDialog dialog = new  GeneralAlertDialog(RealTimeMovePathActivity.this, getResources().getString(R.string.delete_selected_favorite_items));    			
		    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//삭제하기					
						DaoRealTImePathBox dao =DaoRealTImePathBox.getInstance();
						for (int i = 0; i < mCheckedMovePathList.size(); i++) {
							dao.deleteDataByStartTime(getApplicationContext(), mMovePathList.get(mCheckedMovePathList.get(i)).searchedTime);
						}
						mMovePathAdapter.setCheckMode();
						mConfirmButton.setVisibility(View.GONE);
						mHandler.sendEmptyMessage(0);
					}
				});
				dialog.setNegativeButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
		    	dialog.show();			
						
			}
		});
		mMovePathAdapter = new Adapter_MovePath_Box(this);	
	}	
    private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {		
			
			DaoRealTImePathBox dao =DaoRealTImePathBox.getInstance();
			ArrayList<EntityRealTimePathBox> list = dao.getAllRealTimePathInfoList(getApplicationContext());
			EntityMovePath entity = null;
			if(mMovePathList.size() > 0){
				mMovePathList.clear();
			}
			for (int i = 0; i <list.size(); i++) {
				entity = new EntityMovePath();
				entity.id = list.get(i).getID();
				entity.path = list.get(i).mStartLocation + " > " + list.get(i).mDestination;
	    		entity.startLat  = list.get(i).mStartLat;
	    		entity.startLon = list.get(i).mStartLon;
	    		entity.destinationLat = list.get(i).mDestinationLat;
	    		entity.destinationLon = list.get(i).mDestinatioLon;
				entity.date= new SimpleDateFormat("yy/MM/dd a hh:mm").format(list.get(i).mTime);
				entity.searchedTime =list.get(i).mAlarmStartTime; //경로 찾기 시작한 시간 
				mMovePathList.add(entity);
			}
			mMovePathAdapter.setMovePathList(mMovePathList);
			setListAdapter(mMovePathAdapter);
			mProgressDialog.dismiss();		
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
    	if(mMovePathAdapter.isCheckMode()){    		
    		onCheckBoxClick(position);
    		mMovePathAdapter.notifyDataSetChanged();
    	}else{    		
    		Intent intent = new Intent(this, RealTimeMovePathDetailBoxActivity.class);    
    		intent.putExtra("startLat", mMovePathList.get(position).startLat);
    		intent.putExtra("startLon", mMovePathList.get(position).startLon);
    		intent.putExtra("destinationLat", mMovePathList.get(position).destinationLat);
    		intent.putExtra("destinationLon", mMovePathList.get(position).destinationLon);
    		intent.putExtra("searchedTime", mMovePathList.get(position).searchedTime);    
    		startActivity(intent);    		
    	}    
	}
	
	public void onCheckBoxClick(int position) { 
		
		boolean isChecked = false;

		isChecked = mMovePathList.get(position).getChecked();	

		isChecked = !isChecked;
		mMovePathList.get(position).setChecked(isChecked);
		// 체크 여부를 추가 또는 제거.
		if (isChecked == true) {
			mCheckedMovePathList.add(Integer.valueOf(position));
		} else {
			mCheckedMovePathList.remove(Integer.valueOf(position));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_delete_alldelete, menu);
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(mMovePathList.size() ==0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		
		switch (item.getItemId()) {
		
		case R.id.delete:
			mConfirmButton.setVisibility(View.VISIBLE);
			mMovePathAdapter.setCheckMode();
			mMovePathAdapter.notifyDataSetChanged();
		break;	
		case R.id.all_delete:
			//전체 삭제 로직				 
			GeneralAlertDialog dialog = new  GeneralAlertDialog(RealTimeMovePathActivity.this, getResources().getString(R.string.delete_total_items));    			
	    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//삭제하기					
					DaoRealTImePathBox dao =DaoRealTImePathBox.getInstance();
					dao.deleteAllData(getApplicationContext());
					//mMovePathAdapter.setCheckMode();
					mConfirmButton.setVisibility(View.GONE);
					mHandler.sendEmptyMessage(0);
				}
			});
			dialog.setNegativeButton(new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
	    	dialog.show();	
		break;	
	
		default:
			break;
		}	
		
		return true;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	  
		if(mMovePathAdapter.isCheckMode()){
			mMovePathAdapter.setCheckMode();
			mMovePathAdapter.notifyDataSetChanged();
			return;
		}
		
		super.onBackPressed();
	}
}
