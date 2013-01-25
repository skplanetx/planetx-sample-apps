package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMainMyList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : 음악공유서비스 Main 내 플레이리스트 Adapter
 * @클래스명 : Adapter_MelonMainList_Myplay
 * TODO
 */
public class Adapter_MelonMainList_Myplay extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMainMyList> mMyplayChart = new ArrayList<EntityMainMyList>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonMainList_Myplay(Context context) {
		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMyplayInfolist(ArrayList<EntityMainMyList> list) {
		mMyplayChart = list;
	}

	public int getCount() {
		return mMyplayChart.size();
	}

	public EntityMainMyList getItem(int position) {
		return mMyplayChart.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			convertView = mLiInflater.inflate(R.layout.list_item_myplay_chart, null);
			viewHolder = new ViewHolder();
			viewHolder.songName_textView=(TextView)convertView.findViewById(R.id.song_name_tv);
			viewHolder.artistName_textView=(TextView)convertView.findViewById(R.id.artist_name_tv);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMainMyList chartInfo =(EntityMainMyList)mMyplayChart.get(position);
	
		viewHolder.songName_textView.setText(chartInfo.songName);
		viewHolder.artistName_textView.setText(chartInfo.artistName);
		
		return convertView;
	}

	public static class ViewHolder {
		public TextView artistName_textView;
		public TextView songName_textView;
	}
}
