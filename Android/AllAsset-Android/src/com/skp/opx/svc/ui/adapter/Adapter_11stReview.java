package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.Entity11stReviews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @설명 : 11번가 리뷰 Adapter 
 * @클래스명 : Adapter_11stReview
 * 
 */
public class Adapter_11stReview extends BaseAdapter {

	private Context	mContext;
	private ArrayList<Entity11stReviews> m11stReviews = new ArrayList<Entity11stReviews>();
	private LayoutInflater mLiInflater;
	private Entity11stReviews reviewsInfo;

	public Adapter_11stReview(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void set11stReviewList(ArrayList<Entity11stReviews> list) {

		m11stReviews = list;
	}

	public int getCount() {
		return m11stReviews.size();
	}

	public Entity11stReviews getItem(int position) {
		return m11stReviews.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_eleven_st_review, null);
			viewHolder = new ViewHolder();
			viewHolder.title_textView = (TextView)convertView.findViewById(R.id.title_tv);
			viewHolder.writer_textView = (TextView)convertView.findViewById(R.id.writer_tv);
			viewHolder.date_textView = (TextView)convertView.findViewById(R.id.date_tv);
			viewHolder.detail_button = (Button)convertView.findViewById(R.id.detail_bt);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		reviewsInfo = (Entity11stReviews)m11stReviews.get(position);

		viewHolder.title_textView.setText(reviewsInfo.Title);
		viewHolder.writer_textView.setText(reviewsInfo.Writer);
		viewHolder.date_textView.setText(reviewsInfo.Date);
		viewHolder.detail_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent review_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewsInfo.Url));
				mContext.startActivity(review_intent);

			}
		});

		return convertView;
	}


	public static class ViewHolder {

		public TextView title_textView;
		public TextView writer_textView;
		public TextView date_textView;
		public Button   detail_button;

	}


}
