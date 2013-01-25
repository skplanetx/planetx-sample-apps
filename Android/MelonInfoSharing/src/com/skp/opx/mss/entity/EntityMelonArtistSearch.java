package com.skp.opx.mss.entity;

import com.skp.opx.sdk.EntityAbstract;
 
/**
 * @설명 : Melon 가수 검색 Entity
 * @클래스명 : EntityMelonArtistSearch
 * 
 */
public class EntityMelonArtistSearch extends EntityAbstract {

	public int		menuId, artistId;
	public String 	artistName;  		
	
	public EntityMelonArtistSearch() {}
	
	public EntityMelonArtistSearch(int menuId, int artistId, String artistName) {

		this.menuId			= menuId;
		this.artistId		= artistId;
		this.artistName 	= artistName;
	}

}
