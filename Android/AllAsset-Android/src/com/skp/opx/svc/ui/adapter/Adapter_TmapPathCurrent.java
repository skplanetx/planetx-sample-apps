package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.database.EntitySearchdPathBox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Tmap 최근 검색 Adapter
 * @클래스명 : Adapter_TmapPathCurrent
 * 
 */
public class Adapter_TmapPathCurrent extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntitySearchdPathBox> mCurrenPathList = new ArrayList<EntitySearchdPathBox>();
	private LayoutInflater mLiInflater;

	public Adapter_TmapPathCurrent(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setTmapPathCurrentList(ArrayList<EntitySearchdPathBox> list) {

		mCurrenPathList = list;
	}

	public int getCount() {
		return mCurrenPathList.size();
	}

	public EntitySearchdPathBox getItem(int position) {
		return mCurrenPathList.get(position);
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

		EntitySearchdPathBox movePathDetailInfo = (EntitySearchdPathBox)mCurrenPathList.get(position);		
		if(movePathDetailInfo.mDestination.equals("")){
			viewHolder.location_textView.setText(mContext.getString(R.string.no_info));
		}else{
			viewHolder.location_textView.setText(movePathDetailInfo.mDestination);
		}

		return convertView;
	}

	public static class ViewHolder {

		public TextView location_textView;

	}



}
