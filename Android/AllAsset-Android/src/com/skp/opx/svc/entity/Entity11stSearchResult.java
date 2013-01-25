
package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : 11번가 상품 검색결과 Entity
 * @클래스명 : Entity11stSearchResult
 * 
 * @URI : http://apis.skplanetx.com/11st/common/products?
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : NO
 * @QuerystringParameters : version={version}&page={page}&count={count}&searchKeyword={searchKeyword}&={sortCode}&option={option}
 * @ResponseParameter : ProductCode, ProductName, ProductPrice, ProductImage, SellerNick, Seller, ReviewCount, BuySatisfy, Delivery
 * 
 */
public class Entity11stSearchResult extends EntityAbstract {
	
	public int ProductCode;     //상품 코드입니다
	public String ProductName;  //상품 이름입니다
	public int ProductPrice;    //상품 가격입니다
	public String ProductImage; //상품의 이미지 URL 주소입니다
	public String ProductImage300; //상품의 이미지 URL 주소입니다
	public String SellerNick;   //판매자의 닉네임입니다
	public String Seller;       //판매자 ID입니다
	public int SalePrice;       //상품 가격에서 할인이 적용된 가격입니다.(할인 모음가)
	public String Delivery;     //배송 정보입니다
	public int ReviewCount;     //상품 평가 수입니다;
	public int BuySatisfy;      //구매 만족도입니다
	
}
