package com.skp.opx.rpn.entity;


/**
 * @설명 : 즐겨 찾기함 Entity
 * @클래스명 : EntityFavorite 
 *
 */
public class EntityFavorite  {
   
	public long id;
	public String mLocationName;   //위치 이름
	public String mLon;
	public String mLat;
	private boolean	mCheckedState;// 체크 박스 체크 상태

	public EntityFavorite() {}
	
	public EntityFavorite(String locationName){
		this.mLocationName = locationName;
		
	}
	public String getLocationName() {
		return mLocationName;
	}

	public void setLocationName(String locationName) {
		this.mLocationName = locationName;
	}
	
	public boolean getChecked() {
		return mCheckedState;
	}
	
	public void setChecked(boolean checked){
		mCheckedState = checked;
	}
	
	
}
