//
//  Defines.h
//  All Asset
//
//  Created by ; on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//
/* PLIST Path & Keys*/
FOUNDATION_EXPORT NSString *const CONFIGURATION_PLIST_FILE_NAME;
FOUNDATION_EXPORT NSString *const TMAP_RECENT_PLIST_FILE_NAME;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_NAME;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_TEXT;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_IS_LIVE;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_IS_SHORTCUT;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_IS_EDIT;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_DEFAULT_TEXT;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_ID;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_ALLOW_MESSAGE;
FOUNDATION_EXPORT NSString *const CONFIGURATION_KEY_DISPLAY_COUNT;

/* String Const*/
FOUNDATION_EXPORT NSString *const SERVICE_NATEON;
FOUNDATION_EXPORT NSString *const SERVICE_CYWORLD;
FOUNDATION_EXPORT NSString *const SERVICE_ELEVENST;
FOUNDATION_EXPORT NSString *const SERVICE_MELON;
FOUNDATION_EXPORT NSString *const SERVICE_TMAP;
FOUNDATION_EXPORT NSString *const TITLE_TEXT;
FOUNDATION_EXPORT NSString *const EDIT_TEXT;
FOUNDATION_EXPORT NSString *const DONE_TEXT;

FOUNDATION_EXPORT NSString *const CONFIGURATION_TEXT_FIRST;
FOUNDATION_EXPORT NSString *const CONFIGURATION_TEXT_SECOND;
FOUNDATION_EXPORT NSString *const CONFIGURATION_TEXT_EDIT_BUTTON;

/* Image Path*/
FOUNDATION_EXPORT NSString *const CONFIGURATION_IMAGE_UP;
FOUNDATION_EXPORT NSString *const CONFIGURATION_IMAGE_UP_PRESSED;
FOUNDATION_EXPORT NSString *const CONFIGURATION_IMAGE_DOWN;
FOUNDATION_EXPORT NSString *const CONFIGURATION_IMAGE_DOWN_PRESSED;

/* Table View Identifier*/
FOUNDATION_EXPORT NSString *const TV_ID_CONFIGURATION;
FOUNDATION_EXPORT NSString *const TV_ID_LOGIN;
FOUNDATION_EXPORT NSString *const TV_ID_NATEON;
FOUNDATION_EXPORT NSString *const TV_ID_ELEVENST_CATEGOTY;
FOUNDATION_EXPORT NSString *const TV_ID_ELEVENST_ITEMLIST;
FOUNDATION_EXPORT NSString *const TV_ID_ELEVENST_COMMENTLIST;
FOUNDATION_EXPORT NSString *const TV_ID_ELEVENST_REVIEWLIST;
FOUNDATION_EXPORT NSString *const TV_ID_MELON_NEWSONG;
FOUNDATION_EXPORT NSString *const TV_ID_MELON_NEWALBUM;
FOUNDATION_EXPORT NSString *const TV_ID_MELON_TOPCHART;
FOUNDATION_EXPORT NSString *const TV_ID_MELON_SEARCH;
FOUNDATION_EXPORT NSString *const TV_ID_CYWORLD_FRIENDLIST;
FOUNDATION_EXPORT NSString *const TV_ID_CYWORLD_PHOTOALBUM;
FOUNDATION_EXPORT NSString *const TV_ID_CYWORLD_GUESTBOOK;
FOUNDATION_EXPORT NSString *const TV_ID_TMAP_SEARCHPATH;
FOUNDATION_EXPORT NSString *const TV_ID_TMAP_SEARCHPATH_DETAIL;
FOUNDATION_EXPORT NSString *const TV_ID_CONFIGURATION_ELEVENST; //neep305

//--------------------------------
//  written by neep305
//--------------------------------
#ifndef DEBUG_MODE
    #define DEBUG_MODE 1
#endif

/** common */
#define IndicatorStyle UIActivityIndicatorViewStyleWhiteLarge

/* App Info */
#define MY_CLIENT_ID                @"<your key>"
#define MY_SECRET                   @"<your key>"
#define MY_APP_KEY                  @"<your key>"
#define MY_SCOPE                    @"<your key>"

#define KEY_APP_KEY                 @"appKey"
#define KEY_CLIEND_ID               @"clientId"
#define KEY_SECRET                  @"secret"
#define KEY_SCOPE                   @"scope"

/** API 호출 PARAM KEY */
#define KEY_VER                     @"version"
#define KEY_PAGE                    @"page"
#define KEY_COUNT                   @"count"
#define KEY_KEYWORD                 @"searchKeyword"
#define KEY_OPTION                  @"option"
#define KEY_SORTCODE                @"sortCode"

/** 공통적으로 API 파라메터로 사용하는 페이지, 갯수 */
#define VERSION                     @"1"
#define PAGE                        @"1"
#define COUNT                       @"10"

#define REQ_NATEON                  0
#define REQ_CYWORLD                 1
#define REQ_ELEVENST                2
#define REQ_MELON                   3
#define REQ_TMAP                    4

/** 공통 메시지 */
#define MSG_API_REQUEST_FAILED      @"API 요청에 실패하였습니다."
#define MSG_LAUNCH_BROWSER          @"웹브라우저를 실행하시겠습니까?"
#define CONFIRM                     @"확인"
#define CANCEL                      @"취소"

/** Configuration INDEX */
#define CONFIG_INDEX_NATEON         0
#define CONFIG_INDEX_CYWORLD        1
#define CONFIG_INDEX_11ST           2
#define CONFIG_INDEX_MELON          3
#define CONFIG_INDEX_TMAP           4

/** Configuration 11st Message */
#define CONFIG_SAVE_SUCCESS         @"카테고리가 설정되었습니다."
#define CONFIG_SAVE_FAIL            @"카테고리 설정 중 오류가 발생하였습니다."

/** 11st CATEGORY TYPE */
#define CATE_PARENT                 10
#define CATE_CHILDREN               11
#define CATE_KEYWORD                12

/** MELON REQ TYPE*/
#define REQ_NEW_SONG                100
#define REQ_NEW_ALBUM               101
#define REQ_CHART                   102
#define REQ_SEARCH_SONG             103
#define REQ_SEARCH_ALBUM            104
#define REQ_SEARCH_ARTIST           105

#define MELON_WEEKLY_TOP_CHART      @"/melon/charts/topgenres"
#define MELON_REALTIME_CHART        @"/melon/charts/realtime"
#define MELON_ALBUM_TOP_CHART       @"/melon/charts/topalbums"
#define MELON_WEB_PAGE              @"http://m.melon.com/cds/common/mobile/openapigate_dispatcher.htm?type=%@&cid=%@&menuId=%@"

/** 11번가 */
#define ELEVENST_CATEGORY_PATH      @"/11st/common/categories"
#define ELEVENST_PRODUCT_PATH       @"/11st/common/products"
#define ELEVENST_ONE_CATEGORY_PATH  @"/11st/common/categories/%@"

/**
 CP: 인기 순
 A: 누적 판매 순
 G: 평가 높은 순
 I: 후기/리뷰 많은 순
 L: 낮은 가격 순
 H: 높은 가격 순
 N: 최근 등록 순
 */
#define SORT_CP                     @"CP"
#define SORT_A                      @"A"
#define SORT_G                      @"G"
#define SORT_I                      @"I"
#define SORT_L                      @"L"
#define SORT_H                      @"H"
#define SORT_N                      @"N"

#define TEXT_SORT_CP                @"인기 순"
#define TEXT_SORT_A                 @"누적 판매 순"
#define TEXT_SORT_G                 @"평가 높은 순"
#define TEXT_SORT_I                 @"후기/리뷰 많은 순"
#define TEXT_SORT_L                 @"낮은 가격 순"
#define TEXT_SORT_H                 @"높은 가격 순"
#define TEXT_SORT_N                 @"최근 등록 순"

//11번가 카테고리용 Dictionary KEY
#define STR_GROUP                   @"Group"
#define STR_ARRAY                   @"Array"
#define STR_NAME                    @"Name"
#define STR_ID                      @"Id"
#define STR_TITLE                   @"Title"
#define STR_PRICE                   @"Price"
#define STR_SALE_PRICE              @"SalePrice"
#define STR_MILEAGE                 @"Mileage"
#define STR_INFREE                  @"InFree"
#define STR_SELLER                  @"Seller"
#define STR_COMMENT                 @"Comment"  //만족도 OO%, 후기/리뷰 OO건
#define STR_CONDITION               @"Condition"
#define STR_IMAGE_URL               @"ImageURL"
#define STR_IMAGE300_URL            @"Image300_URL"
#define STR_SATISFY                 @"Satisfy"
#define STR_REVIEW_CNT              @"ReviewCnt"
#define STR_CHILD                   @"Child"

/** NateOn 호출 URL 경로 */
#define NATEON_MESSAGE_PATH         @"/nateon/notes/%@"

/** 싸이월드 호출 URL 경로 */
#define CY_BESTIES_PATH             @"/cyworld/cys/besties"
#define CY_FOLDER_LIST_PATH         @"/cyworld/minihome/%@/albums"
#define CY_PHOTO_LIST_PATH          @"/cyworld/minihome/%@/albums/%@/items"
#define CY_GUESTBOOK_PATH           @"/cyworld/minihome/%@/visitbook/%@/items"
#define CY_PHOTO_DETAIL_PATH        @"/cyworld/minihome/%@/albums/%@/items/%@"

#define THISYEAR                    @"2012"

//싸이월드 Dictionary KEY
#define STR_DATE                    @"Date"
#define STR_CONTENT                 @"Content"

/** TMAP */
#define TMAP_ROUTES_PATH            @"/tmap/routes"
#define TMAP_TRAFFIC_PATH           @"/tmap/traffic"
#define TMAP_POI_PATH               @"/tmap/pois"
#define TMAP_JOBKIND                @"/tmap/poi/categories"
#define TMAP_NEAR_PLACE             @"/tmap/pois/around"
#define TMAP_REVERSE_GEO_PATH       @"/tmap/geo/reversegeocoding"

#define KEY_LAT                     @"lat"  //위도
#define KEY_LON                     @"lon"  //경도
#define KEY_COORDTYPE               @"coordType"
#define KEY_END_LON                 @"endX"   //경도
#define KEY_END_LAT                 @"endY"   //위도
#define KEY_START_LON               @"startX"
#define KEY_START_LAT               @"startY"
#define KEY_RSD_COORDTYPE           @"reqCoordType"
#define KEY_RES_COORDTYPE           @"resCoordType"

#define COORDTYPE                   @"WGS84GEO"

#define STR_BIZ_CODE                @"BizCode"
#define STR_BIZ_NAME                @"BizName"
#define STR_SHOPNAME                @"ShopName" //가게명
#define STR_CONTENT                 @"Content"  //장소소개
#define STR_TEL                     @"Tel"      //가게 전화번호
#define STR_ADDRESS                 @"Address"  //가게 전화번호
#define STR_LAT                     @"Latitude"
#define STR_LON                     @"Longitude"

typedef enum {d11 = 11, d12, d13, d14, d15, d16, d17, d18, d19, d101 = 101, d102, d103, d104, d105, d106, d111, d112, d113, d114, d115, d116, d117, d118, d119, d120, d121, d122, d123,d124, d131 = 131, d132, d133, d134, d135, d136, d137, d138, d139, d140, d141, d142, d200 = 200, d201 } DirectionType;

#define D11                         @"직진"
#define D12                         @"좌회전"
#define D13                         @"우회전"
#define D14                         @"U턴"
#define D15                         @"P턴"
#define D16                         @"8시 방향 좌회전"
#define D17                         @"10시 방향 좌회전"
#define D18                         @"2시 방향 우회전"
#define D19                         @"4시 방향 우회전"
#define D101                        @"우측 고속도로 입구"
#define D102                        @"좌측 고속도로 입구"
#define D103                        @"전방 고속도로 입구"
#define D104                        @"우측 고속도로 출구"
#define D105                        @"좌측 고속도로 출구"
#define D106                        @"전방 고속도로 출구"
#define D111                        @"우측 도시고속도로 입구"
#define D112                        @"좌측 도시고속도로 입구"
#define D113                        @"전방 도시고속도로 입구"
#define D114                        @"우측 도시고속도로 출구"
#define D115                        @"좌측 도시고속도로 출구"
#define D116                        @"전방 도시고속도로 출구"
#define D117                        @"우측 방향"
#define D118                        @"좌측 방향"
#define D119                        @"지하차도"
#define D120                        @"고가도로"
#define D121                        @"터널"
#define D122                        @"교량"
#define D123                        @"지하차도 옆"
#define D124                        @"고가도로 옆"
#define D131                        @"1시 방향"
#define D132                        @"2시 방향"
#define D133                        @"3시 방향"
#define D134                        @"4시 방향"
#define D135                        @"5시 방향"
#define D136                        @"6시 방향"
#define D137                        @"7시 방향"
#define D138                        @"8시 방향"
#define D139                        @"9시 방향"
#define D140                        @"10시 방향"
#define D141                        @"11시 방향"
#define D142                        @"12시 방향"
#define D200                        @"출발지"
#define D201                        @"도착지"

/* URL */
FOUNDATION_EXPORT NSString *const SERVER;
FOUNDATION_EXPORT NSString *const SERVER_SSL;

/* OAuth Path & Keys */
FOUNDATION_EXPORT NSString *const ELEVEN_CATEGORY_PLIST_FILE_NAME;  //neep305
FOUNDATION_EXPORT NSString *const ELEVEN_KEY_ISSAVED;
FOUNDATION_EXPORT NSString *const ELEVEN_KEY_CATEGORYCODE;
FOUNDATION_EXPORT NSString *const ELEVEN_KEY_CATEGORYNAME;

FOUNDATION_EXPORT NSString *const OAUTH_PLIST_FILE_NAME;            //neep305
FOUNDATION_EXPORT NSString *const OAUTH_KEY_ACCESS_TOKEN;
FOUNDATION_EXPORT NSString *const OAUTH_KEY_REFRESH_TOKEN;
FOUNDATION_EXPORT NSString *const OAUTH_KEY_IS_SAVED;
