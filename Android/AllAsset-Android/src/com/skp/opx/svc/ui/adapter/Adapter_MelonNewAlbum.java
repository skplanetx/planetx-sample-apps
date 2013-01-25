package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityMelonNewAlbum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Melon 최신 앨범 Adapter
 * @클래스명 : Adapter_MelonNewAlbum
 * 
 */
public class Adapter_MelonNewAlbum extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonNewAlbum> mMelonNew = new ArrayList<EntityMelonNewAlbum>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonNewAlbum(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonNewList(ArrayList<EntityMelonNewAlbum> list) {

		mMelonNew = list;
	}

	public int getCount() {
		return mMelonNew.size();
	}

	public EntityMelonNewAlbum getItem(int position) {
		return mMelonNew.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_melon_common_1, null);
			viewHolder = new ViewHolder();
			viewHolder.album_textView = (TextView)convertView.findViewById(R.id.album_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.album_artist_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.date_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonNewAlbum newInfo = (EntityMelonNewAlbum)mMelonNew.get(position);
		
		viewHolder.album_textView.setText(newInfo.albumName);
		viewHolder.artist_textView.setText(newInfo.artistName);
		viewHolder.date_textView.setText(newInfo.issueDate);
		
		return convertView;
	}


	public static class ViewHolder {
		
		public TextView album_textView;
		public TextView artist_textView;
		public TextView date_textView;

	}


}
