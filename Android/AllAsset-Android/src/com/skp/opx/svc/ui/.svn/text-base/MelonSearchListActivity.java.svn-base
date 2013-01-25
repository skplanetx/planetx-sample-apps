package com.skp.opx.svc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.skp.opx.svc.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.Define;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.svc.entity.EntityMelonSearchAlbum;
import com.skp.opx.svc.entity.EntityMelonSearchArtist;
import com.skp.opx.svc.entity.EntityMelonSearchSong;
import com.skp.opx.svc.ui.adapter.Adapter_MelonSearchAlbum;
import com.skp.opx.svc.ui.adapter.Adapter_MelonSearchArtist;
import com.skp.opx.svc.ui.adapter.Adapter_MelonSearchSong;

/**
 * @설명 : Melon 검색 Activity
 * @클래스명 : MelonSearchListActivity
 * 
 */
public class MelonSearchListActivity extends ListActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener, OnEditorActionListener{

	private InputMethodManager mInputMethodManager;

	private Adapter_MelonSearchSong    mSearchSongAdapter;
	private Adapter_MelonSearchAlbum   mSearchAlbumAdapter;
	private Adapter_MelonSearchArtist  mSearchArtistAdpater;
	private ArrayList<EntityMelonSearchSong> mSongArray;
	private ArrayList<EntityMelonSearchAlbum> mAlbumArray;
	private ArrayList<EntityMelonSearchArtist> mArtistArray;

	private EditText mSearchEt;
	private String mServiceType;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_melon_search_list);

		initWidgets();
	}

	/** 
	 *  View 초기화 Method
	 * */
	private void initWidgets() {

		mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		RadioGroup tabRGroup = (RadioGroup)findViewById(R.id.search_rg);
		tabRGroup.setOnCheckedChangeListener(this);
		mSearchEt = (EditText)findViewById(R.id.melon_search_et);
		mSearchEt.setOnEditorActionListener(this);
		findViewById(R.id.home_bt).setOnClickListener(this);
		findViewById(R.id.edit_bt).setOnClickListener(this);
		
		mSearchSongAdapter = new Adapter_MelonSearchSong(this);
		mSearchAlbumAdapter = new Adapter_MelonSearchAlbum(this);
		mSearchArtistAdpater = new Adapter_MelonSearchArtist(this);
	}

	/**
	 * @설명 : Melon 곡검색
	 * @RequestURI : http://apis.skplanetx.com/melon/songs
	 */
	private void initSongSearchList(String keyword) {

		mServiceType = "song";

		//Querystring Parameter
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		map.put("searchKeyword", keyword); //곡 검색 키워드입니다

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_SEARCH_SONGS_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MelonSearchListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonSearchSong(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mSongArray = (ArrayList<EntityMelonSearchSong>)entityArray;

					mSearchSongAdapter.setMelonSearchList(mSongArray);
					setListAdapter(mSearchSongAdapter);
				}
			}, null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @설명 : Melon 앨범검색
	 * @RequestURI : http://apis.skplanetx.com/melon/albums
	 */
	private void initAlbumSearchList(String keyword) {

		mServiceType = "album";

		//Querystring Parameter
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		map.put("searchKeyword", keyword); //곡 검색 키워드입니다

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_SEARCH_ALBUMS_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MelonSearchListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonSearchAlbum(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mAlbumArray = (ArrayList<EntityMelonSearchAlbum>)entityArray;

					mSearchAlbumAdapter.setMelonSearchList(mAlbumArray);
					setListAdapter(mSearchAlbumAdapter);
				}
			}, null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @설명 : Melon 아티스트검색
	 * @RequestURI : http://apis.skplanetx.com/melon/artists
	 */
	private void initArtistSearchList(String keyword) {

		mServiceType = "artist";

		//Querystring Parameter
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", 0);   //조회할 목록의 페이지를 지정합니다
		map.put("count", 10); //페이지당 출력되는 곡 수를 지정합니다
		map.put("searchKeyword", keyword); //곡 검색 키워드입니다

		//Bundle 생성
		RequestBundle bundle = AsyncRequester.make_GET_DELTE_bundle(this, Define.MELON_SEARCH_ARISTS_URI, map);

		try {
			//API 호출
			AsyncRequester.request(MelonSearchListActivity.this, bundle, HttpMethod.GET, new EntityParserHandler(new EntityMelonSearchArtist(), new OnEntityParseComplete() {

				@Override
				public void onParsingComplete(Object entityArray) {
					mArtistArray = (ArrayList<EntityMelonSearchArtist>)entityArray;
					
					mSearchArtistAdpater.setMelonSearchArtistList(mArtistArray);
					setListAdapter(mSearchArtistAdpater);
				}
			}, null, "menuId"));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.home_bt :
			Intent intent_home = new Intent(MelonSearchListActivity.this, MainActivity.class );
			intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
			finish();
			startActivity( intent_home );
			break;
		case R.id.edit_bt :
			mSearchEt.setText("");
			break;
		}
	} 

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		String detailURL= "";

		if(mServiceType.equals("artist")){
			detailURL = "http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=artist&cid=" + mArtistArray.get(position).artistName + "&menuId=" + mArtistArray.get(position).menuId;
		}else if(mServiceType.equals("album")){
			detailURL = "http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=album&cid=" + mAlbumArray.get(position).albumId + "&menuId=" + mAlbumArray.get(position).menuId;
		}else{
			detailURL = "http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=song&cid=" + mSongArray.get(position).songId + "&menuId=" + mSongArray.get(position).menuId;
		}
		Intent melonInfo_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailURL));
		startActivity(melonInfo_intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_melon, menu);
		return result;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.current_music:
			startActivity(new Intent(this, MelonNewSongsListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.current_album:
			startActivity(new Intent(this, MelonNewAlbumListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.music_chart:
			startActivity(new Intent(this, MelonChartListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.music_search:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.music_rb: //곡 검색
			if(!mSearchEt.getText().toString().equals("")){
				initSongSearchList(mSearchEt.getText().toString());
				mSearchSongAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.album_rb: //앨범 검색
			if(!mSearchEt.getText().toString().equals("")){
				initAlbumSearchList(mSearchEt.getText().toString());
				mSearchAlbumAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.artist_rb: //아티스트 검색
			if(!mSearchEt.getText().toString().equals("")){
				initArtistSearchList(mSearchEt.getText().toString());
				mSearchSongAdapter.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			if( mSearchEt.length() == 0 ) {
				Toast.makeText(MelonSearchListActivity.this, R.string.search_keyword_null, Toast.LENGTH_SHORT).show();
				return true;
			}else{ //검색결과 
				initSongSearchList(mSearchEt.getText().toString());

				mInputMethodManager.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
				return true;
			}
		}
		return false;
	} 
}
