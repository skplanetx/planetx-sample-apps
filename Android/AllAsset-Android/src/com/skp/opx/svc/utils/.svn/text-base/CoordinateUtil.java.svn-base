package com.skp.opx.svc.utils;

import com.skp.opx.svc.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

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
	
	public static Location requestLocationUpdate(LocationListener listener){
		
		 Location gpsLocation = null;
         Location networkLocation = null;
         // mLocationManager.removeUpdates(listener);
 
         gpsLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER, R.string.not_support_gps, listener);
         if(gpsLocation ==null){
        	 networkLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER, R.string.not_support_network, listener);  
         	 return networkLocation;
         }else{
        	 return gpsLocation;
         }
      
        
//          if (gpsLocation != null) {
//        	  convertFromWGS84GEOToEPSG3857(String.valueOf(gpsLocation.getLongitude()), String.valueOf(gpsLocation.getLatitude()));
//         } else if (networkLocation != null) {
//        	 convertFromWGS84GEOToEPSG3857(String.valueOf(networkLocation.getLongitude()), String.valueOf(networkLocation.getLatitude()));
//         }   		
	}
	
    private static Location requestUpdatesFromProvider(final String provider, final int errorResId, LocationListener listener) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, UPDATE_MIN, UPDATE_METER, listener); //시간 , 거리
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(mContext, errorResId, Toast.LENGTH_LONG).show();
        }
        return location;
    }    
    private static final int TWO_MINUTES = 1000 * 60 * 2;
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
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
	
}
