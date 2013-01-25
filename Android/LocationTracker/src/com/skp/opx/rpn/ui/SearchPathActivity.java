package com.skp.opx.rpn.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.skp.openplatform.android.sdk.oauth.Constants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.openplatform.android.sdk.oauth.SKPOPException;
import com.skp.opx.core.client.Define;
import com.skp.opx.rpn.R;
import com.skp.opx.rpn.database.DaoDesignatedContactBox;
import com.skp.opx.rpn.database.DaoFavoriteBox;
import com.skp.opx.rpn.database.DaoSearchedPathBox;
import com.skp.opx.rpn.database.EntityFavoriteBox;
import com.skp.opx.rpn.database.EntitySearchdPathBox;
import com.skp.opx.rpn.entity.EntityMovePathDetail;
import com.skp.opx.rpn.ui.adapter.Adapter_MovePathDetail;
import com.skp.opx.rpn.ui.dialog.GeneralAlertDialog;
import com.skp.opx.rpn.util.ApproximaAlert;
import com.skp.opx.rpn.util.ConvertUnitUtil;
import com.skp.opx.rpn.util.CoordinateUtil;
import com.skp.opx.rpn.util.PreferenceUtil;
import com.skp.opx.rpn.util.RpnAlarmManager;
import com.skp.opx.sdk.ErrorMessage;

/**
 * @설명 : 탐색된 경로 Activity
 * @클래스명 : SearchPathActivity 
 *
 */
public class SearchPathActivity extends ListActivity implements android.view.View.OnClickListener{

	private Button mStartButton;
	private Button mContactSettingButton; //연락처 설정
	private Button mFavoriteButton;//즐겨 찾기
	private ProgressDialog mProgressDialog;
	private TextView mStartLocation;
	private TextView mDestination;
	private TextView mDistance; //총 거리
	private TextView mTime; //예상 소요 시간
	private TMapView mTmapview; //TMap
	private String rsdTotLen = ""; //총길이
	private String rsdTotDtm  = "";//소요시간		
	private ArrayList<String> mNameList = new ArrayList<String>(); //point name
	private ArrayList<String> mTuryTypeList = new ArrayList<String>(); 
	private ArrayList<String> mDistanceList = new ArrayList<String>(); 
	private ArrayList<String> mDescriptionList = new ArrayList<String>();
	
	private String mRsdStartXPos = ""; //출발점 경도
	private String mRsdStartYPos = "";//출발점 위도
	private String mRsdEndXPos = "";//도착점 경도
	private String mRsdEndYPos = "";//출발점 위도
	private long mSearchedTime = 0; //경로 구분 아이디 역할 
	
	private final int FINDPATH = 0;
	private final int SHOWDETAILPATH = 1;
	private final int SAVEPATH = 2;
	private final int FIND_START_LOCATION_NAME = 3;
	
	//상세 경로 리스트
	private Adapter_MovePathDetail mMovePathDetailAdapter;
	private ArrayList<EntityMovePathDetail> mMovePathDetailList = new ArrayList<EntityMovePathDetail>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());       
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
        
        initwidget();
        
        // 출발지가 현재 위치일 경우 revserse geocoding 하고 경로 요청  
        if(getIntent().getStringExtra("start").equals(getString(R.string.current_location))){
           setStartLocation(mRsdStartXPos, mRsdStartYPos);  	
        }else{        	 
        	mHandler.sendEmptyMessage(FINDPATH); //경로 요청     
        	mStartLocation.setText(getIntent().getStringExtra("start"));
        }
    }
    
    private void initwidget() {
    	
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.search_path));
        mProgressDialog.show();
        getIntentData();
        setContentView(R.layout.activity_search_path);       
        mDistance = (TextView)findViewById(R.id.distance);
        mTime = (TextView)findViewById(R.id.will_arrive_time);
        mStartButton = (Button)findViewById(R.id.start_bt);
        mStartButton.setOnClickListener(this);
        mContactSettingButton = (Button)findViewById(R.id.addressSetting_bt);        
        mContactSettingButton.setOnClickListener(this);
        mFavoriteButton = (Button)findViewById(R.id.favorite_bt);
        mFavoriteButton.setOnClickListener(this);
        mDestination =(TextView)findViewById(R.id.destination);
        mDestination.setText(getIntent().getStringExtra("destination").replace(" ", "")); 
        mDestination.setSelected(true);
        mStartLocation = (TextView)findViewById(R.id.start_location);  
        mStartLocation.setSelected(true);
        
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
		mTmapview.setIconVisibility(true);
		mTmapview.setTrackingMode(true);
		linear.addView(mTmapview);
    }
    
    /**
     * TMap 경로탐색 및 표시한다.
     */
	private void findPath() {
		
		TMapData tmapdata = new TMapData();
		mTmapview.setLocationPoint(Double.parseDouble(mRsdStartXPos), Double.parseDouble(mRsdStartYPos));
		mTmapview.setCenterPoint(Double.parseDouble(mRsdStartXPos), Double.parseDouble(mRsdStartYPos));
		TMapPoint startpoint = new TMapPoint(Double.parseDouble(mRsdStartYPos), Double.parseDouble(mRsdStartXPos));
		TMapPoint endpoint = new TMapPoint(Double.parseDouble(mRsdEndYPos), Double.parseDouble(mRsdEndXPos));
		
		try {
			TMapPolyLine polyline = tmapdata.findPathData(startpoint, endpoint);
			mTmapview.addTMapPolyLine("TestID", polyline);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 출발지 현재 위치 이름 검색 revese geo coding 후 경로 검색 시작
     */
    private void setStartLocation(String lon, String lat){
    	CoordinateUtil.getLocatinNameFromCoordinate(lon, lat, new RequestListener() {
			
			@Override
			public void onSKPOPException(SKPOPException e) {
				// TODO Auto-generated method stub
				if(mProgressDialog != null){
					mProgressDialog.dismiss();					
				}
				ErrorMessage.showErrorDialog(SearchPathActivity.this, e.getMessage());
				
			}
			
			@Override
			public void onMalformedURLException(MalformedURLException e) {
				// TODO Auto-generated method stub
				if(mProgressDialog != null){
					mProgressDialog.dismiss();					
				}
				ErrorMessage.showErrorDialog(SearchPathActivity.this, e.getMessage());
			}
			
			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				if(mProgressDialog != null){
					mProgressDialog.dismiss();					
				}
				ErrorMessage.showErrorDialog(SearchPathActivity.this, e.getMessage());
			}

			@Override
			public void onComplete(ResponseMessage result) {
				
				if(result.getStatusCode().equalsIgnoreCase("200") == false && result.getStatusCode().equalsIgnoreCase("201") == false ) {
					ErrorMessage.showErrorDialog(SearchPathActivity.this, result.getResultMessage());
					if(mProgressDialog != null){
						mProgressDialog.dismiss();					
					}
					return;
				}
				//정상적으로 현재 위치 이름 검색되면 세팅 
				try {					
					JSONObject thedata = new JSONObject(result.getResultMessage());
					JSONObject object = thedata.getJSONObject("addressInfo");
					final String fullAddress = object.getString("fullAddress");
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mStartLocation.setText(fullAddress);						
						}
					});
					mHandler.sendEmptyMessage(FINDPATH); //경로 요청      
				} catch (Exception e) {
					// TODO: handle exception
					if(mProgressDialog != null){
						mProgressDialog.dismiss();
					}
					e.printStackTrace();
				}		
			}
		});
    }
    
    private void getIntentData(){
    	//좌표 받아오기
    	Intent intent = getIntent();    	
		mRsdStartXPos = intent.getStringExtra("rsdStartXPos");
		mRsdStartYPos = intent.getStringExtra("rsdStartYPos");
		mRsdEndXPos = intent.getStringExtra("rsdEndXPos");
		mRsdEndYPos = intent.getStringExtra("rsdEndYPos");
    }
    
    private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			if(msg.what == FINDPATH){ //경로 찾기
				//TMap 표시
				findPath();
				//상세표시
				getRouteByCar();
			}else if(msg.what ==SAVEPATH){ //상세 경로 저장
				long len = Long.parseLong(rsdTotLen);
				mDistance.setText(ConvertUnitUtil.convertMeterToKiroMeter(String.valueOf(len))); //미터 
				mTime.setText(ConvertUnitUtil.convertSecondToProperTime(rsdTotDtm));
				mSearchedTime = System.currentTimeMillis(); 							
				// 찾은 경로 디비 저장 > 이동 경로 관리함
				DaoSearchedPathBox daoSearchedPathfBox = DaoSearchedPathBox.getInstance();	
				EntitySearchdPathBox entity= null;
				
				for (int i = 0; i <mNameList.size(); i++) {
					entity = new EntitySearchdPathBox();
					entity.mStartLocation =mStartLocation.getText().toString();
					entity.mDestination = mDestination.getText().toString();
					entity.mTotalTime = mTime.getText().toString();
					entity.mName = mNameList.get(i);
					entity.mTurnType = mTuryTypeList.get(i);
					entity.mDistance = mDistanceList.get(i);
					entity.mTotalDistance = mDistance.getText().toString();
					entity.mTime = "";
					entity.mSpeed = "";
					entity.mSearchedTime = mSearchedTime;	
					entity.mDestinatioLon = mRsdEndXPos;
					entity.mDestinationLat = mRsdEndYPos;
					//출발지 검색 추가
					entity.mStartLon = mRsdStartXPos; 
				    entity.mStartLat = mRsdStartYPos;

					try {
						daoSearchedPathfBox.insertSearchedPathInfo(getApplicationContext(), entity);			
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}			
				
			}else if(msg.what == SHOWDETAILPATH){//상세 경로 보여주기(디비 저장하기 전 보여 줘야 속도 향상)
				
				EntityMovePathDetail entity = null;
					
				for (int i = 0; i < mNameList.size(); i++) {
						entity = new EntityMovePathDetail();
						entity.mName = mNameList.get(i);
						entity.mTurnType = mTuryTypeList.get(i);
						entity.mDistance = mDistanceList.get(i);
						entity.mDescription = mDescriptionList.get(i);
						mMovePathDetailList.add(entity);
				}			
					
				mMovePathDetailAdapter  = new Adapter_MovePathDetail(getApplicationContext());
				mMovePathDetailAdapter.setMovePathDetailList(mMovePathDetailList);
				setListAdapter(mMovePathDetailAdapter);
	
				if(mProgressDialog != null){
					mProgressDialog.dismiss();					
				}
				mHandler.sendEmptyMessage(SAVEPATH);
			}else if(msg.what == FIND_START_LOCATION_NAME){ //현재 위치 revese geo coding				
				mHandler.sendEmptyMessage(FINDPATH); //경로 요청     
			}
			
			super.handleMessage(msg);
		}
	};

	/**
	 * @설명 : T map 자동차 경로 안내
	 * @RequestURI : https://apis.skplanetx.com/tmap/routes
	 */
	private void getRouteByCar(){				
		//Querystring Parameters	
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("version", "1");//API의 버전 정보입니다
		param.put("reqCoordType", "WGS84GEO");//출발지, 경유지, 목적지 좌표계 유형을 지정합니다
		param.put("endX", mRsdEndXPos);//목적지 X좌표: 경도
		param.put("endY", mRsdEndYPos);//목적지 Y좌표: 위도
		param.put("startX", mRsdStartXPos);//출발지 X좌표: 경도
		param.put("startY", mRsdStartYPos);//목적지 Y좌표: 위도
		//Bundle 설정
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setUrl("https://apis.skplanetx.com/tmap/routes");
		requestBundle.setParameters(param);
		requestBundle.setHttpMethod(HttpMethod.POST);
		requestBundle.setResponseType(CONTENT_TYPE.KML);
		APIRequest api = new APIRequest();
		try {
			//API 호출
			api.request(requestBundle, new RequestListener() {
				 
				@Override
				public void onSKPOPException(SKPOPException e) {
					// TODO Auto-generated method stub
					Log.d("test", "onSKPOPException");
					if(mProgressDialog != null){
						mProgressDialog.dismiss();					
					}
					ErrorMessage.showErrorDialog(SearchPathActivity.this, e.getMessage());
				}
				
				@Override
				public void onMalformedURLException(MalformedURLException e) {
					// TODO Auto-generated method stub
					Log.d("test", "onMalformedURLException");
					if(mProgressDialog != null){
						mProgressDialog.dismiss();					
					}
					ErrorMessage.showErrorDialog(SearchPathActivity.this, e.getMessage());
				}
				
				@Override
				public void onIOException(IOException e) {
					// TODO Auto-generated method stub
					Log.d("test", "onIOException");
					if(mProgressDialog != null){
						mProgressDialog.dismiss();					
					}
					ErrorMessage.showErrorDialog(SearchPathActivity.this, e.getMessage());
				}

				@Override
				public void onComplete(ResponseMessage result) {
					if(result.getStatusCode().equalsIgnoreCase("200") == false && result.getStatusCode().equalsIgnoreCase("201") == false ) {
							ErrorMessage.showErrorDialog(SearchPathActivity.this, result.getResultMessage());
							if(mProgressDialog != null){
								mProgressDialog.dismiss();					
							}
					}else{
						startKMLParsing(getStreamFromString(result.getResultMessage()));						
					}
				}
			});			
			
		} catch (SKPOPException e) {
			e.printStackTrace();
			if(mProgressDialog != null){
				mProgressDialog.dismiss();					
			}
		}	

		
	}

	/** 
	 *  자동차 경로 파싱 Method
	 * */
	private void startKMLParsing(InputStream stream){
		
		String tagName = "";

		String nodeType = "";
		String name = ""; //이름
		String turnType = "";
		String distance = "";//구간 거리
		String description = "";  //구간 이동 설명
		
		boolean isPoint = false;
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> turnTypeList = new ArrayList<String>();
		ArrayList<String> distanceList = new ArrayList<String>();
		ArrayList<String> descriptionList = new ArrayList<String>();
		
		try {

			XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();

			parser.setInput(stream, "utf-8"); // 가져오기를 시작하는 곳
 
			int eventType = parser.getEventType();
            ///필요한데이터: 총길이, 총 소요시간:  name, 속도, 시간 , 주행 , 턴 타입
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {

					tagName = parser.getName();

				} else if (eventType == XmlPullParser.TEXT) {

					if (tagName.equals("tmap:totalDistance")) { //총길이
						rsdTotLen = parser.getText();
					}else if(tagName.equals("tmap:totalTime")){ //총 소요시간 //초
						rsdTotDtm = parser.getText();
					}else if(tagName.equals("tmap:nodeType")){ //노드 타입
					    nodeType += parser.getText();
					    if(nodeType.equals("POINT")){
					    	isPoint = true;
					    }else{
					    	isPoint = false;
					    }
					}else if(tagName.equals("description")){ //설명 추가
							description += parser.getText();			
					}else if(tagName.equals("name")){ //이릉(point 일 경우만 사용)
							name += parser.getText();			
					}else if(tagName.equals("tmap:turnType")){//point일 경우만 존재
							turnType+= parser.getText();
					}else if(tagName.equals("tmap:distance")){//line 일경우만 존재
						distance += parser.getText();
					}

				} else if (eventType == XmlPullParser.END_TAG) {

					tagName = parser.getName();
                    
					if (tagName.equals("Placemark") && isPoint) { //포인트 일 경우만 이름 필요

						nameList.add(name);
						name = "";
						
						turnTypeList.add(turnType);
						turnType = "";
						
						descriptionList.add(description);
						description = "";
						
					}else if(tagName.equals("Placemark") && isPoint){//포인트 일 경우만
						
					}else if(tagName.equals("Placemark") && !isPoint){//라인 일 경우만
						distanceList.add(distance);
						distance = "";
					}
					if(tagName.equals("Placemark")){
						name = "";
						turnType = "";
						distance = "";
						nodeType = "";
						description = "";
					}
					tagName = ""; //테그 네임 초기화
				}
				eventType = parser.next();
			}
			nameList.remove(nameList.size()-1); //도착지는 필요한 정보가 없음..
			mNameList = (ArrayList<String>) nameList.clone();
			turnTypeList.remove(turnTypeList.size()-1);//도착지는 턴타입이 필요 없음.
			mTuryTypeList = (ArrayList<String>) turnTypeList.clone();			
			mDistanceList = (ArrayList<String>) distanceList.clone();
			mDescriptionList = (ArrayList<String>) descriptionList.clone();
			
	        //상세 경로 보여주기
			mHandler.sendEmptyMessage(SHOWDETAILPATH);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static InputStream getStreamFromString(String str)
	{
		byte[] bytes = null;
		try {
			bytes = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayInputStream(bytes);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent = null;
		
		switch (v.getId()) {
		
		case R.id.start_bt:
			if (mSearchedTime == 0){
				Toast.makeText(this, "경로를 찾지 못하였습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			//새로 경로 탐색시 이전 에 돌던 알람 매니저 정지 시킴						
			//지정 연락처가 설정 되었는지 확인하고, 미설정시 알림 팝업
			DaoDesignatedContactBox dao = DaoDesignatedContactBox.getInstance();
			int size = dao.getAllDesignatedContactInfoList(this).size();
			
			if(size==0){ //지정 연락처가 없음
				GeneralAlertDialog dialog = new GeneralAlertDialog(this, getResources().getString(R.string.no_designated_contact));
				dialog.setPostiveButton(new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				dialog.show();
			}else{ //시작
				//연속으로 두번 누를시 일어하는 이슈 수정.
				mStartButton.setEnabled(false);
				//경로 찾기 시작한 시간 가지고 있어야함 (디비 인서트시에 group by 기준)
				Define.ALARM_START_TIME = System.currentTimeMillis();
				Toast.makeText(this,getResources().getString(R.string.start_alert), Toast.LENGTH_LONG).show();
				//알람 매니저 시작시 메시지 전송 상태 유지.
				PreferenceUtil.setSendingMsgMode(this, true);
//				//알람 매니저로 문자 , 쪽지 전송하기
				RpnAlarmManager am = RpnAlarmManager.getInstance();
				if (am.isPendingIntentExist()){					
					am.stopAlarm();
				}				
				am.startAlarm(this);
				//목적지 알람 등록
				ApproximaAlert.getInstance().registerLocation(this, Double.parseDouble(mRsdEndXPos),Double.parseDouble(mRsdEndYPos));
				//1초 후 시작 버튼활성화
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mStartButton.setEnabled(true);
					}
				}, 1000);
				
			}

			break;
		case R.id.addressSetting_bt://연락처 설정
			intent = new Intent(this, AddressCheckBoxActivity.class);
			//화면 들어가서 바로 선택 되도록 함.
			
			intent.putExtra("fromPathAlert", true); //체크박스, 아래 확인 버튼식 UI 
			startActivity(intent);
			break;
		case R.id.favorite_bt://즐겨 찾기
			//즐겨 찾기 설정 팝업 
			GeneralAlertDialog inputDialog = new GeneralAlertDialog(SearchPathActivity.this, getResources().getString(R.string.input_favorite_name));
			final EditText text = new EditText(this);
			text.setText(mDestination.getText().toString());
			//목적지가 자동 입력됨			
			inputDialog.setView(text);
			inputDialog.setPostiveButton(new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//1.등록된 장소인지 확인후 토스트 띄움.
					
					//2.새장소일경우 에 저장(목적지 좌표도 있어야함)
					DaoFavoriteBox dao = DaoFavoriteBox.getInstance();
					EntityFavoriteBox entity = new EntityFavoriteBox();
					entity.mName = mDestination.getText().toString();					
					entity.mLon = mRsdEndXPos;
					entity.mLat = mRsdEndYPos;
					try {
						dao.insertFavoriteInfo(getApplicationContext(), entity);
						Toast.makeText(getApplicationContext(), R.string.input_favorite_name_success, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
					
				}
			});
			inputDialog.setNegativeButton(new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			inputDialog.show();
			
			break;

		default:
			break;
		}
	}  
    
    
}
