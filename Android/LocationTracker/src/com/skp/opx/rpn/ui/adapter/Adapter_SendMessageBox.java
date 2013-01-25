
package com.skp.opx.rpn.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntitySendMessageBox;

/**
 * @설명 : 발신함 Adapter
 * @클래스명 : Adapter_SendMessageBox 
 *
 */
public class Adapter_SendMessageBox extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntitySendMessageBox> mSendMessageList = new ArrayList<EntitySendMessageBox>();
	private LayoutInflater mLiInflater;
	private boolean mIsCheckMode;

	public Adapter_SendMessageBox(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setSendMessageList(ArrayList<EntitySendMessageBox> list) {

		mSendMessageList = list;
	}

	public void setCheckMode(){
		
		mIsCheckMode = !mIsCheckMode;
		
	}
	public int getCount() {
		return mSendMessageList.size();
	}

	public EntitySendMessageBox getItem(int position) {
		return mSendMessageList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_send_message_box, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);
			viewHolder.contentPreview  = (TextView)convertView.findViewById(R.id.content_preview);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
			viewHolder.checkBox.setClickable(false);
			viewHolder.checkBox.setFocusable(false);
			viewHolder.date = (TextView)convertView.findViewById(R.id.date);
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntitySendMessageBox sendMessageInfo = (EntitySendMessageBox)mSendMessageList.get(position);		
		viewHolder.name.setText(sendMessageInfo.getName());
		viewHolder.contentPreview.setText(sendMessageInfo.getContentPreview());
		viewHolder.date.setText(sendMessageInfo.getDate());
		if(mIsCheckMode){
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}else{
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		viewHolder.checkBox.setChecked(sendMessageInfo.getChecked());
		return convertView;
	}
	
	public boolean isCheckMode(){
		return mIsCheckMode;		
	}

	public static class ViewHolder {

		public TextView name;
		public TextView contentPreview;
		public TextView date;
		public CheckBox checkBox;

	}



}
