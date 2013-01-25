package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMelonNewAlbumChart;
import com.skp.opx.mss.entity.EntityMelonRealtimeChart;

/**
 * @설명 : 최신앨범 차트 조회 Adapter
 * @클래스명 : Adapter_MelonChartNewAlbum
 * TODO
 */
public class Adapter_MelonChartNewAlbum extends BaseAdapter {
	private Context	mContext;
	private ArrayList<EntityMelonNewAlbumChart> mMelonChart = new ArrayList<EntityMelonNewAlbumChart>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonChartNewAlbum(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonChartList(ArrayList<EntityMelonNewAlbumChart> list) {

		mMelonChart = list;
	}

	public int getCount() {
		return mMelonChart.size();
	}

	public EntityMelonNewAlbumChart getItem(int position) {
		return mMelonChart.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_newrelease_chart, null);
			viewHolder = new ViewHolder();
			viewHolder.album_textView = (TextView)convertView.findViewById(R.id.main_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_name_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.release_date_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonNewAlbumChart chartInfo = (EntityMelonNewAlbumChart)mMelonChart.get(position);
		viewHolder.album_textView.setText(chartInfo.albumName);
		viewHolder.artist_textView.setText(chartInfo.artistName);
		viewHolder.date_textView.setText(chartInfo.issueDate);
		return convertView;
	}


	public static class ViewHolder {

		public TextView song_textView;
		public TextView album_textView;
		public TextView artist_textView;
		public TextView date_textView;

	}


}