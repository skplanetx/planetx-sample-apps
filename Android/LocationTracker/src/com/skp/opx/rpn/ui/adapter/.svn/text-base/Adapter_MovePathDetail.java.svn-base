
package com.skp.opx.rpn.ui.adapter;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityMovePathDetail;
import com.skp.opx.rpn.util.TurnTypeUtil;

/**
 * @설명 : 이동 경로 상세 Adapter
 * @클래스명 : Adapter_MovePathDetail 
 *
 */
public class Adapter_MovePathDetail extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityMovePathDetail> mSendMessageList = new ArrayList<EntityMovePathDetail>();
	private LayoutInflater mLiInflater;

	public Adapter_MovePathDetail(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMovePathDetailList(ArrayList<EntityMovePathDetail> list) {

		mSendMessageList = list;
	}

	public int getCount() {
		return mSendMessageList.size();
	}

	public EntityMovePathDetail getItem(int position) {
		return mSendMessageList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_move_path_detail, null);
			viewHolder = new ViewHolder();
			viewHolder.location = (TextView)convertView.findViewById(R.id.location_tv);
			viewHolder.turnType = (TextView)convertView.findViewById(R.id.turntype_tv);
			viewHolder.distance = (TextView)convertView.findViewById(R.id.distance_tv);
			viewHolder.description = (TextView)convertView.findViewById(R.id.description_tv);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityMovePathDetail movePathDetailInfo = (EntityMovePathDetail)mSendMessageList.get(position);		
		if(movePathDetailInfo.mName.equals("")){
			viewHolder.location.setText("정보없음");
		}else{
			viewHolder.location.setText(movePathDetailInfo.mName);
		}
		if(movePathDetailInfo.mDescription.equals("")){
			viewHolder.description.setText("");
		}else{
			viewHolder.description.setText(movePathDetailInfo.mDescription);
		}

		if(position ==0){ //출발지는 방향 정보 없음
			viewHolder.turnType.setText("");		
		}else{
			viewHolder.turnType.setText(TurnTypeUtil.getTurnType(mContext, movePathDetailInfo.mTurnType));	
		}

		viewHolder.distance.setText(movePathDetailInfo.mDistance + " m");

		return convertView;
	}

	public static class ViewHolder {

		public TextView location;
		public TextView turnType;
		public TextView distance;
		public TextView description;

	}


}
