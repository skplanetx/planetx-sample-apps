package com.skp.opx.sns.entity;

import com.skp.opx.sdk.EntityAbstract; 


/**
 * @설명 : 댓글 조회 Entity
 * @클래스명 : EntityCommentViews
 * @URI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds/{feedId}/comments?version={version}&socialAccessToken={socialAccessToken}&socialAccessTokenSecret={socialAccessTokenSecret}&category={category}&index={index}&count={count}
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : NO
 */
public class EntityCommentViews extends EntityAbstract {
	
    public String name; 			//이름
    public String content; 		//컨텐츠(글)
    public String publishTime; //공개한 시간
}
