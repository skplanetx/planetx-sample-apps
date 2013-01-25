package com.skp.opx.rpn.database;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * @설명 : DB Helper
 * @클래스명 : Helper 
 *
 */
public class Helper extends SQLiteOpenHelper {
	
	private static Helper sTheInstance = null;
	private SQLiteDatabase mSQLiteDatabase = null; 
	
	public static synchronized Helper getInstance(Context context) {
		
		if(sTheInstance == null) {
			sTheInstance = new Helper(context);
		}
		
		return sTheInstance;
	}

	private Helper(Context context) throws SQLiteException {
		super(context, "feelingk.db", null, 1);
		
		mSQLiteDatabase = super.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		List<String> tableList = TableCreator.getCreateTableDDL();
		Iterator<String> iter = tableList.iterator();
		while(iter.hasNext()) {
			db.execSQL(iter.next());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

	public synchronized SQLiteDatabase getWritableDatabase() {
		
		return mSQLiteDatabase;
	}

	public void beginTransaction() throws SQLiteException {

		try {
			if(!mSQLiteDatabase.inTransaction() ){
				mSQLiteDatabase.beginTransaction();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLiteException("Exception in beginTransaction");
		}
	}

	public void setTransactionSuccessful() throws SQLiteException {

		try {
			if(mSQLiteDatabase.inTransaction() ) {
				mSQLiteDatabase.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLiteException("Exception in setTransactionSuccessful");
		}
	}
	
	//지우면 안됨... close 안됨
	@Override
	public  void close() {
//		if(mSQLiteDatabase != null) {
//			mSQLiteDatabase.close();
//		}
	}
	
	public void endTransaction() throws SQLiteException {

		try {
			if(mSQLiteDatabase.inTransaction() ) {
				mSQLiteDatabase.endTransaction();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLiteException("Exception in endTransaction");
		}
	}
}
