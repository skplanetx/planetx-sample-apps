package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.Entity11stSearchResult;
import com.skp.opx.svc.utils.ImageDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @설명 : 11번가 검색결과 Adapter  
 * @클래스명 : Adapter_11stSearch
 * 
 */
public class Adapter_11stSearch extends BaseAdapter {

	private Context	mContext;
	private ArrayList<Entity11stSearchResult> m11stSearchList = new ArrayList<Entity11stSearchResult>();
	private LayoutInflater mLiInflater;

	public Adapter_11stSearch(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void set11stSearchList(ArrayList<Entity11stSearchResult> list) {

		m11stSearchList = list;
	}

	public int getCount() {
		return m11stSearchList.size();
	}

	public Entity11stSearchResult getItem(int position) {
		return m11stSearchList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_eleven_st_search, null);
			viewHolder = new ViewHolder();
			viewHolder.image_imageView = (ImageView)convertView.findViewById(R.id.image_iv);
			viewHolder.name_textView = (TextView)convertView.findViewById(R.id.name_tv);
			viewHolder.price_textView = (TextView)convertView.findViewById(R.id.price_tv);
			viewHolder.sale_textView = (TextView)convertView.findViewById(R.id.sale_tv);
			viewHolder.nick_textView = (TextView)convertView.findViewById(R.id.nick_tv);
			viewHolder.seller_textView = (TextView)convertView.findViewById(R.id.seller_tv);
			viewHolder.satisfy_textView = (TextView)convertView.findViewById(R.id.satisfy_tv);
			viewHolder.review_textView = (TextView)convertView.findViewById(R.id.review_tv);
			viewHolder.delivery_textview = (TextView)convertView.findViewById(R.id.delivery_tv);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		Entity11stSearchResult productsInfo = (Entity11stSearchResult)m11stSearchList.get(position);

		ImageDownloader.download(productsInfo.ProductImage, viewHolder.image_imageView);
		viewHolder.name_textView.setText(productsInfo.ProductName);
		viewHolder.price_textView.setText(String.valueOf(productsInfo.ProductPrice) + mContext.getString(R.string.won));
		viewHolder.sale_textView.setText(mContext.getString(R.string.discount_price) + String.valueOf(productsInfo.SalePrice) + mContext.getString(R.string.won));
		viewHolder.seller_textView.setText(mContext.getString(R.string.seller) + " : " + productsInfo.Seller);
		viewHolder.nick_textView.setText("("+ productsInfo.SellerNick + ")");
		viewHolder.satisfy_textView.setText(mContext.getString(R.string.product_satis) + String.valueOf(productsInfo.BuySatisfy)+ "%");
		viewHolder.review_textView.setText(mContext.getString(R.string.postscipt_review) + String.valueOf(productsInfo.ReviewCount)+ mContext.getString(R.string.cnt));
		viewHolder.delivery_textview.setText(mContext.getString(R.string.delivery) + productsInfo.Delivery);

		return convertView;
	}

	public static class ViewHolder {

		public ImageView image_imageView;
		public TextView name_textView;
		public TextView price_textView;
		public TextView sale_textView;
		public TextView nick_textView;
		public TextView seller_textView;
		public TextView satisfy_textView;
		public TextView review_textView;
		public TextView delivery_textview;

	}

}
