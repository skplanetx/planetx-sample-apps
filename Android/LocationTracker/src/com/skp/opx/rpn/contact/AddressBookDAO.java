package com.skp.opx.rpn.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;

/**
 * @설명 : 주소록 DAO
 * @클래스명 : AddressBookDAO 
 *
 */
public class AddressBookDAO {

	public static final String[] DATA_PROJECTION = new String[] { Data.DISPLAY_NAME, Data.DATA1 };

	public static List<EntityContact> getContactList (Context context) {

		ArrayList<EntityContact> contactList = new ArrayList<EntityContact>();
		StringBuilder where = new StringBuilder();

		where.append(Data.MIMETYPE); where.append("="); where.append("'"); where.append(Phone.CONTENT_ITEM_TYPE); where.append("'");
		where.append(" AND ");
		where.append("("); 
		where.append(Data.DATA2); where.append("='2'");
		where.append(")");

		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(Data.CONTENT_URI, DATA_PROJECTION, where.toString(), null, null);

			if (cursor != null) {
				while (cursor.moveToNext()) {
					contactList.add(new EntityContact(cursor.getString(0), cursor.getString(1).replace("-", "")));
				}
			}
		} 	
		catch (Exception e) {
			Log.e(AddressBookDAO.class.getSimpleName(), e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
				cursor = null;
			}
		}
		return contactList;
	}
}
