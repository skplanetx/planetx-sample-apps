//
//  StringUtil.h
//  SKPOPSDKDev
//
//  Created by Lion User on 29/07/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StringUtil : NSObject

+(NSString *)getValueFromQueryString:(NSString *)str Key:(NSString *)key;
+(NSString *)getQueryStringFromDictionary:(NSDictionary *)dict;

@end
