/**
 * 
 * @클래스명 : Adapter_Address
 * @작성자 : 이호석
 * @작성일 : 2012. 9. 24.
 * @최종수정일 : 2012. 9. 24.
 * @수정이력 - 수정일, 수정자, 수정 내용
 * 주소록 어댑터
 */
package com.skp.opx.rpn.ui.adapter;

import java.util.ArrayList;
import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityContact;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @설명 : 주소록 Adapter
 * @클래스명 : Adapter_Address 
 *
 */
public class Adapter_Address extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityContact> mContactsList = new ArrayList<EntityContact>();
	private LayoutInflater mLiInflater;
	private boolean mIsCheckMode;

	public Adapter_Address(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setAddressList(ArrayList<EntityContact> list) {

		mContactsList = list;
	}

	public void setCheckMode(){
		
		mIsCheckMode = !mIsCheckMode;
		
	}
	public int getCount() {
		return mContactsList.size();
	}

	public EntityContact getItem(int position) {
		return mContactsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_contact, null);
			viewHolder = new ViewHolder();
			viewHolder.subTitle = (TextView)convertView.findViewById(R.id.sub_title);
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);
			viewHolder.contact = (TextView)convertView.findViewById(R.id.contact);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityContact contactInfo = (EntityContact)mContactsList.get(position);
		if(contactInfo.getTitleType() == 1){
			viewHolder.subTitle.setVisibility(View.VISIBLE);
			viewHolder.subTitle.setText(R.string.my_address);
		}else if(contactInfo.getTitleType() == 2){
			viewHolder.subTitle.setVisibility(View.VISIBLE);
			viewHolder.subTitle.setText(R.string.nate_on);
		}else{
			viewHolder.subTitle.setVisibility(View.GONE);
		}
		viewHolder.name.setText(contactInfo.getName());
		viewHolder.contact.setText(contactInfo.getContact());
		if(mIsCheckMode){
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}else{
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		viewHolder.checkBox.setFocusable(false);
		viewHolder.checkBox.setClickable(false);
		viewHolder.checkBox.setChecked(contactInfo.getChecked());
		return convertView;
	}
	
	public boolean isCheckMode(){
		return mIsCheckMode;		
	}

	public static class ViewHolder {

		public TextView 	subTitle;
		public TextView		name;
		public TextView		contact;
		public CheckBox	checkBox;

	}



}
