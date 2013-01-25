//
//  TMapMarkerItem.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 5..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TMapPoint.h"
#import "TMapObject.h"

typedef enum {
    GONE,           // 화면에 보이지 않음
    HIDDEN,         // 영역을 차지하고 있으나 화면에 보이지않음
    VISIBLE          // 화면에 보임
} TMapMarkerItemType;

@interface TMapMarkerItem : TMapObject
{
    UIImage*    _image;
    NSString*   _markerTitle;
    int         _visibleMode;
    CGPoint    _anchorPoint;
}

@property (nonatomic, retain) UIImage* image;

@property (nonatomic, copy) NSString* markerTitle;

@property (nonatomic, assign) int visibleMode;

@property (nonatomic, assign) CGPoint anchorPoint;


- (id)initWithTMapPoint:(TMapPoint *)_point;

- (void)setTMapPoint:(TMapPoint *)_point;

- (TMapPoint*)getTMapPoint;

// 마커 타이틀
- (void)setName:(NSString*)_name;

- (NSString*)getName;

- (void)setVisible:(int)visible;

- (int)getVisible;

- (void)setIcon:(UIImage *)icon;    // anchorPoint (0.5, 0.5)

// anchorPoint {0.0, 0.0} 에서  {1.0, 1.0} 사이값 default:중점 {0.5, 0.5}
- (void)setIcon:(UIImage *)icon anchorPoint:(CGPoint)anchorPoint;

- (UIImage*)getIcon;

//
- (NSString*)getID;



@end
 