package com.skp.opx.rpn.database;

/**
 * @설명 : 발신함 Entity
 * @클래스명 : EntitySendBox 
 *
 */
public class EntitySendBox {

	private long 	ID; //디비 이름과 동일 해야함
	
	public String 	mMdn;
	public String   mReceiver; //받는 사람
	public String	mStartLocation;
	public String 	mDestnationLocation;
	public long 	mDeliveryTime;//전송시간
	public String 	mExpectionArrivedTime; //예상 시간
	public String 	mMessage;
	public int		mMessageType;

	public EntitySendBox() {}

	public long getID() {
		
		return ID;
	}
}
