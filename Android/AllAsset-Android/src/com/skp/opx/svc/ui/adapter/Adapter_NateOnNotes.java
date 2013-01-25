package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;
import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityNateOnMsgQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : NateOn 친구 목록 Adapter
 * @클래스명 : Adapter_NateOnBuddies
 * 
 */
public class Adapter_NateOnNotes extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityNateOnMsgQuery> mNateOnNotes = new ArrayList<EntityNateOnMsgQuery>();
	private LayoutInflater mLiInflater;

	public Adapter_NateOnNotes(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setNateOnBuddiesList(ArrayList<EntityNateOnMsgQuery> list) {

		mNateOnNotes = list;
	}

	public int getCount() {
		return mNateOnNotes.size();
	}

	public EntityNateOnMsgQuery getItem(int position) {
		return mNateOnNotes.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final EntityNateOnMsgQuery notesInfos = (EntityNateOnMsgQuery)mNateOnNotes.get(position);

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_nateon_notes, null);
			viewHolder = new ViewHolder();
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.date_tv);
			viewHolder.name_textView = (TextView)convertView.findViewById(R.id.body_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityNateOnMsgQuery buddiesInfo = (EntityNateOnMsgQuery)mNateOnNotes.get(position);

//		if(buddiesInfo.date !=null){
//			viewHolder.date_textView.setText(buddiesInfo.date);
//		}
		
		if(buddiesInfo.message !=null){
			viewHolder.name_textView.setText(buddiesInfo.message);
		}

		return convertView;
	}


	public static class ViewHolder {

		public TextView  name_textView;
		public TextView  date_textView;

	}


}
