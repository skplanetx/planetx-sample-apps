package com.tcloud.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.jdom.JDOMException;

import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.HttpMethod;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.RequestListener;
import com.skp.openplatform.android.sdk.common.ResponseMessage;
import com.tcloud.openapi.data.MetaData;
import com.tcloud.openapi.data.MetaDatas;
import com.tcloud.openapi.data.MusicData;
import com.tcloud.openapi.data.extract.XmlExtractor;
import com.tcloud.openapi.network.Const;
import com.tcloud.openapi.util.MetaDataUtil;
import com.tcloud.openapi.util.Util;
import com.tcloud.openapi.util.XMLUtil;

public class MusicListActivity extends BaseContentListActivity<MusicData> {
	@Override
	protected void initType() {
		type = MetaDatas.Type.Music;
	}
	@Override
	protected void searchList() {
		Log.d(TAG, "searchList call");
		
		String url = Const.SERVER_URL + "/music?version=" + Const.API_VERSION 
				+ "&page=1&count=10&searchkeyword=" + searchEdit.getText().toString()
				+ "&searchtype=0";
		
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setRequestType(CONTENT_TYPE.XML);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);

		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {
			@Override
			public void onComplete(ResponseMessage result) {
				try {
					Map<String, ?> entity = XmlExtractor.parse(result.toString());
					datas = MetaDataUtil.getMusicDatas(getApplicationContext(), entity);
					Log.d(TAG, "죄회 결과 갯수 : " + datas.dataCount());
					if(datas.dataCount() == 0 ) {
						handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "조회 결과가 없습니다."));
					}
					for(int i = 0; i < datas.dataCount(); i++) {
						list.add(i, datas.get(i).name);
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

	@Override
	protected void detailViewActivity(int position) {
		String url = Const.SERVER_URL + "/music/" + currentData.objectId + "?version=" + Const.API_VERSION;
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
					currentData = MetaDataUtil.getMusicData(MusicListActivity.this, (Map<String, String>)entity);
					Intent intent = new Intent(MusicListActivity.this, MusicDetailActivity.class);
					intent.putExtra(MetaData.NAME, currentData.name);
					intent.putExtra(MetaData.CREATED_DATE, currentData.createdDate);
					intent.putExtra(MetaData.MODIFIED_DATE, currentData.modifiedDate);
					intent.putExtra(MetaData.PATH, currentData.path);
					intent.putExtra(MetaData.SIZE, currentData.size);
					intent.putExtra(MetaData.DOWNLOAD_URL, currentData.downloadUrl);
					intent.putExtra(MetaData.RESOLUTION, currentData.resolution);
					intent.putExtra(MetaData.THUMBNAIL_URL, currentData.thumbnailUrl);
					intent.putExtra(MetaData.OBJECT_ID, currentData.objectId);
					intent.putExtra(MusicData.MUSIC_ID, currentData.musicId);
//					
					startActivity(intent);						
					dialog.dismiss();
					
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
	protected void addContentToGroup(String id) {
		Log.d(TAG, "addContentToGroup---");
		
		String url = Const.SERVER_URL + "/music/groups/" + id + "/music?version=" + Const.API_VERSION;
		String payload = XMLUtil.createIdPayLoad("objectId", currentData.objectId);
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
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "그룹에 추가 성공"));
				requestContentList();
				dialog.dismiss();
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "그룹에 추가 실패"));
			}
			
		};

		APIRequest api = new APIRequest();
		try {
			api.request(requestBundle, requestListener);
		} catch (PlanetXSDKException e1) {
			e1.printStackTrace();
		}		
	}

	@Override
	protected void requestAllContentList() {
		Log.d(TAG, "requestAllContentList---");
		
		String url = Const.SERVER_URL + "/music?version=" + Const.API_VERSION 
				+ "&page=1&count=10";
		
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setRequestType(CONTENT_TYPE.XML);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);

		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {
			@Override
			public void onComplete(ResponseMessage result) {
				try {
					Map<String, ?> entity = XmlExtractor.parse(result.toString());
					datas = MetaDataUtil.getMusicDatas(getApplicationContext(), entity);
					Log.d(TAG, "죄회 결과 갯수 : " + datas.dataCount());
					if(datas.dataCount() == 0 ) {
						handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "조회 결과가 없습니다."));
					}
					for(int i = 0; i < datas.dataCount(); i++) {
						list.add(i, datas.get(i).name);
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

	@Override
	protected void requestGroupContentList() {
		list.clear();
		
		String url = Const.SERVER_URL + "/music/groups/" + groupId + "?version=" + Const.API_VERSION 
				+ "&page=1&count=10";
		
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.GET);
		requestBundle.setRequestType(CONTENT_TYPE.XML);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);

		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {
			@Override
			public void onComplete(ResponseMessage result) {
				if(result == null || result.toString().length() <= 0)
					return;
				try {
					Map<String, ?> entity = XmlExtractor.parse(result.toString());
					datas = MetaDataUtil.getMusicDatas(getApplicationContext(), entity);
					Log.d(TAG, "죄회 결과 갯수 : " + datas.dataCount());
					if(datas.dataCount() == 0 ) {
						handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "조회 결과가 없습니다."));
					}
					for(int i = 0; i < datas.dataCount(); i++) {
						list.add(i, datas.get(i).name);
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

	@Override
	protected void requestTagContentList() {
		list.clear();
		
		String url = Const.SERVER_URL + "/music/tags/" + tagId + 
				"?version=" + Const.API_VERSION + "&page=1&count=10";
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
					datas = MetaDataUtil.getMusicDatas(getApplicationContext(), entity);
					for(int i = 0; i < datas.dataCount(); i++) {
						list.add(i, datas.get(i).name);
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

	@Override
	protected void deleteFromAll() {
		
		String url = Const.SERVER_URL + "/music/" + currentData.objectId + "?version=" + Const.API_VERSION;
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.DELETE);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		
		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "메타 정보 삭제 성공"));
				requestContentList();
				dialog.dismiss();
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "메타 정보 삭제 실패"));
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
	protected void deleteFromGroup() {
		String url = Const.SERVER_URL + "/music/groups/" + groupId + "/music/" + currentData.objectId + "?version=" + Const.API_VERSION;
		RequestBundle requestBundle = new RequestBundle();
		requestBundle.setHttpMethod(HttpMethod.DELETE);
		requestBundle.setResponseType(CONTENT_TYPE.XML);
		requestBundle.setUrl(url);
		
		Util.printRequest(url, null);
		
		RequestListener requestListener = new RequestListener() {

			@Override
			public void onComplete(ResponseMessage result) {
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "그룹에서 삭제 성공"));
				requestContentList();
				dialog.dismiss();
			}

			@Override
			public void onPlanetSDKException(PlanetXSDKException e) {
				handler.sendMessage(Message.obtain(handler, SHOW_TOAST, "그룹에서 삭제 실패"));
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
	protected void deleteFromTag() {
		Log.d(TAG, "deleteFromTag call");
	}
}