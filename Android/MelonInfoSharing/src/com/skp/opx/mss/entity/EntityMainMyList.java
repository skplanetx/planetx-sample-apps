package com.skp.opx.mss.entity;

import android.util.Log;

/**
 * @설명 : Main page Entity - 내 플레이리스트
 * @클래스명 : EntityMainMyList
 * 
 */
public class EntityMainMyList  {

	public String 	songName;		//Myplaylist 곡명
	public String 	artistName;		//Myplaylist 아티스트 이름
	public String   uriPath;        //저장 위치
	public String   mimeType;       //mimeType
	
	public EntityMainMyList() {}
	
	public EntityMainMyList(String songName, String artistName) {
		this.songName=songName;
		this.artistName=artistName;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		//확장자 없애기
		if(songName.contains(".mp3")){
			String[] song;
			song=songName.split(".mp3");
			this.songName=song[0].toString();
		}
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	
}
