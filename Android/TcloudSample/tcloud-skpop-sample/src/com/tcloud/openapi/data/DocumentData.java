package com.tcloud.openapi.data;


public class DocumentData extends MetaData {
	public static final String DOCUMENTS = "documents";
	public static final String DOCUMENT_ID = "documentId";
	public static final String DOC_TYPE = "docType";
	
	public String documentId;
	public String docType;
	
	@Override
	public void set(String key, String value) {
		super.set(key, value);
		if(key.equals(DOCUMENT_ID))		documentId = value;
		if(key.equals(DOC_TYPE))		docType = value;
	}
}
