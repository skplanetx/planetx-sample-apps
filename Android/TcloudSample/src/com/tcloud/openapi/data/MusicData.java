package com.tcloud.openapi.data;

public class MusicData extends MetaData {
	public static final String MUSIC_LIST = "music";
	public static final String MUSIC_ID = "musicId";
	public static final String ALBUM = "album";
	public static final String DURATION = "duration";
	public static final String GENRE = "genre";
	public static final String SINGER = "singer";
	public static final String TITLE = "title";
	public static final String TRACK = "track";
	
	public String musicId;
	public String album;
	public String duration;
	public String genre;
	public String singer;
	public String title;
	public String track;
	
	@Override
	public void set(String key, String value) {
		super.set(key, value);
		if(key.equals(MUSIC_ID))	musicId = value;
		if(key.equals(ALBUM))		album = value;
		if(key.equals(DURATION))	duration = value;
		if(key.equals(GENRE))		genre = value;
		if(key.equals(SINGER))		singer = value;
		if(key.equals(TITLE))		title = value;
		if(key.equals(TRACK))		track = value;
	}
}
