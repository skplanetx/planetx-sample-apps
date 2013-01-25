package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.Entity11stCategorySub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : 11번가 하위 카테고리 Adapter (카테고리 선택) 
 * @클래스명 : Adapter_11stSubCategory
 * 
 */
public class Adapter_11stSubCategory extends BaseAdapter {

	private Context	mContext;
	private ArrayList<Entity11stCategorySub> m11stCategoryList = new ArrayList<Entity11stCategorySub>();
	private LayoutInflater mLiInflater;

	public Adapter_11stSubCategory(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void set11stSubCategoryList(ArrayList<Entity11stCategorySub> list) {

		m11stCategoryList = list;
	}

	public int getCount() {
		return m11stCategoryList.size();
	}

	public Entity11stCategorySub getItem(int position) {
		return m11stCategoryList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_common_1, null);
			viewHolder = new ViewHolder();
			viewHolder.name_textView = (TextView)convertView.findViewById(R.id.item_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		Entity11stCategorySub categoryInfo = (Entity11stCategorySub)m11stCategoryList.get(position);
		
		viewHolder.name_textView.setText(categoryInfo.CategoryName);
		
		return convertView;
	}


	public static class ViewHolder {

		public TextView name_textView;

	}



}
