package com.skp.opx.rpn.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.skp.opx.core.client.Define;
import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityMyEvent;
import com.skp.opx.rpn.ui.adapter.Adapter_MyEvents;
import com.skp.opx.rpn.util.RFC3339Date;

/**
 * @설명 : 내 이벤트 Activity
 * @클래스명 : MyEventActivity 
 *
 */
public class MyEventActivity extends ListActivity {
	
	private String mToken = ""; 
	private static final int REQUEST_AUTHENTICATE = 0;
	private GoogleAccountManager accountManager;
	private static final String AUTH_TOKEN_TYPE = "cl";
	private ProgressDialog mProgressDialog;
	private Adapter_MyEvents mMyEventAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading));
        
		accountManager = new GoogleAccountManager(this);
	    setContentView(R.layout.activity_my_events);
		mHandler.sendEmptyMessage(0);		
	
	}
	
	 private Handler mHandler = new Handler()
		{
			public void handleMessage(Message msg) {
				
			if(msg.what == 0){
				mProgressDialog.show();
				gotAccount();
			}else if(msg.what ==1){				
				mMyEventAdapter = new Adapter_MyEvents(getApplicationContext());
				mMyEventAdapter.setMyEventsList(mMyEventList);
				setListAdapter(mMyEventAdapter);
				if(mProgressDialog!=null){
					mProgressDialog.dismiss();					
				}
			}				
				super.handleMessage(msg);
			}
		};	
	
	/** 
	 *  구글 계정 확인 Method
	 * */
	private void chooseAccount() {

		accountManager.getAccountManager().getAuthTokenByFeatures(GoogleAccountManager.ACCOUNT_TYPE,
				AUTH_TOKEN_TYPE,
				null,
				MyEventActivity.this,
				null,
				null,
				new AccountManagerCallback<Bundle>() {

			public void run(AccountManagerFuture<Bundle> future) {//허용하시겠습니까.자동으로 뜨게 됨
				Bundle bundle;
				try {
					bundle = future.getResult();
					String a = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
					mToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
					
				} catch (OperationCanceledException e) {
					// user canceled
				} catch (AuthenticatorException e) {
					Log.e(getLocalClassName(), e.getMessage(), e);
				} catch (IOException e) {
					Log.e(getLocalClassName(), e.getMessage(), e);
				}
			}
		},
		null);
	}
	/** 
	 *  계정 정보 가져오는 Method
	 * */
	private void gotAccount() {

		Account account = accountManager.getAccountByName(getAccountFull());

		if (account == null) {
			chooseAccount();
			return;
		}

		accountManager.getAccountManager().getAuthToken(account, AUTH_TOKEN_TYPE, true, new AccountManagerCallback<Bundle>() {

			public void run(AccountManagerFuture<Bundle> future) {

				try {
					Bundle bundle = future.getResult();
					if (bundle.containsKey(AccountManager.KEY_INTENT)) {
						Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
						intent.setFlags(intent.getFlags() & ~Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivityForResult(intent, REQUEST_AUTHENTICATE);
					} else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
						mToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
						Thread t = new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								getMyEvents(mToken);
								
							}
						});
						t.start();
						
					}
				} catch (Exception e) {
					Log.e(getLocalClassName(), e.getMessage(), e);
				}
			}
		}, null);
	}
	
	/** 
	 *  계정 정보 리턴 Method
	 * */
	private String getAccount() {

		Account[] accounts = AccountManager.get(this).getAccounts(); 

		for(Account account : accounts) {
			if( account.type.equals("com.google")) {
				return account.name.replace("@gmail.com", "");
			}
		}

		throw new NotFoundException();
	}
	
	/** 
	 *  계정 정보 Full 리턴 Method
	 * */
	private String getAccountFull(){
		Account[] accounts = AccountManager.get(this).getAccounts(); 

		for(Account account : accounts) {
			if( account.type.equals("com.google")) {
				return account.name;
			}
		}

		throw new NotFoundException();
		
		
	}
	
	/** 
	 *  구글 캘린더의 내 일정 가져오는 Method
	 * */
	private void getMyEvents(String token){
		

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());

		try {
            //오늘 날짜 이벤트 부터 싱글 이벤트 날짜 순으로 가져온다.
			String strUrl = "https://www.googleapis.com/calendar/v3/calendars/" +
								getAccount() + 
								"%40gmail.com/events?key="+Define.ClientCredentials.KEY + "&timeMin=" +
								String.format("%d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) +1, cal.get(Calendar.DAY_OF_MONTH)) +
								"T" +
								String.format("%02d:%02d:%02d", 0, 0, 0) +
								".000Z" + "&orderBy=startTime&singleEvents=true";


			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(strUrl);
			httpget.setHeader("Authorization", GoogleHeaders.getGoogleLoginValue(token));
			org.apache.http.HttpResponse response;

			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){

				InputStream inputstream = entity.getContent();
				BufferedReader buffredreader = new BufferedReader(new InputStreamReader(inputstream));

				StringBuilder stringbuilder = new StringBuilder();
				String currentline = null;

				while ((currentline = buffredreader.readLine()) != null) {
 
					stringbuilder.append(currentline + "\n");				 			
				}
			
				startEventParse(stringbuilder.toString());
				mHandler.sendEmptyMessage(1);
				
			}		  
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	private ArrayList<EntityMyEvent> mMyEventList = new ArrayList<EntityMyEvent>();
	
	/** 
	 *  내 일정 파싱 Method
	 * */
	private void startEventParse(String response){
				
		try {
			
			JSONObject thedata = new JSONObject(response);
			JSONArray items = thedata.getJSONArray("items");
			EntityMyEvent entity = null;
 			for (int i = 0; i < items.length(); i++) {

				JSONObject data  = items.getJSONObject(i);

				String summary = data.getString("summary");
				String location = data.getString("location");
				JSONObject startDate = data.getJSONObject("start");
				String startTime = startDate.getString("dateTime");
				JSONObject endDate  = data.getJSONObject("end");
				String endTime = endDate.getString("dateTime");	
				entity = new EntityMyEvent();
				Date startingDate = RFC3339Date.parseRFC3339Date(startTime);
				Date endingDate = RFC3339Date.parseRFC3339Date(endTime);				
				
				entity.startDate = new SimpleDateFormat("yyyy년 MM 월 dd일").format(startingDate);//날짜 표시
				entity.startTime = new SimpleDateFormat("a hh시 MM분").format(startingDate);//날짜 표시
				entity.endTime = new SimpleDateFormat("a hh시 MM분").format(endingDate);//날짜 표시	
				entity.summary = summary;
				entity.location = location;
				mMyEventList.add(entity);
			}			  
		} catch (Exception e) {
			e.printStackTrace();
			//e.printStackTrace();
		}		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String location = mMyEventList.get(position).location;
		intent.putExtra("choice" , location);
		setResult(RESULT_OK, intent);
		finish();
	}
}
