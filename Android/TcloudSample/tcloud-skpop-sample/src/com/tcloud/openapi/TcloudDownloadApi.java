package com.tcloud.openapi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.skp.openplatform.android.sdk.oauth.OAuthInfoManager;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.network.ProgressListener;
import com.tcloud.openapi.util.Util;

public class TcloudDownloadApi {
	public static final String TAG = TcloudDownloadApi.class.getSimpleName();
	private static final int COMPLETE = 1;
	private static final int ERROR = 2;
	private static final int TRANSFER = 3;
	
	private HttpClient httpClient;
	private HttpUriRequest request;
	private HttpResponse response;
	
	private BufferedOutputStream fos;
	
	private class TransferStatus {
		long transferred;
		long total;
		
		public TransferStatus(long transferred, long total) {
			this.transferred = transferred;
			this.total = total;
		}
	}
	
	class AsyncDownloadThread implements Runnable {
		private String uri;
		private File file;
		private boolean response;
		private AsyncFileDownloadListener listener;
		
		public AsyncDownloadThread(String uri, File file, AsyncFileDownloadListener listener) {
			this.uri = uri;
			this.file = file;
			this.listener = listener;
		}
		
		final Handler handler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(final Message msg) {
				if(msg.what == COMPLETE) {
					listener.onComplete(response);
				}
				
				if(msg.what == ERROR) {
					listener.onError((Exception)msg.obj);
				}
				
				if(msg.what == TRANSFER) {
					TransferStatus status = (TransferStatus)msg.obj;
					listener.transferred(status.transferred, status.total);
				}
			}
		};

		@Override
		public void run() {
			try {
				response = download(uri, file, new ProgressListener() {
					@Override
					public void transferred(long num, long length) {
						TransferStatus status = new TransferStatus(num, length);
						handler.sendMessage(Message.obtain(handler, TRANSFER, status));
					}
				});
				handler.sendMessage(Message.obtain(handler, COMPLETE, response));
			} catch (ClientProtocolException e) {
				handler.sendMessage(Message.obtain(handler, ERROR, e));
			} catch (IOException e) {
				handler.sendMessage(Message.obtain(handler, ERROR, e));
			}
		}
	}
	
	public TcloudDownloadApi() {
		final SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		final HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean());
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf8");
		final ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		httpClient = new DefaultHttpClient(connectionManager, params);		
	}
	
	public void download(String uri, File file, AsyncFileDownloadListener listener) {
		AsyncDownloadThread asyncThread = new AsyncDownloadThread(uri, file, listener);
		new Thread(asyncThread).start();
	}
	
	public boolean download(String uri, File file, ProgressListener listener) throws ClientProtocolException, IOException {
//		URL url = new URL(uri);
//		request = new HttpGet(url.toString());
//		Log.d(TAG, "download : " + url);
////		putNorthBoundHeader(request);
//		response = httpClient.execute(request);
//		Log.d(TAG, "download response : " + response.getStatusLine());
//		if(response.getStatusLine().getStatusCode() != 200) {
//			Log.d(TAG, "download : " + response.getStatusLine().getStatusCode());
//			return false;
//		}
//		HttpEntity entity = response.getEntity();
//		Header[] headers = response.getHeaders("Content-Length");
//		Header header = headers[0];
//		
//		int total = Integer.parseInt(header.getValue());
//		int downloaded = 0;
//		if(entity == null){
//			Log.d(TAG, "entity is null");
//			return false;
//		}
//		
//		stream = entity.getContent();
//		byte buf[] = new byte[1024*1024];
//		int numBytesRead;
//		fos = new BufferedOutputStream(new FileOutputStream(file));
//		do {
//			Log.d(TAG, "do start");
//			numBytesRead = stream.read();
//			Log.d(TAG, "numBytesRead = " + numBytesRead);
//			if(numBytesRead <= 0) continue;
//			Log.d(TAG, "write start");
//			fos.write(buf, 0, numBytesRead);
//			Log.d(TAG, "write end");
//			downloaded += numBytesRead;
//			if(listener != null) {
//				listener.transferred(downloaded, total);
//				Log.d(TAG, "downloaded is = " + downloaded + " , total = " + total);
//			}else{
//				Log.d(TAG, "listener is null");
//			}
//		} while(numBytesRead > 0);
//		Log.d(TAG, "do finish");
//
//		fos.flush();
//		fos.close();
//		stream.close();
//		
//		if(downloaded < total){
//			return false;
//		}
//		
//		return true;
		
		
		URL url = new URL(uri);
		request = new HttpGet(url.toString());
		response = httpClient.execute(request);
		if(response.getStatusLine().getStatusCode() != 200) {
			Log.d(TAG, "download : " + response.getStatusLine().getStatusCode());
			return false;
		}
		HttpEntity entity = response.getEntity();
		Header[] headers = response.getHeaders("Content-Length");
		Header header = headers[0];
		
		if(entity == null){
			Log.d(TAG, "entity is null");
			return false;
		}
		
		int total = Integer.parseInt(header.getValue());
		       
		 InputStream is = null;
		 int downloaded = 0;
		 
		 
		 fos = new BufferedOutputStream(new FileOutputStream(file));
//		 URL url = new URL(uri);            
		 url.openConnection();            
		 is = entity.getContent();           
		 byte[] buffer = new byte[1024];            
		 int readBytes;            
		 
		 while ((readBytes = is.read(buffer)) != -1) {                
			 fos.write(buffer, 0, readBytes);
			 downloaded += readBytes;
			 listener.transferred(downloaded, total);
		 }
		 
		 fos.flush();
		 fos.close();
		 is.close();
		 
		 if(downloaded < total){
				return false;
			}
		 
		 
		 return true;
		
	}
	
	public void cancel() {
		request.abort();
	}
}
