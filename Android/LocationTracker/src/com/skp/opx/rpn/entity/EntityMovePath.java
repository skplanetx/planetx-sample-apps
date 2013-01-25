package com.skp.opx.rpn.entity;

/**
 * @설명 : 이동 경로 Entity
 * @클래스명 : EntityMovePath 
 *
 */
public class EntityMovePath  {

	public long id;
	public String path;	 //경로	
	public String date; //날짜
	public long searchedTime;
	public String destinationLon;
	public String destinationLat;
    public String startLon;
    public String startLat;
	private boolean	mCheckedState;// 체크 박스 체크 상태
	
	public EntityMovePath() {}
	
	public EntityMovePath(String path, String date){
		this.path = path;
		this.date = date;
		
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean ismCheckedState() {
		return mCheckedState;
	}

	public void setmCheckedState(boolean mCheckedState) {
		this.mCheckedState = mCheckedState;
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
