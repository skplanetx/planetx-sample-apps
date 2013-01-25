package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : Melon 아티스트 검색  Entity
 * @클래스명 : EntityMelonSearchArtist
 * 
 * @URI : http://apis.skplanetx.com/melon/artists
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : NO
 * @QuerystringParameters : version={version}&page={page}&count={count}&searchKeyword={searchKeyword}
 * @ResponseParameter : menuId, artistId, artistName, genreNames
 * 
 */
public class EntityMelonSearchArtist extends EntityAbstract  {

	public int menuId;         //메뉴ID입니다 (곡, 앨범, 아티스트에 대한 상세 페이지로 이동하기 위한 용도로 사용 됩니다 )
	public int artistId;       //아티스트의 ID입니다
	public String artistName;  //아티스트의 이름입니다
	public String genreNames;   //장르 이름입니다

}
