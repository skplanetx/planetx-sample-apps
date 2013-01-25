package com.skp.opx.mss.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : Melon 실시간 차트 Entity
 * @클래스명 : EntityMelonRealtimeChart
 *
 */
public class EntityMelonRealtimeChart extends EntityAbstract  {

	public int 			menuId;         //메뉴ID입니다 (곡, 앨범, 아티스트에 대한 상세 페이지로 이동하기 위한 용도로 사용 됩니다 )
	public String 		songName;    	//곡 이름입니다
	public String 		artistName;  	//아티스트의 이름입니다
	public int 			currentRank;    //곡의 현재 순위입니다
	public String 		albumName;  	//앨범의 이름입니다
	public int			songId;			//곡 ID입니다

}
