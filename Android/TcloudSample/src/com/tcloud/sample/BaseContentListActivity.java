package com.tcloud.sample;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tcloud.openapi.data.GroupInfo;
import com.tcloud.openapi.data.MetaData;
import com.tcloud.openapi.data.MetaDatas;
import com.tcloud.openapi.data.TagInfo;

public abstract class BaseContentListActivity<T extends MetaData> extends ListActivity {
	public static final String TAG = BaseContentListActivity.class.getSimpleName();
	protected List<String> list;
	protected ArrayAdapter<String> adapter;
	protected MetaDatas<T> datas;
	protected MetaDatas<T> selectedDatas;
	protected EditText searchEdit;
	protected ListSelectDialog dialog;
	
	protected T currentData;
	protected String groupId;
	protected String tagId;
	protected String category;
	
	protected MetaDatas.Type type;
	
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
		setContentView(R.layout.basecontentlist);
		initType();
		
		searchEdit = (EditText)findViewById(R.id.searchedit);
		
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

		Intent intent = getIntent();
		category = intent.getStringExtra(BaseGroupActivity.CATEGORY);
		
		if(category.equals(BaseGroupActivity.CATEGORY_GROUP)) {
			groupId = intent.getStringExtra(GroupInfo.GROUP_ID);
		}
		
		if(category.equals(BaseGroupActivity.CATEGORY_TAG)) {
			tagId = intent.getStringExtra(TagInfo.TAG_ID);
		}
		
		setListAdapter(adapter);
	}
	
	public void onSearchList(View v) {
		Log.d(TAG, "onSearchList call");
		list.clear();
		searchList();
	}
	
	public void onRequestList(View v) {
		requestContentList();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		if(category.equals(BaseGroupActivity.CATEGORY_ALL)) 	allListClicked(position);
		if(category.equals(BaseGroupActivity.CATEGORY_GROUP))	groupListClicked(position);
		if(category.equals(BaseGroupActivity.CATEGORY_TAG))		tagListClicked(position);
	}
	
	private void allListClicked(final int position) {
		currentData = datas.get(position);
		Log.d(TAG, "allListClicked : " + currentData.name + "-" + currentData.objectId);
		dialog = new ListSelectDialog(this, groupId, currentData.objectId, type);
		dialog.setOnClickListener(new ListSelectDialog.listDialogOnclickListener() {
			
			@Override
			public void onDetailButton() {
				detailViewActivity(position);
			}
			
			@Override
			public void onDelButton() {
//				delContentFromGroup();
				deleteContent();
				
			}
			
			@Override
			public void onAddButton(String selectGroupId) {
				addContentToGroup(selectGroupId);
			}
		});
		dialog.show();
	}	
	
	private void groupListClicked(final int position) {
		Log.d(TAG, "onListItemClick : " + position);
		currentData = datas.get(position);
		dialog = new ListSelectDialog(this, groupId, currentData.objectId, type);
		dialog.setOnClickListener(new ListSelectDialog.listDialogOnclickListener() {
			
			@Override
			public void onAddButton(String selectGroupId) {
				Log.d(TAG, "list dialog onclick : " + selectGroupId);
				if(selectGroupId == null) {
					Toast.makeText(getApplicationContext(), "전체 파일 목록을 가져옵니다.", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					return;
				}
				addContentToGroup(selectGroupId);
			}

			@Override
			public void onDetailButton() {
				detailViewActivity(position);
			}

			@Override
			public void onDelButton() {
				deleteContent();
			}
		});
		dialog.show();		
	}
	
	private void tagListClicked(final int position) {
		
	}
	
	protected void requestContentList() {
		list.clear();
		if(category.equals(BaseGroupActivity.CATEGORY_ALL))
			requestAllContentList();
		if(category.equals(BaseGroupActivity.CATEGORY_GROUP))
			requestGroupContentList();
		if(category.equals(BaseGroupActivity.CATEGORY_TAG))
			requestTagContentList();
		
	}
	
	private void deleteContent() {
		if(category.equals(BaseGroupActivity.CATEGORY_ALL))
			deleteFromAll();
		if(category.equals(BaseGroupActivity.CATEGORY_GROUP))
			deleteFromGroup();
		if(category.equals(BaseGroupActivity.CATEGORY_TAG))
			deleteFromTag();
	}
	
	protected abstract void initType();
	protected abstract void searchList();
	protected abstract void detailViewActivity(final int position);
//	protected abstract void delContentFromGroup();
	protected abstract void addContentToGroup(String id);
	
	protected abstract void requestAllContentList();
	protected abstract void requestGroupContentList();
	protected abstract void requestTagContentList();
	
	protected abstract void deleteFromAll();
	protected abstract void deleteFromGroup();
	protected abstract void deleteFromTag();
	
}
