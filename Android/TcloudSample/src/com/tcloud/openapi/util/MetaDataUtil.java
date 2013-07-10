package com.tcloud.openapi.util;

import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.tcloud.openapi.data.DocumentData;
import com.tcloud.openapi.data.ErrorData;
import com.tcloud.openapi.data.ImageData;
import com.tcloud.openapi.data.MetaDatas;
import com.tcloud.openapi.data.MovieData;
import com.tcloud.openapi.data.MusicData;

public class MetaDataUtil {
	
	public static final String TAG = MetaDataUtil.class.getSimpleName();
	
//	public static MetaDatas<ImageData> getPhotoData(Map<String, ?> map) {
//		String title = (String)map.get(MetaDatas.TITLE);
//		MetaDatas<ImageData> metaDatas = new MetaDatas<ImageData>();
//		if(title != null && title.equals("error")) {
//			handleError(map);
//			return metaDatas;
//		}
//		
//		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
//		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
//		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
//		
//		Map<String, Object> imagesMap = (Map<String, Object>)map.get(ImageData.IMAGES);
//		for(String imageKey : imagesMap.keySet()) {
//			Map<String, String> imageMap = (Map<String, String>)imagesMap.get(imageKey);
//			ImageData data = new ImageData();
//			for(String infoKey : imageMap.keySet()) {
//				Log.d(TAG, "data set : " + infoKey + ":" + imageMap.get(infoKey));
//				data.set(infoKey, imageMap.get(infoKey));
//			}
//			metaDatas.add(data);
//		}
//		return metaDatas;
//	}
	
	public static MetaDatas<ImageData> getPhotoDatas(Context context, Map<String, ?> map) {
		String title = (String)map.get(MetaDatas.TITLE);
		MetaDatas<ImageData> metaDatas = new MetaDatas<ImageData>();
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return metaDatas;
		}
		
		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
		
		Map<String, Object> imagesMap = (Map<String, Object>)map.get(ImageData.IMAGES);
		for(String imageKey : imagesMap.keySet()) {
			Map<String, String> imageMap = (Map<String, String>)imagesMap.get(imageKey);
			ImageData data = new ImageData();
			for(String infoKey : imageMap.keySet()) {
				Log.d(TAG, "data set : " + infoKey + ":" + imageMap.get(infoKey));
				data.set(infoKey, imageMap.get(infoKey));
			}
			metaDatas.add(data);
		}
		return metaDatas;
	}

	
	public static ImageData getPhotoData(Context context, Map<String, String>map){
		
		ImageData imageData = new  ImageData();
		
		
		for(String infoKey : map.keySet()){
			Log.d(TAG, "key = " + infoKey + ", " + map.get(infoKey));
			imageData.set(infoKey, map.get(infoKey));
		}
		
		return imageData;
	}
	
//	public static MetaDatas<MusicData> getMusicData(Map<String, ?> map) {
//		String title = (String)map.get(MetaDatas.TITLE);
//		MetaDatas<MusicData> metaDatas = new MetaDatas<MusicData>();
//		if(title != null && title.equals("error")) {
//			handleError(map);
//			return metaDatas;
//		}
//
//		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
//		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
//		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
//		
//		Map<String, Object> musicListMap = (Map<String, Object>)map.get(MusicData.MUSIC_LIST);
//		for(String musicKey : musicListMap.keySet()) {
//			Map<String, String> musicMap = (Map<String, String>)musicListMap.get(musicKey);
//			MusicData data = new MusicData();
//			for(String infoKey : musicMap.keySet()) {
//				data.set(infoKey, musicMap.get(infoKey));
//			}
//			metaDatas.add(data);
//		}
//		return metaDatas;
//	}

	public static MetaDatas<MusicData> getMusicDatas(Context context, Map<String, ?> map) {
		String title = (String)map.get(MetaDatas.TITLE);
		MetaDatas<MusicData> metaDatas = new MetaDatas<MusicData>();
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return metaDatas;
		}

		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
		
		Map<String, Object> musicListMap = (Map<String, Object>)map.get(MusicData.MUSIC_LIST);
		for(String musicKey : musicListMap.keySet()) {
			Map<String, String> musicMap = (Map<String, String>)musicListMap.get(musicKey);
			MusicData data = new MusicData();
			for(String infoKey : musicMap.keySet()) {
				data.set(infoKey, musicMap.get(infoKey));
			}
			metaDatas.add(data);
		}
		return metaDatas;
	}
	
	public static MusicData getMusicData(Context context,Map<String, String> map){
		
		MusicData musicData = new  MusicData();
		for(String infoKey : map.keySet()){
			Log.d(TAG, "key = " + infoKey + ", " + map.get(infoKey));
			musicData.set(infoKey, map.get(infoKey));
		}
		return musicData;

	}
	
//	public static MetaDatas<MovieData> getMovieData(Map<String, ?> map) {
//		String title = (String)map.get(MetaDatas.TITLE);
//		MetaDatas<MovieData> metaDatas = new MetaDatas<MovieData>();
//		if(title != null && title.equals("error")) {
//			handleError(map);
//			return metaDatas;
//		}
//
//		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
//		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
//		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
//		
//		Map<String, Object> moviesMap = (Map<String, Object>)map.get(MovieData.MOVIES);
//		for(String movieKey : moviesMap.keySet()) {
//			Map<String, String> movieMap = (Map<String, String>)moviesMap.get(movieKey);
//			MovieData data = new MovieData();
//			for(String infoKey : movieMap.keySet()) {
//				data.set(infoKey, movieMap.get(infoKey));
//			}
//			metaDatas.add(data);
//		}
//		return metaDatas;
//	}
	
	public static MetaDatas<MovieData> getMovieDatas(Context context, Map<String, ?> map) {
		String title = (String)map.get(MetaDatas.TITLE);
		MetaDatas<MovieData> metaDatas = new MetaDatas<MovieData>();
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return metaDatas;
		}

		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
		
		Map<String, Object> moviesMap = (Map<String, Object>)map.get(MovieData.MOVIES);
		for(String movieKey : moviesMap.keySet()) {
			Map<String, String> movieMap = (Map<String, String>)moviesMap.get(movieKey);
			MovieData data = new MovieData();
			for(String infoKey : movieMap.keySet()) {
				data.set(infoKey, movieMap.get(infoKey));
			}
			metaDatas.add(data);
		}
		return metaDatas;
	}
	
	public static MovieData getMovieData(Context context,Map<String, String> map){
		
		MovieData movieData = new  MovieData();
		for(String infoKey : map.keySet()){
			Log.d(TAG, "key = " + infoKey + ", " + map.get(infoKey));
			movieData.set(infoKey, map.get(infoKey));
		}
		return movieData;

	}

	
//	public static MetaDatas<DocumentData> getDocumentData(Map<String, ?> map) {
//		String title = (String)map.get(MetaDatas.TITLE);
//		MetaDatas<DocumentData> metaDatas = new MetaDatas<DocumentData>();
//		if(title != null && title.equals("error")) {
//			handleError(map);
//			return metaDatas;
//		}
//
//		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
//		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
//		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
//		
//		Map<String, Object> documentsMap = (Map<String, Object>)map.get(DocumentData.DOCUMENTS);
//		for(String documentKey : documentsMap.keySet()) {
//			Map<String, String> documentMap = (Map<String, String>)documentsMap.get(documentKey);
//			DocumentData data = new DocumentData();
//			for(String infoKey : documentMap.keySet()) {
//				data.set(infoKey, documentMap.get(infoKey));
//			}
//			metaDatas.add(data);
//		}
//		return metaDatas;
//	}
	
	public static MetaDatas<DocumentData> getDocumentDatas(Context context, Map<String, ?> map) {
		String title = (String)map.get(MetaDatas.TITLE);
		MetaDatas<DocumentData> metaDatas = new MetaDatas<DocumentData>();
		if(title != null && title.equals("error")) {
			handleError(context, map);
			return metaDatas;
		}

		metaDatas.count = Integer.valueOf((String)map.get(MetaDatas.COUNT));
		metaDatas.page = Integer.valueOf((String)map.get(MetaDatas.PAGE));
		metaDatas.total = Integer.valueOf((String)map.get(MetaDatas.TOTAL));
		
		Map<String, Object> documentsMap = (Map<String, Object>)map.get(DocumentData.DOCUMENTS);
		for(String documentKey : documentsMap.keySet()) {
			Map<String, String> documentMap = (Map<String, String>)documentsMap.get(documentKey);
			DocumentData data = new DocumentData();
			for(String infoKey : documentMap.keySet()) {
				data.set(infoKey, documentMap.get(infoKey));
			}
			metaDatas.add(data);
		}
		return metaDatas;
	}
	
	public static DocumentData getDocumentData(Context context,Map<String, String> map){
		
		DocumentData documentData = new  DocumentData();
		for(String infoKey : map.keySet()){
			Log.d(TAG, "key = " + infoKey + ", " + map.get(infoKey));
			documentData.set(infoKey, map.get(infoKey));
		}
		return documentData;

	}

	
	public static ErrorData handleError(Map<String, ?> map) {
		Log.d(TAG, "MetaDataUtil handle Error");
		ErrorData errorData = new ErrorData();
		for(String key : map.keySet()) {
			Log.d(TAG, "handle error : " + key + ":" + map.keySet());
			errorData.set(key, (String)map.get(key));
		}
		return errorData;
	}
	
	public static void handleError(Context context, Map<String, ?> map) {
//		Toast.makeText(context, (String)map.get("message"), Toast.LENGTH_SHORT).show();
		for(String key: map.keySet()) {
			Object value = (Object) map.get(key);
			Log.d(TAG, "handleError - " + key + ":" + value);
		}		
//		Log.d(TAG, "MetaDataUtil handle Error");
//		ErrorData errorData = new ErrorData();
//		for(String key : map.keySet()) {
//			Log.d(TAG, "handle error : " + key + ":" + map.keySet());
//			errorData.set(key, (String)map.get(key));
//		}
	}

}
