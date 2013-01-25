i. Introduction - "All Asset"
	
	1. Title : All Asset (for iOS)
	
	2. Description : This Service provides interface to take advantage of T map, Melon, NateOn, Cyworld and 11 Street API of SK Planet
              
    	3. API Usage : NateOn, Melon, Cyworld, T map, 11st API



ii.Sample Project Configuration
	
	1. All Asset-Info.plist file in /All\Asset   : It declared project property information.

	2. libs folder in /All\Asset/SKP_API/SKPOPSDKLib

		- libSKPOP_SDK_OAUTH_v1.4.a : OneID certification (This static library is iOS fat-library)

		- libSKPOP_SDK_v1.4.a : Planet X server interface (This static library is iOS fat-library)

        - making a static fat-libary : $ lipo -create libSKPOP_SDK_OAUTH_v1.4_sim.a libSKPOP_SDK_OAUTH_v1.4_dev.a -output libSKPOP_SDK_OAUTH_v1.4.a
    
        - libskpMap_OP-iOS6.0.a : Tmap static library

    3. /All\Asset/SKP_API/SKPOPSDKLib/JSONKit : JSON Util Classes for SDK libary
        
        - JSONKit.h : JSON Util Header file

        - JSONKit.m : JSON Util Source file

    4. /All\Asset/SKP_API/SKPOPSDKLib/Tmap : Tmap Header file
        
        - TMapView.h : View header file for rendering map

	5. Defines.h in /All\Asset/SKP_API/Common : It defined OAuth Key, each API's URI

	   and project configuration.

	    	- Must redefine below content for running this application in Define.h

		/* App Info */
        #define MY_CLIENT_ID                @"< your key here >"
        #define MY_SECRET                   @"< your key here >"
        #define MY_APP_KEY                  @"< your key here >"
        #define MY_SCOPE                    @"< your key here >"
 
    6. plist files in /All\Asset/Supporting Files : It contains plist files for All Asset. 
        
        - TmapRecent.plist : Recent POI information.
        
        - Configuration.plist : MainView configuration information.
        
        - ElevenstCategory.plist : Recent 11st category search information. 

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

	   3) Must select "SK Planet One ID", "Social component", "NateOn", "Cyworld", "Melon", "T map", "11st" service area, 

	      thereby using this application.

       4) Use your App key for TMap API key

         (ex)
         TMapView *mapView = [[TMapView alloc] initWithFrame:CGRectMake(0, 147, 320, 313)];
         [mapView setSKPMapApiKey:@"<your key>"];
         
iv. Configuration

	1. Building Environment: Above iOS SDK 6.0

	2. Running Environment: Above iOS 6.0 

	3. Extract ZIP file. Click All Asset.xcodeproj for operating Xcode.

	4. Edit API Authentication Key value at Define.java in /All\Asset/SKP_API/Common



vi. Contact us for Sample Project

	Ask any questions for detail, "Community: Contact Us"(https://developers.skplanetx.com/community/contact-us/enrollment)

 