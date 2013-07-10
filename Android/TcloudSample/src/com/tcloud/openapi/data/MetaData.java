package com.tcloud.openapi.data;

public abstract class MetaData {
	public static final String CREATED_DATE = "createdDate";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String NAME = "name";
	public static final String PATH = "path";
	public static final String SIZE = "size";
	public static final String DOWNLOAD_URL = "downloadUrl";
	public static final String RESOLUTION = "resolution";
	public static final String THUMBNAIL_URL = "thumbnailUrl";
	public static final String OBJECT_ID = "objectId";
	
	public String createdDate;
	public String modifiedDate;
	public String name;
	public String path;
	public String size;
	public String downloadUrl;
	public String resolution;
	public String thumbnailUrl;
	public String objectId;
	
	public void set(String key, String value) {
		if(key.equals(CREATED_DATE))	createdDate = value;
		if(key.equals(MODIFIED_DATE))	modifiedDate = value;
		if(key.equals(NAME))			name = value;
		if(key.equals(PATH))			path = value;
		if(key.equals(SIZE))			size = value;
		if(key.equals(DOWNLOAD_URL))	downloadUrl = value;
		if(key.equals(RESOLUTION))		resolution = value;
		if(key.equals(THUMBNAIL_URL))	thumbnailUrl = value;
		if(key.equals(OBJECT_ID))		objectId = value;
	}
}
