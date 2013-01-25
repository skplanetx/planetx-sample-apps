package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : Cyworld 방명록 Entity
 * @클래스명 : EntityCyVisitBook
 * 
 * @URI : https://apis.skplanetx.com/cyworld/minihome/{cyId}/visitbook/{year}/items
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : YES
 * @RequestPathParam : cyId, year
 * @QuerystringParameters : version={version}&page={page}&count={count}&searchType={searchType}&searchKeyword={searchKeyword}&listType={listType}
 * @ResponseParameter : content, writeDate, writerId, writerName
 * 
 */
public class EntityCyVisitBook extends EntityAbstract {

	public String content;      //방명록의 내용입니다
	public String writeDate;    //작성 일자입니다
	public String writerId;     //작성자의 ID입니다
	public String writerName;     //작성자의 이름입니다
	public String count;		//카운트 갯수

}
