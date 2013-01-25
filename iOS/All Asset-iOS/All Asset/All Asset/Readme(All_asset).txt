i. 개요 - "All Asset"
	
	1. 명칭 : SKP서비스모아보기 sample application
	
	2. 설명 : SK Planet에서 제공하는 서비스를 한번에 사용할 수 있는 서비스

	3. API 활용 : NateOn, Melon, Cyworld, T map, 11st API



ii.Sample Project 파일 구성
	
	1. /All\Asset All Asset-Info.plist 파일 : All Asset 프로젝트의 기본 Property 정보가 설정되어 있습니다.

	2. /All\Asset/SKP_API/SKPOPSDKLib : 라이브러리 폴더

		- libSKPOP_SDK_OAUTH_v1.4.a : OneID 인증 (device, simulator용 라이브러리를 fat-library로 재생성)

		- libSKPOP_SDK_v1.4.a : Planet X server 연동 (device, simulator용 라이브러리를 fat-libray로 재생성)

        - SDK fat-libary 생성시: $ lipo -create libSKPOP_SDK_OAUTH_v1.4_sim.a libSKPOP_SDK_OAUTH_v1.4_dev.a -output libSKPOP_SDK_OAUTH_v1.4.a

        - libskpMap_OP-iOS6.0.a : Tmap static 라이브러리
        
    3. /All\Asset/SKP_API/SKPOPSDKLib/JSONKit : 라이브러리용 JSON 유틸 클래스
        
        - JSONKit.h : JSON 유틸 헤더파일
        
        - JSONKit.m : JSON 유틸 소스파일
        
    4. /All\Asset/SKP_API/SKPOPSDKLib/Tmap : Tmap 헤더파일    
        
        - TMapView.h : 실제 맵을 표현해주는 View 헤더파일

	5. /All\Asset/SKP_API/Common : 프로젝트 실행에 필요한 OAuth Key, 각 API의 URI 및

	   프로젝트 설정을 정의

	        - Define.h : 샘플 앱 실행시 Define.h 파일을 아래 가이드 내용에 따라 재정의해야 합니다.

		/* App Info */
        #define MY_CLIENT_ID                @"< your key here >"
        #define MY_SECRET                   @"< your key here >"
        #define MY_APP_KEY                  @"< your key here >"
        #define MY_SCOPE                    @"< your key here >"
 
    6. /All\Asset/Supporting Files : 샘플 앱 실행시 사용하는 plist 파일이 등록되어 있습니다.
        
        - TmapRecent.plist : Tmap 최근 검색한 지역 정보
        
        - Configuration.plist : 메인화면 설정 정보
        
        - ElevenstCategory.plist : 최근 검색한 11번가 상품 카테고리 정보

iii. 준비 사항
	
	1. One ID 가입 및 전환 
                  
		: 본 어플리케이션은 One ID로만 로그인할 수 있으므로 One ID(https://oneid.skplanet.co.kr)에 가입하셔야 합니다.

	2. 개발자 센터에 회원 가입
                  
		: Sample Application을 실행하기 위해서는 Planet X에서 발급한 AppKey가 필요합니다.
                    
		  따라서, https://developers.skplanetx.com 사이트에 개발자 가입을 하셔야 합니다. 이미 One ID 계정을 가지고 

		  있다면 사용 동의만 하시면 됩니다.
	
	3. 개발자 센터에 샘플 앱 등록하기

	   1) 개발자 센터 사이트 상단 메뉴에서 [ 내 정보 > 앱 정보 ] 페이지에서 앱을 등록하셔야 합니다.

	   2) 앱 등록 완료한 후 인증키(AppKey)를 발급받으셔야 합니다. 
                    
		- 인증키는 Planet X API를 호출하기 위해서 사용하는 인증키로 어플리케이션 실행을 위해 매우 중요한 값이며
                 
		- Client ID, Client secret, App key 세개의 값이 발급되며 이 정보는 아래 iv. 환경설정 4번 항목에서 사용됩니다.

	   3) 앱을 등록할 때 [Select Service] 에서 "SK Planet One ID", "네이트온", "싸이월드", "멜론", "T map",

	      "11번가"를 꼭 선택해야 합니다.
          
	      이는 앱이 사용하는 API가 속한 서비스별로 사용 승인을 득해야 하기 때문입니다.          
 
       4) Tmap API Key는 개발자 센터에 등록한 App key를  사용하시면 됩니다.
        
         (ex)
         TMapView *mapView = [[TMapView alloc] initWithFrame:CGRectMake(0, 147, 320, 313)];
         [mapView setSKPMapApiKey:@"<your key>"];
         
iv. 환경 설정

	1. iOS SDK 6.0 설치

	2. 단말 및 emulator 6.0 이상

	3. 다운받은 샘플 프로젝트의 압축을 푼 후 All Asset.xcodeproj를 클릭하여 Xcode를 실행합니다.

	4. /All\Asset/SKP_API/Common/Define.h에 개발자 센터에서 앱 등록 후 발급받은 Client ID, 

	   Client secret, App key를 입력하고 저장하셔야 합니다.



vi. Sample Project에 대한 문의처

	궁금하신 사항은 아래 Planet X 개발자 센터의 [ 문의하기 ] 메뉴를 통해 문의해 주십시요
   
	(https://developers.skplanetx.com/community/contact-us/enrollment)