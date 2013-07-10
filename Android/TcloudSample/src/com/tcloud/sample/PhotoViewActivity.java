package com.tcloud.sample;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.tcloud.openapi.network.HttpConnector;
import com.tcloud.openapi.network.HttpMethod;

public class PhotoViewActivity extends Activity {
	public static final String TAG = PhotoViewActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photoview);
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				getOriginalImage();
				
			}
		}).start();
		
		

	}
	protected void getOriginalImage() {
		Intent intent = getIntent();
		String originalUri = intent.getStringExtra("url");
		final ImageView iv = (ImageView)findViewById(R.id.imageView);
		Log.d(TAG, "photoviewactivity : " + originalUri);
		
		
		HttpConnector connector = new HttpConnector();
		try {
			connector.request(HttpMethod.GET, originalUri);
//			Log.d(TAG, "response entity : " + connector.getContent());
			final Bitmap image = BitmapFactory.decodeStream(connector.getContent());
			PhotoViewActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					iv.setImageBitmap(image);
				}
			});
//			Bitmap resized = Bitmap.createScaledBitmap(image, 450, 200, true);
//			iv.setImageBitmap(resized);
//			iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//			iv.setPadding(3, 3, 3, 3);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
