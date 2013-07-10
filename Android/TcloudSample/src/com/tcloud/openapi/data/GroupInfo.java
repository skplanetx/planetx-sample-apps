package com.tcloud.openapi.data;

public class GroupInfo {
	public static final String CREATED_DATE = "createdDate";
	public static final String GROUP_ID = "groupId";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String NAME = "name";
	
	public String createdDate;
	public String groupId;
	public String modifiedDate;
	public String name;
	
	public void set(String key, String value) {
		if(key.equals(CREATED_DATE))	createdDate = value;	
		if(key.equals(GROUP_ID))		groupId = value;	
		if(key.equals(MODIFIED_DATE))	modifiedDate = value;	
		if(key.equals(NAME))			name = value;	
	}
}
