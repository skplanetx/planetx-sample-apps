package com.tcloud.openapi.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import android.util.Log;

import com.tcloud.openapi.network.StringUtil;

public class Util {
	private static final String TAG = "OpenApi:Util";
	
	public static void printRequest(String url, String payload) {
		Log.d(TAG, "----------------------uri--------------------");
		if(url != null && url.length() > 0) {
			Log.d(TAG, url);
		}
		Log.d(TAG, "---------------------------------------------");
		Log.d(TAG, "---------------------payload-----------------");
		if(payload != null && payload.length() > 0) {
			Log.d(TAG, payload);
		}
		Log.d(TAG, "---------------------------------------------");
	}

	public static void printRequest(HttpRequest request) {
		if(request == null) {
			Log.d(TAG, "request is null");
			return;
		}
		
		Header[] headers = request.getAllHeaders();
		Log.d(TAG, "---------------------- header -------------------------");
		for(int i = 0; i < headers.length; i++) {
			Header h = headers[i];
			Log.d(TAG, "Header - " + h.getName() + ":" + h.getValue());
		}	
		Log.d(TAG, "---------------------- entity -------------------------");

		if (request instanceof HttpPost) {
			final HttpPost methodForLog = (HttpPost) request;
			if (methodForLog.getEntity() instanceof UrlEncodedFormEntity) {
				try {
					final InputStream inputStream = methodForLog.getEntity().getContent();
					Log.i(TAG, StringUtil.inputStreamToString(inputStream));
					
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(request instanceof HttpGet) {
			final HttpGet methodForLog = (HttpGet) request;
			Log.i(TAG, "Method : " + methodForLog.getMethod());
			Log.i(TAG, methodForLog.getURI().toString());
		}
		
		if(request instanceof HttpDelete) {
			final HttpDelete methodForLog = (HttpDelete) request;
			Log.i(TAG, "Method : " + methodForLog.getMethod());
			Log.i(TAG, methodForLog.getURI().toString());
		}
		
		if(request instanceof HttpPut) {
			final HttpPut methodForLog = (HttpPut) request;
			Log.i(TAG, "Method : " + methodForLog.getMethod());
			Log.i(TAG, methodForLog.getURI().toString());
		}
		Log.d(TAG, "-------------------------------------------------------");
		
	}	
	
//	public static void printRequest(HttpRequest request) {
//		final Header[] headers = request.getAllHeaders();
//		Log.d(TAG, "------------------ request header  ------------------------");
//		for(Header header : headers) {
//			Log.d(TAG, header.getName() + " : " + header.getValue());
//		}
//		Log.d(TAG, "-----------------------------------------------------------");
//
//		Log.d(TAG, "------------------ request entity  ------------------------");
//		if(request instanceof HttpEntityEnclosingRequest) {
//			final HttpEntityEnclosingRequest requestLog = (HttpEntityEnclosingRequest)request;
//			try {
//				BufferedReader in = new BufferedReader(
//						new InputStreamReader(requestLog.getEntity().getContent()));
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//					
//		}
//		Log.d(TAG, "-----------------------------------------------------------");
//
//		
//		
//	}
	
//	public static void printResponse(HttpResponse response) {
//		if(response == null) {
//			Log.d(TAG, "response is null");
//			return;
//		}
//		
//		try {
//			final Header[] headers = response.getAllHeaders();
//			Log.d(TAG, "---------------------- header -------------------------");
//			
//			for(final Header header : headers) {
//				Log.d(TAG, header.getName() + " : " + header.getValue());
//			}
//			Log.d(TAG, "---------------------------------------------------------");
//
//			Log.d(TAG, "---------------------- entity -------------------------");
//
//			BufferedReader in = new BufferedReader(
//					new InputStreamReader(response.getEntity().getContent()));
//			StringBuffer sb = new StringBuffer("");
//			String line = null;
//			String lineSep = System.getProperty("line.separator");
//			while((line = in.readLine()) != null) {
//				sb.append(line);
//				sb.append(lineSep);
//			}
//			in.close();
//			Log.d(TAG, sb.toString());
//			Log.d(TAG, "---------------------------------------------------------");
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void printResponse(final HttpResponse response, boolean printEntity) {
		if(response == null) {
			Log.d(TAG, "response is null");
		}
		
		Log.d(TAG, "---------------------------------------------------------");
		Log.d(TAG, response.getStatusLine().toString());
		
		Log.d(TAG, "---------------------- header -------------------------");
		
		final Header[] headers = response.getAllHeaders();
		for (final Header header : headers) {
			Log.d(TAG, header.getName() + " : " + header.getValue());
		}

		if(printEntity){
			Log.d(TAG, "---------------------- entity -------------------------");
	
			if(response.getEntity() == null){
				Log.d(TAG, "entity is null.....");
			} else{
				InputStream inputStream = null;
				try {
					inputStream = response.getEntity().getContent();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String content = StringUtil.inputStreamToString(inputStream);
				Log.d(TAG, content);
			}
			Log.d(TAG, "---------------------------------------------------------");
		}
	}

	public static void printResponse(HttpResponse response) {
		if(response == null) {
			Log.d(TAG, "response is null");
			return;
		}
		
		Log.d(TAG, "---------------------------------------------------------");
		Log.d(TAG, response.getStatusLine().toString());
		
		Log.d(TAG, "---------------------- header -------------------------");
		
		final Header[] headers = response.getAllHeaders();
		for (final Header header : headers) {
			Log.d(TAG, header.getName() + " : " + header.getValue());
		}

		Log.d(TAG, "---------------------------------------------------------");
	}
	
	public static void printEntity(String entity) {
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new ByteArrayInputStream(entity.getBytes("UTF-8"))), 8192);
			StringBuffer sb = new StringBuffer("");
			String line = null;
			String lineSep = System.getProperty("line.separator");
			while((line = in.readLine()) != null) {
				sb.append(line);
				sb.append(lineSep);
			}
			in.close();
			Log.d(TAG, sb.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getHeaderValue(HttpResponse response, String name) {
		final Header[] headers = response.getAllHeaders();
		for (final Header header : headers) {
			Log.d(TAG, header.getName() + " : " + header.getValue());
			if(name.equalsIgnoreCase(header.getName())) {
				return header.getValue();
			}
		}
		return "";
	}

}
