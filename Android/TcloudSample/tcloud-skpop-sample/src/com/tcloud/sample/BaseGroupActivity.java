package com.tcloud.sample;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tcloud.openapi.data.GroupData;
import com.tcloud.openapi.data.GroupInfo;
import com.tcloud.openapi.data.TagData;
import com.tcloud.openapi.data.TagInfo;

public abstract class BaseGroupActivity extends ListActivity {
	public static final String TAG = BaseGroupActivity.class.getSimpleName();
	
	public static final String CATEGORY = "category";
	public static final String CATEGORY_ALL = "all";
	public static final String CATEGORY_GROUP = "group";
	public static final String CATEGORY_TAG = "tag";
	
	protected EditText groupNameEdit;
	protected List<String> list;
	protected ArrayAdapter<String> adapter;
	protected GroupSelectDialog dialog;
	
	protected GroupData groupData;
	protected GroupInfo currentGroupInfo;
	protected TagData tagData;
	protected TagInfo currentTagInfo;
	
	protected String currentCategory;
	
	protected static final int NOTIFY_DATA_CHANGE = 1;
	protected static final int SHOW_TOAST = 2;
	
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {
			if(msg.what == NOTIFY_DATA_CHANGE) {
				adapter.notifyDataSetChanged();
				return;
			}
			
			if(msg.what == SHOW_TOAST) {
				String message = (String)(msg.obj);
				if(message != null && message.length() > 0) {
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group);
		
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
		
		groupNameEdit = (EditText)findViewById(R.id.groupedit);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		Log.d(TAG, "onListItemClick : " + currentCategory);
		if(currentCategory.equals(CATEGORY_GROUP)) {
			if(position == groupData.size()) {
				allListClicked(position);
				list.clear();
				adapter.notifyDataSetChanged();
			} else {
				groupListClicked(position);
			}
		}
		
		if(currentCategory.equals(CATEGORY_TAG)) {
			tagListClicked(position);
		}
		
		if(currentCategory.equals(CATEGORY_ALL)) {
			allListClicked(position);
		}
	}
	
	public void onCreateGroup(View v) {
		createGroup(groupNameEdit.getText().toString());
		InputMethodManager imm = (InputMethodManager)(getSystemService(Context.INPUT_METHOD_SERVICE)); 
		imm.hideSoftInputFromWindow(groupNameEdit.getWindowToken(), 0);
	}
	
	public void onRequestGroup(View v) {
		currentCategory = CATEGORY_GROUP;
		requestGroupList();
	}
	
	public void onRequestTag(View v) {
		currentCategory = CATEGORY_TAG;
		requestTagList();
	}
	
	protected abstract void allListClicked(final int position);
	protected abstract void groupListClicked(final int position);
	protected abstract void tagListClicked(final int position);
	protected abstract void createGroup(String name);
	protected abstract void requestGroupList();
	protected abstract void requestTagList();
	protected abstract void deleteGroup(final int position);
	protected abstract void modifyGroup(String name);
}
