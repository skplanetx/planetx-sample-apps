//
//  TMapObject.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 5..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import "TMapAnnotation.h"

@interface TMapObject : NSObject
{
    CLLocationCoordinate2D  _coordinate;
    NSString*   _objectID;
    TMapAnnotation* _annotation;
}
@property (nonatomic, assign) TMapAnnotation* annotation;

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;

@property (nonatomic, copy) NSString* objectID;

- (void)setID:(NSString*)_ID;

- (NSString*)getID;

@end
