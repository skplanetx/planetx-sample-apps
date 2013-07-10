package com.tcloud.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.jdom.JDOMException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.tcloud.openapi.data.AmountData;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MapUtil;
import com.tcloud.openapi.util.Util;

public class TcloudMainActivity extends Activity{
	public static final String TAG = TcloudMainActivity.class.getSimpleName();
	
	protected static final int SHOW_TOAST = 1;
	
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {
			
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
        setContentView(R.layout.tcloudmain);
    }
	
    public void OnFileUpload(View v) {
    	Intent intent = new Intent(this, FileBrowser.class);
    	startActivity(intent);
    }

    public void OnMetaGroup(View v) {
    	this.startActivity(new Intent(this, MetaGroupActivity.class));
    }
    
	public void onAmount(View v) {
		String url = Const.SERVER_URL + "/usage?version=" + Const.API_VERSION;
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		
		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				try {
					if("200".equals(result.getStatusCode())){
						Map<String, ?> entity = XmlExtractor.parse(result.toString());
						AmountData data = MapUtil.getUsageData(getApplicationContext(), entity);
						handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "total : " + data.total + ", used : " + data.used + ", available : " + data.available));
					} else{
						handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "ERROR : "+ result.getStatusCode()+", "+result.toString()));
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
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "Get Storage Amount error, error"+e.toString()));
			}
		};
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}		
	}
}
