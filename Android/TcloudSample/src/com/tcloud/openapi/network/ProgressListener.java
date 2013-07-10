package com.tcloud.openapi.network;

public interface ProgressListener {
	public void transferred(long num, long length);
}
