package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;
import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityNateOnBuddiesInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @설명 : NateOn 친구 목록 Adapter
 * @클래스명 : Adapter_NateOnBuddies
 * 
 */
public class Adapter_NateOnBuddies extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityNateOnBuddiesInfo> mNateOnBuddies = new ArrayList<EntityNateOnBuddiesInfo>();
	private LayoutInflater mLiInflater;

	public Adapter_NateOnBuddies(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setNateOnBuddiesList(ArrayList<EntityNateOnBuddiesInfo> list) {

		mNateOnBuddies = list;
	}

	public int getCount() {
		return mNateOnBuddies.size();
	}

	public EntityNateOnBuddiesInfo getItem(int position) {
		return mNateOnBuddies.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final EntityNateOnBuddiesInfo buddiesInfos = (EntityNateOnBuddiesInfo)mNateOnBuddies.get(position);

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_nateon_buddie, null);
			viewHolder = new ViewHolder();
			viewHolder.seperate_textView = (TextView)convertView.findViewById(R.id.seperate_textView);
			viewHolder.image_imageView = (ImageView)convertView.findViewById(R.id.image_iv);
			viewHolder.name_textView = (TextView)convertView.findViewById(R.id.name_tv);
			viewHolder.id_textView = (TextView)convertView.findViewById(R.id.nick_name_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityNateOnBuddiesInfo buddiesInfo = (EntityNateOnBuddiesInfo)mNateOnBuddies.get(position);

		/* 친구목록 폴더 보임을 위한 seperate view add **/
		if(position == 0 || !mNateOnBuddies.get(position-1).groupName.equals(buddiesInfos.groupName)) {
			viewHolder.seperate_textView.setText(buddiesInfos.groupName);
			viewHolder.seperate_textView.setVisibility(View.VISIBLE);
		} else {
			viewHolder.seperate_textView.setVisibility(View.GONE);
		}
		
		viewHolder.name_textView.setText(buddiesInfo.name);
		if(buddiesInfo.nickname !=null){
			viewHolder.id_textView.setText(buddiesInfo.nickname);
		}

		return convertView;
	}


	public static class ViewHolder {

		public TextView	 seperate_textView;
		public ImageView image_imageView;
		public TextView  name_textView;
		public TextView  id_textView;

	}


}
