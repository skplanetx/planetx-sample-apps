package com.skp.opx.rpn.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.opx.rpn.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.rpn.Constants.IntentDefine;
import com.skp.opx.rpn.database.DaoSearchedPathBox;
import com.skp.opx.rpn.database.EntitySearchdPathBox;
import com.skp.opx.rpn.entity.EntityAutoComplete;
import com.skp.opx.rpn.entity.EntityMovePath;
import com.skp.opx.rpn.entity.EntityTotalSearch;
import com.skp.opx.rpn.ui.adapter.Adapter_AutoComplete;
import com.skp.opx.rpn.ui.adapter.Adapter_MovePath;
import com.skp.opx.rpn.ui.dialog.AlertListDialog;
import com.skp.opx.rpn.ui.dialog.GeneralAlertDialog;
import com.skp.opx.rpn.util.CoordinateUtil;
import com.skp.opx.rpn.util.PreferenceUtil;
import com.skp.opx.rpn.util.ServiceUtil;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OAuthenticate;
import com.skp.opx.sdk.OnEntityParseComplete;


/**
 * @설명 : 출발지, 목적지 설정 Activity
 * @클래스명 : PathAlertActivity 
 *
 */
public class PathAlertActivity extends ListActivity implements OnClickListener{

	private Button mStartButton;
	private Button mEndButton;
    private Button mHome;
    private Button mComapany;
    private TextView mRecentSearchPath;
    private ProgressDialog mProgressDialog= null;
	private LocationManager mLocationManager;
	private AutoCompleteTextView mStartLocationTextView;//출발지 
	private AutoCompleteTextView mDestinationTextView;
	//출발지 자동 검색 추가
	private EntityAutoComplete mStartEntity; //선택된 출발지 (이름, 좌표)
	private EntityAutoComplete mDestinationEntity; //선택된 목적지 (이름, 좌표)
	private Button mSearchButton;
	//추가 : 최근 검색한 이동 경로 보여 주기 : 목적지 입력	
	private Adapter_MovePath mMovePathAdapter;
	private ArrayList<EntityMovePath> mMovePathList = new ArrayList<EntityMovePath>();
	private InputMethodManager mInputMethodManager;			// 입력 시스템
    //출발지 자동완성 추가
	private Adapter_AutoComplete mAutoCompleteStartAdapter;
	private ArrayList<EntityAutoComplete> mAutoCompleteStartList = new ArrayList<EntityAutoComplete>();
	//도착지 자동완성
	private Adapter_AutoComplete mAutoCompleteDestinationAdapter;
	private ArrayList<EntityAutoComplete> mAutoCompleteDestinationList = new ArrayList<EntityAutoComplete>();
    private boolean doSearchStart = true;
	private boolean doSearchDestination = true;
	private ProgressBar mSearchProgressBarStart;
    private ProgressBar mSearchProgressBarDestination;
    private Location mStartingLocation = null; //현재 위치
    private String mStartLongitude = "";
    private String mStartLatitude = "";
    private String mDestinationLongitude;
    private String mDestinationLatitude;
   
    private static final int UPDATE_MIN = 1000*60*5; //5분마다 현재 위치 갱신(메인화면에서 5분이상 머무는 경우는 없음으로)
    private static final int UPDATE_METER = 100;
    
    private boolean mIsAutoSearched; //자동완성에서 목적지 설정한 경우(정확한 좌표)    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAuth();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading));
		mInputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);		
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_path_alert);
        mRecentSearchPath = (TextView)findViewById(R.id.recent_search_path);
        mSearchProgressBarStart = (ProgressBar)findViewById(R.id.search_progress_bar_start);
        mSearchProgressBarDestination = (ProgressBar)findViewById(R.id.search_progress_bar_destination);
        mStartButton = (Button)findViewById(R.id.start_bt);
        mStartButton.setOnClickListener(this);
        mEndButton = (Button)findViewById(R.id.dest_bt);
        mEndButton.setOnClickListener(this);    
        mSearchButton = (Button)findViewById(R.id.search);
        mSearchButton.setOnClickListener(this);
        mHome = (Button)findViewById(R.id.home_bt);
        mHome.setOnClickListener(this);
        mComapany = (Button)findViewById(R.id.company_bt);
        mComapany.setOnClickListener(this);   
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMovePathAdapter = new Adapter_MovePath(getApplicationContext());
        mMovePathAdapter.setOnClickListener(this);
        //출발지 자동완성
        //mStartLocation = (EditText)findViewById(R.id.start_et);
        mStartLocationTextView = (AutoCompleteTextView)findViewById(R.id.auto_complete_start);
        mStartLocationTextView.setThreshold(1);
        mStartLocationTextView.addTextChangedListener(new StartRequestTextWatcher());
        mStartLocationTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				doSearchStart = false; //선택시에는 자동 완성 다시 뜨지 않도록
				mStartLocationTextView.setText("");
				mStartLocationTextView.append(((TextView)view).getText());	 
				mStartEntity = mAutoCompleteStartList.get(position);
				mStartLongitude = mStartEntity.x; //longitude
				mStartLatitude = mStartEntity.y; //latitude
				//mIsAutoSearched = true; //출발지 자동 완성 떠있을시 검색하는 경우 없을듯 하여...예외처리는 하지 않는다.  
				mAutoCompleteStartList.clear(); //자동 완성 리스트 초기화				
			}
		
        });
        
        mDestinationTextView= (AutoCompleteTextView)findViewById(R.id.auto_complete_destination);
        mDestinationTextView.setThreshold(1);
        mDestinationTextView.addTextChangedListener(new DestinationRequestTextWatcher());
        mDestinationTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				//자동완성 끄기 //선택된 후에는 재검색 안함
				doSearchDestination = false;
				mDestinationTextView.setText("");
				mDestinationTextView.append(((TextView)view).getText());	 
				mDestinationEntity = mAutoCompleteDestinationList.get(position);
				mDestinationLongitude = mDestinationEntity.x; //longitude
				mDestinationLatitude = mDestinationEntity.y; //latitude
				mIsAutoSearched = true; //목적지 자동검색 떠있을시 검색하는 경우 첫번째 장소로 자동 선택
				mAutoCompleteDestinationList.clear(); //자동 완성 리스트 초기화
			}
		});
    }
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //gps활성화 한번만 묻도록 수정.
        if (!PreferenceUtil.getGPSDialogShown(this)){        	
            if (!gpsEnabled) {
    			GeneralAlertDialog dialog = new  GeneralAlertDialog(PathAlertActivity.this, 
    					getResources().getString(R.string.enable_gps),
    					getResources().getString(R.string.enable_gps_dialog));    			
    	    	dialog.setPostiveButton(new DialogInterface.OnClickListener() {
    				
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					// TODO Auto-generated method stub
    					 PreferenceUtil.setGPSDialogShown(getApplicationContext());
    					 enableLocationSettings();    					 
    				}
    			});			
    	    	dialog.show();	       
            }      
        	
        }      
        if(networkEnabled ==false){
        	Toast.makeText(this, R.string.not_support_network, Toast.LENGTH_LONG).show();
        	return;
        }
        mHandler.sendEmptyMessage(1); // 과거 경로 요청
    }
  

    /** 
     *  gps 설정 화면으로 이동 Method
     * */
    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		setup();
		mDestinationTextView.requestFocus();
	}   
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mLocationManager.removeUpdates(listener);
	}

    /** 
     *  현재 위치 찾기 Method
     * */
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
    
    /** 
     *  Location 반환 Method
     * */
    private Location requestUpdatesFromProvider(final String provider, final int errorResId, boolean showToast) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, UPDATE_MIN, UPDATE_METER, listener); //시간 , 거리
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

    /** 
     *  현재 좌표 업데이트 Method
     * */
    private void updateCooridiate(Location location){  
    	
    	if(location == null) {
    		Toast.makeText(this, R.string.canot_know_location, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	mStartingLocation = location;
		mStartLatitude = String.valueOf(location.getLatitude());
		mStartLongitude = String.valueOf(location.getLongitude());	
		mStartLocationTextView.setHint(getString(R.string.current_location));
    }
    
    private final LocationListener listener = new LocationListener() {

        public void onLocationChanged(Location location) {
        	
        	if(location != null && mStartingLocation != null && CoordinateUtil.isBetterLocation(location, mStartingLocation))
        		updateCooridiate( location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
  
    /**
     * @설명 : 목적지 TextWatcher
     * @클래스명 : DestinationRequestTextWatcher 
     *
     */
    private class DestinationRequestTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1,	int i2) {
			// TODO Auto-generated method stub
			 if (((AutoCompleteTextView) mDestinationTextView).isPerformingCompletion()) {
		            return;
		        }
		        if (charSequence.length() < 2) {// 2자 미만은 검색 제외
		        	if(mSearchProgressBarDestination != null){ //문자 없음에도 프로그래스바 도는 현상 수정.
		        		mSearchProgressBarDestination.setVisibility(View.GONE);		        		
		        	}
		            return;
		        }
		        if(doSearchDestination){//빨리 입력하는 것 고려 타이밍을 준다 
		        	mSearchProgressBarDestination.setVisibility(View.VISIBLE);
		        	mHandler.sendEmptyMessageDelayed(3, 500);		        	
		        }else{
		        	if(mSearchProgressBarDestination != null){ //문자 없음에도 프로그래스바 도는 현상 수정.
		        		mSearchProgressBarDestination.setVisibility(View.GONE);		        		
		        	}
		        }
		        doSearchDestination = true;
		      
		} 
    	
    }
    /**
     * @설명 : 출발지 TextWatcher
     * @클래스명 : DestinationRequestTextWatcher 
     *
     */
    private class StartRequestTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1,	int i2) {
			// TODO Auto-generated method stub
			 if (((AutoCompleteTextView) mStartLocationTextView).isPerformingCompletion()) {
		            return;
		        }
		        if (charSequence.length() < 2) {// 2자 미만은 검색 제외
		        	if(mSearchProgressBarStart != null){ //문자 없음에도 프로그래스바 도는 현상 수정.
		        		mSearchProgressBarStart.setVisibility(View.GONE);		        		
		        	}
		            return;
		        }
		        if(doSearchStart){//빨리 입력하는 것 고려 타이밍을 준다 
		        	mSearchProgressBarStart.setVisibility(View.VISIBLE);
		        	mHandler.sendEmptyMessageDelayed(5, 500);		        	
		        }else{
		        	if(mSearchProgressBarStart != null){ //문자 없음에도 프로그래스바 도는 현상 수정.
		        		mSearchProgressBarStart.setVisibility(View.GONE);		        		
		        	}
		        }
		        doSearchStart = true;		      
		} 
    	
    }
	/** 
	 *  Auth 요청 Method
	 * */
	private void getAuth(){
		
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) { //ICS 버전에서 Exception 나는 현상 대응

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
					new Handler().post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							OAuthenticate.publicAuthenticate(PathAlertActivity.this, Define.OAuth.KEY);
							OAuthenticate.privateAuthenticate(PathAlertActivity.this, "nateon/buddy,nateon/note", Define.OAuth.KEY, Define.OAuth.CLIENT_ID, Define.OAuth.SECRET);
						}
					});	
					Looper.loop();					
				}
			});
			thread.start();			

		}else{
			mHandler.sendEmptyMessage(4);			
		}		
	}
    private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			
		if(msg.what ==0){
			getAuth();		
		}else if(msg.what ==1){ //과거 검색 경로 
			DaoSearchedPathBox daoSearchedPathfBox = DaoSearchedPathBox.getInstance();	
			//20정도 제한해서 가져오자 
			ArrayList<EntitySearchdPathBox> list = daoSearchedPathfBox.getAllSearchedPathInfoList(getApplicationContext());
			EntityMovePath entity = null;
			if(mMovePathList.size() > 0){
				mMovePathList.clear();
			}
			for (int i = 0; i < list.size(); i++) {
				entity = new EntityMovePath();
				entity.path = list.get(i).mStartLocation + " > " + list.get(i).mDestination;				
				entity.date = new SimpleDateFormat("yy/MM/dd a hh:mm").format(list.get(i).mSearchedTime);
				entity.destinationLon = list.get(i).mDestinatioLon;
				entity.destinationLat = list.get(i).mDestinationLat;
				entity.startLon = list.get(i).mStartLon;
				entity.startLat = list.get(i).mStartLat;
				mMovePathList.add(entity);
			}
			if(mMovePathList.size() > 0){
				mRecentSearchPath.setVisibility(View.VISIBLE);
			}else{
				mRecentSearchPath.setVisibility(View.GONE);
			}
			mMovePathAdapter.setMovePathList(mMovePathList);
			setListAdapter(mMovePathAdapter);
			mProgressDialog.dismiss();		
			
		}else if(msg.what ==3){ // 도착지 검색
			searchTotalDestination(mDestinationTextView.getText().toString());
			
		}else if(msg.what ==4){
			OAuthenticate.publicAuthenticate(PathAlertActivity.this, Define.OAuth.KEY);
			OAuthenticate.privateAuthenticate(PathAlertActivity.this, "nateon/buddy,nateon/note", Define.OAuth.KEY, Define.OAuth.CLIENT_ID, Define.OAuth.SECRET);
		}else if(msg.what ==5){ //출발지 검색
			searchTotalStart(mStartLocationTextView.getText().toString());
		}
			super.handleMessage(msg);
		}
	};
	
    /** 
     *  검색 시작 Method
     * */
    private void startSearch(){
    	//출발위치가 즐겨 찾기인 경우 무조건 좌표가 있음으로 따로 자동 검색이 필요 없다.
		if((mStartLocationTextView.getText().toString().length() > 0 || mStartLocationTextView.getHint().toString().length() > 0) && mDestinationTextView.getText().toString().length() >0){
			
			if(mStartLatitude.equals("") || mStartLongitude.equals("")){
				Toast.makeText(this, "현재 위치 검색중 입니다. 잠시후 시도해 주세요.", Toast.LENGTH_LONG).show();  
				return;
			}
			//탐색 시작, 출발지, 목적지 넘겨야함 
			//키보드 내리기
			mInputMethodManager.hideSoftInputFromWindow(mDestinationTextView.getWindowToken(), 0);
			Intent intent = null;
			if(mIsAutoSearched){ //자동검색에서 선택시  
				
				intent = new Intent(getApplicationContext(), SearchPathActivity.class);
				if(mStartLocationTextView.getText().length() ==0 ){
					intent.putExtra("start", mStartLocationTextView.getHint().toString()); 
				}else{
					intent.putExtra("start", mStartLocationTextView.getText().toString());
				}
				
				intent.putExtra("destination", mDestinationTextView.getText().toString());	
				intent.putExtra("rsdStartXPos", mStartLongitude);
				intent.putExtra("rsdStartYPos", mStartLatitude);
				intent.putExtra("rsdEndXPos", mDestinationLongitude);
				intent.putExtra("rsdEndYPos", mDestinationLatitude);
				startActivity(intent);
			}else{
			    
				if(mAutoCompleteDestinationList.size() > 0){ //자동 완성 리스트가 떠 있을시
			    	EntityAutoComplete entity = mAutoCompleteDestinationList.get(0);
			    	intent = new Intent(getApplicationContext(), SearchPathActivity.class); 
					if(mStartLocationTextView.getText().length() ==0 ){
						intent.putExtra("start", mStartLocationTextView.getHint().toString());
					}else{
						intent.putExtra("start", mStartLocationTextView.getText().toString());
					}
					
					intent.putExtra("destination", entity.mName);	
					intent.putExtra("rsdStartXPos", mStartLongitude);
					intent.putExtra("rsdStartYPos", mStartLatitude);
					intent.putExtra("rsdEndXPos", entity.x);
					intent.putExtra("rsdEndYPos", entity.y);
					startActivity(intent);
			    	
			    }
			}
		
			
		}else{
			Toast.makeText(this, "출발지 혹은 목적지를 입력해 주세요.", Toast.LENGTH_LONG).show();  
		}
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_bt://시작지			
			final AlertListDialog start_loc_dialog = new AlertListDialog(this, getResources().getString(R.string.start_location_choice), getResources().getStringArray(R.array.start_location_array)); 
			start_loc_dialog.setOnItemSelectedListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> view, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub					
					if(position == 0){ //현재 위치
						//현재 위치 , 현재 좌표 를 넘겨야함
						//gps 현재 위치값 
						setup();
						Toast.makeText(PathAlertActivity.this, "현재 위치로 설정 되었습니다.", Toast.LENGTH_LONG).show();
						//위치 찾기 완료
					}else if(position ==1){//즐겨 찾기
						Intent intent = new  Intent(getApplicationContext(), FavoriteActivity.class);
						intent.putExtra("fromPathAlert", true);
						intent.putExtra("isStart", true);
						startActivityForResult(intent, IntentDefine.FAVORIATE_RESULT);
						
					}else if(position ==2){//지도에서 선택						
						Intent intent = new  Intent(getApplicationContext(), MapViewActivity.class);
						startActivityForResult(intent, IntentDefine.MAP_RESULT);
						startActivity(intent);
						
					}
					start_loc_dialog.dismiss();
				}
				
			});
			start_loc_dialog.show();
			break;
		case R.id.dest_bt://도착지		
			
			final AlertListDialog end_loc_dialog = new AlertListDialog(this, getResources().getString(R.string.end_location_choice), getResources().getStringArray(R.array.end_location_array)); 
			end_loc_dialog.setOnItemSelectedListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> view, View arg1,
						int position, long arg3) {
				
					if(position == 0){ //즐겨찾기
						
						Intent intent = new  Intent(getApplicationContext(), FavoriteActivity.class);
						intent.putExtra("fromPathAlert", true);
						intent.putExtra("isStart", false);
						startActivityForResult(intent, IntentDefine.FAVORIATE_RESULT);						
					}else if(position ==1){//일정에서 선택: 구글 캘린더 연동						
						Intent intent = new  Intent(getApplicationContext(), MyEventActivity.class);
						startActivityForResult(intent, IntentDefine.EVENT_RESULT);
					}else if(position ==2){//지도에서 선택
						Intent intent = new  Intent(getApplicationContext(), MapViewActivity.class);
						startActivityForResult(intent, IntentDefine.MAP_RESULT);
					}
					end_loc_dialog.dismiss();					
					
				}		
					
			});
			end_loc_dialog.show();
			break;	
		case R.id.home_bt: //집으로
			
			String destinationHome = PreferenceUtil.getHomeAsMyDestination(this);
			if(destinationHome.equals("")){
				Toast.makeText(this,R.string.setting_required, Toast.LENGTH_LONG).show();				
			}else{
				String [] info = destinationHome.split(";");
				setup();//출발지가 현재 위치가 아닌 경우 대응
				startSearchImmediate(getString(R.string.current_location), info[0],  mStartLongitude, mStartLatitude, info[1], info[2]);
			}
			
			break;
			
		case R.id.company_bt: //회사로
			String destinationWork = PreferenceUtil.getWorkPlaceAsMyDestination(this);
			if(destinationWork.equals("")){
				Toast.makeText(this,R.string.setting_required, Toast.LENGTH_LONG).show();				
			}else{
				String [] info = destinationWork.split(";");
				setup();//출발지가 현재 위치가 아닌 경우 대응
				startSearchImmediate(getString(R.string.current_location), info[0],  mStartLongitude, mStartLatitude, info[1], info[2]);
			}
			break;
			
		case R.id.search: //경로 찾기	
			startSearch();
			break; 
		case R.id.button: //목적지로(멤버변수 세팅 없이 바로 검색) 
			//출발지는 현재 위치 목적지는 선택된것			
			EntityMovePath entity = mMovePathList.get((Integer)v.getTag());
			String loc [] = entity.path.split(">");
			startSearchImmediate(loc[0], loc[1], entity.startLon, entity.startLat , entity.destinationLon , entity.destinationLat );
			break;

		default:
			break;
		}
		
	}
	/**
	 * 목적지로, 집으로, 홈으로 선택시 저장된 좌표로 바로 이동 Method
	 */
	private void startSearchImmediate(String startLocation, String destination, String startLon, String startLat, String endLon, String endLat){
		
		mInputMethodManager.hideSoftInputFromWindow(mDestinationTextView.getWindowToken(), 0);
		
		Intent intent  = new Intent(getApplicationContext(), SearchPathActivity.class);			
		//출발지 검색 추가 //reverse reverse geocoding 
		intent.putExtra("start", startLocation);
		intent.putExtra("destination", destination);	
		intent.putExtra("rsdStartXPos", startLon);
		intent.putExtra("rsdStartYPos", startLat);
		intent.putExtra("rsdEndXPos", endLon);
		intent.putExtra("rsdEndYPos", endLat);
		startActivity(intent);
		
		
	}

	/**
	 * @설명 : T map POI 통합검색
	 * @RequestURI : https://apis.skplanetx.com/tmap/pois
	 */
	private void searchTotalStart(String keyWord){
		//Querystring Parameters	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", "10");//요청 시 받을 검색 목록의 개수입니다
		map.put("searchKeyword", keyWord);//검색어입니다
		map.put("resCoordType", "WGS84GEO"); //받고자 하는 응답 좌표계 유형을 지정합니다
		//Bundle 설정
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.TMAP_TOTAL_SEARCH_URI, map, false);

		try{
			//API 호출
			AsyncRequester.request(this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityTotalSearch(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					ArrayList<EntityTotalSearch> array = (ArrayList<EntityTotalSearch>)entityArray;
					EntityAutoComplete entity = null;
					
					if(mAutoCompleteStartList.size()>0){
						mAutoCompleteStartList.clear();
					}
					for (int i = 0; i < array.size(); i++) {
						entity = new EntityAutoComplete();
						entity.mName = array.get(i).name;
						entity.x = String.valueOf(array.get(i).frontLon);
						entity.y = String.valueOf(array.get(i).frontLat);
						mAutoCompleteStartList.add(entity);
					}
				       mSearchProgressBarStart.setVisibility(View.GONE);
					   mAutoCompleteStartAdapter = new Adapter_AutoComplete(getApplicationContext(), R.layout.list_item_auto_complete, mAutoCompleteStartList);
				       
				       mStartLocationTextView.setAdapter(mAutoCompleteStartAdapter);
				       mStartLocationTextView.showDropDown();			       
				       //mIsAutoSearched = false; //자동 완성 재 검색시
				}
				
			}), false, false);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			if(mSearchProgressBarDestination!=null)
				mSearchProgressBarDestination.setVisibility(View.GONE);
		}	
		
	}

	/**
	 * @설명 : T map POI 통합검색
	 * @RequestURI : https://apis.skplanetx.com/tmap/pois
	 */
	private void searchTotalDestination(String keyWord){
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", "10");//요청 시 받을 검색 목록의 개수입니다
		map.put("searchKeyword", keyWord);//검색어입니다
		map.put("resCoordType", "WGS84GEO"); //받고자 하는 응답 좌표계 유형을 지정합니다
		//Bundle 설정
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.TMAP_TOTAL_SEARCH_URI, map, false);

		try{
			//API 호출
			AsyncRequester.request(this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityTotalSearch(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					ArrayList<EntityTotalSearch> array = (ArrayList<EntityTotalSearch>)entityArray;
					EntityAutoComplete entity = null;
					
					if(mAutoCompleteDestinationList.size()>0){
						mAutoCompleteDestinationList.clear();
					}
					for (int i = 0; i < array.size(); i++) {
						entity = new EntityAutoComplete();
						entity.mName = array.get(i).name;
						entity.x = String.valueOf(array.get(i).frontLon);
						entity.y = String.valueOf(array.get(i).frontLat);
						//mAutoCompleteAdapter.add(entity);
						mAutoCompleteDestinationList.add(entity);
					}
					mSearchProgressBarDestination.setVisibility(View.GONE);
					   mAutoCompleteDestinationAdapter = new Adapter_AutoComplete(getApplicationContext(), R.layout.list_item_auto_complete, mAutoCompleteDestinationList);
				       
				       mDestinationTextView.setAdapter(mAutoCompleteDestinationAdapter);
				       mDestinationTextView.showDropDown();			       
				       mIsAutoSearched = false; //자동 완성 재 검색시
				}
				
			}), false, false);
		} catch (CloneNotSupportedException e) {
			//mHandler.sendEmptyMessage(DIALOG_DISMISS);
			e.printStackTrace();
			if(mSearchProgressBarDestination!=null)
				mSearchProgressBarDestination.setVisibility(View.GONE);
		}	
		
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(resultCode == RESULT_OK){ //경로 탐색 페이지로 이동
    		//탐색중
    		
    		switch (requestCode) {
    		
			case IntentDefine.FAVORIATE_RESULT:			
				String result = data.getStringExtra("choice");
				String [] favorite = result.split(";");
			      
				if(data.getBooleanExtra("isStart", false)){
					mStartLocationTextView.setText(favorite[0]);
					mStartLocationTextView.setSelection(mStartLocationTextView.getText().length());	
					//즐겨 찾기시 출발점 좌표도 세팅해야함.					
					mStartLongitude  = favorite [1];
					mStartLatitude = favorite [2];
				}else{
					mDestinationTextView.setText(favorite[0]);
					mDestinationTextView.setSelection(mDestinationTextView.getText().length());	
					//즐겨 찾기시 도착점 좌표도 세팅해야함.	
					mDestinationLongitude = favorite[1];
					mDestinationLatitude = favorite[2];
				}				
				break;
			case IntentDefine.EVENT_RESULT:
				mDestinationTextView.setText("");
				mDestinationTextView.setText(data.getStringExtra("choice"));
				break;
			case IntentDefine.MAP_RESULT:
				
				break;
			default:
				break;
			}   
    		
    	} 	
    	
    }
    
    //option menu    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		Intent intent = null;
		
		switch (item.getItemId()) {

		case R.id.phone_address:
			intent = new Intent(this, AddressCheckBoxActivity.class);	
			startActivity(intent);
		break;		  
		case R.id.storage_box:
			intent = new Intent(this, StorageActivity.class);
			startActivity(intent);
		break;			
		case R.id.setting:
			intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
		break;
	    //알람 멈춤
		case R.id.stop_sending_msg:
			ServiceUtil.stopMsgService(this);	
			Toast.makeText(this, R.string.sending_msg_stop, Toast.LENGTH_LONG).show();			

		break;
		default:
			break;
		}		
		
		return true;
	}	
}
