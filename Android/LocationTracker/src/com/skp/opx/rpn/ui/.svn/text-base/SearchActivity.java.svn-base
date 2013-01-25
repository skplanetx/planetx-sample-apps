package com.skp.opx.rpn.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.skp.opx.rpn.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.rpn.entity.EntityAutoComplete;
import com.skp.opx.rpn.entity.EntityTotalSearch;
import com.skp.opx.rpn.ui.adapter.Adapter_AutoComplete;
import com.skp.opx.rpn.util.PreferenceUtil;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;

/**
 * @설명 : 검색 Activity
 * @클래스명 : SearchActivity 
 *
 */
public class SearchActivity extends ListActivity implements TextWatcher{

	private EditText mSearchBox;
	private ArrayList<EntityAutoComplete> mSearchList = new ArrayList<EntityAutoComplete>();
    private Adapter_AutoComplete mAutoCompleteAdapter;
    private ProgressBar mSearchProgressBar;
    private boolean mIsFromHomeSetting ;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
		 mSearchBox = (EditText)findViewById(R.id.search_box);
		 mSearchBox.addTextChangedListener(this);
		 mSearchProgressBar = (ProgressBar)findViewById(R.id.search_progress_bar);	 
		 mIsFromHomeSetting = getIntent().getBooleanExtra("isFromHomeSetting", true);
	}		
	
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
		mHandler.sendEmptyMessageDelayed(0, 600);
		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		EntityAutoComplete entity = mSearchList.get(position);
		
		String info = entity.mName + ";" + entity.x + ";" + entity.y; 
		if (mIsFromHomeSetting){
			PreferenceUtil.setHomeAsMyDestination(this, info);			
		}else{
			PreferenceUtil.setWorkPlaceAsMyDestination(this, info);
		}
		Toast.makeText(this,R.string.setting_ok, Toast.LENGTH_LONG).show();
		finish();
	}
	/**
	 * @설명 : T map POI 통합검색
	 * @RequestURI : https://apis.skplanetx.com/tmap/pois
	 */
	private void searchTotal(String keyWord){
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", "10");//요청 시 받을 검색 목록의 개수입니다
		map.put("searchKeyword", keyWord);//검색어입니다
		map.put("resCoordType", "WGS84GEO"); //받고자 하는 응답 좌표계 유형을 지정합니다
		//Bundle 설정
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.TMAP_TOTAL_SEARCH_URI, map);

		try{
			//API 호출
			AsyncRequester.request(this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityTotalSearch(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					ArrayList<EntityTotalSearch> array = (ArrayList<EntityTotalSearch>)entityArray;
					EntityAutoComplete entity = null;
					
					if(mSearchList.size()>0){
						mSearchList.clear();
					}
					for (int i = 0; i < array.size(); i++) {
						entity = new EntityAutoComplete();
						entity.mName = array.get(i).name;
						entity.x = String.valueOf(array.get(i).frontLon);
						entity.y = String.valueOf(array.get(i).frontLat);
						mSearchList.add(entity);
					}
					   mSearchProgressBar.setVisibility(View.GONE);
				       mAutoCompleteAdapter = new Adapter_AutoComplete(getApplicationContext(), R.layout.list_item_auto_complete, mSearchList);
				       setListAdapter(mAutoCompleteAdapter);
				}
				
			}),false, false);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}	
	}
	
	  private Handler mHandler = new Handler()
		{
			public void handleMessage(Message msg) {
				mSearchProgressBar.setVisibility(View.VISIBLE);
				searchTotal(mSearchBox.getText().toString());
				super.handleMessage(msg);
			}
		};
}
