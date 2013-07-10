package com.tcloud.openapi;


public interface AsyncFileDownloadListener {
	public void onComplete(boolean result);
	public void onError(Exception e);
	public void transferred(long num, long length);
}
