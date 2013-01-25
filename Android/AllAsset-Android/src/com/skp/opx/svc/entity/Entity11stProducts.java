package com.skp.opx.svc.entity;

import com.skp.opx.sdk.EntityAbstract;

/**
 * @설명 : 11번가 상품 상세정보 Entity (지정한 상품 코드에 해당하는 상품 정보를 가져옵니다.)
 * @클래스명 : Entity11stProducts
 * 
 * @URI : http://apis.skplanetx.com/11st/common/products/{productCode}
 * @Protocol/HTTP Method : REST / Get Method 
 * @OAuth : NO
 * @QuerystringParameters : version={version}&option={option}
 * @ResponseParameter : ProductName, ProductImage, Price, LowestPrice, BasicImage, Point, Installment
 * 
 */
public class Entity11stProducts extends EntityAbstract {

	public int ProductCode;    //상품 코드입니다
	public String ProductName;    //상품 이름입니다
	public String Price;          //상품 가격입니다
	public String LowestPrice;    //11번가에서 제공할 수 있는 최저 가격입니다
	public String BasicImage;     //상품 기본 이미지의 URL 주소입니다
	public int Point;          //상품 구매 시 제공되는 포인트 정보입니다
	public String Installment;    //상품 무이자 할부 정보입니다

}
