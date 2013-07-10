package com.tcloud.sample;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jdom.JDOMException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MapUtil;
import com.tcloud.openapi.util.Util;


public class FileBrowser extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener
{
	public static final String TAG = FileBrowser.class.getSimpleName();
    private GridView mGrid;
    private File mCurrentDir;
    private ArrayList<File> mFiles;
    private String[] mFilters;
    private String[] mAudioExt;
    private String[] mImageExt;
    private String[] mArchiveExt;
    private String[] mWebExt;
    private String[] mTextExt;
    private String[] mVideoExt;
    private String[] mGeoPosExt;
    private IconView mLastSelected;
    
    private EditText uploadUriEdit;
    private EditText uploadFileEdit;
    private ProgressBar progressBar;
    
    
	private static final int SET_TOKEN_TEXT = 1;
	private static final int SET_UPLOAD_TEXT = 2;
	
	final Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {
			if(msg.what == SET_TOKEN_TEXT) {
				String token = (String)(msg.obj);
				uploadUriEdit.setText(token);
				return;
			}
			
			if(msg.what == SET_UPLOAD_TEXT) {
				String token = (String)(msg.obj);
				uploadFileEdit.setText(token);
				return;
			}
		}
	};    
        
        
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.filebrowser);
            
            mFiles = new ArrayList<File>();
            
            mAudioExt = getResources().getStringArray(R.array.fileEndingAudio);
            mImageExt = getResources().getStringArray(R.array.fileEndingImage);
            mArchiveExt = getResources().getStringArray(R.array.fileEndingPackage);
            mWebExt = getResources().getStringArray(R.array.fileEndingWebText);
            mTextExt = getResources().getStringArray(R.array.fileEndingText);
            mVideoExt = getResources().getStringArray(R.array.fileEndingVideo);
            mGeoPosExt = getResources().getStringArray(R.array.fileEndingGeoPosition);
            
            Intent intent = getIntent();

            
            mFilters = intent.getStringArrayExtra("FileFilter");
            
            if(intent.getData() == null) browseTo(new File("/mnt/sdcard/"));
            else browseTo(new File(intent.getDataString()));
            
            mGrid = (GridView)findViewById(R.id.fileview);

            mGrid.setOnItemClickListener(this);
            mGrid.setOnItemSelectedListener(this);
            mGrid.setAdapter(new IconAdapter());
            
            uploadUriEdit = (EditText)findViewById(R.id.uploaduriedit);
            uploadFileEdit = (EditText)findViewById(R.id.uploadfileedit);
            progressBar = (ProgressBar)findViewById(R.id.fileuploadprogress);
    }
    
    public void onUriRequest(View v) {
//    	Map<String, String> param = new HashMap<String, String>();
//    	param.put(ParamList.VERSION, "1");
//    	
//    	TcloudOpenApi api = new TcloudOpenApi();
//    	api.requestUploadToken(param, new AsyncInquiryListener() {
//		
//			@Override
//			public void onError(Exception e) {
//				e.printStackTrace();
//				Log.d(TAG, "onError");
//			}
//			
//			@Override
//			public void onComplete(Map<String, ?> response) {
//				Log.d(TAG, "onComplete");
////				uploadUriEdit.setText((CharSequence)response.get("token"));
//				uploadUriEdit.setText(MapUtil.getToken(FileBrowser.this, response));
//			}
//		});
    	
		String url = Const.SERVER_URL + "/token?version=" + Const.API_VERSION;
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		
		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				Log.d(TAG, "onComplete : " + result.toString());
				Map<String, ?> entity;
				try {
					entity = XmlExtractor.parse(result.toString());
					String token = MapUtil.getToken(FileBrowser.this, entity);
					handler.sendMessage(Message.obtain(handler, SET_TOKEN_TEXT, token));

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
				Log.d(TAG, "onSKPOPException " + e);
			}

		};		
    	
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
    }
    
    public void onFileUpload(View v) {
    	String uri = uploadUriEdit.getText().toString();
    	String filePath = uploadFileEdit.getText().toString();
    	Log.d(TAG, "upload file path : " + filePath);
    	if(filePath.equals("")) {
    		return;
    	}
    	
    	File f = new File(filePath);
    	
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setUrl(uri);
		requestBundle.setHttpMethod(HttpMethod.POST);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUploadFile("file",f);
		
		RequestListener requestListener = new RequestListener() {
			@Override
			public void onPlanetSDKException(PlanetXSDKException arg0) {
				handler.sendMessage(Message.obtain(handler, SET_UPLOAD_TEXT, "upload failed exception"));
			}

			@Override
			public void onComplete(ResponseMessage arg0) {
				handler.sendMessage(Message.obtain(handler, SET_UPLOAD_TEXT, "upload request success"));
			}
		};	
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
    }
    
    public void onCancel(View v) {
    	Toast.makeText(getApplicationContext(), "cancel uploading....", Toast.LENGTH_SHORT).show();
    	progressBar.setProgress(0);
    }
    
    private synchronized void browseTo(final File location)
    {
            mCurrentDir = location;
            
            mFiles.clear();
            
            this.setTitle(mCurrentDir.getName().compareTo("") == 0 ? mCurrentDir.getPath() : mCurrentDir.getName());
            
            if(location.getParentFile() != null) mFiles.add(mCurrentDir.getParentFile());
            
            for(File file : mCurrentDir.listFiles())
            {
                    if(file.isDirectory())
                    {
                            mFiles.add(file);
                    }
                    else if(mFilters != null)
                    {
                            for(String ext : mFilters)
                            {
                                    if(file.getName().endsWith(ext))
                                    {
                                            mFiles.add(file);
                                            continue;
                                    }
                            }
                    }
                    else
                    {
                            mFiles.add(file);
                    }
            }
            
            if(mGrid != null) mGrid.setAdapter(new IconAdapter());
    }

    public class IconAdapter extends BaseAdapter
    {
            @Override
            public int getCount()
            {
                    return mFiles.size();
            }

            @Override
            public Object getItem(int index)
            {
                    return mFiles.get(index);
            }

            @Override
            public long getItemId(int index)
            {
                    return index;
            }

            @Override
            public View getView(int index, View convertView, ViewGroup parent)
            {
                    IconView icon;
                    File currentFile = mFiles.get(index);
                    
                    int iconId;
                    String filename;
                    
                    if(index == 0 && (currentFile.getParentFile() == null || currentFile.getParentFile().getAbsolutePath().compareTo(mCurrentDir.getAbsolutePath()) != 0))
                    {
                            iconId = R.drawable.updirectory;
                            filename = new String("..");
                    }
                    else
                    {
                            iconId = getIconId(index);
                            filename = currentFile.getName();
                    }

                    if(convertView == null)
                    {                   
                            icon = new IconView(FileBrowser.this, iconId, filename);
                    }
                    else
                    {
                            icon = (IconView)convertView;
                            icon.setIconResId(iconId);
                            icon.setFileName(filename);
                    }
                    
                    return icon;
            }
            
            private int getIconId(int index)
            {
                    File file = mFiles.get(index);
                    
                    if(file.isDirectory()) return R.drawable.directory;
                    
                    for(String ext : mAudioExt)
                    {
                            if(file.getName().endsWith(ext)) return R.drawable.audio;
                    }
                    
                    for(String ext : mArchiveExt)
                    {
                            if(file.getName().endsWith(ext)) return R.drawable.archive;
                    }
                    
                    for(String ext : mImageExt)
                    {
                            if(file.getName().endsWith(ext)) return R.drawable.image;
                    }
                    
                    for(String ext : mWebExt)
                    {
                            if(file.getName().endsWith(ext)) return R.drawable.webdoc;
                    }
                    
                    for(String ext : mTextExt)
                    {
                            if(file.getName().endsWith(ext)) return R.drawable.text;
                    }
                    
                    for(String ext : mVideoExt)
                    {
                            if(file.getName().endsWith(ext)) return R.drawable.video;
                    }
                    
                    for(String ext : mGeoPosExt)
                    {
                            if(file.getName().endsWith(ext)) return R.drawable.geoposition;
                    }
                    
                    return R.drawable.unknown;
            }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id)
    {
            File file = mFiles.get((int)id);
            
            if(file.isDirectory())
            {
                    browseTo(file);
            }
            else
            {
            	uploadFileEdit.setText(file.getAbsolutePath());
     
            }
    }

    private String getMimeType(File file)
    {
            for(String ext : mAudioExt)
            {
                    if(file.getName().endsWith(ext)) return "audio/*";
            }
            
            for(String ext : mArchiveExt)
            {
                    if(file.getName().endsWith(ext)) return "archive/*";
            }
            
            for(String ext : mImageExt)
            {
                    if(file.getName().endsWith(ext)) return "image/*";
            }
            
            for(String ext : mWebExt)
            {
                    if(file.getName().endsWith(ext)) return "text/html";
            }
            
            for(String ext : mTextExt)
            {
                    if(file.getName().endsWith(ext)) return "text/plain";
            }
            
            for(String ext : mVideoExt)
            {
                    if(file.getName().endsWith(ext)) return "video/*";
            }
            
            for(String ext : mGeoPosExt)
            {
                    if(file.getName().endsWith(ext)) return "audio/*";
            }
            
            return "";
    }

    @Override
    public void onItemSelected(AdapterView<?> grid, View icon, int arg2, long index)
    {
            if(mLastSelected != null)
            {
                    mLastSelected.deselect();
            }
            
            if(icon != null)
            {
                    mLastSelected = (IconView)icon;
                    mLastSelected.select();
            }
            
    }

    @Override
    public void onNothingSelected(AdapterView<?> grid)
    {
            if(mLastSelected != null)
            {
                    mLastSelected.deselect();
                    mLastSelected = null;
            }
    }
}