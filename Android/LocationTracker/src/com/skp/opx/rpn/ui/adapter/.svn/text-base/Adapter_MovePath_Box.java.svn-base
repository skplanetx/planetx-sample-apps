
package com.skp.opx.rpn.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityMovePath;
import com.skp.opx.rpn.entity.EntitySendMessageBox;

/**
 * @설명 : 이동경로함 Adapter
 * @클래스명 : Adapter_MovePath_Box 
 *
 */
public class Adapter_MovePath_Box extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMovePath> mSendMessageList = new ArrayList<EntityMovePath>();
	private LayoutInflater mLiInflater;
	private boolean mIsCheckMode;
    private OnClickListener mClickListener;
	public Adapter_MovePath_Box(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMovePathList(ArrayList<EntityMovePath> list) {

		mSendMessageList = list;
	}

	public void setCheckMode(){
		
		mIsCheckMode = !mIsCheckMode;
		
	}
	public int getCount() {
		return mSendMessageList.size();
	}

	public EntityMovePath getItem(int position) {
		return mSendMessageList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_move_path_box, null);
			viewHolder = new ViewHolder();
			viewHolder.path = (TextView)convertView.findViewById(R.id.path);
			viewHolder.date = (TextView)convertView.findViewById(R.id.date);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
			viewHolder.checkBox.setClickable(false);
			viewHolder.checkBox.setFocusable(false);
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityMovePath movePathInfo = (EntityMovePath)mSendMessageList.get(position);		
		viewHolder.path.setText(movePathInfo.getPath());
		viewHolder.date.setText(movePathInfo.getDate());
		viewHolder.checkBox.setTag(position);
		viewHolder.checkBox.setOnClickListener(mClickListener);
		if(mIsCheckMode){
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}else{
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		viewHolder.checkBox.setChecked(movePathInfo.getChecked());
		return convertView;
	}
	public void setOnClickListener(OnClickListener clickListener ) {
		mClickListener = clickListener;
	}
	public boolean isCheckMode(){
		return mIsCheckMode;		
	}

	public static class ViewHolder {

		public TextView path;
		public TextView date;
		public CheckBox checkBox;

	}



}
