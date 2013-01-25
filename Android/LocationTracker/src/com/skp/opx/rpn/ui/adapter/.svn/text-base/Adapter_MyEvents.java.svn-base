
package com.skp.opx.rpn.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityFavorite;
import com.skp.opx.rpn.entity.EntityMyEvent;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : 나의 일정 Adapter
 * @클래스명 : Adapter_MyEvents 
 *
 */
public class Adapter_MyEvents extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMyEvent> mMyEventsList = new ArrayList<EntityMyEvent>();
	private LayoutInflater mLiInflater;
    private String mStartDate= "";
    private String mToday = "";
    
	public Adapter_MyEvents(Context context) {
	
		Date today = new Date(System.currentTimeMillis());		
		mToday = new SimpleDateFormat("yyyy년 MM 월 dd일").format(today);//날짜 표시
		
		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMyEventsList(ArrayList<EntityMyEvent> list) {

		mMyEventsList = list;
	}

	public int getCount() {
		return mMyEventsList.size();
	}

	public EntityMyEvent getItem(int position) {
		return mMyEventsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_my_event, null);
			viewHolder = new ViewHolder();
			viewHolder.date = (TextView)convertView.findViewById(R.id.date);
			viewHolder.summary = (TextView)convertView.findViewById(R.id.summary);
			viewHolder.dateTime = (TextView)convertView.findViewById(R.id.dateTime);
			viewHolder.location = (TextView)convertView.findViewById(R.id.location);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityMyEvent data = (EntityMyEvent)mMyEventsList.get(position);		
		
		if(mToday.equalsIgnoreCase(data.startDate)){
			viewHolder.date.setText("오늘");
		}else{
			viewHolder.date.setText(data.startDate);			
		}
		
		if(mStartDate.equalsIgnoreCase(data.startDate)){ 
			viewHolder.date.setVisibility(View.GONE);			
		}else{
			viewHolder.date.setVisibility(View.VISIBLE);
		}
		mStartDate = data.startDate;
		viewHolder.summary.setText(data.summary);
		viewHolder.dateTime.setText(data.startTime  + "  >  " + data.endTime);
		viewHolder.location.setText(data.location);
			
		return convertView;
	}


	public static class ViewHolder {
		public TextView date;//날짜 
		public TextView summary;//이벤트제목 
		public TextView dateTime;//이벤트 시간
		public TextView location;

	}



}
