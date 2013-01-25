package com.skp.opx.rpn.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @설명 : 구글 캘린더 이벤트 날짜 포맷 방식 파싱하여 사용하기 Util
 * @클래스명 : RFC3339Date 
 *
 */
public class RFC3339Date {
	
	  public static java.util.Date parseRFC3339Date(String datestring) throws java.text.ParseException, IndexOutOfBoundsException {
		    Date d = new Date();
		      try {
		  		
		  	    String aa = datestring.substring(0, datestring.indexOf("+"));
		  	    System.out.println(aa);
		  	    String resultDate = aa.substring(0,aa.lastIndexOf(":"));    
		    	  
		        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); 
		        d = s.parse(resultDate);
		      
		      } catch (java.text.ParseException e) {
		    	  e.printStackTrace();
		      }
		      return d;
	  }
}