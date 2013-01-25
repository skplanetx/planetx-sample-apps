package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMelonTopAlbumChart;

/**
 * @설명 : 앨범 Top20 차트 조회 Adapter
 * @클래스명 : Adapter_MelonChartTopAlbum
 * TODO
 */
public class Adapter_MelonChartTopAlbum extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMelonTopAlbumChart> mMelonChart = new ArrayList<EntityMelonTopAlbumChart>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonChartTopAlbum(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonChartList(ArrayList<EntityMelonTopAlbumChart> list) {

		mMelonChart = list;
	}

	public int getCount() {
		return mMelonChart.size();
	}

	public EntityMelonTopAlbumChart getItem(int position) {
		return mMelonChart.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_topalbum_chart, null);
			viewHolder = new ViewHolder();
			viewHolder.rank_textView = (TextView)convertView.findViewById(R.id.current_rank_tv);
			viewHolder.album_textView = (TextView)convertView.findViewById(R.id.album_name_tv);
			viewHolder.artist_textView = (TextView)convertView.findViewById(R.id.artist_name_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.release_date_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonTopAlbumChart chartInfo = (EntityMelonTopAlbumChart)mMelonChart.get(position);

		viewHolder.rank_textView.setText(Integer.toString(chartInfo.currentRank));
		viewHolder.album_textView.setText(chartInfo.albumName);
		viewHolder.artist_textView.setText(chartInfo.artistName);
		viewHolder.date_textView.setText(chartInfo.issueDate);

		return convertView;
	}


	public static class ViewHolder {

		public TextView rank_textView;
		public TextView album_textView;
		public TextView artist_textView;
		public TextView date_textView;

	}


}
