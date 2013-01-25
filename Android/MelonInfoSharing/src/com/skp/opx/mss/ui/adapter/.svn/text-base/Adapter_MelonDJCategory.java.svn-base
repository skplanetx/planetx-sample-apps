package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMelonDJCategory;

/**
 * @설명 : DJ추천 서브카테고리 Adapter
 * @클래스명 : Adapter_MelonDJCategory
 * TODO
 */
public class Adapter_MelonDJCategory extends BaseAdapter {
	private Context	mContext;
	private ArrayList<EntityMelonDJCategory> mMelonCategoryArray = new ArrayList<EntityMelonDJCategory>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonDJCategory(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonDJCategoryList(ArrayList<EntityMelonDJCategory> list) {

		mMelonCategoryArray = list;
	}

	public int getCount() {
		return mMelonCategoryArray.size();
	}

	public EntityMelonDJCategory getItem(int position) {
		return mMelonCategoryArray.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_dj_main, null);
			viewHolder = new ViewHolder();
			viewHolder.name_textview = (TextView)convertView.findViewById(R.id.name_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMelonDJCategory categoryInfo = (EntityMelonDJCategory)mMelonCategoryArray.get(position);
		viewHolder.name_textview.setText(categoryInfo.offeringTitle);
		return convertView;
	}


	public static class ViewHolder {

		public TextView name_textview;
	}



}