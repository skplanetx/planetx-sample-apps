package com.skp.opx.mss.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : Melon 앨범 검색 Entity
 * @클래스명 : EntityMelonAlbumSearch
 * 
 */
public class EntityMelonAlbumSearch extends EntityAbstract {

	public int		menuId, albumId;
	public String 	albumName;  	 //앨범의 이름입니다
	
	public EntityMelonAlbumSearch() {}
	
	public EntityMelonAlbumSearch(int menuId,int albumId, String albumName) {

		this.menuId		= menuId;
		this.albumId	= albumId;
		this.albumName 	= albumName;
	}

}
