package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.Entity11stPostScript;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : 11번가 후기 Adapter  
 * @클래스명 : Adapter_11stPostscript
 * 
 */
public class Adapter_11stPostscript extends BaseAdapter {

	private Context	mContext;
	private ArrayList<Entity11stPostScript> m11stPostscript = new ArrayList<Entity11stPostScript>();
	private LayoutInflater mLiInflater;

	public Adapter_11stPostscript(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void set11stPostscriptList(ArrayList<Entity11stPostScript> list) {

		m11stPostscript = list;
	}

	public int getCount() {
		return m11stPostscript.size();
	}

	public Entity11stPostScript getItem(int position) {
		return m11stPostscript.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_eleven_st_postscripts, null);
			viewHolder = new ViewHolder();
			viewHolder.title_textView = (TextView)convertView.findViewById(R.id.title_tv);
			viewHolder.writer_textView = (TextView)convertView.findViewById(R.id.writer_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.date_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		Entity11stPostScript postscriptInfo = (Entity11stPostScript)m11stPostscript.get(position);
		
		viewHolder.title_textView.setText(postscriptInfo.Title);
		viewHolder.writer_textView.setText(postscriptInfo.Writer);
		viewHolder.date_textView.setText(postscriptInfo.Date);
		
		return convertView;
	}

	public static class ViewHolder {
		
		public TextView title_textView;
		public TextView writer_textView;
		public TextView date_textView;

	}


}
