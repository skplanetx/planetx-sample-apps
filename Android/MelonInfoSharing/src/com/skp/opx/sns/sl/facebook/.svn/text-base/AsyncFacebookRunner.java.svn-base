package com.skp.opx.sns.sl.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.os.Bundle;

/**
 * @설명 : 페이스북 비동기 송수신 클래스
 * @클래스명 : AsyncFacebookRunner
 */
public class AsyncFacebookRunner {

    Facebook fb;

	/** 
	 * AsyncFacebookRunner 생성자
	 * */
    public AsyncFacebookRunner(Facebook fb) {
    	
        this.fb = fb;
    }

	/** 
	 * 로그아웃 실행함수
	 * */
    public void logout(final Context context, final RequestListener listener, final Object state) {
    	
        new Thread() {
            @Override public void run() {
                try {
                    String response = fb.logout(context);
                    if (response.length() == 0 || response.equals("false")){
                        listener.onFacebookError(new FacebookError(
                                "auth.expireSession failed"), state);
                        return;
                    }
                    listener.onComplete(response, state);
                } catch (FileNotFoundException e) {
                    listener.onFileNotFoundException(e, state);
                } catch (MalformedURLException e) {
                    listener.onMalformedURLException(e, state);
                } catch (IOException e) {
                    listener.onIOException(e, state);
                }
            }
        }.start();
    }

	/** 
	 * 로그아웃 실행함수
	 * */
    public void logout(final Context context, final RequestListener listener) {
    	
        logout(context, listener, /* state */ null);
    }

	/** 
	 * 서버 송신 함수
	 * */
    public void request(Bundle parameters, RequestListener listener, final Object state) {
    	
        request(null, parameters, "GET", listener, state);
    }

	/** 
	 * 서버 송신 함수
	 * */
    public void request(Bundle parameters, RequestListener listener) {
    	
    	request(null, parameters, "GET", listener, /* state */ null);
    }

	/** 
	 * 서버 송신 함수
	 * */
    public void request(String graphPath, RequestListener listener, final Object state) {
    	
        request(graphPath, new Bundle(), "GET", listener, state);
    }

	/** 
	 * 서버 송신 함수
	 * */
    public void request(String graphPath, RequestListener listener) {
    	
        request(graphPath, new Bundle(), "GET", listener, /* state */ null);
    }

	/** 
	 * 서버 송신 함수
	 * */
    public void request(String graphPath, Bundle parameters, RequestListener listener, final Object state) {
    	
        request(graphPath, parameters, "GET", listener, state);
    }

	/** 
	 * 서버 송신 함수
	 * */
    public void request(String graphPath, Bundle parameters, RequestListener listener) {
    	
        request(graphPath, parameters, "GET", listener, /* state */ null);
    }

	/** 
	 * 서버 송신 함수
	 * */
    public void request(final String graphPath, final Bundle parameters, final String httpMethod, final RequestListener listener, final Object state) {
    	
        new Thread() {
            @Override public void run() {
                try {
                    String resp = fb.request(graphPath, parameters, httpMethod);
                    listener.onComplete(resp, state);
                } catch (FileNotFoundException e) {
                    listener.onFileNotFoundException(e, state);
                } catch (MalformedURLException e) {
                    listener.onMalformedURLException(e, state);
                } catch (IOException e) {
                    listener.onIOException(e, state);
                }
            }
        }.start();
    }

	/** 
	 * 서버 송신 후 결과 리스너
	 * */
    public static interface RequestListener {

    	/** 
    	 * 송수신 성공시 결과값 반환 인터페이스
    	 * */
        public void onComplete(String response, Object state);

    	/** 
    	 * onIOException 예외처리를 위한 인터페이스
    	 * */
        public void onIOException(IOException e, Object state);

    	/** 
    	 * onFileNotFoundException 예외처리를 위한 인터페이스
    	 * */
        public void onFileNotFoundException(FileNotFoundException e, Object state);
        
    	/** 
    	 * onMalformedURLException 예외처리를 위한 인터페이스
    	 * */
        public void onMalformedURLException(MalformedURLException e, Object state);

    	/** 
    	 * onFacebookError 예외처리를 위한 인터페이스
    	 * */
        public void onFacebookError(FacebookError e, Object state);

    }
}
