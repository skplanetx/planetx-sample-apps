package com.skp.opx.sns.sl.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;

/**
 * @설명 : 페이스북 API 를 이어주는 역활하는 객체
 * @클래스명 : Facebook 
 *
 */
public class Facebook {

    public static final String REDIRECT_URI = "fbconnect://success";
    public static final String CANCEL_URI = "fbconnect://cancel";
    public static final String TOKEN = "access_token";
    public static final String EXPIRES = "expires_in";
    public static final String SINGLE_SIGN_ON_DISABLED = "service_disabled";

    public static final int FORCE_DIALOG_AUTH = -1;

    private static final String LOGIN = "oauth";

    private static final int DEFAULT_AUTH_ACTIVITY_CODE = 32665;

    protected static String DIALOG_BASE_URL =  "https://m.facebook.com/dialog/";
    protected static String GRAPH_BASE_URL  =  "https://graph.facebook.com/";
    protected static String RESTSERVER_URL  =  "https://api.facebook.com/restserver.php";

    private String mAccessToken = null;
    private long mAccessExpires = 0;
    private String mAppId;

    private Activity mAuthActivity;
    private String[] mAuthPermissions;
    private int mAuthActivityCode;
    private DialogListener mAuthDialogListener;
  
    public Facebook(String appId) {
        if (appId == null) {
            throw new IllegalArgumentException(
                    "You must specify your application ID when instantiating " +
                    "a Facebook object. See README for details.");
        }
        mAppId = appId;
    }

    /**
     * Basic Permission 받는 Auth 요청 Method
     */
    public void authorize(Activity activity, final DialogListener listener) {
        authorize(activity, new String[] {}, DEFAULT_AUTH_ACTIVITY_CODE,
                listener);
    }

    /**
     * Custom Permission 받는 Auth 요청 Method  
     *
     */
    public void authorize(Activity activity, String[] permissions,
            final DialogListener listener) {
        authorize(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }
    
    public void authorize_def(Activity activity, String[] permissions,
        final DialogListener listener) {
      authorize2(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }

   
    /** 
     *  Full Auth 요청 Method
     * */
    public void authorize(Activity activity, String[] permissions,
            int activityCode, final DialogListener listener) {

        boolean singleSignOnStarted = false;

        mAuthDialogListener = listener;

        if (activityCode >= 0) {
            singleSignOnStarted = startSingleSignOn(activity, mAppId,
                    permissions, activityCode);
        }
        if (!singleSignOnStarted) {
            startDialogAuth(activity, permissions);
        }
    }
    
    public void authorize2(Activity activity, String[] permissions,
        int activityCode, final DialogListener listener) {
      
      boolean singleSignOnStarted = false;
      
      mAuthDialogListener = listener;      

      if (!singleSignOnStarted) {
        startDialogAuth(activity, permissions);
      }
    }

    /** 
     *  한개의 Auth 요청 핸들링 하는 Method 
     * */
    private boolean startSingleSignOn(Activity activity, String applicationId,
            String[] permissions, int activityCode) {
        boolean didSucceed = true;
        Intent intent = new Intent();

        intent.setClassName("com.facebook.katana",
                "com.facebook.katana.ProxyAuth");
        intent.putExtra("client_id", applicationId);
        if (permissions.length > 0) {
            intent.putExtra("scope", TextUtils.join(",", permissions));
        }

        if (!validateAppSignatureForIntent(activity, intent)) {
            return false;
        }

        mAuthActivity = activity;
        mAuthPermissions = permissions;
        mAuthActivityCode = activityCode;
        try {
            activity.startActivityForResult(intent, activityCode);
        } catch (ActivityNotFoundException e) {
            didSucceed = false;
        }

        return didSucceed;
    }

    /** 
     *  어플리케이션 시그니처 를 쿼리하는 Method
     * */
    private boolean validateAppSignatureForIntent(Activity activity,
            Intent intent) {

        ResolveInfo resolveInfo =
            activity.getPackageManager().resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return false;
        }

        String packageName = resolveInfo.activityInfo.packageName;
        PackageInfo packageInfo;
        try {
            packageInfo = activity.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
        } catch (NameNotFoundException e) {
            return false;
        }

        for (Signature signature : packageInfo.signatures) {
            if (signature.toCharsString().equals(FB_APP_SIGNATURE)) {
                return true;
            }
        }
        return false;
    }

	/** 
	 *  다이얼로그상에서 Auth 요청에 대해 핸들링을 하는 Method
	 * */
	private void startDialogAuth(Activity activity, String[] permissions) {
        Bundle params = new Bundle();
        if (permissions.length > 0) {
            params.putString("scope", TextUtils.join(",", permissions));
        }
        CookieSyncManager.createInstance(activity);
        dialog(activity, LOGIN, params, new DialogListener() {

            public void onComplete(Bundle values) {
                // ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
                setAccessToken(values.getString(TOKEN));
                setAccessExpiresIn(values.getString(EXPIRES));
                if (isSessionValid()) {
                    Log.d("Facebook-authorize", "Login Success! access_token="
                            + getAccessToken() + " expires="
                            + getAccessExpires());
                    mAuthDialogListener.onComplete(values);
                } else {
                    mAuthDialogListener.onFacebookError(new FacebookError(
                                    "Failed to receive access token."));
                }
            }

            public void onError(DialogError error) {
                Log.d("Facebook-authorize", "Login failed: " + error);
                mAuthDialogListener.onError(error);
            }

            public void onFacebookError(FacebookError error) {
                Log.d("Facebook-authorize", "Login failed: " + error);
                mAuthDialogListener.onFacebookError(error);
            }

            public void onCancel() {
                Log.d("Facebook-authorize", "Login canceled");
                mAuthDialogListener.onCancel();
            }
        });
    }

    /** 
     *  Auth 요청후 호출 되는 Call Back Method
     * */
    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
        if (requestCode == mAuthActivityCode) {

            // Successfully redirected.
            if (resultCode == Activity.RESULT_OK) {

                // Check OAuth 2.0/2.10 error code.
                String error = data.getStringExtra("error");
                if (error == null) {
                    error = data.getStringExtra("error_type");
                }

                if (error != null) {
                    if (error.equals(SINGLE_SIGN_ON_DISABLED)
                            || error.equals("AndroidAuthKillSwitchException")) {
                        Log.d("Facebook-authorize", "Hosted auth currently "
                            + "disabled. Retrying dialog auth...");
                        startDialogAuth(mAuthActivity, mAuthPermissions);
                    } else if (error.equals("access_denied")
                            || error.equals("OAuthAccessDeniedException")) {
                        Log.d("Facebook-authorize", "Login canceled by user.");
                        mAuthDialogListener.onCancel();
                    } else {
                        Log.d("Facebook-authorize", "Login failed: " + error);
                        mAuthDialogListener.onFacebookError(
                                new FacebookError(error));
                    }

                } else {
                    setAccessToken(data.getStringExtra(TOKEN));
                    setAccessExpiresIn(data.getStringExtra(EXPIRES));
                    if (isSessionValid()) {
                        Log.d("Facebook-authorize",
                                "Login Success! access_token="
                                        + getAccessToken() + " expires="
                                        + getAccessExpires());
                        mAuthDialogListener.onComplete(data.getExtras());
                    } else {
                        mAuthDialogListener.onFacebookError(new FacebookError(
                                        "Failed to receive access token."));
                    }
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {

                if (data != null) {
                    Log.d("Facebook-authorize",
                            "Login failed: " + data.getStringExtra("error"));
                    mAuthDialogListener.onError(
                            new DialogError(
                                    data.getStringExtra("error"),
                                    data.getIntExtra("error_code", -1),
                                    data.getStringExtra("failing_url")));

                } else {
                    Log.d("Facebook-authorize", "Login canceled by user.");
                    mAuthDialogListener.onCancel();
                }
            }
        }
    }

    /** 
     *  로그아웃 Method
     * */
    public String logout(Context context)
            throws MalformedURLException, IOException {
        Util.clearCookies(context);
        Bundle b = new Bundle();
        b.putString("method", "auth.expireSession");
        String response = request(b);
        setAccessToken(null);
        setAccessExpires(0);
        return response;
    }
  
    /** 
     *  오래된 API를 호출하는 Method
     * */
    public String request(Bundle parameters)
            throws MalformedURLException, IOException {
        if (!parameters.containsKey("method")) {
            throw new IllegalArgumentException("API method must be specified. "
                    + "(parameters must contain key \"method\" and value). See"
                    + " http://developers.facebook.com/docs/reference/rest/");
        }
        return request(null, parameters, "GET");
    }
    
    /** 
     *  파라미터 없이 페이스북 API 호출하는 Method
     * */
    public String request(String graphPath)
            throws MalformedURLException, IOException {
        return request(graphPath, new Bundle(), "GET");
    }

    /** 
     *  Http Get 을 사용하여 페이스북 API 호출하는 Method
     * */
    public String request(String graphPath, Bundle parameters)
            throws MalformedURLException, IOException {
        return request(graphPath, parameters, "GET");
    }


    /** 
     *  동기적으로 페이스북 API 호출하는 Method
     * */
    public String request(String graphPath, Bundle params, String httpMethod)
            throws FileNotFoundException, MalformedURLException, IOException {
        params.putString("format", "json");
        if (isSessionValid()) {
            params.putString(TOKEN, getAccessToken());
        }
        String url = (graphPath != null) ? GRAPH_BASE_URL + graphPath
                                         : RESTSERVER_URL;
        return Util.openUrl(url, httpMethod, params);
    }


    /** 
     *  다이얼로그 생성 Method
     * */
    public void dialog(Context context, String action,
            DialogListener listener) {
        dialog(context, action, new Bundle(), listener);
    }

    /** 
     *  파라미터로 다이얼로그 생성하는 Method
     * */
    public void dialog(Context context, String action, Bundle parameters,
            final DialogListener listener) {

        String endpoint = DIALOG_BASE_URL + action;
        parameters.putString("display", "touch");
        parameters.putString("redirect_uri", REDIRECT_URI);

        if (action.equals(LOGIN)) {
            parameters.putString("type", "user_agent");
            parameters.putString("client_id", mAppId);
        } else {
            parameters.putString("app_id", mAppId);
        }

        if (isSessionValid()) {
            parameters.putString(TOKEN, getAccessToken());
        }
        String url = endpoint + "?" + Util.encodeUrl(parameters);
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Util.showAlert(context, "Error",
                    "Application requires permission to access the Internet");
        } else {
            new FbDialog(context, url, listener).show();
        }
    }

    /**
     * 세션 토큰이 만료 되었는지 확인하는 Method
     */
    public boolean isSessionValid() {
        return (getAccessToken() != null) &&
                ((getAccessExpires() == 0) ||
                        (System.currentTimeMillis() < getAccessExpires()));
    }

    /**
     * 토큰 요청 메소드
     */
    public String getAccessToken() {
        return mAccessToken;
    }

    /**
     * 토큰 만료 기간 리턴하는 Method
     */
    public long getAccessExpires() {
        return mAccessExpires;
    }


    /** 
     *  토큰 세팅하는 Method
     * */
    public void setAccessToken(String token) {
        mAccessToken = token;
    }


    /** 
     * 만료 시간 세팅하는 Method
     * */
    public void setAccessExpires(long time) {
        mAccessExpires = time;
    }

    /**
     * 현재 세션 기간 세팅
     */
    public void setAccessExpiresIn(String expiresIn) {
        if (expiresIn != null && !expiresIn.equals("0")) {
            setAccessExpires(System.currentTimeMillis()
                    + Integer.parseInt(expiresIn) * 1000);
        }
    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        mAppId = appId;
    }


    /**
     * @설명 : 다이얼로그 요청에 대한 콜백
     * @클래스명 : DialogListener 
     *
     */
    public static interface DialogListener {

        
        public void onComplete(Bundle values);
      
        public void onFacebookError(FacebookError e);
        
        public void onError(DialogError e);
      
        public void onCancel();

    }

    public static final String FB_APP_SIGNATURE =
        "30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310"
        + "b3009060355040613025553310b30090603550408130243413112301006035504"
        + "07130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204"
        + "d6f62696c653111300f060355040b130846616365626f6f6b311d301b06035504"
        + "03131446616365626f6f6b20436f72706f726174696f6e3020170d30393038333"
        + "13231353231365a180f32303530303932353231353231365a307a310b30090603"
        + "55040613025553310b30090603550408130243413112301006035504071309506"
        + "16c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c"
        + "653111300f060355040b130846616365626f6f6b311d301b06035504031314466"
        + "16365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d01"
        + "0101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fa"
        + "b00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7"
        + "e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581cc"
        + "fef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603"
        + "f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d010104050"
        + "0038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c"
        + "4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a6"
        + "73149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2"
        + "571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd"
        + "928a2";

}
