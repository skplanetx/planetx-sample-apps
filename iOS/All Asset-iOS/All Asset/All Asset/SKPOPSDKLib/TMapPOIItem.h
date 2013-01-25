//
//  TMapPOIItem.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 17..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import <CoreGraphics/CGColor.h>
#import "TMapPoint.h"
#import "TMapObject.h"
#import "TMapMarkerItem.h"

@interface TMapPOIItem : TMapMarkerItem
{
    NSString*   _address;
    NSString*   _content;
}

@property (nonatomic, copy) NSString* address;

@property (nonatomic, copy) NSString* content;

- (id)initWithPOI:(NSDictionary*)poi;

- (void)setPOI:(NSDictionary*)poi;

- (NSString*)getPOIID;

- (NSString*)getPOIName;

- (TMapPoint*)getPOIPoint;

- (NSString*)getPOIAddress;

- (NSString*)getPOIContent;

- (double)getDistance:(TMapPoint*)compareTmp;

@end
