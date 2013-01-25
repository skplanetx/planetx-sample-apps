package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityTmapTotalSearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Tmap 경로 검색 Adapter
 * @클래스명 : Adapter_TmapPathSearch
 * 
 */
public class Adapter_TmapPathSearch extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityTmapTotalSearch> mTotalSearchList = new ArrayList<EntityTmapTotalSearch>();
	private LayoutInflater mLiInflater;

	public Adapter_TmapPathSearch(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setTotalSearchList(ArrayList<EntityTmapTotalSearch> list) {

		mTotalSearchList = list;
	}

	public int getCount() {
		return mTotalSearchList.size();
	}

	public EntityTmapTotalSearch getItem(int position) {
		return mTotalSearchList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_common_1, null);
			viewHolder = new ViewHolder();
			viewHolder.location_textView = (TextView)convertView.findViewById(R.id.item_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityTmapTotalSearch totalSearchInfo = (EntityTmapTotalSearch)mTotalSearchList.get(position);
		viewHolder.location_textView.setText(totalSearchInfo.name);


		return convertView;
	}


	public static class ViewHolder {

		public TextView location_textView;

	}


}
