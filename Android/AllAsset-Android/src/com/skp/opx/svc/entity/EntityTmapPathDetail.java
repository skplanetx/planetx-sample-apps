package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : Tmap 경로 상세 Entity
 * @클래스명 : EntityTmapPathDetail
 * 
 * @URI :  https://apis.skplanetx.com/tmap/routes
 * @Protocol/HTTP Method : REST / Post Method  
 * @OAuth : NO
 * @QuerystringParameters : version={version}
 * @ResponseParameter : 
 * 
 */
public class EntityTmapPathDetail extends EntityAbstract {

	public String 	mStartLocation; // 출발지
	public String   mDestination; // 도착지
	public String 	mName;
	public String   mTurnType;
	public String 	mTime; //구간 소요시간
	public String   mTotalTime; //총 소요 시간
	public String	mDistance;
	public String   mTotalDistance;
	public String 	mSpeed;	
	public String   mDestinatioLon; //도착지 좌표
	public String   mDestinationLat;
	public String   mDescription;

}
