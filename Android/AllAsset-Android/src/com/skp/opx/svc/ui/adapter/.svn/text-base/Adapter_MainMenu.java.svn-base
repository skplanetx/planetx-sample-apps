package com.skp.opx.svc.ui.adapter;

import java.util.ArrayList;

import com.skp.opx.svc.R;
import com.skp.opx.svc.entity.EntityMainMenu;
import com.skp.opx.svc.ui.CyworldFriendListActivity;
import com.skp.opx.svc.ui.MelonChartListActivity;
import com.skp.opx.svc.ui.NateOnBuddiesListActivity;
import com.skp.opx.svc.ui.St11MainCategoryActivity;
import com.skp.opx.svc.ui.TmapPathSearchActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;


/**
 * @설명 : Main 메뉴 Adapter
 * @클래스명 : Adapter_MainMenu
 * 
 */
public class Adapter_MainMenu extends BaseAdapter{

	private Context	mContext;
	private ArrayList<EntityMainMenu> mMenuList = new ArrayList<EntityMainMenu>();
	private LayoutInflater mLiInflater;
	private EntityMainMenu listInfo;

	public Adapter_MainMenu(Context context) {
		mContext = context;
		mLiInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setMainlist(ArrayList<EntityMainMenu> list) {
		mMenuList = list;
	}

	public int getCount() {
		return mMenuList.size();
	}

	public EntityMainMenu getItem(int position) {
		return mMenuList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			convertView = mLiInflater.inflate(R.layout.list_item_menu, null);
			viewHolder = new ViewHolder();
			viewHolder.menu_bt=(Button)convertView.findViewById(R.id.item_tv);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		listInfo =(EntityMainMenu)mMenuList.get(position);

		viewHolder.menu_bt.setText(listInfo.menuTitle);

		viewHolder.menu_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mMenuList.get(position).menuTitle.equals(mContext.getString(R.string.nate_on))){
					Intent intent = new Intent(mContext, NateOnBuddiesListActivity.class );
					mContext.startActivity( intent );
				}else if(mMenuList.get(position).menuTitle.equals(mContext.getString(R.string.cyworld))){
					Intent intent = new Intent(mContext, CyworldFriendListActivity.class );
					mContext.startActivity( intent );
				}else if(mMenuList.get(position).menuTitle.equals(mContext.getString(R.string.melon))){
					Intent intent = new Intent(mContext, MelonChartListActivity.class );
					mContext.startActivity( intent );
				}else if(mMenuList.get(position).menuTitle.equals(mContext.getString(R.string.eleven_st))){
					Intent intent = new Intent(mContext, St11MainCategoryActivity.class );
					mContext.startActivity( intent );
				}else if(mMenuList.get(position).menuTitle.equals(mContext.getString(R.string.tmap))){
					Intent intent = new Intent(mContext, TmapPathSearchActivity.class );
					mContext.startActivity( intent );
				}
			}
		});

		return convertView;
	}

	public static class ViewHolder {
		public Button menu_bt;
	}

}
