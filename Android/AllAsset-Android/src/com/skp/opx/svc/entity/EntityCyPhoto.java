package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : Cyworld 미니홈피 사진첩 목록보기 Entity
 * @클래스명 : EntityCyPhoto 
 *
 * @URI : https://apis.skplanetx.com/cyworld/minihome
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : YES
 * @QuerystringParameters : [폴더 목록 보기] {cyId}/albums?version={version}
 *                          [사진 목록 보기] {cyId}/albums/{folderNo}/items?version={version}&page={page}&count={count}&searchType={searchType}&searchKeyword={searchKeyword}
 * @ResponseParameter : folderName, title, photoVmUrl, content, itemOpen, writeDate
 * 
 */
public class EntityCyPhoto extends EntityAbstract {

	public int    itemSeq;        //사진첩 게시물의 키가 되는 일련번호입니다
	public int    folderNo;         //사진첩 게시물이 저장되는 폴더의 번호입니다

	public String title;          //사진첩 게시물의 제목입니다
	public String photoVmUrl;     //이미지 파일의 전체 URL 경로입니다

	public String itemOpen;       //<게시물 공개 설정>
	public String writeDate;      //작성 일자입니다

}
