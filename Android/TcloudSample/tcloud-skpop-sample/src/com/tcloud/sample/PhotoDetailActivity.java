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
import com.tcloud.openapi.data.ImageData;
import com.tcloud.openapi.data.MetaData;
import com.tcloud.openapi.data.MetaDatas;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MetaDataUtil;
import com.tcloud.openapi.util.Util;

public class PhotoDetailActivity extends Activity {
	public static final String TAG = PhotoDetailActivity.class.getSimpleName();
	EditText textName;
	EditText textCreateDate;
	EditText textModifiedDate;
	EditText textPath;
	EditText textSize;

	EditText textDownloadUrl;
	EditText textResolution;
	EditText textThumbPath;
	EditText textObjectId;
	EditText textImageId;
	
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
		setContentView(R.layout.photodetail);
		
		Intent intent = getIntent();
		
		textName = (EditText)this.findViewById(R.id.photonameedit);
		textCreateDate = (EditText)this.findViewById(R.id.photocreatedateedit);
		textModifiedDate = (EditText)this.findViewById(R.id.photomodifieddateedit);
		textPath = (EditText)this.findViewById(R.id.photopathedit);
		textSize = (EditText)this.findViewById(R.id.photosizeedit);

		textDownloadUrl = (EditText)this.findViewById(R.id.photodownloaduriedit);
		textResolution = (EditText)this.findViewById(R.id.photoresolutionedit);
		textThumbPath = (EditText)this.findViewById(R.id.photothumbpathedit);
		textObjectId = (EditText)this.findViewById(R.id.photostorageidedit);
		textImageId = (EditText)this.findViewById(R.id.photoimageidedit);
	
		textName.setText(intent.getStringExtra(MetaData.NAME));
		textCreateDate.setText(intent.getStringExtra(MetaData.CREATED_DATE));
		textModifiedDate.setText(intent.getStringExtra(MetaData.MODIFIED_DATE));
		textPath.setText(intent.getStringExtra(MetaData.PATH));
		textSize.setText(intent.getStringExtra(MetaData.SIZE));

		textDownloadUrl.setText(intent.getStringExtra(MetaData.DOWNLOAD_URL));
		textResolution.setText(intent.getStringExtra(MetaData.RESOLUTION));
		textThumbPath.setText(intent.getStringExtra(MetaData.THUMBNAIL_URL));
		textObjectId.setText(intent.getStringExtra(MetaData.OBJECT_ID));
		textImageId.setText(intent.getStringExtra(ImageData.NAME));
	}
	
	public void onMetaView(View v) {
		
	}
	
	public void onPhotoView(View v) {
//		Map<String, String> param = new HashMap<String, String>();
//		param.put(ParamList.VERSION, "1");
//		param.put(ParamList.OBJECT_ID, textObjectId.getText().toString());
//		
//		TcloudOpenApi api = new TcloudOpenApi();
//		api.imageOriginalUrlWithObjectId(param, new AsyncInquiryListener() {
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
//					Toast.makeText(PhotoDetailActivity.this, errorData.getMessage(), Toast.LENGTH_SHORT).show();
//					return;
//				}
//				
//				for(String key : response.keySet()) {
//					Log.d(TAG, "streaming url : " + key + "-" + response.get(key));
//					if(key.equals("url")) {
//						originalUri = (String)response.get("url");
//						Log.d(TAG, "origin : " + originalUri);
//					}
//				}
//				Intent intent = new Intent(PhotoDetailActivity.this, PhotoViewActivity.class);
//				intent.putExtra("url", originalUri);
//				startActivity(intent);				
//			}
//		});
		
		String url = Const.SERVER_URL + "/images/" + textObjectId.getText().toString() + "/originalurl?version=" + Const.API_VERSION; 
		
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
							originalUri = (String)entity.get("url");
							Log.d(TAG, "origin : " + originalUri);
						}
					}
					Intent intent = new Intent(PhotoDetailActivity.this, PhotoViewActivity.class);
					intent.putExtra("url", originalUri);
					startActivity(intent);				
					
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
	
	public void onUrlView(View v) {
	}
	
	public void onDownload(View v) {
		Intent intent = new Intent(PhotoDetailActivity.this, FileDownloadActivity.class);
		intent.putExtra("donwloadurl", textDownloadUrl.getText().toString());
		intent.putExtra("name", textName.getText().toString());
		startActivity(intent);
	}
}
