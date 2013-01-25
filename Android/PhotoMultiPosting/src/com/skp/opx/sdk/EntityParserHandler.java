package com.skp.opx.sdk;


import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

/**
 * @설명 : 수신받은 데이터를 Json에서 Entity클래스 형태로 변환해 주는 Parser Handler 클래스
 * @클래스명 : EntityParserHandler
 */
public class EntityParserHandler extends Handler {

	private OnEntityParseComplete mParseComplete; //파싱 완료시, 완료 interface
	private EntityAbstract mEntityAbstract, mNextEntityAbstract; //파싱될 Entity, 파싱될 다음 Entity
	private ArrayList<EntityAbstract> mEntityAbstractArray = new ArrayList<EntityAbstract>(); //파싱된 Entity들의 저장 Array
	private java.lang.reflect.Field[] mEntityClassFields; //파싱될 Entity의 클래스 Field정보
	private String mStrRemoveNode = null, mUniqueObjectName = null; //파싱하지 하지 않을 Node 이름, 수신되는 값의 필드는 1개인데 EntityArray의 모든 Entity에 추가하고 싶은 멤버변수명
	private Object mUniqueObject = null; //수신되는 값의 필드는 1개인데 EntityArray의 모든 Entity에 추가하고 싶은 멤버변수
	private String mFirstFindObjectName = null; //Entity의 멤버변수 이름과 같은 JSONObject의 이름. 첫번째 이름명.
	private String mStrBeforeObjectName = "", mStrBeforeBeforeObjectName = ""; //이전의 JSONObject의 이름, 이전이전의 JSONObject의 이름
	private boolean mIsArrayIndexAddEntity = false; //Array기반으로 Entity를 ADD할때 사용한다.
	
	/** 
	 * 생성자 : 파싱받을 Entity와 파싱완료 interface를 파라미터로 받는다.
	 * */
	public EntityParserHandler(EntityAbstract entity, OnEntityParseComplete onParseComplete) throws CloneNotSupportedException {

		initFields(entity, onParseComplete);
	}

	/** 
	 * 생성자 : 파싱받을 Entity와 파싱완료 interface를 파라미터로 받는다. 파싱받지 않을 Node 추가.
	 * */
	public EntityParserHandler(EntityAbstract entity, OnEntityParseComplete onParseComplete, String strRemoveNode) throws CloneNotSupportedException {

		mStrRemoveNode = strRemoveNode;
		initFields(entity, onParseComplete);
	}

	/** 
	 * 생성자 : 파싱받을 Entity와 파싱완료 interface를 파라미터로 받는다. 파싱받지 않을 Node 추가. 첫번째 Array에서 Item 단위로 파싱로직 추가.
	 * */
	public EntityParserHandler(EntityAbstract entity, OnEntityParseComplete onParseComplete, String strRemoveNode, boolean isArrayIndexAddEntity) throws CloneNotSupportedException {

		mStrRemoveNode = strRemoveNode;
		mIsArrayIndexAddEntity = true;
		initFields(entity, onParseComplete);
	}
	
	/** 
	 * 생성자 : 파싱받을 Entity와 파싱완료 interface를 파라미터로 받는다. 파싱받지 않을 Node 추가. Array가 아닌 단일값이 존재하며, 모든 Entity에 적용되어야 할 값 추가.
	 * */
	public EntityParserHandler(EntityAbstract entity, OnEntityParseComplete onParseComplete, String strRemoveNode, String strUniqueObjectName) throws CloneNotSupportedException {

		mUniqueObjectName = strUniqueObjectName;
		mStrRemoveNode = strRemoveNode;
		initFields(entity, onParseComplete);
	}

	/** 
	 * 생성자 파라미터로 등록된 파싱될 entity, 완료 interface 및 데이터를 초기화 시킨다.
	 * */
	private void initFields(EntityAbstract entity, OnEntityParseComplete onParseComplete) throws CloneNotSupportedException {

		mParseComplete = onParseComplete;
		mEntityClassFields = entity.getClass().getDeclaredFields();
		mEntityAbstract = entity;
		mNextEntityAbstract = (EntityAbstract)entity.clone();
	}

	/** 
	 * JSONObject Node 탐색 및 Entity 값 저장 재귀함수. JSONObject 노드를 탐색하며 전달받은 Entity의 내부 변수명과 같은 이름이 있으면 Entity에 값을 저장한다. 
	 * */
	private void recursiveFindEntity(JSONObject jsonObject) throws Exception {

		if(jsonObject == null) {
			return;
		}

		Iterator iter = jsonObject.keys();
		while(iter.hasNext()) {
			//JSONObject의 key name (변수명)
			String strName = (String)iter.next();
			//JSONObject의 Key value (변수값)
			Object object = jsonObject.get(strName);

			//mUniqueObjectName와 같은 일경우 Object값을 저장후 다음 노드를 찾는다.
			if(mUniqueObjectName != null && mUniqueObjectName.equalsIgnoreCase(strName)) {
				mUniqueObject = object;
				continue;
			}
			//파싱하지 않을 Node의 Name일 경우
			if(mStrRemoveNode != null && strName.equalsIgnoreCase(mStrRemoveNode)) {
				return;
			}

			//JSONObject형태일 경우 JSONObject를 찾는 재귀함수를 호출한다.
			if(object instanceof JSONObject) {
				recursiveFindEntity((JSONObject)object);
			//JSONArray형태일 경우 Array노드를 찾는 재귀함수를 호출한다.
			} else if(object instanceof JSONArray) {
				recursiveFindEntityArray((JSONArray)object);
			//JSONObject, JSONArray가 아닌 경우 값을 저장하고 있는 Object이다.
			} else {
				for(int index = 0, maxIndex = mEntityClassFields.length; index < maxIndex; index++) {
					//Entity에 존재하는 변수 이름과 JSONObject의 이름이 일치하면 파싱한다.
					if(mEntityClassFields[index].getName().equalsIgnoreCase(strName) == true) {
						//같은 이름의 JSONObject의 이름이 존재하는 경우 예외처리
						if(maxIndex >= 4 && (strName.equalsIgnoreCase(mStrBeforeObjectName) || strName.equalsIgnoreCase(mStrBeforeBeforeObjectName))) {
							continue;
						}

						//JSONObject에서 첫번째로 찾은 변수이름과 같은 항목을 찾으면 Entity를 Array에 포함시키고 새로운 Entity를 생성한다.
						if(strName.equalsIgnoreCase(mFirstFindObjectName) && mIsArrayIndexAddEntity == false) {
							addEntityInArray();
						}

						//이전의 변수 이름 저장
						mStrBeforeBeforeObjectName = mStrBeforeObjectName;
						//이전, 이전의 변수 이름 저장
						mStrBeforeObjectName = strName;
						//Entity객체에 JSONObject에서 추출한 변수값을 저장한다.
						mEntityClassFields[index].set(mEntityAbstract, object);

						//JSONObject에서 찾은 첫번째 변수 이름.
						if(mFirstFindObjectName == null) {
							mFirstFindObjectName = strName;
						}
						break;
					//Entity의 이름이 mUniqueObjectName의 이름과 같으면 위에서 저장된 mUniqueObject값을 Entity에 저장한다.
					} else if(mEntityClassFields[index].getName().equalsIgnoreCase(mUniqueObjectName)) {
						if(mUniqueObject != null) {
							mEntityClassFields[index].set(mEntityAbstract, mUniqueObject);
						}
					}
				}
			}
		}
	}
	
	private void addEntityInArray() throws Exception {
		
		mEntityAbstractArray.add(mEntityAbstract);
		mEntityAbstract = mNextEntityAbstract;
		mNextEntityAbstract = (EntityAbstract) mEntityAbstract.clone();
	}

	/** 
	 * JSONArray Node 탐색 재귀함수.
	 * */
	private void recursiveFindEntityArray(JSONArray jsonArray) throws Exception {

		for(int index = 0, maxIndex = jsonArray.length(); index < maxIndex; index++) {
			recursiveFindEntity(jsonArray.getJSONObject(index));
			
			//Array에서 Item마다 1개의 Entity를 ADD한다.
			if(mIsArrayIndexAddEntity) {
				addEntityInArray();
			}
		}
	}

	/** 
	 * 수신데이터를 Handler를 통해 전달받아 파싱함수를 호출 및 파싱완료 인터페이스를 호출하는 함수
	 * */
	@Override
	public void handleMessage(Message msg) {

		if(msg == null || msg.obj == null) {
			return;
		}

		try {
			//수신받은 데이터를 재귀함수를 호출하여 파싱작업을 시작한다.
			recursiveFindEntity(new JSONObject((String)msg.obj));
			
			//마지막으로 값이 저장된 Entity를 Array에 저장한다.
			if(mFirstFindObjectName != null && mIsArrayIndexAddEntity == false) {
				mEntityAbstractArray.add(mEntityAbstract);
			}
			//mUniqueObjectName과 mUniqueObject의 존재하면 모든 Entity에 해당 이름에 해당하는 변수에 값을 저장한다.
			if(mUniqueObjectName != null && mUniqueObject != null) {
				int uniqueIndex = 0;
				
				for(int index = 0, maxIndex = mEntityClassFields.length; index < maxIndex; index++) {
					if(mEntityClassFields[index].getName().equalsIgnoreCase(mUniqueObjectName)){
						uniqueIndex = index;
						break;
					}
				}
				
				for(EntityAbstract entity : mEntityAbstractArray) {
					mEntityClassFields[uniqueIndex].set(entity, mUniqueObject);
				}
			}
			//파싱완료 인터페이스를 호출한다.
			mParseComplete.onParsingComplete(mEntityAbstractArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//송수신시 팝업되는 프로그래스바를 종료한다.
			PopupDialogUtil.dismissProgressDialog();
		}
	} 
}
