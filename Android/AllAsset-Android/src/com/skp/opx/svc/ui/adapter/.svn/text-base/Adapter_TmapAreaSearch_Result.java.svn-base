package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityTmapAreaSearch;

/**
 * @설명 : Tmap 주변검색 결과 Adapter
 * @클래스명 : Adapter_TmapAreaSearch_Result
 * 
 */
public class Adapter_TmapAreaSearch_Result extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityTmapAreaSearch> mAreaSearchList = new ArrayList<EntityTmapAreaSearch>();
	private LayoutInflater mLiInflater;
	public Adapter_TmapAreaSearch_Result(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setAreaSearchList(ArrayList<EntityTmapAreaSearch> list) {

		mAreaSearchList = list;
	}

	public int getCount() {
		return mAreaSearchList.size();
	}

	public EntityTmapAreaSearch getItem(int position) {
		return mAreaSearchList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_tmap_area_search_result, null);
			viewHolder = new ViewHolder();
			//viewHolder.path = (TextView)convertView.findViewById(R.id.path);
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);
			viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);
			viewHolder.telNo = (TextView)convertView.findViewById(R.id.tel);
			viewHolder.address = (TextView)convertView.findViewById(R.id.address);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityTmapAreaSearch areaSearchInfo = (EntityTmapAreaSearch)mAreaSearchList.get(position);		
		viewHolder.name.setText(areaSearchInfo.name);
		if(areaSearchInfo.desc.equals("")){
			viewHolder.desc.setText("설명 없음");		
		}else{
			viewHolder.desc.setText(areaSearchInfo.desc);			
		}
		if(areaSearchInfo.telNo.equals("")){
			viewHolder.telNo.setText("연락처없음");	
		}else{
			viewHolder.telNo.setText(areaSearchInfo.telNo);			
		}
        
		viewHolder.address.setText(areaSearchInfo.upperAddrName + " " + areaSearchInfo.middleAddrName + " " + areaSearchInfo.lowerAddrName+ " " + areaSearchInfo.firstNo);
		return convertView;
	}

	public static class ViewHolder {

		public TextView name;
		public TextView desc;
		public TextView telNo;
		public Button button;
		public TextView address; 
	
	}



}
