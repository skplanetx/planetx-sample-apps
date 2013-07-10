package com.tcloud.openapi.util;

import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.tcloud.openapi.data.AmountData;
import com.tcloud.openapi.data.GroupData;
import com.tcloud.openapi.data.GroupInfo;
import com.tcloud.openapi.data.TagData;
import com.tcloud.openapi.data.TagInfo;

public class MapUtil {
	public static final String TAG = MapUtil.class.getSimpleName();
	public static final String TITLE = "title";
	
	public static String getToken(Context context, Map<String, ?> map) {
		String title = (String)map.get(TITLE);
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return "";
		}
		return (String)map.get("token");
	}

	public static String getToken(Map<String, ?> map) {
		String title = (String)map.get(TITLE);
		if(title != null && title.equals("error")) {
			handleError(map);
			return "";
		}
		return (String)map.get("token");
	}	
	
	public static AmountData getUsageData(Context context, Map<String, ?> map) {
		AmountData data = new AmountData();
		String title = (String)map.get(TITLE);
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return data;
		}
		data.set(AmountData.TOTAL, (String) map.get(AmountData.TOTAL));
		data.set(AmountData.USED, (String) map.get(AmountData.USED));
		data.set(AmountData.AVAILABLE, (String) map.get(AmountData.AVAILABLE));
		return data;		
	}	
	
	public static GroupData getGroupData(Context context, Map<String, ?> map) {
		String title = (String)map.get(GroupData.TITLE);
		GroupData data = new GroupData();
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return data;
		}
		data.total = Integer.valueOf((String)map.get(GroupData.TOTAL));
		Map<String, Object> groupListMap = (Map<String, Object>)map.get(GroupData.GROUPS);
		for(String groupKey : groupListMap.keySet()) {
			Map<String, String> groupMap = (Map<String, String>)groupListMap.get(groupKey);
			GroupInfo info = new GroupInfo();
			for(String infoKey : groupMap.keySet()) {
				info.set(infoKey, groupMap.get(infoKey));
			}
			data.add(info);
		}
		return data;
	}
	
	public static GroupData getGroupData(Map<String, ?> map) {
		String title = (String)map.get(GroupData.TITLE);
		GroupData data = new GroupData();
		if(title != null && title.equals("error")) {
			handleError(map);
			return data;
		}
		data.total = Integer.valueOf((String)map.get(GroupData.TOTAL));
		Map<String, Object> groupListMap = (Map<String, Object>)map.get(GroupData.GROUPS);
		for(String groupKey : groupListMap.keySet()) {
			Map<String, String> groupMap = (Map<String, String>)groupListMap.get(groupKey);
			GroupInfo info = new GroupInfo();
			for(String infoKey : groupMap.keySet()) {
				info.set(infoKey, groupMap.get(infoKey));
			}
			data.add(info);
		}
		return data;
	}
	
	public static TagData getTagData(Context context, Map<String, ?> map) {
		String title = (String)map.get(GroupData.TITLE);
		TagData data = new TagData();
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return data;
		}
		data.total = Integer.valueOf((String)map.get(TagData.TOTAL));
		Map<String, Object> tagListMap = (Map<String, Object>)map.get(TagData.TAGS);
		for(String tagKey : tagListMap.keySet()) {
			Map<String, String> tagMap = (Map<String, String>)tagListMap.get(tagKey);
			TagInfo info = new TagInfo();
			for(String infoKey : tagMap.keySet()) {
				info.set(infoKey, tagMap.get(infoKey));
			}
			data.add(info);
		}
		return data;	
	}
	
//	public static TagData getTagData(Map<String, ?> map) {
//		String title = (String)map.get(GroupData.TITLE);
//		TagData data = new TagData();
//		if(title != null && title.equals("error")) {
//			handleError(map);
//			return data;
//		}
//		data.total = Integer.valueOf((String)map.get(TagData.TOTAL));
//		Map<String, Object> tagListMap = (Map<String, Object>)map.get(TagData.TAGS);
//		for(String tagKey : tagListMap.keySet()) {
//			Map<String, String> tagMap = (Map<String, String>)tagListMap.get(tagKey);
//			TagInfo info = new TagInfo();
//			for(String infoKey : tagMap.keySet()) {
//				info.set(infoKey, tagMap.get(infoKey));
//			}
//			data.add(info);
//		}
//		return data;	
//	}
	
	public static void handleError(Map<String, ?> map) {
		Log.d(TAG, "error");
	}
	
	public static void handleError(Context context, Map<String, ?> map) {
//		Toast.makeText(context, (String)map.get("message"), Toast.LENGTH_SHORT).show();
		for(String key: map.keySet()) {
			Object value = (Object) map.get(key);
			Log.d(TAG, "handleError - " + key + ":" + value);
		}
	}
}
