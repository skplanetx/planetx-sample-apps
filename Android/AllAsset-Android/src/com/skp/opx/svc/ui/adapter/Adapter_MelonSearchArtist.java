package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityMelonSearchArtist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Melon 아티스트 검색 Adapter
 * @클래스명 : Adapter_MelonSearchArtist
 * 
 */
public class Adapter_MelonSearchArtist extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonSearchArtist> mSearchArtistArray = new ArrayList<EntityMelonSearchArtist>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonSearchArtist(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonSearchArtistList(ArrayList<EntityMelonSearchArtist> list) {

		mSearchArtistArray = list;
	}

	public int getCount() {
		return mSearchArtistArray.size();
	}

	public EntityMelonSearchArtist getItem(int position) {
		return mSearchArtistArray.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_melon_artist, null);
			viewHolder = new ViewHolder();
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_tv);
			viewHolder.genre_textView = (TextView)convertView.findViewById(R.id.genre_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonSearchArtist mArtistInfo = (EntityMelonSearchArtist)mSearchArtistArray.get(position);
		
		viewHolder.artist_textView.setText(mArtistInfo.artistName);
		viewHolder.genre_textView.setText(mArtistInfo.genreNames);

		return convertView;
	}


	public static class ViewHolder {

		public TextView artist_textView;
		public TextView genre_textView;

	}

}
