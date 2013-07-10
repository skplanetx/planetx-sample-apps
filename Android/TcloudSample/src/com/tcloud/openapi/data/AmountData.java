package com.tcloud.openapi.data;

public class AmountData {
	public static final String TOTAL = "total";
	public static final String USED = "used";
	public static final String AVAILABLE = "available";
	
	public String total;
	public String used;
	public String available;
	
	public void set(String key, String value) {
		if(key.equals(TOTAL))			total = value;	
		if(key.equals(USED))			used = value;	
		if(key.equals(AVAILABLE))		available = value;	
	}
}
