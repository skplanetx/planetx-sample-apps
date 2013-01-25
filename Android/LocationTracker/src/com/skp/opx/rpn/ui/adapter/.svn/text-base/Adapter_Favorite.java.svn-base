
package com.skp.opx.rpn.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityFavorite;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @설명 : 즐겨 찾기함 Adapter 
 * @클래스명 : Adapter_Favorite 
 *
 */
public class Adapter_Favorite extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityFavorite> mFavoriteList = new ArrayList<EntityFavorite>();
	private LayoutInflater mLiInflater;
	private boolean mIsRadioButtonMode;
	private boolean mIsCheckBoxMode;
	
	public Adapter_Favorite(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setFavoriteList(ArrayList<EntityFavorite> list) {

		mFavoriteList = list;
	}

	public int getCount() {
		return mFavoriteList.size();
	}

	public EntityFavorite getItem(int position) {
		return mFavoriteList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_favorite, null);
			viewHolder = new ViewHolder();
			viewHolder.location = (TextView)convertView.findViewById(R.id.location);
			viewHolder.radioButton = (RadioButton)convertView.findViewById(R.id.radio_button);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
			viewHolder.radioButton.setFocusable(false);
			viewHolder.radioButton.setClickable(false);
			viewHolder.checkBox.setFocusable(false);
			viewHolder.checkBox.setClickable(false);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityFavorite favoriteInfo = (EntityFavorite)mFavoriteList.get(position);		
		
		
		if(mIsRadioButtonMode){//이름 변경
			viewHolder.radioButton.setVisibility(View.VISIBLE);
			viewHolder.radioButton.setChecked(favoriteInfo.getChecked());
			viewHolder.checkBox.setVisibility(View.GONE);					
		}else if (mIsCheckBoxMode){//삭제
			viewHolder.radioButton.setVisibility(View.GONE);
			viewHolder.checkBox.setVisibility(View.VISIBLE);
			viewHolder.checkBox.setChecked(favoriteInfo.getChecked());
		}else{
			viewHolder.radioButton.setVisibility(View.GONE);
			viewHolder.checkBox.setVisibility(View.GONE);
		}
       
		viewHolder.location.setText(favoriteInfo.getLocationName());
		
		
		return convertView;
	}
	
	public void setRadioButtonMode(boolean isRadioButtonMode){
		mIsRadioButtonMode = isRadioButtonMode;
	}
	
	public void setCheckBoxMode(boolean isCheckBoxMode){
		mIsCheckBoxMode = isCheckBoxMode;
	}
	
	public boolean isRadioButtonMode(){
		return mIsRadioButtonMode;
	}
	
	public boolean isCheckBoxMode(){
		return mIsCheckBoxMode;
	}	
	
	public static class ViewHolder {

		public TextView location;
		public RadioButton radioButton;
		public CheckBox checkBox;

	}



}
