package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.Entity11stProducts;
import com.skp.opx.svc.entity.Entity11stSearchResult;
import com.skp.opx.svc.utils.ImageDownloader;

/**
 * @설명 : 11번가 상품 상세 Activity
 * @클래스명 : St11DetailActivity
 * 
 */
public class St11DetailActivity extends Activity implements OnClickListener{

	private ImageView mImageIv;
	private TextView mProductTv;
	private TextView mSatisfyTv;
	private Button   mReviewBt;
	private TextView mPriceTv;
	private TextView mLowestPriceTv;
	private TextView mPointTv;
	private TextView mCreditTv;

	private ArrayList<Entity11stProducts> mDetailArray;
	private Entity11stSearchResult mProductInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_eleven_st_detail);

		mProductInfo = (Entity11stSearchResult)getIntent().getSerializableExtra("ENTITY");

		initWidgets();
		setProductDetail(mProductInfo.ProductCode);
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		findViewById(R.id.back_bt).setOnClickListener(this);
		findViewById(R.id.postscipt_review_bt).setOnClickListener(this);
		mImageIv = (ImageView)findViewById(R.id.image_iv);
		mProductTv = (TextView)findViewById(R.id.product_tv);
		mSatisfyTv = (TextView)findViewById(R.id.satisfy_tv);
		mReviewBt = (Button)findViewById(R.id.postscipt_review_bt);
		mPriceTv = (TextView)findViewById(R.id.price_tv);
		mLowestPriceTv = (TextView)findViewById(R.id.sale_tv);
		mPointTv = (TextView)findViewById(R.id.point_tv);
		mCreditTv = (TextView)findViewById(R.id.creditcard_tv);

	}

	/**
	 * @설명 : 11번가 상품정보 조회
	 * @RequestURI : http://apis.skplanetx.com/11st/common/products/{productCode}
	 * @RequestPathParam :
	 * {productCode} 상품 코드 입니다
	 */
	private void setProductDetail(int code){

		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("option", "Product"); //부가 정보를 지정합니다.

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.ELEVEN_ST_PRODUCT_SEARHC_URI + "/" + code , map);

		try {
			//API 호출
			AsyncRequester.request(St11DetailActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new Entity11stProducts(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mDetailArray = (ArrayList<Entity11stProducts>)entityArray;

					ImageDownloader.download(mProductInfo.ProductImage300, mImageIv);
					mProductTv.setText(mDetailArray.get(0).ProductName);
					mSatisfyTv.setText(getString(R.string.product_satis)+ mProductInfo.BuySatisfy + "%");
					mReviewBt.setText(String.valueOf(getString(R.string.postscipt_review) + mProductInfo.ReviewCount + getString(R.string.cnt)));
					mPriceTv.setText(mDetailArray.get(0).Price);
					mLowestPriceTv.setText(mDetailArray.get(0).LowestPrice);
					mPointTv.setText(Integer.toString(mDetailArray.get(0).Point) + getString(R.string.point));
					mCreditTv.setText(mDetailArray.get(0).Installment);
				}
			}));

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.back_bt :
			finish();
			break;
		case R.id.postscipt_review_bt :
			Intent intent_review = new Intent(St11DetailActivity.this, St11PostscriptReviewListActivity.class );
			intent_review.putExtra("ENTITY", mProductInfo);
			startActivity( intent_review );
			break;

		}
	} 

}
