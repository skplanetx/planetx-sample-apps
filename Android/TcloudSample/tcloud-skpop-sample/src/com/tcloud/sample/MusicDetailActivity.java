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
import com.tcloud.openapi.data.MusicData;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MetaDataUtil;
import com.tcloud.openapi.util.Util;

public class MusicDetailActivity extends Activity {
	public static final String TAG = MusicDetailActivity.class.getSimpleName();
	EditText musicInfo;
	EditText streamingUrlEdit;
	String streamingUrl;
//	String originalUrl;
	String downloadUrl;
	String musicname;
	
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
		setContentView(R.layout.musicdetail);
		
		streamingUrlEdit = (EditText)findViewById(R.id.musicstreamingedit);
		
		Intent intent = getIntent();
		downloadUrl = intent.getStringExtra(MetaData.DOWNLOAD_URL);
		musicname = intent.getStringExtra(MetaData.NAME);
		
		musicInfo = (EditText)findViewById(R.id.musicinfoedit);
		String info = "title - " + intent.getStringExtra(MusicData.TITLE) + "\n" 
				+ "album - " + intent.getStringExtra(MusicData.ALBUM) + "\n"
				+ "singer - " + intent.getStringExtra(MusicData.SINGER) + "\n"
				+ "downloadurl - " + downloadUrl;
		musicInfo.setText(info);
		objectId = intent.getStringExtra(MetaData.OBJECT_ID);
//		originalUrl = "http://61.34.4.99:7000/tcloud/music/" + intent.getStringExtra(MusicData.OBJECT_ID) + "/originalurl?version=1" + TcloudSession.sessionParameter() + "&test=xml";
//		Log.d(TAG, originalUrl);
	}

	public void onDownload(View v) {
		Intent intent = new Intent(MusicDetailActivity.this, FileDownloadActivity.class);
		intent.putExtra("donwloadurl", downloadUrl);
		intent.putExtra("name", musicname);
		startActivity(intent);
	}
	
	public void onThumbNail(View v) {
		
	}
	
	public void onStreamingUrl(View v) {
		String url = Const.SERVER_URL + "/music/" + objectId + "/originalurl?version=" + Const.API_VERSION; 
		
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
//					streamingUrlEdit.setText(streamingUrl);	
					
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
		intent.setDataAndType(Uri.parse(streamingUrl), "audio/*");
		startActivity(intent);
	}

}
