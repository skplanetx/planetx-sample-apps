package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.Entity11stPostScript;
import com.skp.opx.svc.entity.Entity11stReviews;
import com.skp.opx.svc.entity.Entity11stSearchResult;
import com.skp.opx.svc.ui.adapter.Adapter_11stPostscript;
import com.skp.opx.svc.ui.adapter.Adapter_11stReview;

/**
 * @설명 : 11번가 후기/리뷰 Activity
 * @클래스명 : St11PostscriptReviewListActivity
 * 
 */
public class St11PostscriptReviewListActivity extends ListActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener{

	private Adapter_11stPostscript mPostscriptAdapter;
	private Adapter_11stReview mReviewAdapter;
	private ArrayList<Entity11stPostScript> mPostscriptArray; 
	private ArrayList<Entity11stReviews> mReviewArray;
	private Entity11stSearchResult mProductInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eleven_st_postscript_review);

		mProductInfo = (Entity11stSearchResult)getIntent().getSerializableExtra("ENTITY");

		initWidgets();
		initProductPostscriptList();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		RadioGroup tabRadioGroup = (RadioGroup)findViewById(R.id.postscript_review_rg);
		tabRadioGroup.setOnCheckedChangeListener(this);
		findViewById(R.id.back_bt).setOnClickListener(this);

		mPostscriptAdapter = new Adapter_11stPostscript(this);
		mReviewAdapter= new Adapter_11stReview(this);
	}

	/**
	 * @설명 : 11번가  상품정보 조회
	 * @RequestURI : http://apis.skplanetx.com/11st/common/products/{productCode}
	 * @RequestPathParam :
	 * {productCode} 상품 코드 입니다
	 */
	private void initProductPostscriptList() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("option", "PostScripts"); //부가 정보를 지정합니다.

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.ELEVEN_ST_PRODUCT_SEARHC_URI +  "/" + mProductInfo.ProductCode , map);
		try {
			//API 호출
			AsyncRequester.request(St11PostscriptReviewListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stPostScript(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mPostscriptArray = (ArrayList<Entity11stPostScript>)entityArray;
					mPostscriptAdapter.set11stPostscriptList(mPostscriptArray);
					setListAdapter(mPostscriptAdapter);
					mPostscriptAdapter.notifyDataSetChanged();
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @설명 : 11번가  상품정보 조회
	 * @RequestURI : http://apis.skplanetx.com/11st/common/products/{productCode}
	 */
	private void initProductReviewList() {

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("option", "SemiReviews"); //부가 정보를 지정합니다.

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.ELEVEN_ST_PRODUCT_SEARHC_URI + "/" +  mProductInfo.ProductCode , map);

		try {
			//API 호출
			AsyncRequester.request(St11PostscriptReviewListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stReviews(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mReviewArray = (ArrayList<Entity11stReviews>)entityArray;
					mReviewAdapter.set11stReviewList(mReviewArray);
					setListAdapter(mReviewAdapter);
					mReviewAdapter.notifyDataSetChanged();
				}
			}));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch( v.getId() ){
		case R.id.back_bt :
			finish();
			break;
		}
	} 

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.postscript_rb: //후기
			initProductPostscriptList();
			break;
		case R.id.review_rb: //리뷰
			initProductReviewList();
			break;
		}
	} 

}
