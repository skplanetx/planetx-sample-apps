i. 개요 - "Photo Multi Posting"
	
	1. 명칭 : 멀티포토포스팅 sample application
	
	2. 설명 : T cloud 및 단말 갤러리의 이미지를 멀티로 SNS에 공유할 수 있는 서비스

	3. API 활용 : T cloud, Social Component API



ii.Sample Project 파일 구성
	
	1. /OpenPlatform_MPP의 AndroidManifest.xml 파일 : Planet X의 라이브러리를 사용할 때 꼭 설정해야 할 

	   permission과 Activity가 선언되어 있습니다.

	2. /OpenPlatform_MPP/libs : 라이브러리 폴더

		- SKPOP_SDK.jar : Planet X server 연동

		- SKPOP_SDK_OAuth.jar : OneID 인증

		- twitter4j-core-2.1.3-SNAPSHOT.jar : Twitter 연동

	3. /OpenPlatform_MPP/src/com/skp/opx/core/client : 프로젝트 실행에 필요한 OAuth Key, SNS key, 각 API의 

	   URI 및 프로젝트 설정을 정의

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

	   3) 앱을 등록할 때 [Select Service] 에서 "SK Planet One ID", "T cloud", "소셜 컴포넌트"를 꼭 선택해야 합니다.
              
	      이는 앱이 사용하는 API가 속한 서비스별로 사용 승인을 득해야 하기 때문입니다.                   
	 
	4. 샘플 테스트를 위해서는 각 소셜별(Facebook, Twitter) 사용자 가입이 선행되어야 합니다. 
 


iv. 환경 설정

	1. Android SDK API level 10 이상 설치

	2. 단말 및 emulator의 앱실행 버전 Gingerbread 2.3 이상 (버전별 에뮬레이터 버그문제로 인하여 아이스크림샌드위치 에뮬레이터를 권장합니다.)

	3. 다운받은 샘플 프로젝트의 압축을 푼 후 OpenPlatform_MPP 폴더를 eclipse에서 import.

	4. OpenPlatform_MPP/src/com/skp/opx/core/client/Define.java의 OAuth class에 개발자 센터에서 앱 등록 후 발급받은 Client ID, 

	   Client secret, App key를 입력하고 저장하셔야 합니다.

	5. 각 SNS 별 앱등록 및 설정
	 
	   1) 각 SNS 사이트 별 앱등록 시, 패키지명의 중복을 방지하기 위하여 패키지명을 변경합니다.

	        : eclipse 실행 > Package Explore > Android Tools > Rename Application Package > 패키지명 변경

	   2) 각 SNS(Facebook, Twitter)별로 개발자센터 사이트에 앱을 등록하고 인증키를 발급 받습니다.

	      이 작업은 Social Component를 사용하기 위해서는 필히 하셔야 하며, 인증키가 있어야 각 SNS에 접근할 수 있습니다.

	      자세한 내용은 "https://developers.skplanetx.com/apidoc/kor/social" 소개 페이지를 참조하시기 바랍니다.
	
	              - Facebook : Facebook AppKey 발급받기 항목 

		- Twitter : twitter AppKey 발급받기 항목  
	  
	   3) 각 SNS 사이트에서 발급받은 key 및 ID 정보를 개발자 센터 (https://developers.skplanetx.com)에 등록합니다.

		: [ 내 정보(my-center) > 앱 정보(app-station) > 소셜 프로바이더 관리(social-provider) ] 에서 해당 앱을 클릭해서 각 SNS에서 

		  발급받은 key 및 ID 를 입력하세요.

	   4) 발급받은 key 및 ID 정보를 앱 소스에 저장해야 합니다.

		: OpenPlatform_MPP/src/com/skp/opx/core/client/Define.java의 Twitter Key class 및 Facebook Key class에 입력하세요.

 		  /** Twitter callback uri, consumer key, consume secret key */
 		  public static final class Twitter_Key{
 		  	public static final String TWITTER_LINK_ID    	    = "< your key here >";
  			public static final String TWITTER_CALLBACK_URL     = "< your key here >";
  			public static final String TWITTER_CONSUMER_KEY     = "< your key here >";
  			public static final String TWITTER_CONSUMER_SECRET  = "< your key here >";
 		  }
 
 		  /** Facebook App ID & Auth code */
 		  public static final class Facebook_Key {
  		  	public static final String FACEBOOK_APP_ID   = "< your key here >";
 		  }
		  


vi. Sample Project에 대한 문의처

	궁금하신 사항은 아래 Planet X 개발자 센터의 [ 문의하기 ] 메뉴를 통해 문의해 주십시요
   
	(https://developers.skplanetx.com/community/contact-us/enrollment)