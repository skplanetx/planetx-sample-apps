package com.tcloud.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.tcloud.openapi.data.GroupData;
import com.tcloud.openapi.data.MetaDatas.Type;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MapUtil;
import com.tcloud.openapi.util.Util;

public class ListSelectDialog extends Dialog {
	public interface listDialogOnclickListener {
		public void onAddButton(String selectGroupId);
		public void onDelButton();
		public void onDetailButton();
	}
	
	public static final String TAG = ListSelectDialog.class.getSimpleName();
	private ArrayAdapter<String> adapter;
	private List<String> list;
	private ListView listView;
	private String groupId;
	private GroupData groupData;
	private String contentId;
	private Type type;
	private String selectGroupId;
	
	protected static final int NOTIFY_DATA_CHANGE = 1;
	
	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {
			if(msg.what == NOTIFY_DATA_CHANGE) {
				adapter.notifyDataSetChanged();
				return;
			}
		}
	};		
	
	
	private listDialogOnclickListener listener;
	
	public void setOnClickListener(listDialogOnclickListener listener) {
		this.listener = listener;
	}
	
	public ListSelectDialog(final Context context, String groupId, String contentId, Type type) {
		super(context);
		setContentView(R.layout.listselect);
		this.groupId = groupId;
		this.contentId = contentId;
		this.type = type;
		
		switch(type) {
		case Photo:
			setTitle("Photo group list");
			break;
		case Music:
			setTitle("Music group list");
			break;
		case Video:
			setTitle("Video group list");
			break;
		case Document:
			setTitle("Document group list");
			break;
		}
		
		Button addGroupButton = (Button)findViewById(R.id.addlistgroupbutton);
		addGroupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.addlistgroupbutton) {
					listener.onAddButton(selectGroupId);
				}
			}
		});
		
		Button detailViewButton = (Button)findViewById(R.id.listdetailbutton);
		detailViewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onDetailButton();
			}
		});
		
		Button delGroupButton = (Button)findViewById(R.id.dellistgroupbutton);
		delGroupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onDelButton();
			}
		});
		
		list = new ArrayList<String>();
		listView = (ListView)findViewById(R.id.dialoglist);
		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice, list);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listView.setSelection(position);
				selectGroupId = groupData.get(position).groupId;
			}
		});
		requestGroup();
	}
	
	public void requestGroup() {
		if(type == Type.Photo) {
			
			String url = Const.SERVER_URL + "/image/groups?version=" + Const.API_VERSION;
			RequestBundle requestBundle = new RequestBundle();
			requestBundle.setHttpMethod(HttpMethod.GET);
			requestBundle.setResponseType(CONTENT_TYPE.XML);
			requestBundle.setUrl(url);
			
			Util.printRequest(url, null);	
			
			RequestListener requestListener = new RequestListener() {

				@Override
				public void onComplete(ResponseMessage result) {
					try {
						Map<String, ?> entity = XmlExtractor.parse(result.toString());
						groupData = MapUtil.getGroupData(entity);
						for(int i = 0; i < groupData.size(); i++) {
							if(groupData.get(i).groupId == groupId)
								continue;
							list.add(i, groupData.get(i).name);
						}
						handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
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
		
		if(type == Type.Music) {
//			api.musicGrouplistWithParameters(param, new AsyncInquiryListener() {
//				
//				@Override
//				public void onError(Exception e) {
//					e.printStackTrace();
//				}
//				
//				@Override
//				public void onComplete(Map<String, ?> response) {
//					groupData = MapUtil.getGroupData(response);
//					for(int i = 0; i < groupData.size(); i++) {
//						if(groupData.get(i).groupId == groupId)
//							continue;
//						list.add(i, groupData.get(i).name);
//					}
//					adapter.notifyDataSetChanged();
//				}
//			});
			String url = Const.SERVER_URL + "/music/groups?version=" + Const.API_VERSION;
			RequestBundle requestBundle = new RequestBundle();
			requestBundle.setHttpMethod(HttpMethod.GET);
			requestBundle.setResponseType(CONTENT_TYPE.XML);
			requestBundle.setUrl(url);
			
			Util.printRequest(url, null);	
			
			RequestListener requestListener = new RequestListener() {

				@Override
				public void onComplete(ResponseMessage result) {
					try {
						Map<String, ?> entity = XmlExtractor.parse(result.toString());
						groupData = MapUtil.getGroupData(entity);
						for(int i = 0; i < groupData.size(); i++) {
							if(groupData.get(i).groupId == groupId)
								continue;
							list.add(i, groupData.get(i).name);
						}
						handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
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
		
		if(type == Type.Video) {
//			api.movieGrouplistWithParameters(param, new AsyncInquiryListener() {
//				
//				@Override
//				public void onError(Exception e) {
//					e.printStackTrace();
//				}
//				
//				@Override
//				public void onComplete(Map<String, ?> response) {
//					groupData = MapUtil.getGroupData(response);
//					for(int i = 0; i < groupData.size(); i++) {
//						if(groupData.get(i).groupId == groupId)
//							continue;
//						list.add(i, groupData.get(i).name);
//					}
//					adapter.notifyDataSetChanged();
//				}
//			});
			
			String url = Const.SERVER_URL + "/movie/groups?version=" + Const.API_VERSION;
			RequestBundle requestBundle = new RequestBundle();
			requestBundle.setHttpMethod(HttpMethod.GET);
			requestBundle.setResponseType(CONTENT_TYPE.XML);
			requestBundle.setUrl(url);
			
			Util.printRequest(url, null);	
			
			RequestListener requestListener = new RequestListener() {

				@Override
				public void onComplete(ResponseMessage result) {
					try {
						Map<String, ?> entity = XmlExtractor.parse(result.toString());
						groupData = MapUtil.getGroupData(entity);
						for(int i = 0; i < groupData.size(); i++) {
							if(groupData.get(i).groupId == groupId)
								continue;
							list.add(i, groupData.get(i).name);
						}
						handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
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

		if(type == Type.Document) {
			String url = Const.SERVER_URL + "/document/groups?version=" + Const.API_VERSION;
			RequestBundle requestBundle = new RequestBundle();
			requestBundle.setHttpMethod(HttpMethod.GET);
			requestBundle.setResponseType(CONTENT_TYPE.XML);
			requestBundle.setUrl(url);
			
			Util.printRequest(url, null);	
			
			RequestListener requestListener = new RequestListener() {

				@Override
				public void onComplete(ResponseMessage result) {
					try {
						Map<String, ?> entity = XmlExtractor.parse(result.toString());
						groupData = MapUtil.getGroupData(entity);
						for(int i = 0; i < groupData.size(); i++) {
							if(groupData.get(i).groupId == groupId)
								continue;
							list.add(i, groupData.get(i).name);
						}
						handler.sendMessage(Message.obtain(handler, NOTIFY_DATA_CHANGE));
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
	}
}
