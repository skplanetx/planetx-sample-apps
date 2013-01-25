i. 개요 - "All Asset"
	
	1. 명칭 : SKP서비스모아보기 sample application
	
	2. 설명 : SK Planet에서 제공하는 서비스를 한번에 사용할 수 있는 서비스

	3. API 활용 : NateOn, Melon, Cyworld, T map, 11st API



ii.Sample Project 파일 구성
	
	1. /OpenPlatform_SVC의 AndroidManifest.xml 파일 : Planet X의 라이브러리를 사용할 때 꼭 설정해야 할 

	   permission과 Activity가 선언되어 있습니다.

	2. /OpenPlatform_SVC/libs : 라이브러리 폴더

		- SKPOP_SDK.jar : Planet X server 연동

		- SKPOP_SDK_OAuth.jar : OneID 인증
		
		- TmapSDK.1.0.0.jar : Tmap static 라이브러리

	3. /OpenPlatform_SVC/src/com/skp/opx/core/client : 프로젝트 실행에 필요한 OAuth Key, 각 API의 URI 및

	   프로젝트 설정을 정의

	        - Define.java : 샘플 앱 실행시 Define.java 파일을 아래 가이드 내용에 따라 재정의해야 합니다.

		  /** Authentication */
 		  public final class OAuth {
  		  	public static final String CLIENT_ID       = "< your key here >";
 			public static final String SECRET   	= "< your key here >";
  			public static final String KEY    	= "< your key here >";
  		  }
 


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
 


iv. 환경 설정

	1. Android SDK API level 10 이상 설치

	2. 단말 및 emulator의 앱실행 버전 Gingerbread 2.3 이상 (버전별 에뮬레이터 버그문제로 인하여 아이스크림샌드위치 에뮬레이터를 권장합니다.)

	3. 다운받은 샘플 프로젝트의 압축을 푼 후 OpenPlatform_SVC 폴더를 eclipse에서 import.

	4. OpenPlatform_SVC/src/com/skp/opx/core/client/Define.java의 OAuth class에 개발자 센터에서 앱 등록 후 발급받은 Client ID, 

	   Client secret, App key를 입력하고 저장하셔야 합니다.

	※ emulator로 테스트 하시는 경우
	
	   T map API를 사용하시기 위해서는 emulator에서 GPS 정보를 직접 설정해 주셔야 합니다.
	
	   : DDMS -> Location Controls -> Logitude, Latitude 값 입력 후 send
	  
	   [참고] 현재 Android emulator는 API Level 10(Gingerbread)의 경우 GPS 기능을 지원하지 않습니다. 

	          따라서 emulator 생성시 target API Level을 15(ICS)로 설정하시기 바랍니다.



vi. Sample Project에 대한 문의처

	궁금하신 사항은 아래 Planet X 개발자 센터의 [ 문의하기 ] 메뉴를 통해 문의해 주십시요
   
	(https://developers.skplanetx.com/community/contact-us/enrollment)