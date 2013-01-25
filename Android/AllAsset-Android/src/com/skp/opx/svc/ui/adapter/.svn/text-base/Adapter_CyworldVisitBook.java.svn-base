package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityCyVisitBook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Cyworld 방명록 Adapter
 * @클래스명 : Adapter_CyworldVisitBook
 * 
 */
public class Adapter_CyworldVisitBook extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityCyVisitBook> mCyVisitBook = new ArrayList<EntityCyVisitBook>();
	private LayoutInflater mLiInflater;

	public Adapter_CyworldVisitBook(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setCyworldVisitList(ArrayList<EntityCyVisitBook> list) {

		mCyVisitBook = list;
	}

	public int getCount() {
		return mCyVisitBook.size();
	}

	public EntityCyVisitBook getItem(int position) {
		return mCyVisitBook.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_cy_visitbook, null);
			viewHolder = new ViewHolder();
			viewHolder.name_textView = (TextView)convertView.findViewById(R.id.name_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.date_tv);
			viewHolder.post_textView = (TextView)convertView.findViewById(R.id.post_tv);


			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityCyVisitBook visitboookInfo = (EntityCyVisitBook)mCyVisitBook.get(position);

		viewHolder.name_textView.setText(visitboookInfo.writerName);
		viewHolder.date_textView.setText(visitboookInfo.writeDate);
		viewHolder.post_textView.setText(visitboookInfo.content);

		return convertView;
	}


	public static class ViewHolder {

		public TextView name_textView;
		public TextView date_textView;
		public TextView post_textView;
	}


}
