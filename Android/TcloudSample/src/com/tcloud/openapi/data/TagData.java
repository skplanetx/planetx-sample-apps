package com.tcloud.openapi.data;

import java.util.ArrayList;
import java.util.List;

public class TagData {
//	public static final String TAG = TagData.class.getSimpleName();
	
	public static final String TAG = "tag";
	public static final String TITLE = "title";
	public static final String TOTAL = "total";
	public static final String TAGS = "tags";
	
	public int total;
	public List<TagInfo> tagList;
	
	public int index;
	
	public TagData() {
		tagList = new ArrayList<TagInfo>();
	}
	
	public void add(TagInfo tagInfo) {
		tagList.add(index++, tagInfo);
	}
	
	public TagInfo get(int location) {
		return tagList.get(location);
	}
	
	public int size() {
		return tagList.size();
	}
}
