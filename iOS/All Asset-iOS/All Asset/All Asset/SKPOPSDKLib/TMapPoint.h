//
//  TMapPoint.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 5..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//


#import <CoreLocation/CoreLocation.h>

@interface TMapPoint : NSObject

// Ver 0.1.0 (2012.10.08) 추가
@property (nonatomic, assign) CLLocationCoordinate2D coordinate;

+ (id)mapPointWithCoordinate:(CLLocationCoordinate2D)coordinate;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate;

- (id)initWithLon:(double)lon Lat:(double)lat;

- (void)setLatitude:(double)lat;
- (void)setLongitude:(double)lon;
- (double)getLatitude;
- (double)getLongitude;

- (NSString *)toString;
- (double)getDistanceWith:(TMapPoint*)compareTmp;

- (CGPoint)getKatec;
- (double)getKatechLat;
- (double)getKatechLon;

- (bool)equalWith:(TMapPoint*)p;

@end


