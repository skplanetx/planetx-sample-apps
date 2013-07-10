package com.tcloud.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.jdom.JDOMException;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.tcloud.openapi.data.GroupData;
import com.tcloud.openapi.data.GroupInfo;
import com.tcloud.openapi.data.TagInfo;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MapUtil;
import com.tcloud.openapi.util.Util;
import com.tcloud.openapi.util.XMLUtil;
import com.tcloud.sample.GroupSelectDialog.buttonType;
import com.tcloud.sample.GroupSelectDialog.dialogOnClickListener;

public class MovieGroupActivity extends BaseGroupActivity {
	public static final String TAG = MovieGroupActivity.class.getSimpleName();

	@Override
	protected void allListClicked(int position) {
		Log.d(TAG, "allListClicked");
		currentGroupInfo = new GroupInfo();
		currentGroupInfo.groupId = "0";
		currentGroupInfo.modifiedDate = "0";
		currentGroupInfo.createdDate = "0";
		currentCategory = BaseGroupActivity.CATEGORY_ALL;
		Intent intent = new Intent(getApplicationContext(), MovieListActivity.class);
		intent.putExtra(CATEGORY, currentCategory);
		intent.putExtra(GroupInfo.GROUP_ID, currentGroupInfo.groupId);
		startActivity(intent);			
	}

	@Override
	protected void groupListClicked(final int position) {
		Log.d(TAG, "onListItemClick : " + position + " - " + groupData.size());
		currentGroupInfo = groupData.get(position);
		
		dialog = new GroupSelectDialog(this);
		dialog.setTitle(currentGroupInfo.name);
		dialog.setOnClickListener(new dialogOnClickListener() {
			@Override
			public void onClick(buttonType type) {
				if(type == buttonType.View) {
					Intent intent = new Intent(MovieGroupActivity.this, MovieListActivity.class);
					Log.d(TAG, "group list item : " + currentGroupInfo.groupId);
					intent.putExtra(CATEGORY, currentCategory);
					intent.putExtra(GroupInfo.GROUP_ID, currentGroupInfo.groupId);
					startActivity(intent);
					dialog.dismiss();
					list.clear();
					adapter.notifyDataSetChanged();
				}

				if(type == buttonType.Delete) {
					deleteGroup(position);
				}

				if(type == buttonType.Modify) {
					modifyGroup(dialog.getModifyName());
				}
			}
		});
		dialog.show();
		
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		dialog.getWindow().setLayout(display.getWidth(), display.getHeight()/3);		
	}

	@Override
	protected void tagListClicked(int position) {
		currentTagInfo = tagData.get(position);
		Intent intent = new Intent(getApplicationContext(), MovieListActivity.class);
		intent.putExtra(CATEGORY, currentCategory);
		intent.putExtra(TagInfo.CREATED_DATE, currentTagInfo.createdDate);
		intent.putExtra(TagInfo.MODIFIED_DATE, currentTagInfo.modifiedDate);
		intent.putExtra(TagInfo.NAME, currentTagInfo.name);
		intent.putExtra(TagInfo.TAG_ID, currentTagInfo.tagId);
		startActivity(intent);		
	}

	@Override
	protected void createGroup(String name) {
		String url = Const.SERVER_URL + "/movie/groups?version=" + Const.API_VERSION;
		String payload = XMLUtil.createGroupPayLoad(name);
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.POST);
		requestBundle.setRequestType(CONTENT_TYPE.XML);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		requestBundle.setPayload(payload);
		
		Util.printRequest(url, payload);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				Log.d(TAG, "onComplete : " + result.toString());
				requestGroupList();
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "생성 성공 : " + result.toString()));
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				Log.d(TAG, "onPlanetSDKException " + e);
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "생성 실패 : " + e.getMessage()));
			}
		};
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void requestGroupList() {
		list.clear();
		
		String url = Const.SERVER_URL + "/movie/groups?version=" + Const.API_VERSION;
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		
		Util.printRequest(url, null);

		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				Log.d(TAG, "onComplete : " + result.toString());
				try {
					Map<String, ?> entity = XmlExtractor.parse(result.toString());
					groupData = MapUtil.getGroupData(getApplicationContext(), entity);
					if(groupData.size() == 0) {
						currentCategory = CATEGORY_ALL;
						list.add(0, CATEGORY_ALL);
						handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
						return;
					}
					int i = 0;
					for(i = 0; i <groupData.size(); i++) {
						list.add(i, groupData.get(i).name);
					}
					list.add(i, CATEGORY_ALL);
					handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
					currentCategory = GroupData.GROUP;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				e.printStackTrace();
			}

		};		
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void requestTagList() {
		list.clear();
		
		String url = Const.SERVER_URL + "/movie/tags?version=" + Const.API_VERSION;
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		
		Util.printRequest(url, null);

		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				Log.d(TAG, "onComplete : " + result.toString());
				try {
					Map<String, ?> entity = XmlExtractor.parse(result.toString());
					tagData = MapUtil.getTagData(getApplicationContext(), entity);
					if(tagData.size() == 0) {
						handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "조회 결과가 없습니다."));
						handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
						return;
					}

					for(int i = 0; i <tagData.size(); i++) {
						list.add(i, tagData.get(i).name);
					}
					handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					list.clear();
					handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
					handler.sendMessage(Message.obtain(handler, SHOW_TOAST, e.getMessage()));
				} catch (JDOMException e) {
					e.printStackTrace();
					list.clear();
					handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
					handler.sendMessage(Message.obtain(handler, SHOW_TOAST, e.getMessage()));
				} catch (IOException e) {
					e.printStackTrace();
					list.clear();
					handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
					handler.sendMessage(Message.obtain(handler, SHOW_TOAST, e.getMessage()));
				}
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				e.printStackTrace();
			}

		};		
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void deleteGroup(int position) {
		
		String url = Const.SERVER_URL + "/movie/groups/" + currentGroupInfo.groupId + "?version=" + Const.API_VERSION;
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.DELETE);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		
		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				Log.d(TAG, "onComplete : " + result.toString());
				requestGroupList();
				dialog.dismiss();
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				Log.d(TAG, "onPlanetSDKException " + e);
				dialog.dismiss();
			}
		};
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void modifyGroup(String name) {
		
		String url = Const.SERVER_URL + "/movie/groups/" + currentGroupInfo.groupId + "?version=" + Const.API_VERSION;
		String payload = XMLUtil.createGroupPayLoad(name);
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.PUT);
		requestBundle.setRequestType(CONTENT_TYPE.XML);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		requestBundle.setPayload(payload);
		
		Util.printRequest(url, payload);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				Log.d(TAG, "onComplete : " + result.toString());
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "수정 완료 "));
				requestGroupList();
				dialog.dismiss();
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				Log.d(TAG, "onPlanetSDKException " + e);
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "수정 실패 : " + e.getMessage()));
			}
		};		
		
		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e1) {
			e1.printStackTrace();
		}		
	}

}
