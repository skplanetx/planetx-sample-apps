package com.skp.opx.rpn.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.skp.opx.rpn.R;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.oauth.Constants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;

/**
 * @설명 : 좌표 구하기 관련 Util  
 * @클래스명 : CoordinateUtil 
 *
 */
public class CoordinateUtil{

	private static LocationManager mLocationManager;			
	private static Context mContext;
    private static final int UPDATE_MIN = 0;
    private static final int UPDATE_METER = 0;
    
	public static synchronized LocationManager getInstance(Context context){
		
		mContext = context;
		
		if (mLocationManager == null) {
			mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);				
		}
		return mLocationManager;
	}
	
	/** 
	 *  Location Manager 업데이트 요청
	 * */
	public static synchronized Location requestLocationUpdate(LocationListener listener){
				
		Location gpsLocation = null;
    	Location networkLocation = null;
    	Location passiveLocation = null;
    	
    	//프로바이더 두개를 사용하는 것이 가장 정확도가 높다. gps 가 항상 정확하지 않음 으로     		
		gpsLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER, R.string.canot_know_location, listener); 
	    networkLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER, R.string.not_support_network, listener);	
	    passiveLocation = requestUpdatesFromProvider(LocationManager.PASSIVE_PROVIDER, R.string.canot_know_location, listener);	
	    
 		if (gpsLocation != null && networkLocation != null) {
            return CoordinateUtil.getBetterLocation(gpsLocation, networkLocation);
        } else if (gpsLocation != null) {
            return gpsLocation;
        } else if (networkLocation != null) {
        	return networkLocation;
        }else{
        	return passiveLocation;
        }
	}
	
    /** 
     *  현재 위치 반환 Method
     * */
    private static synchronized Location requestUpdatesFromProvider(final String provider, final int errorResId, LocationListener listener) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, UPDATE_MIN, UPDATE_METER, listener); //시간 , 거리
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(mContext, errorResId, Toast.LENGTH_LONG).show();
        }
        return location;
    }    
 
	/**
	 * @설명 : T map Reverse Geo Coding
	 * @RequestURI : https://apis.skplanetx.com/tmap/geo/reversegeocoding
	 */
    public static synchronized void getLocatinNameFromCoordinate(String lon, String lat, RequestListener requestListener){
		//Querystring Parameters
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("version", "1");//API의 버전 정보입니다
        map.put("lat", lat);// 지구상의 위치를 나타내는 좌표에서 위도 좌표입니다
        map.put("lon", lon);//지구상의 위치를 나타내는 좌표에서 경도 좌표입니다
        map.put("coordType", "WGS84GEO");//지구 위의 위치를 나타내는 좌표 타입 종류 입니다  
		//Bundle 설정
        RequestBundle reqBundle = new RequestBundle();
		reqBundle.setUrl("https://apis.skplanetx.com/tmap/geo/reversegeocoding");
		reqBundle.setParameters(map);
		reqBundle.setResponseType(CONTENT_TYPE.JSON);	
		reqBundle.setHttpMethod(HttpMethod.GET);
		APIRequest api = new APIRequest();
		
		try {
			//API 호출
			api.request(reqBundle, requestListener);    	
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
    	
    }
    
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    
    /** 
     *  최근 위치 판단 Method (최근 받은 좌표라해도 정확한 좌표는 아님으로 정확도 판단)
     * */
    public static synchronized boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }
    
    /** 
     *  최근 위치 판단 Method (최근 받은 좌표라해도 정확한 좌표는 아님으로 정확도 판단)
     * */
    public static Location getBetterLocation(Location newLocation, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return newLocation;
        }

        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return newLocation;
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return newLocation;
        } else if (isNewer && !isLessAccurate) {
            return newLocation;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return newLocation;
        }
        return currentBestLocation;
    }
    
    /** 
     *  동일한 위치 Provider 인지 판단 Method
     * */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    
    /** 
     *  자원 반납 Method
     * */
    public static synchronized void release (){
    	
    	if(mLocationManager != null){
    		mLocationManager = null;    		
    	}
    	if(mContext != null){
    		mContext = null;
    	}
    	
    }
	
}
