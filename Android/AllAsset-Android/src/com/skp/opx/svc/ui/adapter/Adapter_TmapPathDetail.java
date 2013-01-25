package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;
import com.skp.opx.svc.R;
import com.skp.opx.svc.constants.Defines;
import com.skp.opx.svc.entity.EntityTmapPathDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @설명 : Tmap 경로검색결과 상세 Adapter
 * @클래스명 : Adapter_TmapPathDetail
 * 
 */
public class Adapter_TmapPathDetail extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityTmapPathDetail> mSendMessageList = new ArrayList<EntityTmapPathDetail>();
	private LayoutInflater mLiInflater;

	public Adapter_TmapPathDetail(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMovePathDetailList(ArrayList<EntityTmapPathDetail> list) {

		mSendMessageList = list;
	}

	public int getCount() {
		return mSendMessageList.size();
	}

	public EntityTmapPathDetail getItem(int position) {
		return mSendMessageList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_tmap_path, null);
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

		EntityTmapPathDetail movePathDetailInfo = (EntityTmapPathDetail)mSendMessageList.get(position);		
		if(movePathDetailInfo.mName.equals("")){
			viewHolder.location.setText("");
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
			viewHolder.turnType.setText(Defines.getTurnType(mContext, movePathDetailInfo.mTurnType));	
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
