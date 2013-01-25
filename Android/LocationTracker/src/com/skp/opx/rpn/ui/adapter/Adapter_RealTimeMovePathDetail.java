
package com.skp.opx.rpn.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.w3c.dom.Text;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityMovePath;
import com.skp.opx.rpn.entity.EntityRealTimeMovePathDetail;
import com.skp.opx.rpn.entity.EntitySendMessageBox;
import com.skp.opx.rpn.util.TurnTypeUtil;

/**
 * @설명 : 실시간 이동 경로 상세 Adapter
 * @클래스명 : Adapter_RealTimeMovePathDetail 
 *
 */
public class Adapter_RealTimeMovePathDetail extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityRealTimeMovePathDetail> mSendMessageList = new ArrayList<EntityRealTimeMovePathDetail>();
	private LayoutInflater mLiInflater;
	private boolean mIsCheckMode;

	public Adapter_RealTimeMovePathDetail(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMovePathDetailList(ArrayList<EntityRealTimeMovePathDetail> list) {

		mSendMessageList = list;
	}

	public void setCheckMode(){
		
		mIsCheckMode = !mIsCheckMode;
		
	}
	public int getCount() {
		return mSendMessageList.size();
	}

	public EntityRealTimeMovePathDetail getItem(int position) {
		return mSendMessageList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_real_time_move_path_detail, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);
			viewHolder.time = (TextView)convertView.findViewById(R.id.time);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.check_box);
			viewHolder.checkBox.setClickable(false);
			viewHolder.checkBox.setFocusable(false);
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityRealTimeMovePathDetail movePathDetailInfo = (EntityRealTimeMovePathDetail)mSendMessageList.get(position);	
		if(movePathDetailInfo.mName.equals("")){
			viewHolder.name.setText(mContext.getString(R.string.no_info));
		}else{
			viewHolder.name.setText(movePathDetailInfo.mName);
		}	
		
		viewHolder.time.setText( new SimpleDateFormat("yy/MM/dd a hh:mm").format(movePathDetailInfo.mTime));
		
		if(mIsCheckMode){
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		}else{
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		viewHolder.checkBox.setChecked(movePathDetailInfo.getChecked());
		return convertView;
	}
	
	public boolean isCheckMode(){
		return mIsCheckMode;		
	}

	public static class ViewHolder {

		public TextView name;
		public TextView time;
		public CheckBox checkBox;

	}



}
