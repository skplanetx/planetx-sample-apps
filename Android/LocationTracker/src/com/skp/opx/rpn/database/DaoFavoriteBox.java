package com.skp.opx.rpn.database;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
/**
 * @설명 : 즐겨찾기 보관함 DAO
 * @클래스명 : DaoFavoriteBox 
 *
 */
public class DaoFavoriteBox {

	private static DaoFavoriteBox sTheInstance = null;
	private DaoFavoriteBox() {}
	
	public static synchronized DaoFavoriteBox getInstance() {
		
		if(sTheInstance == null) {
			sTheInstance = new DaoFavoriteBox();
		}
		
		return sTheInstance;
	}
	
	/** 
	 *  즐겨 찾기 모든 데이터 삭제 Method
	 * */
	public void deleteAllData(Context context) throws SQLiteException {
		
		String sql = "delete from " + EntityFavoriteBox.class.getSimpleName();
		Helper.getInstance(context).getWritableDatabase().execSQL(sql, new String[0]);
		Helper.getInstance(context).close();
	}	
	
	/** 
	 *  즐겨 찾기 아이디로 데이터 삭제 Method
	 * */
	public void deleteData(Context context, long id) {

		String sql ="delete from " + EntityFavoriteBox.class.getSimpleName() + " where ID = " + id;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}		
	
	/** 
	 *  즐겨 찾기 데이터 Insert Method
	 * */
	public long insertFavoriteInfo(Context context, EntityFavoriteBox entity) throws Exception {
		
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
	 *  즐겨 찾기 Select Method
	 * */
	public ArrayList<EntityFavoriteBox> getFavoriteInfoList(Context context) throws SQLiteException {
		
		String sql = "select * from " + EntityFavoriteBox.class.getSimpleName() + " limit 50";
		return selectFavoriteInfoList(context, sql);
	}
	
	/** 
	 *  즐겨 찾기 이름 변경 Method
	 * */
	public long updateFavoriteName(Context context, long id, String name){
		
		ContentValues values = new ContentValues();
		values.put("mName", name);
		
		long extraID = Helper.getInstance(context).getWritableDatabase().update("EntityFavoriteBox", values, "ID = " + id , null);
		Helper.getInstance(context).close();
		return extraID;
	}
	protected ArrayList<EntityFavoriteBox> selectFavoriteInfoList(Context context, String sql) throws SQLiteException {
		
		ArrayList<EntityFavoriteBox> rowList = new ArrayList<EntityFavoriteBox>();
		Cursor cursor = null;
		
		try {
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);

			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				EntityFavoriteBox callInfo = null;
				
				while(!cursor.isAfterLast()) {
					callInfo = setFavoritInfo(cursor);
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
	
	private EntityFavoriteBox setFavoritInfo(Cursor cursor) throws Exception {
		
		EntityFavoriteBox entity = new EntityFavoriteBox();
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
	
	protected int selectFavoriteInfoCount(Context context, String sql) throws SQLiteException {
		
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
