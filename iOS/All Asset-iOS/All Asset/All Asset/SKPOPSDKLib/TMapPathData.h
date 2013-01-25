//
//  TMapPathData.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 19..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import "TMapPoint.h"
#import "TMapPolyLine.h"

@interface TMapPathData : NSObject

@property (nonatomic, readonly) NSString* errorMessage;

- (NSArray*)requestFindAllPOI:(NSString *)keyword;
- (NSArray*)requestFindTitlePOI:(NSString*)keyword;
- (NSArray*)requestFindAddressPOI:(NSString*)keyword;
- (NSArray*)requestFindGeoPOI:(TMapPoint*)point LCode:(NSString*)classLCode MCode:(NSString*)classMCode;

- (TMapPolyLine *)findPathDataFrom:(TMapPoint*)startPoint to:(TMapPoint*)endPoint;
- (NSArray *)getBizCategory;
- (NSString*)convertGpsToAddressAt:(TMapPoint*)tmp;

- (NSInteger)validateApiKey:(NSString*)appID;

@end
