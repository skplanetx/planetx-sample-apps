package com.tcloud.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.jdom.JDOMException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.tcloud.openapi.data.ErrorData;
import com.tcloud.openapi.data.MetaData;
import com.tcloud.openapi.data.MetaDatas;
import com.tcloud.openapi.data.MovieData;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MetaDataUtil;
import com.tcloud.openapi.util.Util;

public class MovieDetailActivity extends Activity {
	public static final String TAG = MusicDetailActivity.class.getSimpleName();
	EditText movieInfo;
	EditText streamingUrlEdit;
	String streamingUrl;
//	String originalUrl;	
	String downloadUrl;
	String filename;
	
	String objectId;
	
	protected static final int SHOW_TOAST = 1;
	protected static final int SET_URL = 2;
	
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {
			
			if(msg.what == SET_URL) {
				String message = (String)(msg.obj);
				if(message != null && message.length() > 0) {
					streamingUrlEdit.setText(message);	
				}
			}
			
			if(msg.what == SHOW_TOAST) {
				String message = (String)(msg.obj);
				if(message != null && message.length() > 0) {
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moviedetail);
		
		streamingUrlEdit = (EditText)findViewById(R.id.moviestreamingedit);
		Intent intent = getIntent();
		downloadUrl = intent.getStringExtra(MetaData.DOWNLOAD_URL);		
		filename = intent.getStringExtra(MetaData.NAME);	
		
		movieInfo = (EditText)findViewById(R.id.movieinfoedit);
		String info = "name - " + intent.getStringExtra(MovieData.NAME) + "\n"
				+ "size - " + intent.getStringExtra(MovieData.SIZE) + "\n"
				+ "duration - " + intent.getStringExtra(MovieData.DURATION) + "\n"
				+ "downloadUrl - " + downloadUrl;
		movieInfo.setText(info);
		objectId = intent.getStringExtra(MetaData.OBJECT_ID);
//		originalUrl = "http://61.34.4.99:7000/tcloud/movies/" + intent.getStringExtra(MetaData.OBJECT_ID) + "/originalurl?version=1" + TcloudSession.sessionParameter() + "&test=xml";
	}
	
	
	
	public void onDownload(View v) {
		Intent intent = new Intent(MovieDetailActivity.this, FileDownloadActivity.class);
		intent.putExtra("donwloadurl", downloadUrl);
		intent.putExtra("name", filename);
		startActivity(intent);
		
	}
	
	public void onThumbNail(View v) {
		
	}
	
	public void onStreamingUrl(View v) {
//		Map<String, String> param = new HashMap<String, String>();
//		param.put(ParamList.VERSION, "1");
//		param.put(ParamList.OBJECT_ID, objectId);	
//
//		TcloudOpenApi api = new TcloudOpenApi();
//		
//		api.movieOriginalUrlWithObjectId(param, new AsyncInquiryListener() {
//			
//			@Override
//			public void onError(Exception e) {
//				e.printStackTrace();
//			}
//			
//			@Override
//			public void onComplete(Map<String, ?> response) {
//				String originalUri = "";
//				String title = (String)response.get(MetaDatas.TITLE);
//				
//				if(title.equals("error")) {
//					ErrorData errorData = MetaDataUtil.handleError(response);
//					Toast.makeText(MovieDetailActivity.this, errorData.getMessage(), Toast.LENGTH_SHORT).show();
//					return;
//				}
//				
//				for(String key : response.keySet()) {
//					Log.d(TAG, "streaming url : " + key + "-" + response.get(key));
//					if(key.equals("url")) {
//						streamingUrl = (String)response.get("url");
//						Log.d(TAG, "origin : " + originalUri);
//					}
//				}
//				streamingUrlEdit.setText(streamingUrl);
//	
//			}
//		});	
		
		String url = Const.SERVER_URL + "/movies/" + objectId + "/originalurl?version=" + Const.API_VERSION; 
		
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);

		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				try {
					Map<String, ?> entity = XmlExtractor.parse(result.toString());
					String originalUri = "";
					String title = (String)entity.get(MetaDatas.TITLE);
					
					if(title.equals("error")) {
						ErrorData errorData = MetaDataUtil.handleError(entity);
						handler.sendMessage(Message.obtain(handler, SHOW_TOAST, errorData.getMessage()));
						return;
					}
					
					for(String key : entity.keySet()) {
						Log.d(TAG, "streaming url : " + key + "-" + entity.get(key));
						if(key.equals("url")) {
							streamingUrl = (String)entity.get("url");
							Log.d(TAG, "origin : " + originalUri);
						}
					}
					
					if(streamingUrl != null && streamingUrl.length() > 0) {
						handler.sendMessage(Message.obtain(handler, SET_URL, streamingUrl));
					}
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				e.printStackTrace();
			}
			
		};
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}
	
	public void onStreaming(View v) {
		if(streamingUrl == null || streamingUrl.equals("")) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(streamingUrl), "video/*");
		startActivity(intent);
	}

}
