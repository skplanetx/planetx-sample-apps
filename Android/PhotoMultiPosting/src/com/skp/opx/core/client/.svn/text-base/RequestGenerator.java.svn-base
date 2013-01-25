package com.skp.opx.core.client;

import com.skp.opx.sdk.Constants;

public class RequestGenerator {
	public static final String makeURI_CommentInfo(String strSocialName, String strLinkId, String strFeedId, String strSocialAccessToken, String strFolderID){
		
		String strURI = String.format("https://apis.skplanetx.com/social/providers/%s/users/%s/feeds/%s/comments?version=%s&socialAccessToken=%s", strSocialName, strLinkId, strFeedId, Constants.VERSION, strSocialAccessToken);
		
		if(strSocialName.equalsIgnoreCase("cyworld")) {
			strURI += "&category=0";
			strURI += "&folderNo=" + strFolderID;
		} 
		
		return strURI;
	}
}
