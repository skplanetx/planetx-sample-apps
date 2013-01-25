package com.skp.opx.sns.ui.adapater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.opx.sns.R;
import com.skp.opx.sns.entity.EntityCommentViews;

/**
 * @설명 : 댓글 리스트 Adapter
 * @클래스명 : CommentListAdapter 
 *
 */
public class CommentListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<EntityCommentViews> mEntityComments;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private SimpleDateFormat newDateFormat = new SimpleDateFormat("yy/MM/dd a hh:mm");

	public CommentListAdapter(Context context, ArrayList<EntityCommentViews> entityComments) {

		mContext		= context;
		mEntityComments = entityComments;
	}

	public void setEntityList(ArrayList<EntityCommentViews> list){
		mEntityComments = list;
	}

	public int getCount() {
		return mEntityComments.size();
	}

	public Object getItem(int position) {
		return mEntityComments.get(position);
	}


	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		CommentViewHolder commentViewHolder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.activity_comment_row, null);

			//holder
			commentViewHolder = new CommentViewHolder();
			commentViewHolder.tvCommentUserName	= (TextView) view.findViewById(R.id.comment_username_tv);
			commentViewHolder.tvCommentContents	= (TextView) view.findViewById(R.id.comment_contents_tv);
			commentViewHolder.tvCommentDate		= (TextView) view.findViewById(R.id.comment_date_tv);

			view.setTag(commentViewHolder);
		} else {
			commentViewHolder = (CommentViewHolder)view.getTag();
		}

		//bind each data to row items
		EntityCommentViews commentInfo = (EntityCommentViews) mEntityComments.get(position);
		commentViewHolder.tvCommentUserName.setText(commentInfo.name);
		commentViewHolder.tvCommentContents.setText(commentInfo.content);
		//		commentViewHolder.tvCommentDate.setText(commentInfo.publishTime);
		try {
			if(commentInfo.publishTime != null) {
				commentInfo.publishTime = commentInfo.publishTime.replace("T", " ");
				commentInfo.publishTime = commentInfo.publishTime.replace("+0000", "");
				Date date = dateFormat.parse(commentInfo.publishTime);
				commentViewHolder.tvCommentDate.setText(newDateFormat.format(date));
			} else {
				commentViewHolder.tvCommentDate.setText(mContext.getString(R.string.no_timeinfo));
			}
		}catch (ParseException e) {
			e.printStackTrace();
		}
			return view;
		}

		static class CommentViewHolder {

			TextView tvCommentUserName;
			TextView tvCommentContents;
			TextView tvCommentDate;
		}
	}
