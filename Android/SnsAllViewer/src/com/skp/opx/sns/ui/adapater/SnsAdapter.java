package com.skp.opx.sns.ui.adapater;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.skp.opx.sns.R;
import com.skp.opx.sns.entity.EntityHomePostsViews;
import com.skp.opx.sns.util.ImageDownloader;

/**
 * @설명 : SNS 모아보기 메인 Adapter
 * @클래스명 : SnsAdapter 
 *
 */
public class SnsAdapter extends BaseAdapter {

	public enum DISPLAY_MODE {SIMPLE, DETAIL}
	
	private Context mContext					= null;
	private List<EntityHomePostsViews> mEntityAllList = new ArrayList<EntityHomePostsViews>();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private SimpleDateFormat newDateFormat = new SimpleDateFormat("yy/MM/dd a hh:mm");
	
	private DISPLAY_MODE mDisplayMode = DISPLAY_MODE.SIMPLE;
	
	public SnsAdapter(Context context) {
		
		mContext 		= context;
	}
	
	public void setEntityArray(List<EntityHomePostsViews> list) {
		
		mEntityAllList = list;
	}
	
	public void clearEntityArray() {
		
		if(mEntityAllList != null) {
			mEntityAllList.clear();
			notifyDataSetChanged();
		}
	}
	
	public void setDisplayMode(DISPLAY_MODE displayMode) {
		
		mDisplayMode = displayMode;
		
		if(mEntityAllList != null) {
			notifyDataSetChanged();
		}
	}
	
	public int getCount() {
		
		return mEntityAllList.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		
		AllSnsListHolder snsListHolder;
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_main_home_row, null);
					
			snsListHolder = new AllSnsListHolder();
			snsListHolder.ivThumbnail 	= (ImageView)view.findViewById(R.id.thumbnail_iv);
			snsListHolder.tvUserName	= (TextView)view.findViewById(R.id.username_tv);
			snsListHolder.tvWrittenDate	= (TextView)view.findViewById(R.id.written_date_tv);
			snsListHolder.tvBody			= (TextView)view.findViewById(R.id.body_tv);
			snsListHolder.tvPicture		= (ImageView)view.findViewById(R.id.picture_tv);
			snsListHolder.subLayout					= (LinearLayout)view.findViewById(R.id.sub_layout);
			snsListHolder.tvBodyLayout				= (LinearLayout)view.findViewById(R.id.body_tv_layout);
			snsListHolder.tvPictureLayout			= (LinearLayout)view.findViewById(R.id.picture_tv_lauout);
			snsListHolder.tvWrittenDateLyout		= (LinearLayout)view.findViewById(R.id.witten_date_tv_lauout);
			
			view.setTag(snsListHolder);
		} else {
			snsListHolder	= (AllSnsListHolder)view.getTag();
		}
		EntityHomePostsViews entity = mEntityAllList.get(position);
		
		try {
			if(entity.publishTime != null) {
				entity.publishTime = entity.publishTime.replace("T", " ");
				entity.publishTime = entity.publishTime.replace("+0000", "");
				Date date = dateFormat.parse(entity.publishTime);
				snsListHolder.tvWrittenDate.setText(newDateFormat.format(date));
			} else {
				snsListHolder.tvWrittenDate.setText(mContext.getString(R.string.no_timeinfo));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ImageDownloader.download(entity.image, snsListHolder.ivThumbnail);
		snsListHolder.tvUserName.setText(entity.name);
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		if(mDisplayMode == DISPLAY_MODE.SIMPLE) {
			params1.weight = 1.0f;
			params2.weight = 1.0f;
			snsListHolder.subLayout.setOrientation(LinearLayout.HORIZONTAL);
			snsListHolder.tvBodyLayout.setLayoutParams(params1);
			snsListHolder.tvWrittenDateLyout.setLayoutParams(params2);
			snsListHolder.tvBody.setSingleLine(true);
			snsListHolder.tvBody.setEllipsize(TruncateAt.END);
			snsListHolder.tvPictureLayout.setVisibility(View.GONE);
			snsListHolder.tvBody.setAutoLinkMask(0);
		} else {
			snsListHolder.subLayout.setOrientation(LinearLayout.VERTICAL);
			snsListHolder.tvBody.setEllipsize(null);
			snsListHolder.tvBody.setSingleLine(false);
			if(entity.picture != null){
				ImageDownloader.download(entity.picture, snsListHolder.tvPicture);
				snsListHolder.tvPictureLayout.setVisibility(View.VISIBLE);
			}else{
				snsListHolder.tvPictureLayout.setVisibility(View.GONE);
			}
		}
		snsListHolder.tvBody.setText(entity.content);
		
		return view;
	}

	static class AllSnsListHolder {
		ImageView ivThumbnail;
		TextView tvUserName;
		TextView tvWrittenDate;
		TextView tvBody;
		ImageView tvPicture;
		LinearLayout tvBodyLayout;
		LinearLayout tvPictureLayout;
		LinearLayout tvWrittenDateLyout;
		LinearLayout subLayout;
	}
}
