package com.skp.opx.rpn.ui;

import java.util.ArrayList;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.database.DaoDesignatedContactBox;
import com.skp.opx.rpn.database.EntityDesignatedContactBox;
import com.skp.opx.rpn.entity.EntityContact;
import com.skp.opx.rpn.ui.adapter.Adapter_Address;
import com.skp.opx.rpn.ui.dialog.GeneralAlertDialog;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @설명 : 지정 연락처 Activity
 * @클래스명 : DesignatedContactActivity 
 *
 */
public class DesignatedContactActivity extends ListActivity implements OnClickListener{

	private Adapter_Address mDesignatedContactAdapter;
	private ArrayList<EntityContact> mDesignatedContactList = new ArrayList<EntityContact>();
	private ArrayList<Integer> mCheckedDesignatedContactList = new ArrayList<Integer>(); // 체크 저장 ID 리스트
	private Button mConfirmButton;
	private boolean mIsDeleteAll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
    	mHandler.sendEmptyMessage(0);
    	
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_designated_contact);
	    mConfirmButton = (Button)findViewById(R.id.confirm_button);
	    mConfirmButton.setOnClickListener(this);
		mDesignatedContactAdapter  = new Adapter_Address(this);
		mDesignatedContactAdapter.setAddressList(mDesignatedContactList);
		setListAdapter(mDesignatedContactAdapter);		
		
	}
    private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			DaoDesignatedContactBox dao = DaoDesignatedContactBox.getInstance();
			ArrayList<EntityDesignatedContactBox> list = dao.getAllDesignatedContactInfoList(getApplicationContext());
			//ui entity 로 변경
			EntityContact entity = null;
			if(mDesignatedContactList.size() >0){
				mDesignatedContactList.clear();
			}
			boolean  contactTitle = false;
			boolean  nateOnTitle = false;
			for (int i = 0; i < list.size(); i++) {
				entity = new EntityContact();
				entity.id = list.get(i).getmID();
				entity.name = list.get(i).mName;
				entity.contact = list.get(i).mContact;
				entity.type = list.get(i).mType;
				if( list.get(i).mType == 0 && !contactTitle ){
					contactTitle = true;
					entity.titleType = 1;
				}
				if(list.get(i).mType == 1 && !nateOnTitle ){
					nateOnTitle = true;
					entity.titleType = 2;
				}
				mDesignatedContactList.add(entity);
			} 
			
			mDesignatedContactAdapter = new Adapter_Address(getApplicationContext());                
			mDesignatedContactAdapter.setAddressList(mDesignatedContactList);
		    setListAdapter(mDesignatedContactAdapter);
			
			super.handleMessage(msg);
		}
	};	
	
	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm_button:
			
			if(mCheckedDesignatedContactList.size() == 0 ){    			
	    		Toast.makeText(this,R.string.no_select_item, Toast.LENGTH_SHORT).show();
	    		return;    		
	    	}
			//전체 삭제인지 확인 		
			//삭제 확인 다이얼로그
			GeneralAlertDialog dialog = new  GeneralAlertDialog(DesignatedContactActivity.this, getResources().getString(R.string.delete_selected_favorite_items));    			
	    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//삭제하기
					DaoDesignatedContactBox dao = DaoDesignatedContactBox.getInstance();
					if(mIsDeleteAll){
							dao.deleteAllData(getApplicationContext());
					}else{
						for (int i = 0; i < mCheckedDesignatedContactList.size(); i++) {
							dao.deleteData(getApplicationContext(),mDesignatedContactList.get(mCheckedDesignatedContactList.get(i)).id);
							
						}						
					}					
					
					mDesignatedContactAdapter.setCheckMode();
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
		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		onCheckBoxClick(position);
		mDesignatedContactAdapter.notifyDataSetChanged();
	}
	public void onCheckBoxClick(int position) { 
		
		boolean isChecked = false;

		isChecked = mDesignatedContactList.get(position).getChecked();	

		isChecked = !isChecked;
		mDesignatedContactList.get(position).setChecked(isChecked);
		// 체크 여부를 추가 또는 제거.
		if (isChecked == true) {
			mCheckedDesignatedContactList.add(Integer.valueOf(position));
		} else {
			mCheckedDesignatedContactList.remove(Integer.valueOf(position));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		   getMenuInflater().inflate(R.menu.activity_designated_contact, menu);
	        return true;
	}
	
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	if(mDesignatedContactAdapter.isCheckMode() || mDesignatedContactList.size() ==0){
    		return false;	
    	}else{
    		return super.onPrepareOptionsMenu(menu);    		
    	}
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		
			case R.id.add_address://지정 연락처 추가
				Intent intent = new Intent(this, AddressCheckBoxActivity.class);
				startActivityForResult(intent, 100);
			break;	
			case R.id.delete://삭제하기
				mIsDeleteAll = false;
				mDesignatedContactAdapter.setCheckMode();
				mConfirmButton.setVisibility(View.VISIBLE);
				mDesignatedContactAdapter.notifyDataSetChanged();
				
			break;		  
			case R.id.all_delete://전체 삭제
					//전체 삭제 선택시 다이얼로그 띄우기
				GeneralAlertDialog dialog = new  GeneralAlertDialog(DesignatedContactActivity.this, getResources().getString(R.string.delete_total_items));    			
		    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						DaoDesignatedContactBox dao = DaoDesignatedContactBox.getInstance();
						dao.deleteAllData(getApplicationContext());
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
	
		//지정 연락처 같다 왔으면 무조건 리프레쉬
		mConfirmButton.setVisibility(View.GONE);
		mHandler.sendEmptyMessage(0);
		super.onActivityResult(requestCode, resultCode, data);
	
	
	}
   @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mDesignatedContactList.size(); i++) {
			mDesignatedContactList.get(i).setChecked(false);
			
		} 	
		mConfirmButton.setVisibility(View.GONE);
		
		if(mDesignatedContactAdapter.isCheckMode()){
			mDesignatedContactAdapter.setCheckMode();
			mDesignatedContactAdapter.notifyDataSetChanged();
			return;
		}
	   super.onBackPressed();
	}
	
	
	
}
