package com.tcloud.sample;

import java.io.IOException;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.oauth.OAuthInfoManager;
import com.skp.openplatform.android.sdk.oauth.OAuthListener;
import com.skp.openplatform.android.sdk.oauth.PlanetXOAuthException;

public class TcloudOpenApiActivity extends Activity {
	public static final String TAG = TcloudOpenApiActivity.class.getSimpleName();
	
	EditText accessTokenEdit;
	SharedPreferences prefs = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        prefs = getPreferences(MODE_PRIVATE);

        accessTokenEdit = (EditText)findViewById(R.id.accessToken);
        

        try {
			OAuthInfoManager.restoreOAuthInfo();
		} catch (PlanetXOAuthException e) {
			e.printStackTrace();
		}
        accessTokenEdit.setText(OAuthInfoManager.authorInfo.getAccessToken());
    }

    public void OnReadToken(View v) throws IOException {
        accessTokenEdit.setText(OAuthInfoManager.authorInfo.getAccessToken());
    }

	
	public void OnStartOneIdAuth(View v) throws IOException, PlanetXOAuthException {
    	Properties prop = new Properties();
    	prop.load(this.getResources().getAssets().open("skpop.properties"));

	   	//new OAuthInfoManager(prop.getProperty("skpop.appId"));
    	APIRequest.setAppKey(prop.getProperty("skpop.appId"));

		OAuthInfoManager.clientId = prop.getProperty("skpop.clientId");
		OAuthInfoManager.clientSecret = prop.getProperty("skpop.clientSecret");
		OAuthInfoManager.scope = prop.getProperty("skpop.scope");
		
		String accessToken = OAuthInfoManager.authorInfo.getAccessToken();
		
		
		if(accessToken != null && accessToken.length() > 0) {
			oAuthCompleted();
		}
		else {
			startOAuth();
		}
     }
    
    public void OnAccountReIssue(View v) throws IOException, PlanetXOAuthException {
    		OAuthInfoManager.reissueAccessToken();
	
     }    

    public void OnLogout(View v) {
			OAuthInfoManager.logout(this, null);
//	        accessTokenEdit.setText(OAuthInfoManager.authorInfo.getAccessToken());
     }    

    private void oAuthCompleted() {
		Log.e("auth", "app key : " + APIRequest.getAppKey());
		Log.e("auth", "access token : "
				+ OAuthInfoManager.authorInfo.getAccessToken());
		TcloudOpenApiActivity.this.startActivity(new Intent(
				TcloudOpenApiActivity.this,
				TcloudMainActivity.class));		
	}

	protected void startOAuth() throws PlanetXOAuthException {
		Log.d("auth", "start oauth");

		OAuthInfoManager.login(this, new OAuthListener() {
			@Override
			public void onError(String error) {
				Log.e("auth", "" + error);
				Toast.makeText(getApplicationContext(), "" + error,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onComplete(String arg0) {
				Log.e(TAG, "***********success message : " + arg0);
				try {
					OAuthInfoManager.saveOAuthInfo();
				} catch (PlanetXOAuthException e) {
					e.printStackTrace();
				}
				
				oAuthCompleted();
			}
		});

	}
}
