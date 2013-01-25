//
//  TMapPolygon.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 5..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreGraphics/CGColor.h>

#import "TMapPoint.h"
#import "TMapObject.h"

@interface TMapPolygon : TMapObject 
{
    UIColor*    _lineColor;
    UIColor*    _fillColor;
    CGFloat     _lineWidth;
    
    NSMutableArray*     _points;
}

@property (nonatomic, retain) UIColor*  lineColor;
@property (nonatomic, retain) UIColor*  fillColor;
@property (nonatomic, assign) CGFloat   lineWidth;
@property (nonatomic, readonly) NSMutableArray* points;

- (void)setPolygonAreaColor:(CGColorRef)_color;
- (CGColorRef)getPolygonAreaColor;

- (void)setPolygonLineWidth:(float)_width;
- (float)getPolygonLineWidth;

- (void)addPolygonPoint:(TMapPoint *)point;
- (NSArray *)getPolygonPoint;

- (void)setPolygonLineColor:(CGColorRef)_color;
- (CGColorRef)getPolygonLineColor;

- (void)setPolygonAlpha:(int)alpha;
- (int)getPolygonAlpha;

- (void)setPolygonLineAlpha:(int)alpha;
- (int)getPolygonLineAlpha;

- (double)getPolygonArea;

@end
