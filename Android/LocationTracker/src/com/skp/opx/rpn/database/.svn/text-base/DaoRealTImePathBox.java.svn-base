package com.skp.opx.rpn.database;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

/**
 * @설명 : 실시간 이동 경로함 DAO
 * @클래스명 : DaoRealTImePathBox 
 *
 */
public class DaoRealTImePathBox {

	private static DaoRealTImePathBox sTheInstance = null;
	private DaoRealTImePathBox() {}
	
	public static synchronized DaoRealTImePathBox getInstance() {
		
		if(sTheInstance == null) {
			sTheInstance = new DaoRealTImePathBox();
		}
		
		return sTheInstance;
	}
	
	/** 
	 *  실시간 경로 모두 삭제 Method
	 * */
	public void deleteAllData(Context context) throws SQLiteException {
		
		String sql = "delete from " + EntityRealTimePathBox.class.getSimpleName();
		Helper.getInstance(context).getWritableDatabase().execSQL(sql, new String[0]);
		Helper.getInstance(context).close();
	}	

	/** 
	 *  실시간 경로 아이디로 삭제 Method
	 * */
	public void deleteData(Context context, long id) {

		String sql ="delete from " + EntityRealTimePathBox.class.getSimpleName() + " where ID = " + id;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}		
	/**
	 * 알림 시작 시간으로 실시간 경로 삭제 Method
	 */
	public void deleteDataByStartTime(Context context, long startTime  ) {

		String sql ="delete from " + EntityRealTimePathBox.class.getSimpleName() + " where mAlarmStartTime = " + startTime;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}	
	/** 
	 *  실시간 경로 Insert Method 
	 * */
	public long insertRealTimePathInfo(Context context, EntityRealTimePathBox entity) throws Exception {
		
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
	 * 모든 실시간 경로 리턴 Method
	 */
	public ArrayList<EntityRealTimePathBox> getAllRealTimePathInfoList(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntityRealTimePathBox.class.getSimpleName() + " group by mAlarmStartTime order by mAlarmStartTime desc limit 50";
		return selectRealTimePathInfoList(context, sql);
	}
	/**
	 * 최신 경로 하나만 Select Method (메시지 전송시 사용)
	 */
	public ArrayList<EntityRealTimePathBox> getLatestRealTimePathInfoList(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntityRealTimePathBox.class.getSimpleName() + " order by mSearchedTime desc limit 1";
		return selectRealTimePathInfoList(context, sql);
	}
	
	/**
	 * 경로 설정시간으로 경로 Select Method (실제 경로에 대한 아이디)
	 */
	public ArrayList<EntityRealTimePathBox> getRealTimePathInfoListById(Context context, long searchedTime) throws SQLiteException {
		
		String sql = "select * from " + EntityRealTimePathBox.class.getSimpleName() + " where mAlarmStartTime = " + searchedTime + " limit 50";
		return selectRealTimePathInfoList(context, sql);
	}
	
	protected ArrayList<EntityRealTimePathBox> selectRealTimePathInfoList(Context context, String sql) throws SQLiteException {
		
		ArrayList<EntityRealTimePathBox> rowList = new ArrayList<EntityRealTimePathBox>();
		Cursor cursor = null;
		
		try {
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);

			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				EntityRealTimePathBox callInfo = null;
				
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
	
	private EntityRealTimePathBox setSendInfo(Cursor cursor) throws Exception {
		
		EntityRealTimePathBox entity = new EntityRealTimePathBox();
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
