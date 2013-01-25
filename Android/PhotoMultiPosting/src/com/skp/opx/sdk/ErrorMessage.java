package com.skp.opx.sdk;
/**
 * 
 * @클래스명 : Define
 * @작성자 : 박상현
 * @작성일 : 2012. 10. 8.
 * @최종수정일 : 2012. 10. 8.
 * @수정이력 - 수정일, 수정자, 수정 내용
 * TODO
 */


import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * @설명 : 송수신시 Error가 발생하면 Error내용을 팝업하는 클래스
 * @클래스명 : ErrorMessage
 */
public final class ErrorMessage {
	
	public static String LASTEST_REQUEST_INFO = new String();
	
	/** 
	 * 에러 내용표시 다이어로그를 팝업한다.
	 * */
	public static final void showErrorDialog(final Context context, final String strMessage) {
		
		Log.e("ErrorMessage", strMessage == null ? "unknown error" : strMessage);
	
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				//프로그래스 다이어로그를 종료한다.
				PopupDialogUtil.dismissProgressDialog();
				
				if(Constants.DEBUG_MODE == false) {
					return;
				}
				
				//에러내용을 팝업한다.
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("ERROR MESSAGE !");
				builder.setMessage(LASTEST_REQUEST_INFO + "\r\n" + "<SERVER>" + strMessage);
				builder.setPositiveButton("Confirm", null);
				builder.create().show();
			}
		});
	}
}
