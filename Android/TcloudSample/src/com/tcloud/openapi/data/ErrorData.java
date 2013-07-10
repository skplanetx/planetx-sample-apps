package com.tcloud.openapi.data;

public class ErrorData {
//	public static final String TITLE = "title";
	
	public static final String CATEGORY = "category";
	public static final String CODE = "code";
	public static final String ID = "id";
	public static final String LINK = "link";
	public static final String MESSAGE = "message";
	
	public String category;
	public String code;
	public String id;
	public String link;
	public String message;
	
	public void set(String key, String value) {
		if(key.equals(CATEGORY))	category = value;
		if(key.equals(CODE))		code = value;
		if(key.equals(ID))			id = value;
		if(key.equals(LINK))		link = value;
		if(key.equals(MESSAGE))		message = value;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}
