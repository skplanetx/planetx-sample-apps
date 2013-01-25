//
//  TMapGpsManager.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 19..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "TMapPoint.h"

@protocol TMapGpsManagerDelegate <NSObject>
- (void)locationChanged:(TMapPoint*)newTmp;
- (void)headingChanged:(double)heading;
@end

@interface TMapGpsManager : NSObject <CLLocationManagerDelegate>
{
    id<TMapGpsManagerDelegate> delegate;
    CLLocationManager *locMgr;
    
    TMapPoint* curTMP;
}

@property (nonatomic, assign) id<TMapGpsManagerDelegate> delegate;

- (void)openGps;
- (void)closeGps;
- (void)setMinTime:(int)mintime;
- (int)getMinTime;
- (void)setMinDistance:(int)mindistance;
- (int)getMinDistance;
- (TMapPoint*)getLocation;
- (int)getSatellite;
- (void) setProviderType:(NSString *)type;
- (NSString *)getProvider;

- (void)startHeading;
- (void)stopHeading;

+ (double)getAccuracy;

@end
