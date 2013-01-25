package com.skp.opx.rpn.util;


/**
 * @설명 : 소요시간, 거리 변환 Util
 * @클래스명 : ConvertUnitUtil 
 *
 */
public class ConvertUnitUtil {

	
	/** 
	 *  초를 시간, 시, 분으로 변경 Method
	 * */
	public synchronized static String convertSecondToProperTime(String secondTime){
		try {			
		
			 String expression = "";			
	
			  int sec = Integer.parseInt(secondTime);		  
			  int day = 0;
			  int hour = 0;
			  int minute = 0;
			  int second = 0;
			  
			  day= sec/86400;
			  hour = (sec-(day*86400))/60/60;
			  minute= ((sec-(day*86400))-hour*60*60)/60;
			  second = sec%60;
			  String dayString ="일";
			  String hourString ="시";
			  String minuteString ="분";
			  String secondString ="초";
	
			  if(day !=0){ //일단위
				  expression = day + dayString + hour + hourString + minute + minuteString +second + secondString; 
			  }else if(hour !=0){
				  expression = hour + hourString + minute + minuteString +second + secondString;
			  }else if(minute !=0){
				  expression = minute + minuteString +second + secondString;
			  }else if(second != 0){
				  expression = second + secondString;
			  }
			 
			  return expression;		
			
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}	
	}
	/** 
	 *  미터를 Km 로 변경 Method
	 * */
	public synchronized static String convertMeterToKiroMeter(String inPutmeter){

		String expresstion = "";
		String meterString = "m";
		String kiroMeterString = "km";
		int meter = Integer.parseInt(inPutmeter);
		
		if(meter >= 1000){
			expresstion = String.valueOf(meter / 1000) + kiroMeterString;
		}else{
			expresstion = String.valueOf(meter)+meterString; 
		}		
		return expresstion; 
		
	}
	
}
