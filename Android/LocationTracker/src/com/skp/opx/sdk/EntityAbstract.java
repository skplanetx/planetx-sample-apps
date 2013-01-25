package com.skp.opx.sdk;

import java.io.Serializable;

/**
 * @설명 : 수신시에 사용되는 Entity의 super 클래스 
 * @클래스명 : EntityAbstract
 */
@SuppressWarnings("serial")
public class EntityAbstract implements Serializable, Cloneable {

	public EntityAbstract() {}

	public Object clone() throws CloneNotSupportedException {
		 return super.clone();
	 }
}

