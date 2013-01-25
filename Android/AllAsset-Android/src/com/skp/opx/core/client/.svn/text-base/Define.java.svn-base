package com.skp.opx.core.client;
/**
 * @설명 : 서비스모아보기 Define Data
 * @클래스명 : Define
 * 
 */
public final class Define {

	/** Authentication */
	public final class OAuth {
		public static final String CLIENT_ID = "< your key here >";
		public static final String SECRET = "< your key here >";
		public static final String KEY = "< your key here >";
		public static final String SCOPE  = "tcloud,cyworld,nate,nateon,user";
	}

	/** Common Version Request Parameter  */
	public static final String VERSION = "1";


	/**  
	 * MELON API CALL URI 
	 */
	/** 실시간차트  */
	public static final String MELON_CHARTS_REALTIME_URI = "http://apis.skplanetx.com/melon/charts/realtime";
	/** 주간차트  */
	public static final String MELON_CHARTS_TOP_GENRES_URI = "http://apis.skplanetx.com/melon/charts/topgenres";
	/** 앨범차트  */
	public static final String MELON_CHARTS_TOP_ALBUMS_URI = "http://apis.skplanetx.com/melon/charts/topalbums";
	/** 최신앨범  */
	public static final String MELON_NEW_ALBUMS_URI = "http://apis.skplanetx.com/melon/newreleases/albums";
	/** 최신곡  */
	public static final String MELON_NEW_SONGS_URI = "http://apis.skplanetx.com/melon/newreleases/songs";
	/** 곡 검색 */
	public static final String MELON_SEARCH_SONGS_URI = "http://apis.skplanetx.com/melon/songs";
	/** 앨범 검색 */
	public static final String MELON_SEARCH_ALBUMS_URI = "http://apis.skplanetx.com/melon/albums";
	/** 아티스트 검색 */
	public static final String MELON_SEARCH_ARISTS_URI = "http://apis.skplanetx.com/melon/artists";


	/**
	 *  CYWORLD API CALL URI 
	 *  */
	/** 싸이월드 관심일촌 목록보기 */
	public static final String CYWORLD_BESTIES_URI = "https://apis.skplanetx.com/cyworld/cys/besties";
	/** 싸이월드  미니홈피*/
	public static final String CYWORLD_MINIHOME_URI = "https://apis.skplanetx.com/cyworld/minihome/";
	/** 싸이월드 내 사진첩 폴더 목록보기  */
	public static final String CYWORLD_ME_PHOTO_FOLDER_URI = "https://apis.skplanetx.com/cyworld/minihome/me/albums";
	/** 싸이월드 내 사진첩 목록보기 (전체보기)  */
	public static final String CYWORLD_ME_PHOTO_LIST_URI = "https://apis.skplanetx.com/cyworld/minihome/me/albums/0/items";
	/** 싸이월드 내 방명록 모기 (2012년도)  */
	public static final String CYWORLD_ME_VISIT_URI = "https://apis.skplanetx.com/cyworld/minihome/me/visitbook/";


	/**
	 *  NATEON API CALL URI 
	 *  */
	/** 네이트온 친구 목록  */
	public static final String NATEON_FRIENDS_SEARCH_URI = "https://apis.skplanetx.com/nateon/buddies";
	/** 네이트온 친구 정보 */
	public static final String NATEON_FRIENDS_INFO = "https://apis.skplanetx.com/nateon/buddies/profiles";
	/** 네이트온 쪽지 보내기 */
	public static final String NATEON_SEND_NOTE_URI = "https://apis.skplanetx.com/nateon/notes";
	/** 네이트온 읽지 않은 쪽지 수 */
	public static final String NATEON_SEND_NOTE_UNREAD_CONUTS = "https://apis.skplanetx.com/nateon/notes/unread/counts";


	/**
	 *  11ST API CALL URI 
	 *  */
	/** 카테고리 조회 */
	public static final String ELEVEN_ST_CATEGORY_URI = "http://apis.skplanetx.com/11st/common/categories";
	/** 상품 조회 */
	public static final String ELEVEN_ST_PRODUCT_SEARHC_URI = "http://apis.skplanetx.com/11st/common/products";
	/** 11번가  정렬  */
	public static String getSortCode(int sortCode){

		switch(sortCode){
		case 0: return "CP";  //인기 순 정렬
		case 1: return "A";   //누적 판매 순 정렬
		case 2: return "G";   //평가 높은 순 정렬
		case 3: return "I";   //후기/리뷰 많은 순
		case 4: return "L";   //낮은 가격 순 
		case 5: return "H";   //높은 가격 순 
		case 6: return "N";   //최근 등록 순 
		default: return "CP";   //인기 순 정렬
		}
	}


	/**
	 *  TMAP API CALL URI 
	 *  */
	/** 통합 검색  */
	public static final String TMAP_TOTAL_SEARCH_URI = "https://apis.skplanetx.com/tmap/pois";
	/** 자동차 경로 안내 */
	public static final String TMAP_ROUTES_SEARCH_URI = "https://apis.skplanetx.com/tmap/routes";
	/** 좌표 변환 **/
	public static final String TMAP_CONVERT_COORDINATE_URI = "https://apis.skplanetx.com/tmap/geo/coordconvert";
	/** 주변 검색 **/
	public static final String TMAP_AREA_SEARCH_URI = "https://apis.skplanetx.com/tmap/pois/around";

}
