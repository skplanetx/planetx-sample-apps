package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : NateOn 친구 정보 Entity
 * @클래스명 : EntityNateOnBuddiesInfo
 * 
 * @URI :  https://apis.skplanetx.com/nateon/buddies
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : YES
 * @QuerystringParameters : version={version}&order={order}&page={page}&count={count} 
 * @ResponseParameter : nateId, name, nickname, onlineStatus, groupId, groupName
 * 
 */
public class EntityNateOnBuddiesInfo extends EntityAbstract {

	public String nateId;   //친구의 ID입니다
	public String name;      //친구의 이름입니다
	public String nickname;  //친구의 대화명입니다
	public String onlineStatus; //친구의 현재 상태입니다
	public String groupId;   //그룹의 ID입니다
	public String groupName; //그룹의 이름입니다

}
