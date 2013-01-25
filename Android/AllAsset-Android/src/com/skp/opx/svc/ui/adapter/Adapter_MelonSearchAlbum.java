package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityMelonSearchAlbum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Melon 앨범 검색 Adapter
 * @클래스명 : Adapter_MelonSearchAlbum
 * 
 */
public class Adapter_MelonSearchAlbum extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonSearchAlbum> mMelonSearch = new ArrayList<EntityMelonSearchAlbum>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonSearchAlbum(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonSearchList(ArrayList<EntityMelonSearchAlbum> list) {

		mMelonSearch = list;
	}

	public int getCount() {
		return mMelonSearch.size();
	}

	public EntityMelonSearchAlbum getItem(int position) {
		return mMelonSearch.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_melon_album, null);
			viewHolder = new ViewHolder();
			viewHolder.album_textView = (TextView)convertView.findViewById(R.id.album_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonSearchAlbum mAlbumInfo = (EntityMelonSearchAlbum)mMelonSearch.get(position);

		viewHolder.album_textView.setText(mAlbumInfo.albumName);
		viewHolder.artist_textView.setText(mAlbumInfo.artistName);

		return convertView;
	}


	public static class ViewHolder {

		public TextView album_textView;
		public TextView artist_textView;


	}


}
