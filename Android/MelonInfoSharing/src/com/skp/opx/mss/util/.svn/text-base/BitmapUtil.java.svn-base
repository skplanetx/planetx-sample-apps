package com.skp.opx.mss.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class BitmapUtil {
	
	public static  String requestImageFromURL(String imageUrl, String strDestSavePath){

		try {
			File file = new File(strDestSavePath);
			deleteFolder(file);

			URL url = new URL(imageUrl);
			String fileName = imageUrl.substring( imageUrl.lastIndexOf('/')+1, imageUrl.length() ); 
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			InputStream inputStream = downLoadFile(imageUrl);
			if(inputStream == null) {
				return null;	        	
			}

			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			Bitmap scailedBitmap;
			int width;
			int height;

			//FACEBOOK은 최대 해상도가 960px 이므로, 이미지가 클 경우, 960px로 고정시킨다. 
			if(bitmap.getWidth() > 960 || bitmap.getHeight() > 960) {
				if(bitmap.getWidth() > bitmap.getHeight()) {
					width = 960;
					height = (width * bitmap.getHeight()) / bitmap.getWidth(); 
				} else {
					height = 960;
					width = (height * bitmap.getWidth()) / bitmap.getHeight();
				}

				scailedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
				bitmap.recycle();
			} else {
				scailedBitmap = bitmap;
			}

			file.mkdirs();
			file = new File(strDestSavePath,fileName);
			FileOutputStream fileOutput = new FileOutputStream(file);
			scailedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutput);
			scailedBitmap.recycle();
			fileOutput.close();
			return file.getAbsolutePath();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean deleteFolder(File targetFolder) {

		File[] childFile = targetFolder.listFiles();

		if(childFile == null) {
			return true;
		}

		if (childFile.length > 0) {
			for (int i = 0; i < childFile.length; i++) {
				if (childFile[i].isFile()) {
					childFile[i].delete();
				} else {
					deleteFolder(childFile[i]);
				}
			}
		}

		targetFolder.delete();
		return (!targetFolder.exists());
	}
	
	public static InputStream downLoadFile(String fileURL) {
		
		String path = fileURL.substring(0, fileURL.lastIndexOf('/')+1);
        String fileName = fileURL.substring( fileURL.lastIndexOf('/')+1, fileURL.length() );
        String endodeFileName;
        
		try {
			endodeFileName = URLEncoder.encode(new String(fileName.getBytes("UTF-8")));
			fileURL = path+endodeFileName;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		InputStream inputStream = null;
		URL fileUri = null;
		
		try {
			fileUri = new URL(fileURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if(fileUri == null) {
			return null;
		}
		
		try {
			HttpURLConnection conn = (HttpURLConnection)fileUri.openConnection();
			conn.setDoInput(true);
			conn.connect();			
			inputStream = conn.getInputStream();	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return inputStream;
	}
}
