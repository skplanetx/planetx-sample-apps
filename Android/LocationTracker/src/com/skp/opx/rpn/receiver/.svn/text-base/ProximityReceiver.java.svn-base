package com.skp.opx.rpn.receiver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.skp.opx.rpn.R;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.RequestBundle;
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
import com.skp.opx.rpn.util.ServiceUtil;


/**
 * @설명 : 주변 도착 알림 Receiver
 * @클래스명 : ProximityReceiver 
 *
 */
public class ProximityReceiver extends BroadcastReceiver {
	  private static final int NOTIFICATION_ID = 1000;

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		ServiceUtil.stopMsgService(context);
		// 1. 노티와 함께, 인디케이터에도 표시
		triggerNoti(context);

		// 2. 주변 도착 메시지 발송
		Thread sendMsg = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendArrivaMsg(context);
			}
		});
		sendMsg.start();

		// 3. 발신함에 도착 메시지 저장
		Thread saveMsg = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000); 
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated method stub
				saveArrivalInfo(context);
				
			}
		});
		saveMsg.start();	


	}

	private void triggerNoti(Context context) {

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	null, 0);
		Notification notification = createNotification();
		notification.setLatestEventInfo(context,context.getString(R.string.arrived_near_destination), context.getString(R.string.arrived_near_destination_and_end),pendingIntent);
		notificationManager.notify(NOTIFICATION_ID, notification);

	}
	 /**
	  * 노티피케이션 만들기
	  * @return
	  */
	private Notification createNotification() {
        Notification notification = new Notification();
         
        notification.icon = android.R.drawable.ic_notification_overlay;
        notification.when = System.currentTimeMillis();
         
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
         
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
         
        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;
         
        return notification;
    }

	/**
	 * 도착시 이동 경로함 에 도착 정보 저장
	 * 
	 * @param context
	 */
	private void saveArrivalInfo(Context context) {

		try {
			// 이동 경로 검색한 디비에서 출발지, 목적지 가져옴.
			DaoSearchedPathBox searchPathDao = DaoSearchedPathBox.getInstance();
			ArrayList<EntitySearchdPathBox> list = searchPathDao.getLatestSearchedPathInfoList(context);
			EntitySearchdPathBox entity = list.get(0);
			// 실시간 이동 경로 디비에 인서트 (한 경로 서치에 대해 하나로 묶을수 있는 것 필요)
			DaoRealTImePathBox realTimePathDao = DaoRealTImePathBox.getInstance();
			EntityRealTimePathBox realTimeEntity = new EntityRealTimePathBox();
			realTimeEntity.mStartLocation = entity.mStartLocation;
			realTimeEntity.mDestination = entity.mDestination;
			realTimeEntity.mTotalDistance = entity.mTotalDistance;
			realTimeEntity.mName = context.getString(R.string.near_destination);
            realTimeEntity.mStartLat = entity.mStartLat;
            realTimeEntity.mStartLon = entity.mStartLon;
            realTimeEntity.mDestinationLat = entity.mDestinationLat;
            realTimeEntity.mDestinatioLon = entity.mDestinatioLon;
			realTimeEntity.mTime = System.currentTimeMillis();
			realTimeEntity.mAlarmStartTime = Define.ALARM_START_TIME;
			realTimePathDao.insertRealTimePathInfo(context, realTimeEntity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 주변 도착 알림 종료 메시지
	 * 
	 * @param context
	 */
	private void sendArrivaMsg(Context context) {
		String startLocation = "";
		String destination = "";

		try {
			DaoSearchedPathBox dao = DaoSearchedPathBox.getInstance();
			ArrayList<EntitySearchdPathBox> list = dao
					.getLatestSearchedPathInfoList(context); // 경로 찾은 날짜가 기준.(경로
																// 포인트들 정보)
			EntitySearchdPathBox entity = list.get(0);
			startLocation = entity.mStartLocation;
			destination = entity.mDestination;

			String message = "목적지 주변에 도착 하였습니다.";// PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.message_text_write),
													// "");
			StringBuilder sendMsg = new StringBuilder();
			sendMsg.append(context.getString(R.string.send_msg_title) + "\n");
			sendMsg.append(context.getString(R.string.send_msg_starting_point)
					+ "\n");
			sendMsg.append(startLocation + "\n");
			sendMsg.append(context.getString(R.string.send_msg_destination)
					+ "\n");
			sendMsg.append(destination + "\n");
			sendMsg.append(context.getString(R.string.send_msg_ETE) + "\n");
			sendMsg.append("목적지 주변 도착" + "\n");
			if (!message.equals("")) {
				sendMsg.append(context.getString(R.string.send_msg_messaging)
						+ "\n");
				sendMsg.append(message);
			}

			// 주소록인지 네이트 친구 인지 구분하여 전송해야 한다...정해진 시간 간격에 따라 sms, 네이트 쪽지 전송
			sendSMS(context, startLocation, destination, message,
					sendMsg.toString());
			sendNateMsg(context, startLocation, destination, message,
					sendMsg.toString());

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * sms 보내기
	 */
	private void sendSMS(Context context, String startLocation,
			String destination, String message, String sendMsg) {
		// 1, 주소록 sms 전송
		DaoDesignatedContactBox daoDesignatedContactBox = DaoDesignatedContactBox
				.getInstance();
		ArrayList<EntityDesignatedContactBox> contactList = daoDesignatedContactBox
				.getDesignatedContactInfoList(context);
		// 발신함
		DaoSendBox daoSendBox = DaoSendBox.getInstance();
		EntitySendBox sendBoxEntity = null;

		for (int i = 0; i < contactList.size(); i++) {
			SMSSender.SendSmsMessage(context, contactList.get(i).mContact,
					sendMsg);
			// 발신함 DB에 저장함
			sendBoxEntity = new EntitySendBox();
			sendBoxEntity.mMdn = contactList.get(i).mContact;
			sendBoxEntity.mReceiver = contactList.get(i).mName;
			sendBoxEntity.mStartLocation = startLocation;
			sendBoxEntity.mDestnationLocation = destination;
			sendBoxEntity.mExpectionArrivedTime = context.getString(R.string.arrived_near_destination);
			sendBoxEntity.mMessage = message;
			sendBoxEntity.mMessageType = 0;
			sendBoxEntity.mDeliveryTime = System.currentTimeMillis();

			try {
				daoSendBox.insertSendInfo(context, sendBoxEntity);
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
	private void sendNateMsg(Context context, String startLocation,
			String destination, String message, String sendMsg) {
		// 2. 네이트 친구 쪽지 전송
		DaoDesignatedContactBox dao = DaoDesignatedContactBox.getInstance();
		ArrayList<EntityDesignatedContactBox> nateFriendList = dao
				.getDesignatedNateContactInfoList(context);
		StringBuilder receivers = new StringBuilder();

		for (int i = 0; i < nateFriendList.size(); i++) {

			if (i == nateFriendList.size() - 1) { // 마지막에는 콤마 넣지 않음.
				receivers.append(nateFriendList.get(i).mContact);
			} else {
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
			api.request(requestBundle);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SKPOPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 쪽지 전송후 발신함 디비 저장
		DaoSendBox daoSendBox = DaoSendBox.getInstance();
		EntitySendBox sendBoxEntity = null;

		for (int i = 0; i < nateFriendList.size(); i++) {
			sendBoxEntity = new EntitySendBox();
			sendBoxEntity.mMdn = nateFriendList.get(i).mContact;
			sendBoxEntity.mReceiver = nateFriendList.get(i).mName;
			sendBoxEntity.mStartLocation = startLocation;
			sendBoxEntity.mDestnationLocation = destination;
			sendBoxEntity.mExpectionArrivedTime = context.getString(R.string.arrived_near_destination);
			sendBoxEntity.mMessage = message;
			sendBoxEntity.mMessageType = 1;
			sendBoxEntity.mDeliveryTime = System.currentTimeMillis();
			try {
				daoSendBox.insertSendInfo(context, sendBoxEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
