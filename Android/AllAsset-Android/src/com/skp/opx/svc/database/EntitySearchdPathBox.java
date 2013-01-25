package com.skp.opx.svc.database;

/**
 * @설명 : 목적지 정보 Entity
 * @클래스명 : EntitySearchdPathBox
 * 
 */
public class EntitySearchdPathBox {

	private long 	ID; //디비 이름과 동일 해야함
	
	public String   mDestination;   //도착지
	public String   mDestinatioLon; //도착지 좌표
	public String   mDestinationLat;

	public EntitySearchdPathBox() {}

	public long getID() {
		
		return ID;
	}
}
