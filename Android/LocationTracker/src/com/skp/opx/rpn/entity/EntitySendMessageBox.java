package com.skp.opx.rpn.entity;


/**
 * @설명 : 발신 메시지 함 Entity
 * @클래스명 : EntitySendMessageBox 
 *
 */
public class EntitySendMessageBox  {

	public long id;
	public String name;		  //이름	
	public String contentPreview; //내용 요약
	public String date; //날짜
	public String mdn;
	private boolean	mCheckedState;// 체크 박스 체크 상태
	
	public EntitySendMessageBox() {}
	
	public EntitySendMessageBox(String subTitle, String name, String contentPreview, String date, String mdn){
		this.name = name;
		this.contentPreview = contentPreview;
		this.date = date;
		this.mdn = mdn;
		
	}
	public String getMdn() {
		return mdn;
	}

	public void setMdn(String mdn) {
		this.mdn = mdn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getContentPreview() {
		return contentPreview;
	}

	public void setContentPreview(String contentPreview) {
		this.contentPreview = contentPreview;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean getChecked() {
		return mCheckedState;
	}
	
	public void setChecked(boolean checked){
		mCheckedState = checked;
	}
	
}
