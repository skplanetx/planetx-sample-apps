package com.skp.opx.svc.utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @설명 : URL 이미지 다운로더
 * @클래스명 : ImageDownloader 
 *
 */
public class ImageDownloader{
	public static final int IMGAE_CACHE_LIMIT_SIZE = 50;
	public static HashMap<String, Bitmap> mImageCache = new HashMap<String, Bitmap>();
	
	/** 
	 *  이미지 다운로드 Task 시작 Method
	 * */
	public static void download(String url, ImageView imageView){
		
		if(url == null) {
			return;
		}
		
		Bitmap cachedImage = mImageCache.get(url);
		if(cachedImage != null){
			imageView.setImageBitmap(cachedImage);
		}else if(cancelPotentialDownload(url, imageView)){
			if(mImageCache.size() > IMGAE_CACHE_LIMIT_SIZE){
				mImageCache.clear();
			}
			
			ImageDownloaderTask task = new ImageDownloaderTask(url, imageView);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
			imageView.setImageDrawable(downloadedDrawable);
			task.execute(url);
		}
	}

	/** 
	 *  이미지 다운로드 취소 Method
	 * */
	private static boolean cancelPotentialDownload(String url, ImageView imageView){
		ImageDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if(bitmapDownloaderTask != null){
			String bitmapUrl = bitmapDownloaderTask.url;
			if((bitmapUrl == null) || (!bitmapUrl.equals(url))){
				bitmapDownloaderTask.cancel(true);
			}else{
				return false;
			}
		}
		return true;
	}

	/** 
	 *  다운로드 Task 반환 Method
	 * */
	private static ImageDownloaderTask getBitmapDownloaderTask(ImageView imageView){
		if(imageView != null){
			Drawable drawable = imageView.getDrawable();
			if(drawable instanceof DownloadedDrawable){
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	static class DownloadedDrawable extends ColorDrawable{
		private final WeakReference<ImageDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(ImageDownloaderTask bitmapDownloaderTask){
			super(Color.TRANSPARENT);
			bitmapDownloaderTaskReference = new WeakReference<ImageDownloaderTask>(bitmapDownloaderTask);
		}

		public ImageDownloaderTask getBitmapDownloaderTask()	{
			return bitmapDownloaderTaskReference.get();
		}
	}
}
