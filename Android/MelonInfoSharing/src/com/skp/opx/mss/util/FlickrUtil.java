package com.skp.opx.mss.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skp.opx.mss.ui.R;

public class FlickrUtil {

	private static HttpClient 		mClient;                                      
	private static HttpGet 			mGetMethod;
	public static ImageLoader 		imageLoader = ImageLoader.getInstance();
	private static List<String> title 				= new ArrayList<String>();          	// 리스트를 만들기 위한 
	public static List<String> thumbnailUrl 		= new ArrayList<String>(); 		// 썸네일 URL 들을 순서대로 저장하기 위한 객체
	public static List<String> videoUrl 			= new ArrayList<String>();			// 재생을 위한 동영상 URL 을 순서대로 저장
	public static DisplayImageOptions 	options;

	public static void imageCacheIn (Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPoolSize(3)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.memoryCacheSize(1500000) // 1.5 Mb
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.enableLogging() // Not necessary in common
		.build();
		// 이미지 로더 초기화
		ImageLoader.getInstance().init(config);	
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub_image)
		.showImageForEmptyUri(R.drawable.image_for_empty_url)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
	}

	public static void getYoutubeData(String keyword) {          
		String encodeKeyword = "";		

		try {
			//위의 keyword 를 유튜브로 보내기 위해 utf-8 로 인코딩
			encodeKeyword = URLEncoder.encode(keyword, "UTF-8");      
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} 

		String uri = "http://gdata.youtube.com/feeds/api/videos?vq=" + encodeKeyword + "&max-results=48";  
		mClient = new DefaultHttpClient();          
		mGetMethod = new HttpGet();  
		HttpResponse resp = null;

		try {       
			// 유튜브에 검색을 요청하기 위한 URI 를 준비하고
			mGetMethod.setURI(new URI(uri));              
		} catch (URISyntaxException e) {  
			e.printStackTrace(); 
		}  

		try {              
			 // 유튜브로 검색을 요청(request) 한다.
			resp = mClient.execute(mGetMethod);      
		} catch (ClientProtocolException e) {               
			e.printStackTrace();           
		} catch (IOException e) {              
			e.printStackTrace();          
		}                      

		if (resp.getStatusLine().getStatusCode() == 200) {    
			try {   
				//Response Stream
				InputStream is = resp.getEntity().getContent();  
				try {
					title.clear();
					thumbnailUrl.clear();
					videoUrl.clear();

					//Stream Parsing...
					URL url = new URL(uri);
					is = url.openConnection().getInputStream();

					String nameSpace = null; 
					String parserName = null;                       //  Youtube XML 문서에서 각 부분들의 이름
					String value;                                        //  Youtube XML 문서에서 각 부분들의 값
					Integer depth = 0;       
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(new InputStreamReader(is)); 
					// boolean bThumbnail = false;
					String szTempTitle = null;
					String szOrigTitle = null;

					for (int e = parser.getEventType(); e != parser.END_DOCUMENT; e = parser.next()) {           
						depth = parser.getDepth();                      // xml 문서에서 뎁스를 가져옵니다.
						switch(e) {               
						case XmlPullParser.START_TAG:           	   // START_TAG 부분일 경우       
							parserName = parser.getName();  

							if (depth == 4) {
								// 동영상 1개당 3-4개의 thumbnail. 최초 1장의 thumbnail만 저장
								if (parserName.equals("thumbnail")) {
									//최초의 thumbnail 저장. 동영상의 타이틀이 바뀌지 않으면 즉 같은 동영상이라고 간주.
									if(szTempTitle != szOrigTitle) {
										//이 안에 들어왔다면 최초의 썸네일이란 뜻이므로 이 두 변수를 같게합니다.    
										szTempTitle = szOrigTitle;                 
										// 썸네일 주소를 받아서  
										String urlth = parser.getAttributeValue(nameSpace, "url");
										//썸네일 List 에 저장합니다.
										thumbnailUrl.add(urlth);                        
									}        
								} else if (parserName.equals("player")) {                       
									String urlp = parser.getAttributeValue(nameSpace, "url");   // 동영상주소
									videoUrl.add(urlp);
								}                     
							}              
							break;              

						case XmlPullParser.TEXT: // XML 에서 동영상 정보 가 아닌 텍스트 부분들
							value = null;        
							value = parser.getText();  
							if (depth == 3) {                   
								if (parserName.equals("published")) { 
									int a = 0; 
								} else if (parserName.equals("updated")) {
									int a = 0; 
								}
								
								if (parserName.equals("title")) {
									szOrigTitle = value;
									title.add(value);
								}                 
							}              
							break;         

						case XmlPullParser.END_TAG:
							parserName = parser.getName();      
							if (parserName.equals("entry")) {                    
								//retEntries.add(entry);   
							}
							break; 
						}
					}  

				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}     
				is.close();

			} catch (IllegalStateException e) {                   
				e.printStackTrace();               
			} catch (IOException e) {             
				e.printStackTrace();               
			}     
		}  
	}
}
