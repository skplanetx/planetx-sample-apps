package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityMelonNewMusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Melon 최신 곡 Adpater
 * @클래스명 : Adapter_MelonNewMusic
 * 
 */
public class Adapter_MelonNewMusic extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonNewMusic> mMelonNew = new ArrayList<EntityMelonNewMusic>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonNewMusic(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonNewList(ArrayList<EntityMelonNewMusic> list) {

		mMelonNew = list;
	}

	public int getCount() {
		return mMelonNew.size();
	}

	public EntityMelonNewMusic getItem(int position) {
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
			viewHolder.song_textView = (TextView)convertView.findViewById(R.id.song_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonNewMusic newInfo = (EntityMelonNewMusic)mMelonNew.get(position);
		
		viewHolder.song_textView.setText(newInfo.songName);
		viewHolder.artist_textView.setText(newInfo.artistName);
		
		return convertView;
	}


	public static class ViewHolder {
		
		public TextView song_textView;
		public TextView artist_textView;

	}


}
