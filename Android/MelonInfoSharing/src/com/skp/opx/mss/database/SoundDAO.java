package com.skp.opx.mss.database;

import java.util.ArrayList;

import com.skp.opx.mss.entity.EntityMainMyList;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @설명 : 미디어 스토어에서 음악 관련 정보 쿼리하기
 * @클래스명 : SoundDAO
 * 
 */
public class SoundDAO {

	public static ArrayList<EntityMainMyList> getSoundInfo(Context context){
		
		
		String[] proj = { 
				MediaStore.Audio.Media.DISPLAY_NAME, 
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.MIME_TYPE};

		String selection = null;
		EntityMainMyList soundInfo;
		Cursor cursor = null;
		ArrayList<EntityMainMyList> soundList = new  ArrayList<EntityMainMyList>();
		
		try {
			cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, selection, null, "date_added DESC");
			
			
			if ( null != cursor && cursor.moveToFirst()) {
				int name = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
				int path = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
				int artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
				int mimeType = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE);
				
				do {
					soundInfo = new EntityMainMyList();
					soundInfo.setSongName(cursor.getString(name));
					soundInfo.setArtistName(cursor.getString(artist));
					soundInfo.setUriPath(cursor.getString(path));
					soundInfo.setMimeType(cursor.getString(mimeType));
					if(cursor.getString(name)!=null)
						soundList.add(soundInfo);
					
				} while (cursor.moveToNext());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return soundList;
	}
}
