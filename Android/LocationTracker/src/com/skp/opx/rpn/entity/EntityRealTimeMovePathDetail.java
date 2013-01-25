package com.skp.opx.rpn.entity;


/**
 * @설명 : 실시간 상세 경로 Entity 
 * @클래스명 : EntityRealTimeMovePathDetail 
 *
 */
public class EntityRealTimeMovePathDetail  {

	public String 	mName;
	public long   	mTime;
	public String  	mCurrentLat;
	public String  	mCurrentLon;
	
	private boolean	mCheckedState;// 체크 박스 체크 상태
	
	public EntityRealTimeMovePathDetail() {}	
	
	public boolean ismCheckedState() {
		return mCheckedState;
	}

	public void setmCheckedState(boolean mCheckedState) {
		this.mCheckedState = mCheckedState;
	}

	public boolean getChecked() {
		return mCheckedState;
	}
	
	public void setChecked(boolean checked){
		mCheckedState = checked;
	}
	
}
