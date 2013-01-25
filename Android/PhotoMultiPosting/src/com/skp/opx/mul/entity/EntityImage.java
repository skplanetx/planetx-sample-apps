package com.skp.opx.mul.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : T cloud 사진목록조회 Entity
 * @클래스명 : EntityImage
 */
public class EntityImage  extends EntityAbstract{

	public String	imageId;		//이미지 아이디
	public String 	thumbnailUrl; 	//섬네일 
	public String 	downloadUrl;	//이미지 다운로드

	public EntityImage() {}

	public EntityImage(String imageId, String thumbnailUrl, String downloadUrl){
		this.imageId= imageId;
		this.thumbnailUrl=thumbnailUrl;
		this.downloadUrl=downloadUrl;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}


}
