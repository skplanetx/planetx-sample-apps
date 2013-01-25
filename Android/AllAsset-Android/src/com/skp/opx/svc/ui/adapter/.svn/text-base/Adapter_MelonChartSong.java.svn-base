package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityMelonChart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Melon 곡 차트 Adapter
 * @클래스명 : Adapter_MelonChartSong
 * 
 */
public class Adapter_MelonChartSong extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonChart> mMelonChart = new ArrayList<EntityMelonChart>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonChartSong(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonChartList(ArrayList<EntityMelonChart> list) {

		mMelonChart = list;
	}

	public int getCount() {
		return mMelonChart.size();
	}

	public EntityMelonChart getItem(int position) {
		return mMelonChart.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_melon_common_1, null);
			viewHolder = new ViewHolder();
			viewHolder.rank_textView = (TextView)convertView.findViewById(R.id.rank_tv);
			viewHolder.song_textView = (TextView)convertView.findViewById(R.id.song_tv);
			viewHolder.album_textView = (TextView)convertView.findViewById(R.id.album_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.date_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonChart chartInfo = (EntityMelonChart)mMelonChart.get(position);
		viewHolder.rank_textView.setText(Integer.toString(chartInfo.currentRank));
		viewHolder.song_textView.setText(chartInfo.songName);
		viewHolder.artist_textView.setText(chartInfo.artistName);
		
		return convertView;
	}


	public static class ViewHolder {

		public TextView song_textView;
		public TextView album_textView;
		public TextView artist_textView;
		public TextView date_textView;
		public TextView rank_textView;

	}


}
