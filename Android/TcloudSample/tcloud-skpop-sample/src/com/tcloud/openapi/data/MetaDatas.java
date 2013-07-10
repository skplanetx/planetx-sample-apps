package com.tcloud.openapi.data;

import java.util.ArrayList;
import java.util.List;

public class MetaDatas<T extends MetaData> {
	public enum Type { Photo, Music, Video, Document };
	public static final String TAG = MetaDatas.class.getSimpleName();

	public static final String TITLE = "title";
	
	public static final String COUNT = "count";
	public static final String PAGE = "page";
	public static final String TOTAL = "total";
	
	public int count;
	public int page;
	public int total;
	public List<T> metaDatas;
	
	public int index;
	
	public MetaDatas() {
		metaDatas = new ArrayList<T>();
	}
	
	public void add(T metaData) {
		metaDatas.add(index++, metaData);
	}
	
	public T get(int location) {
		return metaDatas.get(location);
	}
	
	public int dataCount() {
		return metaDatas.size();
	}
}
