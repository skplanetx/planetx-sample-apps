package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMelonRealtimeChart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @설명 : 실시간차트 조회 Adapter
 * @클래스명 : Adapter_MelonChartRealtime
 * TODO
 */
public class Adapter_MelonChartRealtime extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonRealtimeChart> mMelonChart = new ArrayList<EntityMelonRealtimeChart>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonChartRealtime(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonChartList(ArrayList<EntityMelonRealtimeChart> list) {

		mMelonChart = list;
	}

	public int getCount() {
		return mMelonChart.size();
	}

	public EntityMelonRealtimeChart getItem(int position) {
		return mMelonChart.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_realtime_chart, null);
			viewHolder = new ViewHolder();
			viewHolder.rank_textView = (TextView)convertView.findViewById(R.id.current_rank_tv);
			viewHolder.song_textView = (TextView)convertView.findViewById(R.id.song_name_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_name_tv);
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonRealtimeChart chartInfo = (EntityMelonRealtimeChart)mMelonChart.get(position);
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
