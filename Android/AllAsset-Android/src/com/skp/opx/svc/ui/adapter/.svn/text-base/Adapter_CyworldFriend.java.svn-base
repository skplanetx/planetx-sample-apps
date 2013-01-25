package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityCyBesties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Cyworld 일촌 목록
 * @클래스명 : Adapter_CyworldFriend
 * 
 */
public class Adapter_CyworldFriend extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityCyBesties> mCyFriendList = new ArrayList<EntityCyBesties>();
	private LayoutInflater mLiInflater;

	public Adapter_CyworldFriend(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setCyworldFriendList(ArrayList<EntityCyBesties> list) {

		mCyFriendList = list;
	}

	public int getCount() {
		return mCyFriendList.size();
	}

	public EntityCyBesties getItem(int position) {
		return mCyFriendList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_cy_friend, null);
			viewHolder = new ViewHolder();
			viewHolder.name_textView = (TextView)convertView.findViewById(R.id.name_tv);
			viewHolder.nickname_textView = (TextView)convertView.findViewById(R.id.nick_name_tv);


			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityCyBesties freindInfo = (EntityCyBesties)mCyFriendList.get(position);
		
		viewHolder.name_textView.setText(freindInfo.cyName);
		viewHolder.nickname_textView.setText(freindInfo.relationName);
		
		return convertView;
	}


	public static class ViewHolder {

		public TextView name_textView;
		public TextView nickname_textView;
	}


}
