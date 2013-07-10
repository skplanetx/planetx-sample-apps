package com.tcloud.openapi.data;

public class TagInfo {
	public static final String CREATED_DATE = "createdDate";
	public static final String TAG_ID = "tagId";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String NAME = "name";
	
	public String createdDate;
	public String tagId;
	public String modifiedDate;
	public String name;
	
	public void set(String key, String value) {
		if(key.equals(CREATED_DATE))	createdDate = value;	
		if(key.equals(TAG_ID))			tagId = value;	
		if(key.equals(MODIFIED_DATE))	modifiedDate = value;	
		if(key.equals(NAME))			name = value;	
	}
}
