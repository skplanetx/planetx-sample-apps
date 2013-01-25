package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : NateOn 친구 목록 Entity
 * @클래스명 : EntityNateOnBuddiesId
 * 
 * @URI :  https://apis.skplanetx.com/nateon/buddies
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : YES
 * @QuerystringParameters : version={version}&order={order}&page={page}&count={count} 
 * @ResponseParameter : nateId
 * 
 */
public class EntityNateOnBuddiesId extends EntityAbstract {

	public String nateId;  //친구의 ID입니다

}
