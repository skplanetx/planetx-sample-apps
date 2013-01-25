package com.skp.opx.sdk;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.openplatform.android.sdk.oauth.SKPOPException;

/**
 * @설명 : 송수시 필요한 requestbundle 객체생성 메서드 및 송신 부분 구현 클래스
 * @클래스명 : AsyncRequester
 */
public final class AsyncRequester {

	/** 
	 * SDK API상에서 method type이 get 또는 delete의 경우 requestbundle객체를 생성하는 함수.
	 * */
	public static final RequestBundle make_GET_DELTE_bundle(Context context, String strUrl, Map<String, Object> parameters) {

		return make_GET_DELTE_bundle(context, strUrl, parameters, true);
	}

	/** 
	 * SDK API상에서 method type이 get 또는 delete의 경우 requestbundle객체를 생성하는 함수. 에러 팝업 여부 추가
	 * */
	public static final RequestBundle make_GET_DELTE_bundle(Context context, String strUrl, Map<String, Object> parameters, boolean isErrorShow) {

		//파라미터로 Map이 null로 전달된 경우 객체생성한다.
		if(parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		//URL에 version 정보가 포함되어 있지 않은 경우 Map정보에 version정보를 추가시킨다.
		if(strUrl.contains("version=") == false) {
			parameters.put("version", Constants.VERSION);
		}

		//Requestbundle 객체 생성
		RequestBundle reqBundle = new RequestBundle();
		//URL 정보 bundle에 추가
		reqBundle.setUrl(strUrl);
		//parameter 정보 bundle에 추가
		if(parameters != null) {
			reqBundle.setParameters(parameters);
		}
		//Json형식으로 데이터를 수신받는다.
		reqBundle.setResponseType(CONTENT_TYPE.JSON);
		
		//비동기 통신에서 사용할 수신 Listener를 등록한다. 오류팝업 여부 추가.
		if(isErrorShow) { 
			reqBundle.setRequestListener(AsyncReceiver.getInstance(context));
		}else{
			reqBundle.setRequestListener(AsyncReceiver.getInstance(context,false));
		}

		//오류 팝업을 위한 url 및 parameter정보를 static변수에 저장한다.
		if(isErrorShow){
			if(Constants.DEBUG_MODE) {
				ErrorMessage.LASTEST_REQUEST_INFO = "<URL> " + strUrl + "\r\n" + "<PARAM> " + parameters.toString();  
			}
		}
		return reqBundle;
	}

	/** 
	 * SDK API상에서 method type이 put 또는 post의 경우 requestbundle객체를 생성하는 함수.
	 * */
	public static final RequestBundle make_PUT_POST_bundle(Context context, String strUrl, Map<String, Object> parameters,String strPayload, File uploadFile) {

		//파라미터로 Map이 null로 전달된 경우 객체생성한다.
		if(parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		//URL에 version 정보가 포함되어 있지 않은 경우 Map정보에 version정보를 추가시킨다.
		if(strUrl.contains("version=") == false) {
			parameters.put("version", Constants.VERSION);
		}

		//Requestbundle 객체 생성
		RequestBundle reqBundle = new RequestBundle();
		//URL 정보 bundle에 추가
		reqBundle.setUrl(strUrl);
		//parameter 정보 bundle에 추가
		reqBundle.setParameters(parameters);

		//전달할 payload 정보가 있다면 bundle에 추가
		if(strPayload != null) {
			reqBundle.setPayload(strPayload);
		}

		//전달할 이미지 file정보가 있다면 bundle에 추가
		if(uploadFile != null) {
			reqBundle.setUploadFile("image", uploadFile);
		}

		//Json형식으로 데이터를 송신한다.
		reqBundle.setRequestType(CONTENT_TYPE.JSON);
		//Json형식으로 데이터를 수신받는다.
		reqBundle.setResponseType(CONTENT_TYPE.JSON);
		//비동기 통신에서 사용할 수신 Listener를 등록한다.
		reqBundle.setRequestListener(AsyncReceiver.getInstance(context));

		//오류 팝업을 위한 url 및 parameter정보를 static변수에 저장한다.
		if(Constants.DEBUG_MODE) {
			ErrorMessage.LASTEST_REQUEST_INFO = "<URL> " + strUrl + "\r\n" +
					"<PARAM> " + parameters.toString() + "\r\n";
			if(strPayload != null) {
				ErrorMessage.LASTEST_REQUEST_INFO += "<PAYLOAD> " + strPayload.toString();
			}
		}		

		return reqBundle;
	}

	/** 
	 * SDK을 사용하여 송신을 수행하는 함수.
	 * */
	public static final void request(Context context, RequestBundle bundle, HttpMethod httpMethod, Handler responseHandler) {

		request(context, bundle, httpMethod, responseHandler, true, true);
	}

	/** 
	 * SDK을 사용하여 송신을 수행하는 함수. 프로그래스바, 에러팝업 여부 파라미터 추가.
	 * */
	public static final void request(Context context, RequestBundle bundle, HttpMethod httpMethod, Handler responseHandler, boolean isShowProgress, boolean isErrorShow) {

		//비동기 통신에서 사용할 수신 Listener를 등록한다. 오류팝업 여부 추가.
		if(isErrorShow) {
			AsyncReceiver.getInstance(context).setHandler(responseHandler);
		} else {
			AsyncReceiver.getInstance(context, false).setHandler(responseHandler);
		}

		APIRequest api = new APIRequest();

		try {
			//RequestBundle에 저장된 데이터로 송신을 시도한다. 오류팝업 여부 추가.
			if(isErrorShow) {
				api.request(bundle, httpMethod, AsyncReceiver.getInstance(context));
			} else {
				api.request(bundle, httpMethod, AsyncReceiver.getInstance(context, false));
			}

			//송신시 프로그래스바 팝업 여부
			if(isShowProgress) {
				PopupDialogUtil.showProgressDialog(context, "Loading...");
			}
		} catch (SKPOPException e) {
			//송신 예외 발생시, 에러팝업
			ErrorMessage.showErrorDialog(context, e.getMessage());
		} 
	}
}
