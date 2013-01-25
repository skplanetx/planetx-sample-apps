package com.skp.opx.svc.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.skp.openplatform.android.sdk.oauth.SKPOPException;
import com.skp.openplatform.android.sdk.oauth.Constants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sdk.PopupDialogUtil;
import com.skp.opx.svc.R;
import com.skp.opx.svc.constants.Defines;
import com.skp.opx.svc.entity.EntityTmapTotalSearch;
import com.skp.opx.svc.ui.adapter.Adapter_TmapPathSearch;
import com.skp.opx.svc.utils.ConvertUnitUtil;
import com.skp.opx.svc.utils.PreferenceUtil;

/**
 * @설명 : Tmap Main 화면 노출 설정 Activity
 * @클래스명 : MenuSettingTmapActivity
 * 
 */
public class MenuSettingTmapActivity  extends ListActivity implements android.view.View.OnClickListener, OnEditorActionListener{

	private Adapter_TmapPathSearch mTotalSearchAdapter;
	private ArrayList<EntityTmapTotalSearch> mSearchArray;

	private EditText mStartEt;
	private EditText mDestinationEt;
	private Button  mConfirmBt;
	private Button  mCencelBt;
	private Button  mSearchBt;
	private Button  mStartCleanBt;
	private Button  mDestinationBt;

	private InputMethodManager mInputMethodManager;
	
	private SharedPreferences.Editor editor;

	private String rsdStartXPos = "";	//출발지 X	
	private String rsdStartYPos = "";	//출발지 Y
	private String rsdEndXPos = "";	//도착지 X
	private String rsdEndYPos = "";	//도착지 Y
	private String nameKey= "";	
	private String name= "";
	
	private String mRsdTotLen     = ""; //총길이
	private String mRsdTotDtm     = ""; //소요시간	
	
	private final int FINDPATH = 0;
	private final int SHOWDETAILPATH = 1;

	@Override
	protected void onResume() {
		super.onResume();		

	}   

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tmap_path_save);

		initWidgets(); 
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);		
		findViewById(R.id.home_bt).setOnClickListener(this);
		
		mStartEt = (EditText)findViewById(R.id.start_location);
		mDestinationEt = (EditText)findViewById(R.id.destination_location);
		
		mSearchBt = (Button)findViewById(R.id.search);
		mConfirmBt = (Button)findViewById(R.id.confirm_bt);
		mCencelBt = (Button)findViewById(R.id.cencel_bt);
		mStartCleanBt = (Button)findViewById(R.id.start_clean_bt);
		mDestinationBt = (Button)findViewById(R.id.desn_clean_bt);
		
		mStartEt.setOnEditorActionListener(this);
		mDestinationEt.setOnEditorActionListener(this);
		
		mSearchBt.setOnClickListener(this);
		mConfirmBt.setOnClickListener(this);
		mCencelBt.setOnClickListener(this);
		mStartCleanBt.setOnClickListener(this);
		mDestinationBt.setOnClickListener(this);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MenuSettingTmapActivity.this);
		editor = prefs.edit();
		
		mTotalSearchAdapter = new Adapter_TmapPathSearch(this);
	}

	/**
	 * @설명 : T map POI 통합검색
	 * @RequestURI : https://apis.skplanetx.com/tmap/pois
	 *              
	 */
	private void initSearchPathAdpaterList(final String keyword) {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchKeyword", keyword);   //검색어입니다
		map.put("resCoordType", "WGS84GEO"); //받고자 하는 응답 좌표계 유형을 지정합니다

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.TMAP_TOTAL_SEARCH_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MenuSettingTmapActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityTmapTotalSearch(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mSearchArray = (ArrayList<EntityTmapTotalSearch>)entityArray;
					mTotalSearchAdapter.setTotalSearchList(mSearchArray);
					setListAdapter(mTotalSearchAdapter);
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.home_bt :
			home();
			break;
		case R.id.confirm_bt :
			if( !mStartEt.isEnabled() && !mDestinationEt.isEnabled() ){
				mHandler.sendEmptyMessage(FINDPATH);
			}else{
				if( mStartEt.isEnabled() ){
					showToast(R.string.tmap_not_set_start_location);
				}else{
					showToast(R.string.tmap_not_set_destnation_location);
				}
			}
			break;
		case R.id.cencel_bt :
			home();
			break;
		case R.id.search :
			if( mStartEt.getText().length() != 0 && mStartEt.isFocused()){//출발지 에디터에 포커스가 있을 경우
				initSearchPathAdpaterList(mStartEt.getText().toString());
				mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}else if(mDestinationEt.getText().length() != 0 && mDestinationEt.isFocused()) {//목적지 에디터에 포커스가 있을 경우
				initSearchPathAdpaterList(mDestinationEt.getText().toString());
				mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}else{ 
				showToast(R.string.search_keyword_null);
			}
			break;
		case R.id.start_clean_bt :
			setStart(false);
			break;
		case R.id.desn_clean_bt :
			setDesn(false);
			break;
		}
	} 
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == FINDPATH){   //경로 찾기
				getRouteByCar();
			}else if(msg.what == SHOWDETAILPATH){
				long len = Long.parseLong(mRsdTotLen);
				PreferenceUtil.putSharedPreference(MenuSettingTmapActivity.this, 
						"mDistanceTv" , getString(R.string.until_arrived) + ConvertUnitUtil.convertMeterToKiroMeter(String.valueOf(len)));
				PreferenceUtil.putSharedPreference(MenuSettingTmapActivity.this, 
						"mTimeTv", getString(R.string.for_time) + ConvertUnitUtil.convertSecondToProperTime(mRsdTotDtm) );
				home();
			}
			super.handleMessage(msg);
		}
	};
	
	/**
	 * @설명 : show toast
	 */
	private void showToast(int title) {
		Toast.makeText(MenuSettingTmapActivity.this, title, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @설명 : 출발지 에디터 텍스트창 설정
	 */
	private void setStart(boolean flg) {
		mStartEt.setText(flg ? name : "");
		mStartEt.setEnabled(flg ? false : true);
		mStartCleanBt.setVisibility(flg ? View.VISIBLE : View.GONE);
	}

	/**
	 * @설명 : 목적지 에디터 텍스트창 설정
	 */
	private void setDesn(boolean flg) {
		mDestinationEt.setText(flg ? name : "");
		mDestinationEt.setEnabled(flg ? false : true);
		mDestinationBt.setVisibility(flg ? View.VISIBLE : View.GONE);
	}

	/**
	 * @설명 : 메인 Activity로 이동
	 */
	private void home() {
		Intent intent_home = new Intent(MenuSettingTmapActivity.this, MainActivity.class );
		intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
		finish();
		startActivity( intent_home );
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		name = mSearchArray.get(position).name;
		if(mStartEt.isFocused()){
			rsdStartXPos = mSearchArray.get(position).frontLon;
			rsdStartYPos = mSearchArray.get(position).frontLat;
			nameKey = Defines.TMAP_SETTING__START_KEYWORD;
		}else if(mDestinationEt.isFocused()){
			rsdEndXPos = mSearchArray.get(position).frontLon;
			rsdEndYPos = mSearchArray.get(position).frontLat;
			nameKey = Defines.TMAP_SETTING_DESN_KEYWORD;
		}
		

		int title = -1;

		title = (mStartEt.isFocused() ? R.string.tmap_start_location_settings : R.string.tmap_destnation_location_settings);
		
		PopupDialogUtil.showConfirmDialog(MenuSettingTmapActivity.this, R.string.tmap, title, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(mStartEt.isFocused()){
					editor.putString("rsdStartXPos", rsdStartXPos);
					editor.putString("rsdStartYPos", rsdStartYPos);
					setStart(true);
				}else if(mDestinationEt.isFocused()){
					editor.putString("rsdEndXPos", rsdEndXPos);
					editor.putString("rsdEndYPos", rsdEndYPos);
					setDesn(true);
				}
				editor.putString(nameKey, name);
			}
		});
	}
	

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_SEARCH){
			if( v.length() == 0 ) {
				showToast(R.string.search_keyword_null);
				return true;
			}else{ 
				initSearchPathAdpaterList(v.getText().toString());
				mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @설명 : T map 자동차 경로안내
	 * @RequestURI : https://apis.skplanetx.com/tmap/routes
	 *              
	 */
	private void getRouteByCar(){				
		
		//Querystring Parameters
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("version", "1");
		param.put("reqCoordType", "WGS84GEO");  //받고자 하는 응답 좌표계 유형을 지정합니다
		param.put("endX", rsdStartXPos);//목적지 X좌표: 경도
		param.put("endY", rsdStartYPos);//목적지 Y좌표: 위도
		param.put("startX", rsdEndXPos);//출발지 X좌표: 경도
		param.put("startY", rsdEndYPos);//목적지 Y좌표: 위도
		
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setUrl(Define.TMAP_ROUTES_SEARCH_URI);
		requestBundle.setParameters(param);
		requestBundle.setHttpMethod(HttpMethod.POST);
		requestBundle.setResponseType(CONTENT_TYPE.KML);
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, new RequestListener() {

				@Override
				public void onSKPOPException(SKPOPException arg0) {
				}

				@Override
				public void onMalformedURLException(MalformedURLException arg0) {
				}

				@Override
				public void onIOException(IOException arg0) {
				}

				@Override
				public void onComplete(ResponseMessage result) {
					startKMLParsing(getStreamFromString(result.getResultMessage()));
				}

			});			

		} catch (SKPOPException e) {
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
	
	/**
	 * KML Parsing...
	 * */
	private void startKMLParsing(InputStream stream){
		
		String tagName     = "";

		try {
			XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();
			parser.setInput(stream, "utf-8"); // Data GET START

			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) {

					tagName = parser.getName();

				} else if (eventType == XmlPullParser.TEXT) {

					if (tagName.equals("tmap:totalDistance")) { //도착지까지 총 이동거리
						mRsdTotLen = parser.getText();
					}else if(tagName.equals("tmap:totalTime")){ //도착지까지 총 소요시간 (초)
						mRsdTotDtm = parser.getText();
					}
				} else if (eventType == XmlPullParser.END_TAG) {

					tagName = ""; //테그 네임 초기화
				}
				eventType = parser.next();
			}
			editor.commit();
			mHandler.sendEmptyMessage(SHOWDETAILPATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
