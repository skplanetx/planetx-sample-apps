package com.skp.opx.sns.ui.adapater;

import java.util.ArrayList;

import com.skp.opx.sns.R;
import com.skp.opx.sns.entity.EntityFriendsList;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @설명 : 지인 목록 조회 Adapter
 * @클래스명 : FriendsListAdapter 
 *
 */
public class FriendsListAdapter extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityFriendsList> mFriends = new ArrayList<EntityFriendsList>();
	private LayoutInflater mLiInflater;

	public FriendsListAdapter(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setFriendsList(ArrayList<EntityFriendsList> list) {

		mFriends = list;
	}

	public int getCount() {
		return mFriends.size();
	}

	public EntityFriendsList getItem(int position) {
		return mFriends.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_recommend, null);
			viewHolder = new ViewHolder();
			viewHolder.friendsNameTextView = (TextView) convertView.findViewById(R.id.name_tv);
			viewHolder.friendsPhoneTextView= (TextView) convertView.findViewById(R.id.phone_tv);
			viewHolder.requestButton 	   = (Button) convertView.findViewById(R.id.add_bt);
			viewHolder.requestButton.setVisibility(View.GONE);
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityFriendsList friendsInfo = (EntityFriendsList)mFriends.get(position);
		viewHolder.friendsNameTextView.setText(friendsInfo.name);
		viewHolder.friendsPhoneTextView.setText(PhoneNumberUtils.formatNumber(friendsInfo.phoneNumber));

		return convertView;
	}


	public static class ViewHolder {

		public TextView friendsPhoneTextView;
		public Button requestButton;
		public TextView friendsNameTextView;
	}
}
