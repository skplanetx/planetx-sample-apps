package com.tcloud.openapi.data;

public class MovieData extends MetaData {
	public static final String MOVIES = "movies";
	public static final String MOVIE_ID = "movieId";
	public static final String DURATION = "duration";
	
	public String movieId;
	public String duration;
	
	@Override
	public void set(String key, String value) {
		super.set(key, value);
		if(key.equals(MOVIE_ID))	movieId = value;
		if(key.equals(DURATION))	duration = value;
	}
}
