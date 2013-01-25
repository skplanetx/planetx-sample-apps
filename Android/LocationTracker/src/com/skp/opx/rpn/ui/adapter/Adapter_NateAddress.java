
package com.skp.opx.rpn.ui.adapter;

import java.util.ArrayList;
import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityContact;
import com.skp.opx.rpn.entity.EntityNateContact;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @설명 : 네이트온 연락처 Adapter
 * @클래스명 : Adapter_NateAddress 
 *
 */
public class Adapter_NateAddress extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityNateContact> mNateContactsList = new ArrayList<EntityNateContact>();
	private LayoutInflater mLiInflater;
	private boolean mIsCheckMode;

	public Adapter_NateAddress(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setNateAddressList(ArrayList<EntityNateContact> list) {

		mNateContactsList = list;
	}

	public void setCheckMode(){
		
		mIsCheckMode = !mIsCheckMode;
		
	}
	public int getCount() {
		return mNateContactsList.size();
	}

	public EntityNateContact getItem(int position) {
		return mNateContactsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_contact, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);
			viewHolder.contact = (TextView)convertView.findViewById(R.id.contact);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
			viewHolder.checkBox.setVisibility(View.GONE);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityNateContact contactInfo = (EntityNateContact)mNateContactsList.get(position);		
		
		viewHolder.name.setText(contactInfo.name);
		viewHolder.contact.setText(contactInfo.nateId);

		return convertView;
	}
	
	public boolean isCheckMode(){
		return mIsCheckMode;		
	}

	public static class ViewHolder {

		public TextView name;
		public TextView contact;
		public CheckBox checkBox;

	}



}
