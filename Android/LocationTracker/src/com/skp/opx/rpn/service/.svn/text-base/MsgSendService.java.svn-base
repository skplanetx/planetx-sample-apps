package com.skp.opx.rpn.service;

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

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.skp.opx.rpn.R;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.skp.openplatform.android.sdk.oauth.Constants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.openplatform.android.sdk.oauth.SKPOPException;
import com.skp.opx.core.client.Define;
import com.skp.opx.rpn.database.DaoDesignatedContactBox;
import com.skp.opx.rpn.database.DaoRealTImePathBox;
import com.skp.opx.rpn.database.DaoSearchedPathBox;
import com.skp.opx.rpn.database.DaoSendBox;
import com.skp.opx.rpn.database.EntityDesignatedContactBox;
import com.skp.opx.rpn.database.EntityRealTimePathBox;
import com.skp.opx.rpn.database.EntitySearchdPathBox;
import com.skp.opx.rpn.database.EntitySendBox;
import com.skp.opx.rpn.sms.SMSSender;
import com.skp.opx.rpn.util.ConvertUnitUtil;
import com.skp.opx.rpn.util.CoordinateUtil;
import com.skp.opx.rpn.util.PreferenceUtil;

/**
 * @설명 : 메시지 발신 서비스
 * @클래스명 : MsgSendService 
 *
 */
public class MsgSendService extends Service {

	private String mRemainTime= "";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);	
		//예상 소요 시간 : 현재 위치에서 목적지까지 소요 시간 요청함
		requestRemainTime();
	}
	
	/**
	 * sms 보내기
	 */
	private void sendSMS(String startLocation, String destination, String remainTime, String message, String sendMsg){
		//1, 주소록 sms 전송				
		DaoDesignatedContactBox daoDesignatedContactBox = DaoDesignatedContactBox.getInstance();
		ArrayList<EntityDesignatedContactBox> contactList = daoDesignatedContactBox.getDesignatedContactInfoList(this);		
		//발신함
		DaoSendBox daoSendBox = DaoSendBox.getInstance();		
		EntitySendBox sendBoxEntity = null;
		
		for (int i = 0; i < contactList.size(); i++) {
			SMSSender.SendSmsMessage(this, contactList.get(i).mContact, sendMsg);
			//발신함 DB에 저장함 
			sendBoxEntity = new EntitySendBox();
			sendBoxEntity.mMdn = contactList.get(i).mContact;
			sendBoxEntity.mReceiver = contactList.get(i).mName;			
			sendBoxEntity.mStartLocation = startLocation;
			sendBoxEntity.mDestnationLocation = destination;
			sendBoxEntity.mExpectionArrivedTime = remainTime;
			sendBoxEntity.mMessage = message;
			sendBoxEntity.mMessageType =0;
			sendBoxEntity.mDeliveryTime = System.currentTimeMillis();
			
			try {
				daoSendBox.insertSendInfo(this, sendBoxEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
 
	/**
	 * @설명 : 네이트온 쪽지 발송
	 * @RequestURI : https://apis.skplanetx.com/nateon/notes
	 */
	private void sendNateMsg(String startLocation, String destination, String remainTime, String message, String sendMsg){
		DaoDesignatedContactBox dao = DaoDesignatedContactBox.getInstance();
		ArrayList<EntityDesignatedContactBox> nateFriendList = dao.getDesignatedNateContactInfoList(this);
		StringBuilder receivers = new StringBuilder();		

		
		for (int i = 0; i < nateFriendList.size(); i++) {
			
			if(i == nateFriendList.size()-1){ //마지막에는 콤마 넣지 않음.
				receivers.append(nateFriendList.get(i).mContact); 
			}else{
				receivers.append(nateFriendList.get(i).mContact + ";");
			}
			
		}
		//Querystring Parameters
		HashMap param = new HashMap<String, Object>();
		param.put("version", "1");//API의 버전 정보입니다
		param.put("receivers", receivers.toString());//쪽지를 수신할 사용자의 ID를 입력합니다
		param.put("message", sendMsg);//쪽지 내용입니다
		//Bundle 설정
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setUrl("https://apis.skplanetx.com/nateon/notes");
		requestBundle.setParameters(param);
		requestBundle.setHttpMethod(HttpMethod.POST);
		requestBundle.setResponseType(CONTENT_TYPE.JSON);
		APIRequest api = new APIRequest();
		try {
			//API 호출
			api.request(requestBundle, new RequestListener() {
				
				@Override
				public void onSKPOPException(SKPOPException e) {
					// TODO Auto-generated method stub
					Log.d("test", "onSKPOPException");
					
					//ErrorMessage.showErrorDialog(getApplicationContext(), e.getMessage());
				}
				
				@Override
				public void onMalformedURLException(MalformedURLException e) {
					// TODO Auto-generated method stub
					Log.d("test", "onMalformedURLException");
					//ErrorMessage.showErrorDialog(getApplicationContext(), e.getMessage());
				}
				
				@Override
				public void onIOException(IOException e) {
					// TODO Auto-generated method stub
					Log.d("test", "onIOException");
					//ErrorMessage.showErrorDialog(getApplicationContext(), e.getMessage());
				}

				@Override
				public void onComplete(ResponseMessage arg0) {
					
					Log.d("test", "onComplete");
				}
			});			
			
		} catch (SKPOPException e) {
			e.printStackTrace();
		}	
			// 쪽지 전송후 발신함 디비 저장
			DaoSendBox daoSendBox = DaoSendBox.getInstance();	
			EntitySendBox sendBoxEntity= null;
			
			for (int i = 0; i < nateFriendList.size(); i++) {
				sendBoxEntity = new EntitySendBox();
				sendBoxEntity.mMdn = nateFriendList.get(i).mContact;
				sendBoxEntity.mReceiver = nateFriendList.get(i).mName;	
				sendBoxEntity.mStartLocation = startLocation;
				sendBoxEntity.mDestnationLocation = destination;
				sendBoxEntity.mExpectionArrivedTime = remainTime;
				sendBoxEntity.mMessage = message;
				sendBoxEntity.mMessageType =1;
				sendBoxEntity.mDeliveryTime = System.currentTimeMillis();
				try {
					daoSendBox.insertSendInfo(this, sendBoxEntity);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	}
	private static Location mCurrentLocation = null; 
	
    private final LocationListener listener = new LocationListener() {

        public void onLocationChanged(Location location) {
        //리스너를 통해서 이동중 계속 현재 위치를 받는다. receiver 를 통해서도 받을 수 있도록 설계.
        //여기서 현재 위치로 등록 할 것인가에 대한 판단을 한다. 	
        	//Toast.makeText(getApplicationContext(), "location is updated", Toast.LENGTH_LONG).show();
        	if (location != null && CoordinateUtil.isBetterLocation(location, mCurrentLocation)){
        		mCurrentLocation = location;
        		//Toast.makeText(getApplicationContext(), "this is best location!!!!", Toast.LENGTH_LONG).show();        		
        	}
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    
	/**
	 * 도착 까지 남은 시간 요청(현재 좌표에서 도착지 좌표까지 경로 검색 하여 소요 시간 체크)
	 * @return
	 */
	private void requestRemainTime (){	
		
		if(PreferenceUtil.getSendingMsgMode(this)){
		//1. 현재 위치에서 도착 점까지 남은 시간 구하고 전송	//핸들러로 변경해도 무방.			
			Thread msgSendThread = new Thread(new Runnable() {			
				@Override
				public void run() {
					Looper.prepare();
					// TODO Auto-generated method stub					
					//1. 현재 좌표 구한다 (location manager)
					CoordinateUtil.getInstance(getApplicationContext());//location manager 객체 얻고...
					Location newLocation = CoordinateUtil.requestLocationUpdate(listener); //현재 위치
					// best location 판단
					if(newLocation != null && CoordinateUtil.isBetterLocation(newLocation, mCurrentLocation)){
						mCurrentLocation = newLocation;
					}
					mCurrentLocation = CoordinateUtil.requestLocationUpdate(listener); //현재 위치 
					try {
						findRemainTime(String.valueOf(mCurrentLocation.getLongitude()),String.valueOf(mCurrentLocation.getLatitude()));//현재 위치로 남은 시간 검색							
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					
					Looper.loop();	
				}
			});
			msgSendThread.start();
		
		//2. 현재 위치 이름 구하고 저장 > 경로 이동함에 실시간 이동 데이터를 저장으로 변경.
			Thread currentPostionSaveThread = new Thread(new Runnable() {			
				@Override
				public void run() {
					Looper.prepare();
					// TODO Auto-generated method stub					
					//1. 현재 좌표 구한다 (location manager)
					CoordinateUtil.getInstance(getApplicationContext());//location manager 객체 얻고...
					mCurrentLocation = CoordinateUtil.requestLocationUpdate(listener); //현재 위치 
					//findRemainTime(String.valueOf(location.getLongitude()),String.valueOf(location.getLatitude()));//현재 위치로 남은 시간 검색	
					//현재 위치 이름구하기
					findPositioName(String.valueOf(mCurrentLocation.getLongitude()),String.valueOf(mCurrentLocation.getLatitude()));
					Looper.loop();	
				}
			});
			currentPostionSaveThread.start();			
		}			
	}
	/**
	 * 현재 위치 이름 찾기 
	 * @param currutnLon
	 * @param currentLat
	 */
	private void findPositioName(final String currentLon, final String currentLat){
		CoordinateUtil.getInstance(getApplicationContext());
		CoordinateUtil.getLocatinNameFromCoordinate( currentLon, currentLat, new RequestListener() {
			
			@Override
			public void onSKPOPException(SKPOPException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMalformedURLException(MalformedURLException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onIOException(IOException arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onComplete(ResponseMessage result) {
				
				//정상적으로 현재 위치 이름 검색되면 저장 
				try {					
					JSONObject thedata = new JSONObject(result.getResultMessage());
					JSONObject object = thedata.getJSONObject("addressInfo");
					String fullAddress = object.getString("fullAddress") ;
					saveCurrentLocationName(fullAddress, currentLon, currentLat);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 이동 경로함 을 위해 디비 저장 
	 */
    private void saveCurrentLocationName(String fullAddress, String currentLon, String currentLat){
    	
    	try {
    		//이동 경로 검색한 디비에서 출발지, 목적지 가져옴.
    		DaoSearchedPathBox searchPathDao = DaoSearchedPathBox.getInstance();    		
    		ArrayList<EntitySearchdPathBox> list = searchPathDao.getLatestSearchedPathInfoList(getApplicationContext());
    		EntitySearchdPathBox entity = list.get(0);

			//실시간 이동 경로 디비에 인서트 (한 경로 서치에 대해 하나로 묶을수 있는 것 필요)
			DaoRealTImePathBox realTimePathDao = DaoRealTImePathBox.getInstance();
			EntityRealTimePathBox realTimeEntity= new EntityRealTimePathBox();
			realTimeEntity.mStartLocation = entity.mStartLocation;
            realTimeEntity.mDestination = entity.mDestination;
            realTimeEntity.mTotalDistance = entity.mTotalDistance;
            realTimeEntity.mName = fullAddress;
            realTimeEntity.mStartLat = entity.mStartLat;
            realTimeEntity.mStartLon = entity.mStartLon;
            realTimeEntity.mDestinationLat = entity.mDestinationLat;
            realTimeEntity.mDestinatioLon = entity.mDestinatioLon;
            realTimeEntity.mCurrentLon = currentLon;
            realTimeEntity.mCurrentLat = currentLat;
            realTimeEntity.mTime = System.currentTimeMillis();
            realTimeEntity.mAlarmStartTime = Define.ALARM_START_TIME;
			realTimePathDao.insertRealTimePathInfo(getApplicationContext(), realTimeEntity);
			//리스트 업데이트
			Intent intent = new Intent("com.skp.opx.rpn.update");
			sendBroadcast(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    }
	/**
	 * @설명 : T map 자동차 경로 안내
	 * @RequestURI : https://apis.skplanetx.com/tmap/routes
	 */
	private void findRemainTime(String curruntLon, String currentLat){
	
		//도착지 좌표:  디비에서 가장 최근 목적지(디비 컬럼 생성)
		DaoSearchedPathBox dao = DaoSearchedPathBox.getInstance();
		
		ArrayList<EntitySearchdPathBox> list = dao.getLatestSearchedPathInfoList(getApplicationContext());
		EntitySearchdPathBox entity = list.get(0);
		//Querystring Parameters
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("version", "1");//API의 버전 정보입니다
		param.put("reqCoordType", "WGS84GEO");//출발지, 경유지, 목적지 좌표계 유형을 지정합니다
		param.put("endX", entity.mDestinatioLon);//목적지 X좌표: 경도
		param.put("endY", entity.mDestinationLat);//목적지 Y좌표: 위도
		param.put("startX", curruntLon);//출발지 X좌표: 경도
		param.put("startY", currentLat);//출발지 Y좌표: 위도
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
				public void onSKPOPException(SKPOPException arg0) {
					// TODO Auto-generated method stub
					Log.d("test", "onSKPOPException");
				}
				
				@Override
				public void onMalformedURLException(MalformedURLException arg0) {
					// TODO Auto-generated method stub
					Log.d("test", "onMalformedURLException");
				}
				
				@Override
				public void onIOException(IOException arg0) {
					// TODO Auto-generated method stub
					Log.d("test", "onIOException");
				}

				@Override
				public void onComplete(ResponseMessage result) {
					
					Log.d("test", "onComplete");
					mRemainTime = "";
					String second = startKMLParsing(getStreamFromString(result.getResultMessage()));
					
					mRemainTime = ConvertUnitUtil.convertSecondToProperTime(second);
					//현재 위치도 계산하려면
					//findPositioName(curruntLon, currentLat); 이후 메시지 전송
					//도착 시간을 구했으면 메시지 전송 한다.					 
					makeMsgAndSend();
				}
				
			});			
			
		} catch (SKPOPException e) {
			e.printStackTrace();
		}	
		
	}
	/**
	 * 발신 메시지 만들고 메시지 전송한다. 
	 */
	public void makeMsgAndSend(){
		
		String startLocation = "";
		String destination = "";
		
		try{
			DaoSearchedPathBox dao = DaoSearchedPathBox.getInstance();
			ArrayList<EntitySearchdPathBox> list = dao.getLatestSearchedPathInfoList(getApplicationContext()); //경로 찾은 날짜가 기준.(경로 포인트들 정보)
			EntitySearchdPathBox entity = list.get(0);
			startLocation =  entity.mStartLocation;		
			destination = entity.mDestination;
		
			String message =  PreferenceManager.getDefaultSharedPreferences(this).getString(this.getString(R.string.message_text_write), "");
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(getString(R.string.send_msg_title) + "\n");
			sendMsg.append(getString(R.string.send_msg_starting_point) + "\n");
			sendMsg.append(startLocation + "\n");
			sendMsg.append(getString(R.string.send_msg_destination) + "\n");
			sendMsg.append(destination + "\n");
			sendMsg.append(getString(R.string.send_msg_ETE) + "\n");
			sendMsg.append(mRemainTime +"\n");
			if(!message.equals("")){
				sendMsg.append(getString(R.string.send_msg_messaging) + "\n");
				sendMsg.append(message);
			}			
		
		//주소록인지 네이트 친구 인지 구분하여 전송해야 한다...정해진 시간 간격에 따라 sms, 네이트 쪽지 전송	
		sendSMS(startLocation, destination, mRemainTime, message, sendMsg.toString());
		sendNateMsg(startLocation, destination, mRemainTime, message, sendMsg.toString());
		
		} catch (Exception e) {
			// TODO: handle exception
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
	
	private String startKMLParsing(InputStream stream){
		
		String tagName = "";
        String remainTime = "";
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
					if(tagName.equals("tmap:totalTime")){ //총 소요시간: 초단위
							remainTime = parser.getText();
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					tagName = parser.getName();  
					tagName = "";
				}
				eventType = parser.next();
			}	    
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
		return remainTime;
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(mCurrentLocation != null){
			mCurrentLocation = null;
		}
		super.onDestroy();
	}
	
	
}
