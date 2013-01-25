package com.skp.opx.sns.ui.adapater;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.opx.sns.R;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.core.client.RequestGenerator;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityAbstract;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sns.entity.EntityRecommendToFriend;
import com.skp.opx.sns.util.PreferenceUtil;

/**
 * @설명 : 지인 추천 Adapter
 * @클래스명 : RecommendFriendsAdapter 
 *
 */
public class RecommendFriendsAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<EntityRecommendToFriend> mEntityRecommend = new ArrayList<EntityRecommendToFriend>();
	LayoutInflater mInflater;

	public RecommendFriendsAdapter(Context context) {
		mContext	= context;
		mInflater 	= (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setRecommendList(ArrayList<EntityRecommendToFriend> list) {
		mEntityRecommend = list;
	}

	public void clearEntityArray() {

		if(mEntityRecommend != null) {
			mEntityRecommend.clear();
			notifyDataSetChanged();
		}
	}
	public int getCount() {
		return mEntityRecommend.size();
	}

	public EntityRecommendToFriend getItem(int position) {
		return mEntityRecommend.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		RecommendViewHolder recommendViewHolder;

		final EntityRecommendToFriend recommendInfo = (EntityRecommendToFriend) mEntityRecommend.get(position);

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.list_item_recommend, null);

			recommendViewHolder = new RecommendViewHolder();
			recommendViewHolder.recommendName	= (TextView) convertView.findViewById(R.id.name_tv);
			recommendViewHolder.requestButton	= (Button) convertView.findViewById(R.id.add_bt);

			recommendViewHolder.requestButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setTitle(R.string.addGraph_confirm);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub				
							/**
							 * @설명 : 소셜 컴포넌트 소셜 그래프 관리 지인 추가 요청
							 * @RequestURI : https://apis.skplanetx.com/social/graph/users/{appUserId}/nodes/request
							 * @RequestPathParam : 
							 * {appUserId}3rd Party 서비스의 사용자 ID입니다
							 */
							try {
								String strTargetUserID = mEntityRecommend.get(position).appUserId;
								//Request Payload
								String mStrPayload = RequestGenerator.makePayload_AddGraph(strTargetUserID);
								//Bundle 생성
								RequestBundle bundle = AsyncRequester.make_PUT_POST_bundle(mContext, RequestGenerator.makeURI_AddGraph(PreferenceUtil.getAppUserID(mContext, false)), null, mStrPayload, null);
								//API 호출
								AsyncRequester.request(mContext, bundle, HttpMethod.POST, new EntityParserHandler(new EntityAbstract(), new OnEntityParseComplete() {

									@Override
									public void onParsingComplete(Object entityArray) {
										Toast.makeText(mContext, "ADD GRAPH COMPLETE!", Toast.LENGTH_SHORT).show();
									}
								}));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					builder.setNegativeButton(R.string.cancel, null);
					builder.create().show();
				}
			});
			
			convertView.setTag(recommendViewHolder);

		} else {
			recommendViewHolder = (RecommendViewHolder)convertView.getTag();
		}

		recommendViewHolder.recommendName.setText(recommendInfo.name);

		return convertView;
	}

	static class RecommendViewHolder {
		public Button requestButton;
		public TextView recommendName;
	}
}
