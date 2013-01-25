package com.skp.opx.rpn.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ListActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.database.DaoSendBox;
import com.skp.opx.rpn.database.EntitySendBox;
import com.skp.opx.rpn.entity.EntityPathDetail;
import com.skp.opx.rpn.ui.adapter.Adapter_PathDetail;
import com.skp.opx.rpn.ui.dialog.GeneralAlertDialog;

/**
 * @설명 : 발신함 상세 Activity
 * @클래스명 : SendingBoxPathDetailActivity 
 *
 */
public class SendingBoxPathDetailActivity extends ListActivity {
	
	 private Adapter_PathDetail mSendBoxPathDetailAdpater = null;
	 private Button mConfirmButton;
	 private ArrayList<EntityPathDetail> mSendMsgBoxDetailList = new  ArrayList<EntityPathDetail>();
	 private ArrayList<Integer> mCheckedSendMsgBoxDetailList =  new ArrayList<Integer>(); // 체크 저장 ID 리스트
	 private String mName = "";
	 private String mMdn = "";
	 private TextView mNameTextView;
	 private TextView mMdnTextView;
	
	 private boolean mIsNateOn;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_path_detail);
		
		Intent intent = getIntent();
		mName = intent.getStringExtra("name");
		mMdn = intent.getStringExtra("mdn");
		mNameTextView = (TextView)findViewById(R.id.name);
		mNameTextView.setText(mName);
		mMdnTextView = (TextView)findViewById(R.id.phone_num);
		mMdnTextView.setText(mMdn);
		mIsNateOn = intent.getBooleanExtra("type", true);
         mHandler.sendEmptyMessage(0);
		//확인 버튼
		mConfirmButton = (Button)findViewById(R.id.confirm_button);
		mConfirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mCheckedSendMsgBoxDetailList.size() ==0)
				{
					Toast.makeText(getApplicationContext(), R.string.no_select_item, Toast.LENGTH_SHORT).show();
					return;
				}
				//삭제 확인 다이얼로그 
				GeneralAlertDialog dialog = new GeneralAlertDialog(SendingBoxPathDetailActivity.this, getResources().getString(R.string.delete_selected_message));
				dialog.setPostiveButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//삭제
						DaoSendBox dao = DaoSendBox.getInstance();
    					for (int i = 0; i < mCheckedSendMsgBoxDetailList.size(); i++) {
							dao.deleteDataById(getApplicationContext(), mSendMsgBoxDetailList.get(mCheckedSendMsgBoxDetailList.get(i)).id);
						}
    					 //마지막 항목 삭제이면 그 번호는 다 지운다.
    					if(mCheckedSendMsgBoxDetailList.size() == mSendMsgBoxDetailList.size()){
    						finish();
    					}
    					mSendBoxPathDetailAdpater.setCheckBoxMode();
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
		
	}
	 
    private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			
			//특정 사람에 대한 내용만 가져옴 
			DaoSendBox dao = DaoSendBox.getInstance();
			ArrayList<EntitySendBox> list = null;
			if(mIsNateOn){
				//네이트온
				list = dao.getContactSendInfoListByAccount(getApplicationContext(), mMdn);
			}else{
				//연락처
				list = dao.getContactSendInfoListByMdn(getApplicationContext(), mMdn);
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd a hh:mm");
			if(mSendMsgBoxDetailList.size() > 0){
				mSendMsgBoxDetailList.clear();
			}
			for (int i = 0; i < list.size(); i++) {
				long id = list.get(i).getID();
				String starting = list.get(i).mStartLocation;
				String destination = list.get(i).mDestnationLocation;
				String ete = list.get(i).mExpectionArrivedTime;
				String messaging = list.get(i).mMessage;
				String date = dateFormat.format(list.get(i).mDeliveryTime);
				
				mSendMsgBoxDetailList.add(new EntityPathDetail(id, starting, destination, ete, messaging, date));
			}
			
			mSendBoxPathDetailAdpater = new Adapter_PathDetail(SendingBoxPathDetailActivity.this, R.layout.list_item_path_detail);
			mSendBoxPathDetailAdpater.setPathDetailList(mSendMsgBoxDetailList);
			setListAdapter(mSendBoxPathDetailAdpater);
			
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		onCheckBoxClick(position);
		mSendBoxPathDetailAdpater.notifyDataSetChanged();
	}
	
	public void onCheckBoxClick(int position) { 
		
		boolean isChecked = false;

		isChecked = mSendMsgBoxDetailList.get(position).getChecked();	

		isChecked = !isChecked;
		mSendMsgBoxDetailList.get(position).setChecked(isChecked);
		// 체크 여부를 추가 또는 제거.
		if (isChecked == true) {
			mCheckedSendMsgBoxDetailList.add(Integer.valueOf(position));
		} else {
			mCheckedSendMsgBoxDetailList.remove(Integer.valueOf(position));
		}
	}
	//삭제하기,전체 삭제
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_delete_alldelete, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		
		case R.id.delete:
			mConfirmButton.setVisibility(View.VISIBLE);
			mSendBoxPathDetailAdpater.setCheckBoxMode();
			mSendBoxPathDetailAdpater.notifyDataSetChanged();
		break;	
		case R.id.all_delete:
			//전체 삭제 로직				 
			GeneralAlertDialog dialog = new  GeneralAlertDialog(SendingBoxPathDetailActivity.this, getResources().getString(R.string.delete_total_items));    			
	    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					DaoSendBox dao = DaoSendBox.getInstance();
					//contact 기준으로 삭제 한다.
					dao.deleteDataByMdn(getApplicationContext(), mMdn);
					finish();
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
		if(mSendBoxPathDetailAdpater.isCheckBoxMode()){			
			mConfirmButton.setVisibility(View.GONE);
			mSendBoxPathDetailAdpater.setCheckBoxMode();
			mSendBoxPathDetailAdpater.notifyDataSetChanged();
			return;
		}
		
		super.onBackPressed();
	}
	

}
