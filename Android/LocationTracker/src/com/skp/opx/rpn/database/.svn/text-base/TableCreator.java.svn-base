package com.skp.opx.rpn.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @설명 : DB Table 생성 Class
 * @클래스명 : TableCreator 
 *
 */
public final class TableCreator {

	private static String createTableString(Class <?> classTemplate) {
		
		String strMakeQuery = "create table ";
		strMakeQuery += classTemplate.getSimpleName() + " (";
		strMakeQuery += "ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		
		Field fieldArray[] = classTemplate.getFields();
		
		for(int field = 0, maxfield = fieldArray.length; field < maxfield; field++) {
			
			strMakeQuery += fieldArray[field].getName() + " ";
			
			if(fieldArray[field].getType() == int.class ||fieldArray[field].getType() == long.class ) {
				strMakeQuery += "INTEGER";
			} else if(fieldArray[field].getType() == String.class) {
				strMakeQuery += "TEXT";
			}
			
			if(field == maxfield -1) {
				strMakeQuery += " ) ";
			} else {
				strMakeQuery += ", ";
			}
		}
		
		return strMakeQuery;
	}
	
	/** 
	 *  DB 테이블 생성 Method
	 * */
	public static List<String> getCreateTableDDL() {

		ArrayList<String> strDllArrayList = new ArrayList<String>();
		//발신함
		strDllArrayList.add(createTableString(EntitySendBox.class));
		//지정연락처 
		strDllArrayList.add(createTableString(EntityDesignatedContactBox.class));
		//즐겨찾기
		strDllArrayList.add(createTableString(EntityFavoriteBox.class));
		//경로 탐색함 
		strDllArrayList.add(createTableString(EntitySearchdPathBox.class));
		//실시간 경로 이동함
		strDllArrayList.add(createTableString(EntityRealTimePathBox.class));
		return strDllArrayList;		
	}
}