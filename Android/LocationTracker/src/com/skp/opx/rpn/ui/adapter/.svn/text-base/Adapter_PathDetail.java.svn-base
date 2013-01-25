
package com.skp.opx.rpn.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityPathDetail;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @설명 : 경로 상세 Adapter
 * @클래스명 : Adapter_PathDetail 
 *
 */
public class Adapter_PathDetail extends BaseAdapter {
 
	private Context	mContext;
	private ArrayList<EntityPathDetail> mPathDetailList = new ArrayList<EntityPathDetail>();
	private LayoutInflater mLiInflater;
	private int mResourceId;
    public boolean mIsCheckBoxMode;

	public Adapter_PathDetail(Context context, int resourceId) {
				
		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResourceId = resourceId;
	}

	public void setPathDetailList(ArrayList<EntityPathDetail> list) {

		mPathDetailList = list;
	}

	public int getCount() {
		return mPathDetailList.size();
	}

	public EntityPathDetail getItem(int position) {
		return mPathDetailList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(mResourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.starting = (TextView)convertView.findViewById(R.id.starting_point);
			viewHolder.destination = (TextView)convertView.findViewById(R.id.destination);
			viewHolder.ete = (TextView)convertView.findViewById(R.id.estimated_time_en_route);
			viewHolder.messaging = (TextView)convertView.findViewById(R.id.messaging);
			viewHolder.date = (TextView)convertView.findViewById(R.id.date);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.check);
			viewHolder.checkBox.setClickable(false);
			viewHolder.checkBox.setFocusable(false);

			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityPathDetail  data = mPathDetailList.get(position);
		
		viewHolder.starting.setText(data.getStarting());
		viewHolder.destination.setText(data.getDestination());
		viewHolder.ete.setText(data.getETE());
		viewHolder.messaging.setText(data.getMessaging());
		viewHolder.date.setText(data.getDate());
		viewHolder.checkBox.setChecked(data.getChecked());
		
		if(mIsCheckBoxMode) {
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}else{
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public void setCheckBoxMode(){

		mIsCheckBoxMode =!mIsCheckBoxMode;
	}

	public boolean isCheckBoxMode(){
	
		return mIsCheckBoxMode;
	}
	
	public static class ViewHolder {
		public TextView starting;
		public TextView destination;
		public TextView ete;
		public TextView messaging;
		public TextView date;
		public CheckBox checkBox;
	}



}
