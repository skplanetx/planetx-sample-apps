package com.skp.opx.rpn.database;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

/**
 * @설명 : 발신함 DAO 
 * @클래스명 : DaoSendBox 
 *
 */
public class DaoSendBox {

	private static DaoSendBox sTheInstance = null;
	private DaoSendBox() {}
	
	public static synchronized DaoSendBox getInstance() {
		
		if(sTheInstance == null) {
			sTheInstance = new DaoSendBox();
		}
		
		return sTheInstance;
	}
	
	/** 
	 *  발신함 모두 삭제 Method
	 * */
	public void deleteAllData(Context context) throws SQLiteException {
		
		String sql = "delete from " + EntitySendBox.class.getSimpleName();
		Helper.getInstance(context).getWritableDatabase().execSQL(sql, new String[0]);
		Helper.getInstance(context).close();
	}	
	
	/** 
	 *  메시지 타입으로 발신함 삭제 Method
	 * */
	public void deleteAllDataByType(Context context, int messageType) throws SQLiteException {
		
		String sql = "delete from " + EntitySendBox.class.getSimpleName() + " where mMessageType = " + messageType;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql, new String[0]);
		Helper.getInstance(context).close();
	}	

	/** 
	 *  이름으로 데이터 삭제 Method
	 * */
	public void deleteData(Context context, String name) {

		String sql ="delete from " + EntitySendBox.class.getSimpleName() + " where mReceiver = " + "'"+name+"'";
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}	
	/**
	 * 발신함 상세 아이디로 삭제 Method
	 */	
	public void deleteDataById(Context context, long id) {

		String sql ="delete from " + EntitySendBox.class.getSimpleName() + " where ID = " + id;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}	
   /**
    * 전화번호 기준으로 삭제 Method
    */
	public void deleteDataByMdn(Context context, String mdn) {

		String sql ="delete from " + EntitySendBox.class.getSimpleName() + " where mMdn = " + "'"+mdn+"'";
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}	
	
	public long insertSendInfo(Context context, EntitySendBox entity) throws Exception {
		
		ContentValues values = new ContentValues();
		
		Field fieldArray[] = entity.getClass().getFields();
		for(int count = 0, maxCount = fieldArray.length; count < maxCount; count++) {
			if(fieldArray[count].getType() == int.class) {
				values.put(fieldArray[count].getName(), (Integer)fieldArray[count].get(entity));
			} else if(fieldArray[count].getType() == long.class) {
				values.put(fieldArray[count].getName(), (Long)fieldArray[count].get(entity));
			} else if(fieldArray[count].getType() == String.class) {
				values.put(fieldArray[count].getName(), (String)fieldArray[count].get(entity));
			}
		}
		
		long extraID = Helper.getInstance(context).getWritableDatabase().insert(entity.getClass().getSimpleName(), null, values);
		Helper.getInstance(context).close();
		return extraID;
	}
	/**
	 * 발신함 전체 메시지 Select Method
	 */
	public ArrayList<EntitySendBox> getAllSendInfoList(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntitySendBox.class.getSimpleName() +" order by mDeliveryTime DESC limit 50";
		return selectSendInfoList(context, sql);
	}
	/**
	 * 메시지 주소록 발신함용 데이터 Select Method (mdn 으로 구분한 데이터 리턴)
	 */
	public ArrayList<EntitySendBox> getContactSendInfoListByMdn(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntitySendBox.class.getSimpleName() + " where mMessageType = 0 group by mMdn order by mDeliveryTime DESC limit 50";
		return selectSendInfoList(context, sql);
	}
	/**
	 *네이트 쪽지 발신함용 데이터 Select Method (계정 으로 구분한 데이터 리턴)
	 */
	public ArrayList<EntitySendBox> getContactSendInfoListByAccount(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntitySendBox.class.getSimpleName() + " where mMessageType = 1 group by mMdn order by mDeliveryTime DESC";
		return selectSendInfoList(context, sql);
	}
	/**
	 * 메시지 주소록 발신함용 데이터 Select Method (mdn 으로 구분한 데이터 리턴)
	 */
	public ArrayList<EntitySendBox> getContactSendInfoListByMdn(Context context, String mdn) throws SQLiteException {
		
		String sql = "select * from " + EntitySendBox.class.getSimpleName() + " where mMdn = '"+mdn+"' order by mDeliveryTime DESC";
		return selectSendInfoList(context, sql);
	}
	/**
	 *네이트 쪽지 발신함용 데이터 Select Method (계정 으로 구분한 데이터 리턴)
	 */
	public ArrayList<EntitySendBox> getContactSendInfoListByAccount(Context context, String mdn) throws SQLiteException {
		
		String sql = "select * from " + EntitySendBox.class.getSimpleName() + " where mMdn = '"+mdn+"' order by mDeliveryTime DESC";
		return selectSendInfoList(context, sql);
	}
	/**
	 * 주소록으로 발신한 메시지 Select Method 
	 */
	public ArrayList<EntitySendBox> getContactSendInfoList(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntitySendBox.class.getSimpleName() + " where mMessageType = 0 order by mDeliveryTime DESC";
		return selectSendInfoList(context, sql);
	}
	/**
	 * 네이트온 쪽지 발송한 데이터 Select Method
	 */
	public ArrayList<EntitySendBox> getNateFriendsSendInfoList(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntitySendBox.class.getSimpleName() + " where mMessageType = 1 order by mDeliveryTime DESC";
		return selectSendInfoList(context, sql);
	}
	
	protected ArrayList<EntitySendBox> selectSendInfoList(Context context, String sql) throws SQLiteException {
		
		ArrayList<EntitySendBox> rowList = new ArrayList<EntitySendBox>();
		Cursor cursor = null;
		
		try {
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);

			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				EntitySendBox callInfo = null;
				
				while(!cursor.isAfterLast()) {
					callInfo = setSendInfo(cursor);
					rowList.add(callInfo);
					cursor.moveToNext();
				}

			} 
		} catch(Exception e) 	{
			throw new SQLiteException(e.getMessage());
		} finally {
			if(cursor != null) {
				cursor.deactivate();
				cursor.close();
				Helper.getInstance(context).close();
			}
		}

		return rowList;
	}
	
	private EntitySendBox setSendInfo(Cursor cursor) throws Exception {
		
		EntitySendBox entity = new EntitySendBox();
		Field fieldArray[] = entity.getClass().getDeclaredFields();
		
		for(int index = 0, maxIndex = fieldArray.length; index < maxIndex; index++) {
			int colIndex = 0;
			colIndex = cursor.getColumnIndex(fieldArray[index].getName());
			
			if(colIndex >= 0){
				if(fieldArray[index].getType() == int.class) {
					fieldArray[index].set(entity, cursor.getInt(colIndex));
				} else if(fieldArray[index].getType() == long.class) {
					fieldArray[index].setAccessible(true);
					fieldArray[index].set(entity, cursor.getLong(colIndex));
				} else if(fieldArray[index].getType() == String.class) {
					fieldArray[index].set(entity, cursor.getString(colIndex));
				}
			}
		}
		
		return entity;
	}
	
	protected int selectSendInfoCount(Context context, String sql) throws SQLiteException {
		
		Cursor cursor = null;
		
		try{
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);
			int count  = 0;
			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				count = cursor.getInt(cursor.getColumnIndex("RowCount"));
			}
			
			return count;
		} catch(Exception e) 	{
			throw new SQLiteException(e.getMessage());
		} finally {
			if(cursor != null) {
				cursor.deactivate();
				cursor.close();
				Helper.getInstance(context).close();
			}
		}
	}
}
