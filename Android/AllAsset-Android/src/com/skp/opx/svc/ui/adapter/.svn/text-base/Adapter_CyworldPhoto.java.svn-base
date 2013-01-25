package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityCyPhoto;
import com.skp.opx.svc.utils.ImageDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @설명 : Cyworld 사진첩목록 Adapter
 * @클래스명 : Adapter_CyworldPhoto
 * 
 */
public class Adapter_CyworldPhoto extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityCyPhoto> mCyPhotoList = new ArrayList<EntityCyPhoto>();
	private LayoutInflater mLiInflater;

	public Adapter_CyworldPhoto(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setCyworldPhotoList(ArrayList<EntityCyPhoto> list) {

		mCyPhotoList = list;
	}

	public int getCount() {
		return mCyPhotoList.size();
	}

	public EntityCyPhoto getItem(int position) {
		return mCyPhotoList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_cy_photo, null);
			viewHolder = new ViewHolder();
			viewHolder.image_imageView = (ImageView)convertView.findViewById(R.id.image_iv);
			viewHolder.title_textView = (TextView)convertView.findViewById(R.id.title_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.date_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityCyPhoto photoInfo = (EntityCyPhoto)mCyPhotoList.get(position);
		
		ImageDownloader.download(photoInfo.photoVmUrl, viewHolder.image_imageView);
		viewHolder.title_textView.setText(photoInfo.title);
		viewHolder.date_textView.setText(photoInfo.writeDate);
		
		return convertView;
	}


	public static class ViewHolder {

		public ImageView image_imageView;
		public TextView title_textView;
		public TextView date_textView;
	}


}
