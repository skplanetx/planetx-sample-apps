
package com.skp.opx.rpn.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.skp.opx.rpn.R;
import com.skp.opx.rpn.entity.EntityAutoComplete;
import com.skp.opx.rpn.entity.EntityMovePathDetail;
import com.skp.opx.rpn.ui.adapter.Adapter_MovePathDetail.ViewHolder;

/**
 * @설명 : 출발지, 목적지 자동 완성 Adapter
 * @클래스명 : Adapter_AutoComplete 
 *
 */
public class Adapter_AutoComplete<T> extends ArrayAdapter<EntityAutoComplete> implements Filterable{

	private ArrayList<EntityAutoComplete> mAutoCompleteList = new ArrayList<EntityAutoComplete>();
	private LayoutInflater mLiInflater;

    public Adapter_AutoComplete(Context c, int textViewResourceId, 
            ArrayList<EntityAutoComplete> arrays) {
        super(c, textViewResourceId, arrays);
        mAutoCompleteList = arrays;
        this.mLiInflater = LayoutInflater.from(c);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
 
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filterResults.count = getCount();
            }
            
            for (int i = 0; i < mAutoCompleteList.size(); i++) {
				if(mAutoCompleteList.get(i).mName.contains(constraint)){
					return filterResults;
				}else{
					return null;
				}
			}
         
            return filterResults;
        }
 
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            }
        }
    };


	@Override
	public int getPosition(EntityAutoComplete item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

	@Override
	public EntityAutoComplete getItem(int position) {
		// TODO Auto-generated method stub
		return mAutoCompleteList.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(R.layout.list_item_auto_complete, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView)convertView.findViewById(R.id.name);

			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		EntityAutoComplete movePathDetailInfo = (EntityAutoComplete)mAutoCompleteList.get(position);		
		viewHolder.name.setText(movePathDetailInfo.mName);
		
		return convertView;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView name;

	}



}
