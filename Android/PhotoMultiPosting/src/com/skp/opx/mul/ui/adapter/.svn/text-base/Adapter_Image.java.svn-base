package com.skp.opx.mul.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.skp.opx.mul.R;
import com.skp.opx.mul.entity.EntityImage;

/**
 * @설명 : T cloud 사진조회 Adapter
 * @클래스명 : Adapter_Image
 * 
 */
public class Adapter_Image extends BaseAdapter {

	private Context	mContext;
	private ArrayList<EntityImage> mImages = new ArrayList<EntityImage>();
	private LayoutInflater mLiInflater;

	public Adapter_Image(Context context) {
		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setImageInfolist(ArrayList<EntityImage> list) {
		mImages = list;
	}

	public int getCount() {
		return mImages.size();
	}

	public EntityImage getItem(int position) {
		return mImages.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			convertView = mLiInflater.inflate(R.layout.list_item_comment, null);
			viewHolder = new ViewHolder();

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		EntityImage image =(EntityImage)mImages.get(position);
		
		viewHolder.thumbnail.setText(image.getThumbnailUrl());

		return convertView;
	}

	public static class ViewHolder {
		
		public TextView thumbnail;
	}
}
