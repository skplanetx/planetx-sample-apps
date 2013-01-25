package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMelonDJDetail;

/**
 * @설명 : DJ추천 상세 Adapter
 * @클래스명 : Adapter_MelonDJDetail
 * TODO
 */
public class Adapter_MelonDJDetail extends BaseAdapter {
	private Context	mContext;
	private ArrayList<EntityMelonDJDetail> mMelonDetailArray = new ArrayList<EntityMelonDJDetail>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonDJDetail(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonDJDetailList(ArrayList<EntityMelonDJDetail> list) {

		mMelonDetailArray = list;
	}

	public int getCount() {
		return mMelonDetailArray.size();
	}

	public EntityMelonDJDetail getItem(int position) {
		return mMelonDetailArray.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_myplay_chart, null);
			viewHolder = new ViewHolder();
			viewHolder.song_textview = (TextView)convertView.findViewById(R.id.song_name_tv);
			viewHolder.artist_textview = (TextView)convertView.findViewById(R.id.artist_name_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonDJDetail detailInfo = (EntityMelonDJDetail)mMelonDetailArray.get(position);
		viewHolder.song_textview.setText(detailInfo.songName);
		viewHolder.artist_textview.setText(detailInfo.artistName);
		return convertView;
	}


	public static class ViewHolder {

		public TextView song_textview;
		public TextView artist_textview;
	}


}