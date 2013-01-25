package com.skp.opx.sns.entity;

import com.skp.opx.sdk.EntityAbstract; 

/**
 * @설명 : 홈 게시글 조회 Entity
 * @클래스명 : EntityHomePostsViews
 * @URI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds/home?version={version}&socialAccessToken={socialAccessToken}&socialAccessTokenSecret={socialAccessTokenSecret}&index={index}&count={count}
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : NO
 * @QuerystringParameters : version, socialAccessToken, socialAccessTokenSecret, category, index, count
 */
public class EntityHomePostsViews extends EntityAbstract  {

	public String feedId; 		//게시글의 feed ID
    public String name;			//게시자 이름
    public String image;			//게시자 사진
    public String publishTime;	//게시한 시간
    public String title;			//제목
    public String content;		//컨텐츠(글)
    public String link;				//LinkID 
    public String picture;		//게시 이미지
}
