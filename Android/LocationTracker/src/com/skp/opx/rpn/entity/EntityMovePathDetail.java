package com.skp.opx.rpn.entity;

import java.util.ArrayList;


/**
 * @설명 : 이동 경로 상세 Entity
 * @클래스명 : EntityMovePathDetail 
 *
 */
public class EntityMovePathDetail  {

	public String mName;
	public String mTurnType;
	public String mDistance;
	public String mTime;
	public String mSpeed;
	public String mTotalDistance;
	public String mDescription;
	private boolean	mCheckedState;// 체크 박스 체크 상태
	
	public EntityMovePathDetail() {}	
	
	public EntityMovePathDetail(String mName, String mTurnType,
			String mDistance, String mTime, String mSpeed, boolean mCheckedState) {
		super();
		this.mName = mName;
		this.mTurnType = mTurnType;
		this.mDistance = mDistance;
		this.mTime = mTime;
		this.mSpeed = mSpeed;
		this.mCheckedState = mCheckedState;
	}

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
