package com.tcloud.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tcloud.openapi.data.DocumentData;
import com.tcloud.openapi.data.MetaData;

public class DocumentDetailActivity extends Activity {
	public static final String TAG = DocumentDetailActivity.class.getSimpleName();
	EditText documentInfo;
	String objectId;
	String downloadUrl;
	String filename;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.documentdetail);
		
		Intent intent = getIntent();
		downloadUrl = intent.getStringExtra(MetaData.DOWNLOAD_URL);		
		filename = intent.getStringExtra(DocumentData.NAME);	
		
		String info = "name - " + intent.getStringExtra(DocumentData.NAME) + "\n"
				+ "size - " + intent.getStringExtra(DocumentData.SIZE) + "\n"
				+ "downloadUrl - " + downloadUrl;
		
		objectId = intent.getStringExtra(MetaData.OBJECT_ID);
		
		documentInfo = (EditText)findViewById(R.id.documentinfoedit);
		documentInfo.setText(info);
	}
	
	public void onDownload(View v) {
		Intent intent = new Intent(DocumentDetailActivity.this, FileDownloadActivity.class);
		intent.putExtra("donwloadurl", downloadUrl);
		intent.putExtra("name", filename);
		startActivity(intent);
	}
	
	public void onView(View v) {
//		String uri = "http://61.34.4.99:7000/tcloud/documents/" + objectId + "/sharedurl?version=1" + TcloudSession.sessionParameter() + "&test=xml";
//		String sharedUri = null;
//		TcloudOpenApi api = TcloudOpenApi.create();
//		try {
//			String entity = api.inquiryData(uri);
//			Log.d(TAG, "onView : " + entity);
//			Map<String, ?> map = XmlExtractor.parse(new ByteArrayInputStream(entity.getBytes("UTF-8")));
//			for(String key : map.keySet()) {
//				sharedUri = (String)map.get(key);
//			}
//			Log.d(TAG, "shared url : " + sharedUri);
//			Intent intent = new Intent();
//			intent.setAction(android.content.Intent.ACTION_VIEW);
//			File file = new File(sharedUri);
//
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JDOMException e) {
//			e.printStackTrace();
//		}
	}

}
