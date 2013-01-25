package com.skp.opx.core.client;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.oauth.Constants.HttpMethod;
import com.skp.opx.sdk.AsyncRequester;
import com.skp.opx.sdk.EntityAbstract;
import com.skp.opx.sdk.EntityParserHandler;
import com.skp.opx.sdk.OnEntityParseComplete;
import com.skp.opx.sns.entity.EntityUploadPhoneBookPayload;
import com.skp.opx.sns.util.PreferenceUtil;

/**
 * @설명 : SNS SDK 연동시 URI 또는 Payload를 작성하는 클래스
 * @클래스명 : RequestGenerator
 * 
 */
public class RequestGenerator {

	/**
	 * @설명 :  댓글 등록 URI
	 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds/{feedId}/comments?version={version}
	 */
	public static final String makeURI_AddComment(String strFeedId) {

		return String.format("https://apis.skplanetx.com/social/providers/facebook/users/me/feeds/%s/comments?version=%s",strFeedId, Define.VERSION);
	}

	/**
	 * @설명 :  댓글 등록을 위한 Payload 생성
	 */
	public static final String makePayload_AddComment(String strContent, String strSocialAccessToken) throws JSONException {

		JSONObject rootObject = new JSONObject();
		JSONObject comment = new JSONObject();

		comment.put("content", strContent); //컨텐츠(글)
		comment.put("socialAccessToken", strSocialAccessToken); //Social별 AccessToken

		rootObject.put("comment", comment);

		return rootObject.toString();
	}

	/**
	 * @설명 :  사용자 정보 등록을 위한 Payload 생성
	 */
	public static final String makePayload_AddUserInfo(String strUserID, String strName, String strPhone) {

		JSONObject rootObject = new JSONObject();

		try {
			JSONObject profiles = new JSONObject(); 
			JSONObject users = new JSONObject();
			JSONArray user = new JSONArray();

			JSONObject userObject = new JSONObject(); 
			userObject.put("appUserId", strUserID); //사용자 ID
			userObject.put("name", strName); //사용자 이름 
			userObject.put("nationDialing", "82"); //국가 코드(한국)
			userObject.put("phoneNumber", strPhone); //전화번호

			rootObject.put("profiles", profiles);
			profiles.put("users", users);
			users.put("user", user);
			user.put(userObject);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}	

		return rootObject.toString();
	}

	/**
	 * @설명 : 소셜 컴포넌트 소셜 커넥트 사용자 정보 등록
	 * @RequestURI : https://apis.skplanetx.com/social/graph/users
	 */
	public static final void getAppUserId(String strName, String strPhone, Context context, OnEntityParseComplete completeListener) {
		
		//Unique userID 생성 - SKP + 현재시간 조합
		String strUserID = "SKP" + System.currentTimeMillis();
		// Request Payload
		//사용자 정보 등록을 위한 Payload 생성을 위한 함수 호출
		String strPayload  = makePayload_AddUserInfo(strUserID, strName, strPhone );
		//Bundle 생성
		//사용자 정보 등록을 위한 bundle객체 생성 
		RequestBundle bundle = AsyncRequester.make_PUT_POST_bundle(context, "https://apis.skplanetx.com/social/graph/users", null, strPayload, null);

		try {
			//API 호출
			//사용자 정보 등록 요청
			AsyncRequester.request(context, bundle, HttpMethod.PUT, new EntityParserHandler(new EntityAbstract(), completeListener));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		//등록된 사용자 정보 Preference 저장
		PreferenceUtil.setAppUserID(context, strUserID, true);
	}

	/**
	 * @설명 : 폰북 등록 URI 
	 * @RequestURI : https://apis.skplanetx.com/social/graph/users/{appUserId}/phoneBook?version={version}
	 */
	public static final String makeURI_UploadPhoneBook(String strUserID) {
		return String.format("https://apis.skplanetx.com/social/graph/users/%s/phoneBook?version=%s", strUserID, Define.VERSION);
	}

	/**
	 * @설명 :  폰북 등록을 위한 Payload 생성, 1개의 연락처를 등록할때 사용한다.
	 */
	public static final String makePayload_UploadPhoneBook(EntityUploadPhoneBookPayload entity) throws JSONException {

		ArrayList<EntityUploadPhoneBookPayload> arrayList = new ArrayList<EntityUploadPhoneBookPayload>();
		arrayList.add(entity);

		return makePayload_UploadPhoneBook(arrayList);
	}

	/**
	 * @설명 :  폰북 등록을 위한 Payload 생성, 여러개의 연락처를 등록할때 사용한다.
	 */
	public static final String makePayload_UploadPhoneBook(ArrayList<EntityUploadPhoneBookPayload> arrayList) throws JSONException {

		JSONObject rootObject = new JSONObject();
		JSONObject phoneBook= new JSONObject();
		JSONObject phones = new JSONObject();
		JSONArray phone = new JSONArray();

		for(EntityUploadPhoneBookPayload entity : arrayList) {
			JSONObject phoneEntity = new JSONObject();
			phoneEntity.put("nationDialing", entity.nationDialing); //국가 코드(82 : 한국)
			phoneEntity.put("phoneNumber", entity.phoneNumber); //전화번호
			phoneEntity.put("name", entity.name); //이름
			phone.put(phoneEntity);
		}

		phones.put("phone", phone);	
		phoneBook.put("autoNodeYn", "Y");
		phoneBook.put("deleteYn", "N");
		phoneBook.put("phones", phones);
		rootObject.put("phoneBook", phoneBook);

		return rootObject.toString();
	}

	/**
	 * @설명 : 댓글 조회 URI 
	 * @RequestURI : https://apis.skplanetx.com/social/providers/{socialName}/users/{linkId}/feeds/{feedId}/comments?version={version}&socialAccessToken={socialAccessToken}&socialAccessTokenSecret={socialAccessTokenSecret}&category={category}&index={index}&count={count}
	 */
	public static final String makeURI_CommentInfo(String strSocialName, String strLinkId, String strFeedId, String strSocialAccessToken) {
		
		return String.format("https://apis.skplanetx.com/social/providers/%s/users/%s/feeds/%s/comments?version=%s&socialAccessToken=%s", strSocialName, strLinkId, strFeedId, Define.VERSION, strSocialAccessToken);
	}
	
	/**
	 * @설명 : 추천 지인 조회 URI 
	 * @RequestURI : https://apis.skplanetx.com/social/graph/users/{appUserId}/nodes/recommend?version={version}&type={type}&count={count}
	 */
	public static final String makeURI_RecommendFriends(String strUserID){
		return String.format("https://apis.skplanetx.com/social/graph/users/%s/nodes/recommend?count=10&type=0&version=%s",strUserID, Define.VERSION);
	}

	/**
	 * @설명 : 지인 목록 조회 및 검색 URI 
	 * @RequestURI : https://apis.skplanetx.com/social/graph/users/{appUserId}/nodes?version={version}&type={type}&index={index}&count={count}&searchKeyword={searchKeyword}
	 */
	public static final String makeURI_FriendsList(String strUserID){
		return String.format("https://apis.skplanetx.com/social/graph/users/%s/nodes?version=%s&type=0&count=20", strUserID, Define.VERSION);
	}
	
	/**
	 * @설명 : 지인 생성 URI 
	 * @RequestURI : https://apis.skplanetx.com/social/graph/users/{appUserId}/nodes?version={version}
	 */
	public static final String makeURI_AddGraph(String strUserID) {

		return String.format("https://apis.skplanetx.com/social/graph/users/%s/nodes?version=%s", strUserID, Define.VERSION);
	}

	/**
	 * @설명 : 지인 생성을 위한 Payload 생성
	 */
	public static final String makePayload_AddGraph(String strTargetUserID) throws JSONException {

		JSONObject rootObject = new JSONObject();
		JSONObject graph = new JSONObject();

		rootObject.put("graph", graph);
		graph.put("targetUserID", strTargetUserID); //등록할 지인의 userID 
		graph.put("type", "0"); //등록할 지인의 등록 type (0 : 폰북, 1 : 소셜)

		return rootObject.toString();
	}
	
}
