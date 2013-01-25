package com.skp.opx.rpn.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : 주소록 Entity 
 * @클래스명 : EntityContact 
 *
 */
public class EntityContact extends EntityAbstract {

	public Long id;
	public int titleType;		 //타이틀 표시 1 = 내 연락처,2 = 네이트온
	public String name;		     //이름	
	public String contact;		 //연락처
	public int type;			 //주소록
	public boolean	checkedState;// 체크 박스 체크 상태
	
	public EntityContact() {}
	
	public EntityContact(int titleType, String name, String contact, int type){
		this.titleType = titleType;
		this.name = name;
		this.contact = contact;
	}
	public int getTitleType() {
		return titleType;
	}

	public void setTitleType(int titleType) {
		this.titleType = titleType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public boolean getChecked() {
		return checkedState;
	}
	
	public void setChecked(boolean checked){
		checkedState = checked;
	}
	
}
