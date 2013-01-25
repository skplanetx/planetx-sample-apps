package com.skp.opx.mul.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.skp.opx.mul.R;
import com.skp.opx.sns.sl.SnsLoginHandler;
import com.skp.opx.sns.sl.SnsManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.openplatform.android.sdk.oauth.OAuthInfoManager;
import com.skp.openplatform.android.sdk.oauth.OAuthListener;
import com.skp.opx.core.client.Define;
import com.skp.opx.mul.entity.EntityImage;
import com.skp.opx.mul.util.IntentUtil;
import com.skp.opx.mul.util.PreferenceUtil;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.ErrorMessage;
import com.skp.opx.sdk.OAuthenticate;
import com.skp.opx.sdk.OnEntityParseComplete;

/**
 * @설명 : 사진멀티포스팅 Main Activity
 * @클래스명 : HomeActivity
 * 
 */
public class HomeActivity extends CommonActivity implements RadioGroup.OnCheckedChangeListener {

	private ArrayList<EntityImage> mImageList = new ArrayList<EntityImage>();
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private TextView mPicNumber;

	private final int		REQUEST_ALBUM = 1;
	private RadioGroup 		mTabRadioGroup;
	private	RadioButton		mRbTcloud;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);	

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build()); 

		getAuth();
		initWidgets();
		imageCacheIn();
		new AccessHandler(this).loginMultiSnsAccess(SnsManager.isAutoLoginFacebook(this), SnsManager.isAutoLoginTwitter(this), SnsManager.isAutoLoginGooglePlus(this));
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if(PreferenceUtil.getOneIDAccessToken(HomeActivity.this) != null && PreferenceUtil.getOneIDAccessToken(HomeActivity.this).length() > 0) {
					requestImages();
				}
			}
		}, 1000);
	}

	/**
	 * Init View
	 * */
	private void initWidgets() {
		
		mTabRadioGroup = (RadioGroup)findViewById(R.id.main_rg);
		mTabRadioGroup.setOnCheckedChangeListener(this);
		mRbTcloud	= (RadioButton)findViewById(R.id.tcloud_rb);
	}

	public void imageCacheIn() {

		mPicNumber = (TextView)findViewById(R.id.num_pic);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(HomeActivity.this)
		.threadPoolSize(3)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.memoryCacheSize(1500000) // 1.5 Mb
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.enableLogging()
		.build();
		// 이미지 로더 초기화
		ImageLoader.getInstance().init(config);	
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
	}

	/** 
	 * Ouath 처리
	 * */
	private void getAuth(){

		OAuthenticate.privateAuthenticate(HomeActivity.this, "tcloud,cyworld,user", Define.OAuth.KEY, Define.OAuth.CLIENT_ID, Define.OAuth.SECRET, new OAuthListener() {

			@Override
			public void onError(String errorMessage) {

				ErrorMessage.showErrorDialog(HomeActivity.this, errorMessage);
			}

			@Override
			public void onComplete(String result) {

				PreferenceUtil.setOneIDAccessToken(HomeActivity.this, OAuthInfoManager.authorInfo.accessToken);
				requestImages();
			}
		});
	}

	/**
	 * @설명 : T cloud 사진 목록 조회 및 검색
	 * @RequestURI : https://apis.skplanetx.com/tcloud/images
	 */
	private void requestImages() {
		
		//Querystring Parameters
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", 50);	//조회할 페이지의 사진 수를 지정합니다.
		
		//Bundle 설정
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.TCLOUD_IMAGE_SEARCH_URI, map);

		try {
			//API 호출
			AsyncRequester.request(this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityImage(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					ArrayList<EntityImage> array = (ArrayList<EntityImage>)entityArray;

					if(array.size()<=0){
						return;
					}
					
					if(array.size() > 0) {
						findViewById(android.R.id.empty).setVisibility(View.GONE);
					}  else {
						findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
					}

					mImageList =array;
					mPicNumber.setText(mImageList.size() +" "+ getString(R.string.list_ctn));
					GridView gridView = (GridView) findViewById(android.R.id.list);
					gridView.setAdapter(new ImageAdapter());
					gridView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {							

							Intent intent = new Intent(getApplicationContext(), PostingActivity.class);
							String url  = mImageList.get(position).getDownloadUrl();
							intent.putExtra("url", url);
							intent.putExtra("verifier", "Tcloud");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					});
				}

			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mImageList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			imageLoader.displayImage(mImageList.get(position).getThumbnailUrl(), imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
					imageView.setAnimation(anim);
					anim.start();
				}
			});

			return imageView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		setMenuItem(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {
		case MENU_MYPOSTING:
			IntentUtil.moveIntent(HomeActivity.this, MypostingListActivity.class);
			return true;
		case MENU_ACCOUNTSET:
			IntentUtil.moveIntent(HomeActivity.this, AccountSettingActivity.class);
			return true;
		}
		return false;
	}

	protected void setMenuItem(Menu menu) {

		menu.add(0,MENU_MYPOSTING,0,getString(R.string.myposting));
		menu.add(0,MENU_ACCOUNTSET,0,getString(R.string.account_manage));
		super.setMenuItem(menu);
	}

	private class AccessHandler extends SnsLoginHandler {

		public AccessHandler(Activity activity) {super(activity, Define.Twitter_Key.TWITTER_CONSUMER_KEY, Define.Twitter_Key.TWITTER_CONSUMER_SECRET);}

		@Override
		protected void onMultiLoginComplete() {}

		@Override
		protected void onCompleteFacebook() {}

		@Override
		protected void onCompleteTwitter() {}

		@Override
		protected void onCompleteGogglePlus() {}
	}



	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.tcloud_rb: 
			imageCacheIn();
			break;
		case R.id.gallery_rb: 
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(Images.Media.CONTENT_TYPE);
			intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQUEST_ALBUM);

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					((RadioButton)findViewById(R.id.tcloud_rb)).setChecked(true);
					((RadioButton)findViewById(R.id.gallery_rb)).setChecked(false);
				}
			}, 1000);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode != RESULT_OK)
			return;

		if(requestCode == REQUEST_ALBUM) {
			getRealPathFromURI(this, data.getData());
		}
	}


	public void  getRealPathFromURI(Context context , Uri contentUri) {

		String filePath = "";

		String[] uriPaths = contentUri.getPath().split("/");
		boolean isImage = false;
		for (int i=0; i<uriPaths.length; i++) {

			if ("images".equals(uriPaths[i])) {
				isImage = true;
				break;
			}
		}

		if (isImage) {
			String [] proj={MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION,MediaStore.Images.Media._ID };
			Cursor cursor = context.getContentResolver().query( contentUri,
					proj,		// Which columns to return
					null,       // WHERE clause; which rows to return (all rows)
					null,       // WHERE clause selection arguments (none)
					null);		// Order-by clause (ascending by name)

			if(cursor != null){
				try {
					cursor.moveToFirst();
					filePath = cursor.getString( cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)); 	              
				} catch(Exception e){
					e.printStackTrace();
				} finally{
					if(null != cursor){
						cursor.close();
						cursor = null;
					}
				}
			}
		}

		Intent intent=new Intent(HomeActivity.this, PostingActivity.class);
		intent.putExtra("url", filePath.toString());
		intent.putExtra("verifier", "album");
		startActivity(intent);
	}


}
