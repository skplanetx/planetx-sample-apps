package com.tcloud.openapi.data;

import java.util.ArrayList;
import java.util.List;

public class GroupData {
	public static final String TAG = GroupData.class.getSimpleName();
	
	public static final String GROUP_ALL = "group_all";
	public static final String GROUP = "group";
	public static final String TITLE = "title";
	public static final String TOTAL = "total";
	public static final String GROUPS = "groups";
	
	public int total;
	public List<GroupInfo> groupList;
	
	public int index;
	
	public GroupData() {
		groupList = new ArrayList<GroupInfo>();
	}
	
	public void add(GroupInfo groupInfo) {
		groupList.add(index++, groupInfo);
	}
	
	public GroupInfo get(int location) {
		return groupList.get(location);
	}
	
	public int size() {
		return groupList.size();
	}

}
