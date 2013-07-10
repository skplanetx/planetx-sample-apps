package com.tcloud.openapi.network;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.net.ParseException;
import android.util.Log;

import com.skp.openplatform.android.sdk.oauth.OAuthInfoManager;
import com.tcloud.openapi.util.Util;

public class HttpConnector {
	public static final String TAG = HttpConnector.class.getSimpleName();
	private HttpClient httpClient;
	private HttpResponse httpResponse;

	public HttpConnector() {
		final SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		 schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
//				443));
		final HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean());
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf8");
		final ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		httpClient = new DefaultHttpClient(connectionManager, params);
	}

	public void request(HttpMethod method, String uri)
			throws ClientProtocolException, IOException, MalformedURLException {
		HttpRequest request = createRequest(method, uri);
		putNorthBoundHeader(request);
		Util.printRequest(request);
		httpResponse = httpClient.execute((HttpUriRequest) request);
	}

	public void request(HttpMethod method, String uri, byte[] entity)
			throws ClientProtocolException, IOException, MalformedURLException {
		HttpRequest request = createRequest(method, uri, entity);
		putNorthBoundHeader(request);
		Util.printRequest(request);
		httpResponse = httpClient.execute((HttpUriRequest) request);
	}

	public HttpResponse multipartRequest(String uri, String filePath,
			ProgressListener listener) throws ClientProtocolException,
			IOException {
		HttpEntityEnclosingRequest request = new HttpPost(uri);
		FileBody bin = new FileBody(new File(filePath));
		// putSouthBoundHeader(request, "multipart/form-data");
		putNorthBoundHeader(request);
		Util.printRequest(request);

		// Header[] headers = request.getAllHeaders();
		// Log.d(TAG, "request call");
		// for(int i = 0; i < headers.length; i++) {
		// Header h = headers[i];
		// Log.d(TAG, "Header - " + h.getName() + ":" + h.getValue());
		// }

		CountingMultipartEntity reqEntity = new CountingMultipartEntity(
				listener);
		reqEntity.addPart("bin", bin);
		request.setEntity(reqEntity);
		HttpResponse response = httpClient.execute((HttpUriRequest) request);

		return response;
	}

	public HttpResponse getResponse() {
		return httpResponse;
	}

	public String getEntity() throws ParseException, IOException {
		if (httpResponse.getEntity() == null)
			return null;

		InputStream inputStream = httpResponse.getEntity().getContent();
		String content = StringUtil.inputStreamToString(inputStream);

		// String content = Util.printResponse(httpResponse, true);
		return content;
	}

	public Header[] getHeaders(String name) {
		return httpResponse.getHeaders(name);
	}

	public InputStream getContent() throws IllegalStateException, IOException {
		HttpEntity entity = httpResponse.getEntity();
		Util.printResponse(httpResponse);
		return entity.getContent();
	}

	public boolean download(String uri, File file, ProgressListener listener)
			throws ClientProtocolException, IOException {
		URL url = new URL(uri);
		HttpGet httpGet = new HttpGet(url.toString());
		HttpResponse response = httpClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() != 200) {
			Log.d(TAG, "donwload : " + response.getStatusLine().getStatusCode());
			return false;
		}
		HttpEntity entity = response.getEntity();
		Header[] headers = response.getHeaders("Content-Length");
		Header header = headers[0];
		int totalSize = Integer.parseInt(header.getValue());
		int downloadedSize = 0;
		if (entity != null) {
			InputStream stream = entity.getContent();
			byte buf[] = new byte[1024 * 1024];
			int numBytesRead;

			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(file));
			do {
				numBytesRead = stream.read(buf);
				if (numBytesRead > 0) {
					fos.write(buf, 0, numBytesRead);
					downloadedSize += numBytesRead;
					if (listener != null) {
						listener.transferred(downloadedSize, totalSize);
					}
				}
			} while (numBytesRead > 0);

			fos.flush();
			fos.close();
			stream.close();
			httpClient.getConnectionManager().shutdown();
			return true;
		}
		return false;
	}

	private HttpRequest createRequest(HttpMethod method, String uri)
			throws MalformedURLException {
		URL url = new URL(uri);
		if (method == HttpMethod.GET) {
			return new HttpGet(url.toString());
		} else if (method == HttpMethod.DELETE) {
			return new HttpDelete(url.toString());
		} else {
			return null;
		}
	}

	private HttpRequest createRequest(HttpMethod method, String uri,
			byte[] entity) throws MalformedURLException {
		HttpEntityEnclosingRequest httpRequest = null;
		URL url = new URL(uri);
		if (method == HttpMethod.POST) {
			httpRequest = new HttpPost(url.toString());
		} else if (method == HttpMethod.PUT) {
			httpRequest = new HttpPut(url.toString());
		} else {
			return httpRequest;
		}

		httpRequest.setEntity(new ByteArrayEntity(entity));
		return httpRequest;
	}

	private void putNorthBoundHeader(HttpRequest request) {

		Log.d(TAG, "call putNorthBoundHeader");
		request.setHeader("Content-Type", "application/xml;charset=UTF-8");
		request.setHeader("Accept", "text/xml");
		request.setHeader("Accept-Language", "ko");
		request.setHeader("Host", Const.HOST_NAME);

		request.setHeader("appkey", OAuthInfoManager.authorInfo.getAppKey());
		request.setHeader("access_token", OAuthInfoManager.authorInfo.getAccessToken());

	}

}
