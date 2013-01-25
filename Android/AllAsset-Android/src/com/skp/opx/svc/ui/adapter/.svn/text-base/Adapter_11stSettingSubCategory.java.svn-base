package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.constants.Defines;
import com.skp.opx.svc.entity.Entity11stCategorySub;
import com.skp.opx.svc.ui.MainActivity;
import com.skp.opx.svc.ui.dialog.CustomAlertDialog;
import com.skp.opx.svc.utils.PreferenceUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @설명 : 11번가 하위 카테고리 Adapter (카테고리 선택) 
 * @클래스명 : Adapter_11stSettingSubCategory
 * 
 */
public class Adapter_11stSettingSubCategory extends BaseAdapter {

	private Context	mContext;
	private ArrayList<Entity11stCategorySub> m11stCategoryList = new ArrayList<Entity11stCategorySub>();
	private LayoutInflater mLiInflater;
	private Entity11stCategorySub categoryInfo;

	public Adapter_11stSettingSubCategory(Context context) {

		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void set11stSubCategoryList(ArrayList<Entity11stCategorySub> list) {

		m11stCategoryList = list;
	}

	public int getCount() {
		return m11stCategoryList.size();
	}

	public Entity11stCategorySub getItem(int position) {
		return m11stCategoryList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_common_2, null);
			viewHolder = new ViewHolder();
			viewHolder.name_textView = (TextView)convertView.findViewById(R.id.item_tv);
			viewHolder.select_button = (Button)convertView.findViewById(R.id.select_bt);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		categoryInfo = (Entity11stCategorySub)m11stCategoryList.get(position);

		viewHolder.name_textView.setText(categoryInfo.CategoryName);
		viewHolder.select_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Handler dialog_handler = new Handler() {
					@Override
					public void handleMessage(Message message) {

						PreferenceUtil.putSharedPreference(mContext, Defines.ST11_SETTING_KEYWORD , m11stCategoryList.get(position).CategoryName );
						Intent intent_home = new Intent(mContext, MainActivity.class );
						intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
						mContext.startActivity( intent_home );
					}
				};

				CustomAlertDialog alert = new CustomAlertDialog(mContext);
				alert.showYesDialog(mContext.getString(R.string.app_name), mContext.getString(R.string.category_settings), mContext.getString(R.string.confirm), dialog_handler);
			}
		});

		return convertView;
	}


	public static class ViewHolder {

		public TextView name_textView;
		public Button   select_button;

	}



}
