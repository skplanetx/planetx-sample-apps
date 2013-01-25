package com.skp.opx.mss.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;

import com.skp.opx.mss.ui.R;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.skp.opx.mss.util.FlickrUtil;

public class Adapter_Flickr extends BaseAdapter {

	private Context	mContext;

	public Adapter_Flickr(Context context) {

		mContext = context;
	}

	@Override
	public int getCount() {
		return FlickrUtil.thumbnailUrl.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ImageView imageView;

		if (convertView == null) {

			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setAdjustViewBounds(true);
		} else {

			imageView = (ImageView) convertView;
		}

		FlickrUtil.imageLoader.displayImage(FlickrUtil.thumbnailUrl.get(position), imageView, FlickrUtil.options, new SimpleImageLoadingListener() {

			@Override
			public void onLoadingComplete(Bitmap loadedImage) {

				Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
				imageView.setAnimation(anim);
				anim.start();
			}
		});
		
		return imageView;
	}
}
