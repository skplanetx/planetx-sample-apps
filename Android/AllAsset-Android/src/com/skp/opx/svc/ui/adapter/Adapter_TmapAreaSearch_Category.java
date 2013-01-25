package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityTmapAreaSearchCategory;

/**
 * @설명 : Tmap 주번 검색 카테고리 Adapter
 * @클래스명 : Adapter_TmapAreaSearch_Category
 * 
 */
public class Adapter_TmapAreaSearch_Category extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityTmapAreaSearchCategory> mAreaSearchList = new ArrayList<EntityTmapAreaSearchCategory>();
	private LayoutInflater mLiInflater;
	public Adapter_TmapAreaSearch_Category(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setAreaSearchCategoryList(ArrayList<EntityTmapAreaSearchCategory> list) {

		mAreaSearchList = list;
	}

	public int getCount() {
		return mAreaSearchList.size();
	}

	public EntityTmapAreaSearchCategory getItem(int position) {
		return mAreaSearchList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_tmap_area_search_category, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityTmapAreaSearchCategory movePathInfo = (EntityTmapAreaSearchCategory)mAreaSearchList.get(position);		
		viewHolder.name.setText(movePathInfo.name);
		return convertView;
	}

	public static class ViewHolder {

		public TextView name;

	}



}
