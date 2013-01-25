package com.skp.opx.rpn.ui;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.database.DaoFavoriteBox;
import com.skp.opx.rpn.database.EntityFavoriteBox;
import com.skp.opx.rpn.entity.EntityFavorite;
import com.skp.opx.rpn.ui.adapter.Adapter_Favorite;
import com.skp.opx.rpn.ui.dialog.GeneralAlertDialog;

/**
 * @설명 : 즐겨 찾기 Activity
 * @클래스명 : FavoriteActivity 
 *
 */
public class FavoriteActivity extends ListActivity implements OnClickListener{
	
	private Adapter_Favorite mFavoriteAdapter;
	private Button mConfirmButton;
	private ArrayList<EntityFavorite> mFavoriteList = new ArrayList<EntityFavorite>(); 
	private ArrayList<Integer> mCheckedFavoriteList = new ArrayList<Integer>(); // 체크 저장 ID 리스트
	private int mClickedPos;	
	private boolean mIsFromPathAlert; //보관함이 아닌 경로 에서 들어올경우 setResult
	private boolean mIsStart; //출발점 요청인지

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        mIsFromPathAlert = getIntent().getBooleanExtra("fromPathAlert", false);
        mIsStart = getIntent().getBooleanExtra("isStart", false);
    	mHandler.sendEmptyMessage(0);
    	
        setContentView(R.layout.activity_favorite);
        mConfirmButton = (Button)findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);    
        
    }
    private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			DaoFavoriteBox dao = DaoFavoriteBox.getInstance();
			ArrayList<EntityFavoriteBox> list = dao.getFavoriteInfoList(getApplicationContext());
			//ui entity 로 변경
			EntityFavorite entity = null;
			if(mFavoriteList.size()>0){
				mFavoriteList.clear();
			}
			for (int i = 0; i < list.size(); i++) {
				entity = new EntityFavorite();
				entity.id = list.get(i).getmID();
				entity.mLocationName = list.get(i).mName;
				entity.mLon = list.get(i).mLon;
				entity.mLat = list.get(i).mLat;
				mFavoriteList.add(entity);
			}
			
			mFavoriteAdapter = new Adapter_Favorite(getApplicationContext());                
		    mFavoriteAdapter.setFavoriteList(mFavoriteList);
		    setListAdapter(mFavoriteAdapter);
			
			super.handleMessage(msg);
		}
	};	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//경로 설정에서 진입했을겨우 setResult 해줘야함
		if(mIsFromPathAlert){
			Intent intent = getIntent();
			String location = mFavoriteList.get(position).getLocationName();
			String lon = mFavoriteList.get(position).mLon;
			String lat = mFavoriteList.get(position).mLat;
			String result = location + ";" + lon + ";" + lat;
			intent.putExtra("choice" , result);
			intent.putExtra("", mIsStart);
			setResult(RESULT_OK, intent);
			finish();
		}
		
		onCheckBoxClick(position);
		mClickedPos = position;
		mFavoriteAdapter.notifyDataSetChanged();
	}
	
	public void onCheckBoxClick(int position) { 
		
		
		boolean isChecked = false;

		isChecked = mFavoriteList.get(position).getChecked();	

		isChecked = !isChecked;
		mFavoriteList.get(position).setChecked(isChecked);
		// 체크 여부를 추가 또는 제거.
		if (isChecked == true) {
			mCheckedFavoriteList.add(Integer.valueOf(position));
		} else {
			mCheckedFavoriteList.remove(Integer.valueOf(position));
		}
	}

	/** 
	 *  중복 여부 체크 Method
	 * */
	private boolean isDulicate(String name){		
		DaoFavoriteBox dao = DaoFavoriteBox.getInstance();
		ArrayList<EntityFavoriteBox> list = dao.getFavoriteInfoList(this);
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).mName.equals(name)){
				return true;
			}
		}
		
		return false;
	}
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	GeneralAlertDialog dialog = null;
    	switch (v.getId()) {
		
    	case R.id.confirm_button:
    		
    		if(mCheckedFavoriteList.size() == 0 ){    			
    		Toast.makeText(this,R.string.no_select_item, Toast.LENGTH_SHORT).show();
    		return;    		
    		}
    		
    		if (mFavoriteAdapter.isRadioButtonMode()){ //이름 변경 다이얼로그
    			
    			dialog = new  GeneralAlertDialog(FavoriteActivity.this, getResources().getString(R.string.input_name_to_be_changed));
    			final EditText editText = new EditText(this);
    			editText.setText(mFavoriteList.get(mClickedPos).mLocationName);
    	    	dialog.setView(editText);
    	    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
    				
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					// TODO Auto-generated method stub
    				//1.이름 중복 인지 확인				
    				if (isDulicate(editText.getText().toString())){
    					Toast.makeText(getApplicationContext(), R.string.already_registerd, Toast.LENGTH_LONG).show();
    				}else{
    				//이름 변경하기 
    				DaoFavoriteBox dao = DaoFavoriteBox.getInstance();
    				dao.updateFavoriteName(getApplicationContext(), mFavoriteList.get(mClickedPos).id, editText.getText().toString());
    				mConfirmButton.setVisibility(View.GONE);
    				mHandler.sendEmptyMessage(0);
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
    	    	
			}else{ //삭제 확인 다이얼로그
	  			
    			dialog = new  GeneralAlertDialog(FavoriteActivity.this, getResources().getString(R.string.delete_selected_favorite_items));    			
    	    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
    				
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					// TODO Auto-generated method stub
    				    				
    					//삭제
    					DaoFavoriteBox dao = DaoFavoriteBox.getInstance();
    					for (int i = 0; i < mCheckedFavoriteList.size(); i++) {
							dao.deleteData(getApplicationContext(), mFavoriteList.get(mCheckedFavoriteList.get(i)).id);
						}
    					mFavoriteAdapter.setCheckBoxMode(false);
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
    	
    		break;

		default:
			break;
		}
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
        getMenuInflater().inflate(R.menu.activity_favorite, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	
    	if (mIsFromPathAlert || mFavoriteList.size() ==0){ //경로 창에서 진입시 옵션 메뉴 보이면 안됨
    		return false;
    	}
    	if(mFavoriteAdapter.isCheckBoxMode() || mFavoriteAdapter.isRadioButtonMode()){
    		return false;	
    	}else{
    		return super.onPrepareOptionsMenu(menu);    		
    	}
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
			
			case R.id.delete:
			//체크 박스 보이기
			mConfirmButton.setVisibility(View.VISIBLE);
			mFavoriteAdapter.setCheckBoxMode(true);
			mFavoriteAdapter.setRadioButtonMode(false);
			mFavoriteAdapter.notifyDataSetChanged();
			break;		  
			case R.id.all_delete:
				//전체 삭제 로직				 
				GeneralAlertDialog dialog = new  GeneralAlertDialog(FavoriteActivity.this, getResources().getString(R.string.delete_total_items));    			
		    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						DaoFavoriteBox dao = DaoFavoriteBox.getInstance();
						dao.deleteAllData(getApplicationContext());
						mFavoriteAdapter.setCheckBoxMode(false);
						mFavoriteAdapter.setRadioButtonMode(false);
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
		if(mFavoriteAdapter.isCheckBoxMode() || mFavoriteAdapter.isRadioButtonMode()){
			setUncheckMode();
			return;
		 }		
		
		super.onBackPressed();
	}
	
	private void setUncheckMode(){
		for (int i = 0; i < mFavoriteList.size(); i++) {
			mFavoriteList.get(i).setChecked(false);			
		}
		mFavoriteAdapter.setRadioButtonMode(false);
		mFavoriteAdapter.setCheckBoxMode(false);
		mConfirmButton.setVisibility(View.GONE);
		mFavoriteAdapter.notifyDataSetChanged();
	}

}
