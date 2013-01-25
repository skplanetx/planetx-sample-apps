
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
 * @설명 : 이동 경로 Adapter
 * @클래스명 : Adapter_MovePath 
 *
 */
public class Adapter_MovePath extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMovePath> mSendMessageList = new ArrayList<EntityMovePath>();
	private LayoutInflater mLiInflater;
	private boolean mIsCheckMode;
    private OnClickListener mClickListener;
	public Adapter_MovePath(Context context) {

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
			convertView = mLiInflater.inflate(R.layout.list_item_move_path, null);
			viewHolder = new ViewHolder();
			viewHolder.start = (TextView)convertView.findViewById(R.id.start);
			viewHolder.destination = (TextView)convertView.findViewById(R.id.destination);
			viewHolder.date = (TextView)convertView.findViewById(R.id.date);
			viewHolder.button = (Button)convertView.findViewById(R.id.button);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityMovePath movePathInfo = (EntityMovePath)mSendMessageList.get(position);		
		String [] path = movePathInfo.getPath().split(">"); 
		viewHolder.start.setText( mContext.getString(R.string.start_location) + path[0].replace(" ", ""));
		viewHolder.destination.setText( mContext.getString(R.string.dest_location) + path[1].replace(" ", ""));
		viewHolder.date.setText(movePathInfo.getDate());
		viewHolder.button.setTag(position);
		viewHolder.button.setOnClickListener(mClickListener);

		return convertView;
	}
	public void setOnClickListener(OnClickListener clickListener ) {
		mClickListener = clickListener;
	}
	public boolean isCheckMode(){
		return mIsCheckMode;		
	}

	public static class ViewHolder {

		public TextView start;
		public TextView destination;
		public TextView date;
		public Button button;

	}



}
