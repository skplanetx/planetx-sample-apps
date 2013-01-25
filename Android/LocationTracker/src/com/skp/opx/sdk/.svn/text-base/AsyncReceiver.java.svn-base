package com.skp.opx.sdk;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.skp.openplatform.android.sdk.oauth.SKPOPException;

/**
 * @설명 : SDK에서 제공되는 비동기 송수신에서 RequestListener를 상속받아 수신 후 파싱핸들러에 값 전달 및 에러처리하는 클래스
 * @클래스명 : AsyncReceiver
 */
public class AsyncReceiver implements RequestListener {

	private static AsyncReceiver mAsyncReceiver = null; //singleton 패턴 사용을 위한 자신의 객체
	private static Context mContext;							  //프로젝트 자원 사용을 위한 context
	private static boolean mIsErrorShow = true;			  //에러시 에러내용 팝업을 위한 플래그
	private Handler mHandler;										  //데이터 파싱 클래스 핸들러. 외부로 부터 넘겨받는 값

	private AsyncReceiver() {}

	/** 
	 * 메모리의 낭비를 방지하기 위하여 Singleton 패턴형식의 유일 객체를 생성한다.
	 * */
	public static AsyncReceiver getInstance(Context context) {

		mIsErrorShow = true;
		return getInstance(context, true);
	}

	/** 
	 * 메모리의 낭비를 방지하기 위하여 Singleton 패턴형식의 유일 객체를 생성한다.
	 * */
	public static AsyncReceiver getInstance(Context context, boolean isErrorShow) {

		mIsErrorShow = isErrorShow;
		mContext = context;

		if(mAsyncReceiver == null) {
			mAsyncReceiver = new AsyncReceiver();
		}

		return mAsyncReceiver;
	}

	/** 
	 * 수신 완료시 ResponseMessage 객체를 받아 파싱 핸들러 클래스로 전달된다.
	 * */
	@Override
	public void onComplete(ResponseMessage result) {

		
		//200 : 수쉰완료, 201 : 쪽지발송 요청 접수, 204 : 전달할 정보가 없을 경우 --> 제외한 status code는 에러처리한다.
		if(result.getStatusCode().equalsIgnoreCase("200") == false && result.getStatusCode().equalsIgnoreCase("201") == false && result.getStatusCode().equalsIgnoreCase("204") == false ) {
			//에러팝업 플래그 설정시 에러 내용을 팝업한다.
			if(mIsErrorShow) {
				ErrorMessage.showErrorDialog(mContext, result.getResultMessage());
			}
		//정상적으로 데이터가 수신되었을 경우
		} else {
			if(mHandler != null) {
				//파서 핸들러 클래스에 수신된 데이터를 전달한다.
				Message msg = mHandler.obtainMessage();
				msg.obj = result.getResultMessage();
				mHandler.sendMessage(msg);
			}
		}
	}

	/** 
	 * IOException 발생시의 에러처리
	 * */
	@Override
	public void onIOException(IOException e) {

		if(mIsErrorShow)
			ErrorMessage.showErrorDialog(mContext, e.getMessage());
	}

	/** 
	 * onMalformedURLException 발생시의 에러처리
	 * */
	@Override
	public void onMalformedURLException(MalformedURLException e) {

		if(mIsErrorShow)
			ErrorMessage.showErrorDialog(mContext, e.getMessage());
	}

	/** 
	 * onSKPOPException 발생시의 에러처리
	 * */
	@Override
	public void onSKPOPException(SKPOPException e) {

		if(mIsErrorShow)
			ErrorMessage.showErrorDialog(mContext, e.getMessage());
	}

	/** 
	 * 파싱 핸들러를 외부로 부터 전달받는다.
	 * */
	public void setHandler(Handler handler) {

		mHandler = handler;
	}
}
