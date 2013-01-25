i. Introduction - "Location Tracker"
	
	1. Title : Location Tracker (for android)
	
	2. Description : This service enables users to search path and receive guidance to destination.

		          It also provides service of sending user's location by sms or instant message to NateOn buddies in real time
              
    	3. API Usage : NateOn, T map API



ii.Sample Project Configuration
	
	1. AndroidManifest.xml file in /OpenPlatform_RPN  : It declared permission and Activity for property whenever you use Planet X library.

	2. libs folder in /OpenPlatform_RPN/libs

		- SKPOP_SDK.jar : Planet X server interface

		- SKPOP_SDK_OAuth.jar : OneID certification
		
		- TmapSDK.1.0.0.jar : Tmap static library

	3. Defines.java in /OpenPlatform_RPN/src/com/skp/opx/core/client : It defined OAuth Key, each API's URI 

	   and project configuration.

	   	- Must redefine below content for running this application in Define.java

		  /** Authentication */
 		  public final class OAuth {
  		  	public static final String CLIENT_ID     = "< your key here >";
 			public static final String SECRET        = "< your key here >";
  			public static final String KEY           = "< your key here >";
  		  }
 


iii. Prerequisite
	
	1. Register at One ID or Switch to One ID. 
                 
		: Must register for One ID(https://oneid.skplanet.co.kr), thereby using this application.

	2. Register at developer's center
	
		: Must acquire AppKey issued by the Planet X, thereby running this application.
                    
		  Therefore must register at https://developers.skplanetx.com. If you already have it, agree user agreement. 

	3. Register application

	   1) In the "My Center: App Station", register application.

	   2) Acquire API Authentication Key. 
                    
		- It used for authentication Planet X API call.
                 
		- It consists of App Key, Client ID and Client secret, App key. These values applied for below iv. Configuration section.

	   3) Must select "SK Planet One ID","T map" and "NateOn" service area, thereby using this application.
          	 


iv. Configuration

	1. Building Environment: Above Android SDK API level 10

	2. Running Environment: Above Android OS 2.3(Codename: Gingerbread 2.3) (We recommend emulator version of ICS because of some bug issues.) 

	3. Extract ZIP file. Next import at Eclipse's File munu.

	4. Edit API Authentication Key value at Define.java in OpenPlatform_RPN/src/com/skp/opx/core/client/

	¡Ø Testing using emulator

	   Must set GPS information, thereby using T map API 

           : DDMS -> Location Controls -> Edit Logitude and Latitude and send

	   [Ref] AVD with API level 10 as target(Gingerbread) doesn't support GPS function.
		
	         Please create AVD with API level 15 as target. 



vi. Contact us for Sample Project

	Ask any questions for detail, "Community: Contact Us"(https://developers.skplanetx.com/community/contact-us/enrollment)
