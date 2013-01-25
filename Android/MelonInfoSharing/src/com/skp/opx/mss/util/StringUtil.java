package com.skp.opx.mss.util;

/**
 * 
 * @클래스명 : StringUtil
 * @작성자 : 정아름
 * @작성일 : 2012. 10. 30.
 * @최종수정일 : 2012. 10. 30.
 * @수정이력 - 수정일, 수정자, 수정 내용
 * @설명 : StringUtil
 */
public class StringUtil {
	/**
	 * @Method 설명 : String 값이 null 또는 화이트스페이드 일 경우 default 문자열로 반환
	 * @param src
	 * @param def
	 * @return :
	*/
	public static String getString(String src, String def){
		String retValue = src;
		
		if(retValue == null || retValue.equals("")){
			retValue = def;
		}
		
		return retValue;
	}
	
	/**
	 * @Method 설명 : String 값이 null 또는 화이트스페이드 일 경우 화이트스페이스로 반환
	 * @param src
	 * @return :
	*/
	public static String getString(String src){
		return getString(src, "");
	}
	
    /**
     * @Method 설명 : String 값이 유효한 값인지 확인
     * @param str
     * @return :
    */
    public static boolean isEmpty(String str) {
		if( str == null)
			return true;
		
		if(str.equals( "" ))
			return true;
		
		if(str.trim().length() == 0)
			return true;
		
		return false;
	}


}
