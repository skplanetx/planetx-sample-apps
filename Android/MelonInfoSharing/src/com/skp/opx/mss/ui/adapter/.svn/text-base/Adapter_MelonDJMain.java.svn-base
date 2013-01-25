package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMelonDJMain;

/**
 * @설명 : DJ추천 메인 카테고리 Adapter
 * @클래스명 : Adapter_MelonDJMain
 * TODO
 */
public class Adapter_MelonDJMain extends BaseAdapter {
	private Context	mContext;
	private ArrayList<EntityMelonDJMain> mMelonMainArray = new ArrayList<EntityMelonDJMain>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonDJMain(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMelonDJMainList(ArrayList<EntityMelonDJMain> list) {

		mMelonMainArray = list;
	}

	public int getCount() {
		return mMelonMainArray.size();
	}

	public EntityMelonDJMain getItem(int position) {
		return mMelonMainArray.get(position);
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

		EntityMelonDJMain mainInfo = (EntityMelonDJMain)mMelonMainArray.get(position);
		viewHolder.name_textview.setText(mainInfo.categoryName);
		return convertView;
	}


	public static class ViewHolder {

		public TextView name_textview;
	}


}