package com.skp.opx.rpn.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.contact.AddressBookDAO;
import com.skp.opx.rpn.database.DaoDesignatedContactBox;
import com.skp.opx.rpn.database.DaoSendBox;
import com.skp.opx.rpn.database.EntitySendBox;
import com.skp.opx.rpn.entity.EntityContact;
import com.skp.opx.rpn.entity.EntitySendMessageBox;
import com.skp.opx.rpn.ui.adapter.Adapter_Address;
import com.skp.opx.rpn.ui.adapter.Adapter_SendMessageBox;
import com.skp.opx.rpn.ui.dialog.GeneralAlertDialog;

/**
 * @설명 : 메시지 발신함 Activity
 * @클래스명 : SendMessageBoxActivity 
 *
 */
public class SendMessageBoxActivity extends ListActivity implements RadioGroup.OnCheckedChangeListener, OnClickListener{

	private Adapter_SendMessageBox mSendMessageBoxAdapter;
	private ArrayList<EntitySendMessageBox> mSendMessageList = new ArrayList<EntitySendMessageBox>();
	private ArrayList<Integer> mCheckedSendMsgBoxList =  new ArrayList<Integer>(); // 체크 저장 ID 리스트
	private static final int GET_CONTACT = 0; //주소록
    private static final int GET_NATE_FRIENDS = 1;//네이트온 친구 목록
	private ProgressDialog mProgressDialog;
    private static final int DIALOG_DISMISS = 2; 
	private boolean mIsNateContact; //일반 주소록 인지, 네이트온 친구 목록인지 
	private Button mConfirmButton;
	private boolean mIsDeleteAll;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading));
        mHandler.sendEmptyMessage(GET_CONTACT);                
        setContentView(R.layout.activity_send_box);
    	//라디오 버튼 
        RadioGroup tabRadioGroup = (RadioGroup)findViewById(R.id.address_rg);
		tabRadioGroup.setOnCheckedChangeListener(this);
		mConfirmButton = (Button)findViewById(R.id.confirm_button);
		mConfirmButton.setOnClickListener(this);
        mSendMessageBoxAdapter = new Adapter_SendMessageBox(this);
        mSendMessageBoxAdapter.setSendMessageList(mSendMessageList);
        setListAdapter(mSendMessageBoxAdapter);
        
    }
    
    private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {

			switch(msg.what) {
			 
				case GET_CONTACT: 
					mProgressDialog.show();
					getContactSendMsg(getApplicationContext());
				break;			

				case GET_NATE_FRIENDS:
					mProgressDialog.show();
					getNateFriendMsg(getApplicationContext());
					
				break;
				
				case DIALOG_DISMISS:
					mProgressDialog.dismiss();
				break;
				
			}
			super.handleMessage(msg);
		}
	};	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mCheckedSendMsgBoxList.size() == 0 ){    			
    		Toast.makeText(this,R.string.no_select_item, Toast.LENGTH_SHORT).show();
    		return;    		
    	}
		//전체 삭제인지 확인 		
		//삭제 확인 다이얼로그
		GeneralAlertDialog dialog = new  GeneralAlertDialog(SendMessageBoxActivity.this, getResources().getString(R.string.delete_selected_favorite_items));    			
    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//삭제하기
				DaoSendBox dao = DaoSendBox.getInstance();
				if(mIsDeleteAll){
					if(mIsNateContact){
						dao.deleteAllDataByType(getApplicationContext(), 1);
					}else{
						dao.deleteAllDataByType(getApplicationContext(), 0);
					}
						
				}else{
					for (int i = 0; i < mCheckedSendMsgBoxList.size(); i++) { //동일한 이름은 모두 지운다
						dao.deleteData(getApplicationContext(),mSendMessageList.get(mCheckedSendMsgBoxList.get(i)).name);
						
					}						
				}					
				
				mSendMessageBoxAdapter.setCheckMode();
				mConfirmButton.setVisibility(View.GONE);
				if(mIsNateContact){
					mHandler.sendEmptyMessage(GET_NATE_FRIENDS);
				}else{
					mHandler.sendEmptyMessage(GET_CONTACT);
				}
				
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

	@Override
	public void onCheckedChanged(RadioGroup gr, int checkId) {
		// TODO Auto-generated method stub
		if(checkId == R.id.myAddress_rb){
			mHandler.sendEmptyMessage(GET_CONTACT);
			mIsNateContact = false;
		}else if(checkId == R.id.nate_rb){
			//네이트온 친구 목로 조회 요청
			mHandler.sendEmptyMessage(GET_NATE_FRIENDS);
			mIsNateContact = true;
		}
	}

	private void getContactSendMsg(Context context){
		//최신 기준, 번호당 하나씩 뿌려줌
		DaoSendBox dao = DaoSendBox.getInstance();
		 
	      List<EntitySendBox>  contactList = dao.getContactSendInfoListByMdn(context);
	     
	      if(mSendMessageList.size()>0){
	    	  mSendMessageList.clear();
	        }
	        // db 용 엔티티를 UI 용 엔티티로 변환 
	      EntitySendMessageBox entitty= null;         
	      SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd a hh:mm");
	        for (int i = 0; i < contactList.size(); i++) {
	        	entitty = new EntitySendMessageBox();
	        	entitty.id = contactList.get(i).getID();
	        	entitty.mdn = (contactList.get(i).mMdn);
	        	entitty.setName(contactList.get(i).mReceiver);
	        	entitty.setContentPreview(contactList.get(i).mStartLocation + ":" +contactList.get(i).mDestnationLocation);	
	        	
	        	entitty.setDate(String.valueOf(dateFormat.format(contactList.get(i).mDeliveryTime)));
	        	mSendMessageList.add(entitty);
			}        
	        mSendMessageBoxAdapter = new Adapter_SendMessageBox(this);    	
	        mSendMessageBoxAdapter.setSendMessageList(mSendMessageList);
	        setListAdapter(mSendMessageBoxAdapter);
	        mHandler.sendEmptyMessage(DIALOG_DISMISS);
		
		mHandler.sendEmptyMessage(DIALOG_DISMISS);
		
	}
	
	private void getNateFriendMsg(Context context){
		DaoSendBox dao = DaoSendBox.getInstance();
		
	      List<EntitySendBox>  contactList = dao.getContactSendInfoListByAccount(context);
	     
	      if(mSendMessageList.size()>0){
	    	  mSendMessageList.clear();
	        }
	        // db 용 엔티티를 UI 용 엔티티로 변환 
	      EntitySendMessageBox entitty= null;         
	      SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd a hh:mm");
	        for (int i = 0; i < contactList.size(); i++) {
	        	entitty = new EntitySendMessageBox();
	        	entitty.id = (contactList.get(i).getID());
	        	entitty.mdn = (contactList.get(i).mMdn);
	        	entitty.setName(contactList.get(i).mReceiver);
	        	entitty.setContentPreview(contactList.get(i).mStartLocation + ":" +contactList.get(i).mDestnationLocation);	
	        	entitty.setDate(String.valueOf(dateFormat.format(contactList.get(i).mDeliveryTime)));
	        	mSendMessageList.add(entitty);
			}        
	        mSendMessageBoxAdapter = new Adapter_SendMessageBox(this);    	
	        mSendMessageBoxAdapter.setSendMessageList(mSendMessageList);
	        setListAdapter(mSendMessageBoxAdapter);
	        mHandler.sendEmptyMessage(DIALOG_DISMISS);
		
		
	}
	
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	//메시지 발신 상세로 이동
    	// 로직 구현시 메시지 아이디 등을 넘겨 야함 
    	if(mSendMessageBoxAdapter.isCheckMode()){
    		onCheckBoxClick(position);
    		mSendMessageBoxAdapter.notifyDataSetChanged();
    	}else{    		
    		Intent intent = new Intent(this, SendingBoxPathDetailActivity.class);
    		intent.putExtra("name", mSendMessageList.get(position).name);
    		intent.putExtra("mdn", mSendMessageList.get(position).mdn);
    		intent.putExtra("type", mIsNateContact);//true면 네이트온
    		startActivityForResult(intent, 100);    		
    	}    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	//발신 상세에서 삭제 했을경우 갱신
    	//네이트 온에서 왔음 네이트온 으로 갱신 
    	if(mIsNateContact){
    		mHandler.sendEmptyMessage(GET_NATE_FRIENDS);
    	}else{
    		mHandler.sendEmptyMessage(GET_CONTACT);
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
	public void onCheckBoxClick(int position) { 
		
		boolean isChecked = false;

		isChecked = mSendMessageList.get(position).getChecked();	

		isChecked = !isChecked;
		mSendMessageList.get(position).setChecked(isChecked);
		// 체크 여부를 추가 또는 제거.
		if (isChecked == true) {
			mCheckedSendMsgBoxList.add(Integer.valueOf(position));
		} else {
			mCheckedSendMsgBoxList.remove(Integer.valueOf(position));
		}
	}
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_delete_alldelete, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
      if(mSendMessageList.size() ==0){
    	  return false;    	  
      }else{
    	  return true;
      }
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		
		switch (item.getItemId()) {
		
		case R.id.delete:
			mIsDeleteAll = false;
			mSendMessageBoxAdapter.setCheckMode();
			mConfirmButton.setVisibility(View.VISIBLE);
			mSendMessageBoxAdapter.notifyDataSetChanged();
		break;	
	
		case R.id.all_delete:
			mIsDeleteAll = true;
			mSendMessageBoxAdapter.setCheckMode();			
			for (int i = 0; i < mSendMessageList.size(); i++) {
				mSendMessageList.get(i).setChecked(true);
				mCheckedSendMsgBoxList.add(i);
			} 				
			mConfirmButton.setVisibility(View.VISIBLE);
			mSendMessageBoxAdapter.notifyDataSetChanged();
		break;	
	
		default:
			break;
		}	
		
		return true;
	}
}
