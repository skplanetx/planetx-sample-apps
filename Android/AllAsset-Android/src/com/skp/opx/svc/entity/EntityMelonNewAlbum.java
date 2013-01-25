package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : Melon 최신앨범 Entity
 * @클래스명 : EntityMelonNewAlbum
 * 
 * @URI : http://apis.skplanetx.com/melon/newreleases/albums
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : NO
 * @QuerystringParameters : version={version}&page={page}&count={count}
 * @ResponseParameter : menuId, albumName, artistName, issueDate
 * 
 */
public class EntityMelonNewAlbum extends EntityAbstract {

	public int    menuId;      //메뉴 ID입니다
	public String artistName;  //아티스트의 이름입니다
	public int    albumId;     //앨범 ID입니다
	public String albumName;   //해당 곡이 수록된 앨범 이름입니다
	public String issueDate;   //곡 발매일입니다(ISO8601 형식입니다)

}
