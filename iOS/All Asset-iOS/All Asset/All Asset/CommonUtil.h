//
//  CommonUtil.h
//  SK Planet Service
//
//  Created by Jason Nam on 12. 10. 25..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSXMLElement.h"
@interface CommonUtil : NSObject

+(float)systemVersion;
+ (void)commonAlertView:(NSString*)alertMessage;
+ (void)commonCustomAlertView:(NSString*)alertMessage
                CancelMessage:(NSString*)cancelTitle
                    OkMessage:(NSString*)okTitle
                          Tag:(NSInteger)tag
                     delegate:(id)delegate;
+ (void)commonStartActivityCenterIndicator:(UIActivityIndicatorViewStyle)style;
+ (void)commonStopActivityIndicator;
+ (NSString *)getPlistPath:(NSString *)plistName;
+ (void)savePlist:(NSDictionary *)dic plist:(NSString *)plistName;
+ (void)saveTmapPlist:(NSMutableArray *)array;
+ (NSString*)getXMLElement:(NSString*)name document:(NSXMLElement*)document;
+ (void)launchBrowser:(NSString *)url;
@end
