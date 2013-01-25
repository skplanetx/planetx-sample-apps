//
//  ApiUtil.h
//  SK Planet Service
//
//  Created by Jason Nam on 12. 10. 18..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SKPOP_v1.4.h"

@interface ApiUtil : NSObject

+ (NSMutableDictionary *)getAuthParams;
+ (RequestBundle *)initRequestBundle:(NSDictionary *)header url:(NSString *)url params:(NSMutableDictionary *)param payload:(NSString *)payload uploadFilePath:(NSString *)uploadFilePath httpMethod:(SKPopHttpMethod)httpMethod requestType:(SKPopContentType)requestType responseType:(SKPopContentType)responseType;
+ (void)requestAPI:(id)target finished:(SEL)finishSel failed:(SEL)failSel bundle:(RequestBundle *)bundle;

+ (NSString *)makeReceivers:(NSMutableArray *)dictionary;
+ (NSString *)makeMessage:(const char *)message;
+ (NSMutableArray *)getBuddyGroupArray:(NSMutableArray *)buddyArray;
+ (NSString *)getRequestURL:(NSString *)host index:(NSUInteger)reqType;
+ (NSString *)makeWebPageURL:(NSString *)serviceType contentId:(NSString *)contentId menuId:(NSString *)menuId;
+ (BOOL)isElevenstCategorySaved;
+ (NSString *)loadSelectedElevenstCategory;
+ (BOOL)saveElevenstCategory:(NSString *)categoryCode categoryName:(NSString *)categoryName;
+ (void)errorAlert:(NSDictionary *)dic;
+ (NSString *)makeTurnType:(int)turnType;
+ (NSMutableArray *)arrangeTempList:(NSMutableArray *)tempListArray;
@end
