package com.skp.opx.sns.ui.adapater;

import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.QuickContactBadge;
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
import com.skp.opx.sns.contact.EntityContact;
import com.skp.opx.sns.entity.EntityUploadPhoneBookPayload;
import com.skp.opx.sns.util.PreferenceUtil;

/**
 * @설명 : 폰북 등록 Adapter
 * @클래스명 : FriendContactAdapter 
 *
 */
public class FriendContactAdapter extends BaseAdapter {
	Context mContext ;
	List<EntityContact> mEntityContacts;
	LayoutInflater mInflater;

	public FriendContactAdapter(Context context, List<EntityContact> entityContacts) {
		mContext 		= context; 
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mEntityContacts = entityContacts;
	}

	public void setAllChecked(boolean isChecked) {
		Iterator<EntityContact> iter = mEntityContacts.iterator();
		while(iter.hasNext()) {
			iter.next().isChecked = isChecked;
		}
	}

	public boolean getCheckedState(int position){
		return mEntityContacts.get(position).isChecked;
	}

	public List<EntityContact> getEntityChecked(){
		return mEntityContacts;
	}

	@Override
	public int getCount() {
		return mEntityContacts.size();
	}

	@Override
	public EntityContact getItem(int position) {
		// TODO Auto-generated method stub
		return mEntityContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ContactViewHolder contactViewHolder;

		final EntityContact entityContact = (EntityContact) mEntityContacts.get(position);

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.activity_friend_row, null);

			contactViewHolder = new ContactViewHolder();
			contactViewHolder.contactBadge 		= (QuickContactBadge) convertView.findViewById(R.id.contact_qcb);
			contactViewHolder.tvContactName 	= (TextView) convertView.findViewById(R.id.contact_name_tv);
			contactViewHolder.tvContactNumber 	= (TextView) convertView.findViewById(R.id.contact_number_tv);
			contactViewHolder.btContactUpload 	= (Button) convertView.findViewById(R.id.contact_upload_bt);
			contactViewHolder.cbContactUpload 	= (CheckBox) convertView.findViewById(R.id.contact_upload_chk);
			contactViewHolder.cbContactUpload.setTag(position);

			contactViewHolder.cbContactUpload.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					mEntityContacts.get(position).isChecked=isChecked;
				}
			});


			contactViewHolder.btContactUpload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setTitle(R.string.upload_confirm);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {						
							/**
							 * @설명 : 소셜 컴포넌트 소셜 그래프 관리 폰북 등록
							 * @RequestURI : https://apis.skplanetx.com/social/graph/users/{appUserId}/phoneBook
							 * @RequestPathParam : 
							 * {appUserId} 3rd Party 서비스의 사용자 ID입니다
							 */
							try {	
								//Request Payload
								String mStrPayload = RequestGenerator.makePayload_UploadPhoneBook(new EntityUploadPhoneBookPayload(entityContact.phoneNumber, entityContact.name));
								//Bundle 생성
								RequestBundle bundle = AsyncRequester.make_PUT_POST_bundle(mContext, RequestGenerator.makeURI_UploadPhoneBook(PreferenceUtil.getAppUserID(mContext, false)), null, mStrPayload, null);
								//API 호출
								AsyncRequester.request(mContext, bundle, HttpMethod.PUT, new EntityParserHandler(new EntityAbstract(), new OnEntityParseComplete() {

									@Override
									public void onParsingComplete(Object entityArray) {
										Toast.makeText(mContext, R.string.complete_msg, Toast.LENGTH_SHORT).show();
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

			convertView.setTag(contactViewHolder);

		} else {
			contactViewHolder  = (ContactViewHolder) convertView.getTag();
		}

		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, entityContact.contactID);
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
		contactViewHolder.contactBadge.setImageURI(photoUri);

		if(contactViewHolder.contactBadge.getDrawable() == null) {
			contactViewHolder.contactBadge.setImageResource(R.drawable.icon_contact);
		}

		contactViewHolder.contactBadge.assignContactUri(ContentUris.withAppendedId(Contacts.CONTENT_URI, entityContact.contactID));
		contactViewHolder.contactBadge.assignContactFromPhone(entityContact.phoneNumber, true);

		contactViewHolder.tvContactName.setText(entityContact.name);
		contactViewHolder.tvContactNumber.setText(PhoneNumberUtils.formatNumber(entityContact.phoneNumber));

		contactViewHolder.cbContactUpload.setChecked(entityContact.isChecked);

		return convertView;
	}

	static class ContactViewHolder {
		QuickContactBadge 		contactBadge;
		TextView 				tvContactName;
		TextView 				tvContactNumber;
		Button					btContactUpload;
		CheckBox				cbContactUpload;
	}
}
