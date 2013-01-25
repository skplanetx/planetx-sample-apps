package com.skp.opx.mul.ui.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.mul.R;
import com.skp.opx.mul.entity.EntityFeedInfo;

/**
 * @설명 : 사용자 게시글 조회 Adapter
 * @클래스명 : Adapter_MyPosting
 * 
 */
public class Adapter_MyPosting extends BaseAdapter {

	private Context	mContext;
	private LayoutInflater mLiInflater;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy년MM월dd일 a hh시mm분");
	
	private List<EntityFeedInfo> mEntityFeedInfo = new ArrayList<EntityFeedInfo>();
	
	public Adapter_MyPosting(Context context) {
		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setEntityArray(List<EntityFeedInfo> list){
		mEntityFeedInfo = list;
		notifyDataSetChanged();
	}
	
	public void clearEntityArray() {
		
		if(mEntityFeedInfo != null) {
			mEntityFeedInfo.clear();
			notifyDataSetChanged();
		}
	}

	public int getCount() {
		return mEntityFeedInfo.size();
	}

	public EntityFeedInfo getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			convertView = mLiInflater.inflate(R.layout.list_item_myposting, null);
			viewHolder = new ViewHolder();
			viewHolder.content_textView=(TextView)convertView.findViewById(R.id.content_tv);
			viewHolder.date_textView=(TextView)convertView.findViewById(R.id.date_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityFeedInfo feedInfo =(EntityFeedInfo)mEntityFeedInfo.get(position);

		try {
			if(feedInfo.publishTime != null) {
				feedInfo.publishTime = feedInfo.publishTime.replace("T", " ");
				feedInfo.publishTime = feedInfo.publishTime.replace("+0000", "");
				Date date = dateFormat.parse(feedInfo.publishTime);
				viewHolder.date_textView.setText(newDateFormat.format(date));
			}
			else{
				viewHolder.date_textView.setText(mContext.getString(R.string.no_timeinfo));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if(feedInfo.content == null || feedInfo.content.length() <= 0)
		{
			viewHolder.content_textView.setText(mContext.getString(R.string.not_exist_content));
		} else{
			viewHolder.content_textView.setText(feedInfo.content);
		}
		viewHolder.content_textView.setSingleLine(true);
		viewHolder.content_textView.setEllipsize(TruncateAt.END);
		viewHolder.content_textView.setAutoLinkMask(0);
		
		return convertView;
	}

	public static class ViewHolder {
		public TextView content_textView;
		public TextView date_textView;
	}
}
