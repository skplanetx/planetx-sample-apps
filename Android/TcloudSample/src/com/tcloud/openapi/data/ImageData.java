package com.tcloud.openapi.data;

public class ImageData extends MetaData{
	public static final String IMAGES = "images";
	public static final String IMAGE_ID = "imageId";
	
	public String imageId;
	
	@Override
	public void set(String key, String value) {
		super.set(key, value);
		if(key.equals(IMAGE_ID))	imageId = value;
	}

}
