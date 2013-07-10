package com.tcloud.openapi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class XMLUtil {
	public static final String TAG = XMLUtil.class.getSimpleName();
	
	public static String createGroupPayLoad(String name) {
    	XmlSerializer serializer = Xml.newSerializer();
    	OutputStream os = new ByteArrayOutputStream();
    	
    	try {
			serializer.setOutput(os, "UTF-8");
			
			serializer.startDocument(null, Boolean.valueOf(true));
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			serializer.startTag(null, "group");
				serializer.startTag(null, "name");
				serializer.text(name.trim());
				serializer.endTag(null, "name");
			serializer.endTag(null, "group");
			serializer.flush();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Log.d(TAG, os.toString());
    	return os.toString();
	}
	
	public static String createIdPayLoad(String tag, String id) {
    	XmlSerializer serializer = Xml.newSerializer();
    	OutputStream os = new ByteArrayOutputStream();
    	
    	try {
			serializer.setOutput(os, "UTF-8");
			
			serializer.startDocument(null, Boolean.valueOf(true));
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			serializer.startTag(null, "group");
				serializer.startTag(null, tag);
				serializer.text(id.trim());
				serializer.endTag(null, tag);
			serializer.endTag(null, "group");
			serializer.flush();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Log.d(TAG, os.toString());
    	return os.toString();
	}
}
