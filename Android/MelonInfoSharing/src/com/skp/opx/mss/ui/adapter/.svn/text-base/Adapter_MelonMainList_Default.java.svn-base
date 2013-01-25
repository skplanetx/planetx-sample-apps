package com.skp.opx.mss.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.mss.ui.R;
import com.skp.opx.mss.entity.EntityMainList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : 음악공유서비스 Main Default(음악차트) Adapter
 * @클래스명 : Adapter_MelonMainList_Default
 * TODO
 */
public class Adapter_MelonMainList_Default extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMainList> mMainList = new ArrayList<EntityMainList>();
	private LayoutInflater mLiInflater;

	public Adapter_MelonMainList_Default(Context context) {
		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMainlist(ArrayList<EntityMainList> list) {
		mMainList = list;
	}

	public int getCount() {
		return mMainList.size();
	}

	public EntityMainList getItem(int position) {
		return mMainList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			convertView = mLiInflater.inflate(R.layout.list_item_mainlist, null);
			viewHolder = new ViewHolder();
			viewHolder.listName_textView=(TextView)convertView.findViewById(R.id.list_tv);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMainList listInfo =(EntityMainList)mMainList.get(position);
	
		viewHolder.listName_textView.setText(listInfo.listTitle);
		
		return convertView;
	}

	public static class ViewHolder {
		public TextView listName_textView;
	}
}
