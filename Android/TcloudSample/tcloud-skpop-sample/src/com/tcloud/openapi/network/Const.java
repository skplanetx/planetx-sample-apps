package com.tcloud.openapi.network;

public class Const {
	public static final String HOST_NAME = "apis.skplanetx.com";
	public static final String SERVER_URL = "https://"+HOST_NAME+"/tcloud";
	public static final String API_VERSION = "1";

	public static String getVersion() {
		return API_VERSION;
	}
}
