package com.tcloud.openapi.data.extract;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import android.util.Log;

import com.tcloud.openapi.data.MetaDatas;


public class XmlExtractor {
	public static final String TAG = XmlExtractor.class.getSimpleName();
	public static int depth = 0;
	
	public static Map<String,String> parseToken(InputStream is) throws JDOMException, IOException {
		final SAXBuilder builder = new SAXBuilder();
		final Document doc = builder.build(is);
		Element root = doc.getRootElement();

		@SuppressWarnings("unchecked")
		List<Element> list = root.getChildren();

		Map<String, String> map = new HashMap<String, String>();
		for(Element tag : list) {
			Log.d(TAG, "t - " + tag.getName() + ":" + tag.getValue());
			map.put(tag.getName(), tag.getValue());
		}

		return map;
	}
	
//	public static Map<String, ?> parse(InputStream is) throws JDOMException, IOException {
//		final SAXBuilder builder = new SAXBuilder();
//		final Document doc = builder.build(is);
//		Element root = doc.getRootElement();
//		Map<String, ?> map = doParse(root);
//		((Map<String, String>)map).put(MetaDatas.TITLE, root.getName());
//		return map;
//	}
	
	public static Map<String, ?> parse(String xml) throws UnsupportedEncodingException, JDOMException, IOException {
		final SAXBuilder builder = new SAXBuilder();
		final Document doc = builder.build(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		Element root = doc.getRootElement();
		Map<String, ?> map = doParse(root);
		((Map<String, String>)map).put(MetaDatas.TITLE, root.getName());
		return map;
	}
	
	public static Map<String, ?> doParse(Element e) throws JDOMException, IOException {
		depth++;
		Map<String, Object> map = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		List<Element> list = e.getChildren();
		for(Element tag : list) {
			String key = getValidKey(map, tag.getName());
			if(tag.getChildren().size() == 0) {
				map.put(tag.getName(), tag.getValue());
			} else {
				map.put(key, doParse(tag));
			}
		}
		depth--;
		return map;
	}
	
//	public static Map<String, ?> parseGroup(String xml) throws JDOMException, IOException {
//		final SAXBuilder builder = new SAXBuilder();
//		final Document doc = builder.build(new ByteArrayInputStream(xml.getBytes("UTF-8")));
//		Element root = doc.getRootElement();
//		Log.d(TAG, "parseGroup :" + root.getName());
//		Map<String, ?> map = doParseGroup(root);
//		((Map<String, String>)map).put(GroupData.TITLE, root.getName());
//		return map;
//
//	}
//	
//	public static Map<String, ?> doParseGroup(Element e) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Element> list = e.getChildren();
//		for(Element tag : list) {
//			String key = getValidKey(map, tag.getName());
//			if(tag.getChildren().size() == 0) {
//				map.put(tag.getName(), tag.getValue());
//			} else {
//				map.put(key, doParseGroup(tag));
//			}
//		}
//		return map;
//	}
	
	public static String getValidKey(Map<String, ?> map, String key) {
		int count = 0;
		String validKey = key;
		while(map.containsKey(validKey)) {
			validKey = key + count;
			count++;
		}
		return validKey;
	}
	
}
