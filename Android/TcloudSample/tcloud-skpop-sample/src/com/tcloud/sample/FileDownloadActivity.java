package com.tcloud.sample;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tcloud.openapi.AsyncFileDownloadListener;
import com.tcloud.openapi.TcloudDownloadApi;

public class FileDownloadActivity extends Activity {
	public static final String TAG = FileDownloadActivity.class.getSimpleName();
	private EditText downloadEdit;
	private String downloadUrl;
	private String fileName;
	private ProgressBar progressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filedownload);
	
		Intent intent = getIntent();
		downloadUrl = intent.getStringExtra("donwloadurl");
		fileName = intent.getStringExtra("name");
		if(fileName == null){
			fileName = "";
		}
		
		fileName = fileName.replace(" ", "_");
		
		Log.e(TAG, "file name = " + fileName);
		
		downloadEdit = (EditText)findViewById(R.id.downloadfileedit);
		downloadEdit.setText(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName);
		progressBar = (ProgressBar)findViewById(R.id.filedownloadprogress);
	}
	
	public void onFileDownload(View v) throws ClientProtocolException, IOException {

		final File file = new File(downloadEdit.getText().toString());
		
		TcloudDownloadApi api = new TcloudDownloadApi();
		api.download(downloadUrl, file, new AsyncFileDownloadListener() {
			
			@Override
			public void transferred(long num, long length) {
				progressBar.setProgress((int)(num * 100 / length));
			}
			
			@Override
			public void onError(Exception e) {
				progressBar.setProgress(0);
				Toast.makeText(FileDownloadActivity.this, "download failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onComplete(boolean result) {
				if(result) {
					Toast.makeText(FileDownloadActivity.this, "download success", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(FileDownloadActivity.this, "download failed", Toast.LENGTH_SHORT).show();
				}
				progressBar.setProgress(0);
			}
		});
	}
}
