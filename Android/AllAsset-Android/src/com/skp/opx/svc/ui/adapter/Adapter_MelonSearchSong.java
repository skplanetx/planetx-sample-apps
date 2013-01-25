package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityMelonSearchSong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Melon 곡 검색 Adapter
 * @클래스명 : Adapter_MelonSearchSong
 * 
 */
public class Adapter_MelonSearchSong extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonSearchSong> mMelonSearch = new ArrayList<EntityMelonSearchSong>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonSearchSong(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonSearchList(ArrayList<EntityMelonSearchSong> list) {

		mMelonSearch = list;
	}

	public int getCount() {
		return mMelonSearch.size();
	}

	public EntityMelonSearchSong getItem(int position) {
		return mMelonSearch.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_melon_song, null);
			viewHolder = new ViewHolder();
			viewHolder.song_textView = (TextView)convertView.findViewById(R.id.song_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonSearchSong mSongInfo = (EntityMelonSearchSong)mMelonSearch.get(position);

		viewHolder.song_textView.setText(mSongInfo.songName);
		viewHolder.artist_textView.setText(mSongInfo.artistName);

		return convertView;
	}


	public static class ViewHolder {

		public TextView song_textView;
		public TextView artist_textView;
	}

}
