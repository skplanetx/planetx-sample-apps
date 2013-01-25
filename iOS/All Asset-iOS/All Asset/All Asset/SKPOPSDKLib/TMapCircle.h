//
//  TMapCircle.h
//  SKP-Sample
//
//  Created by Heung-Guk Kim on 12. 9. 25..
//  Copyright (c) 2012년 LBC. All rights reserved.
//

#import "TMapPoint.h"
#import "TMapObject.h"

@interface TMapCircle : TMapObject
{
    UIColor*    _lineColor;
    UIColor*    _fillColor;
    CGFloat     _lineWidth;
    
    CGFloat _radius;
    BOOL    _radiusHidden;
}

@property (nonatomic, retain) UIColor* lineColor;

@property (nonatomic, retain) UIColor* fillColor;

@property (nonatomic, assign) CGFloat lineWidth;

@property (nonatomic, assign) CGFloat radius;

@property (nonatomic, readonly) BOOL radiusHidden;

#pragma mark - API methods

// 추가
- (id)initWithTMapPoint:(TMapPoint*)mapPoint;
- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate;

// 기존
- (void)setCenterPoint:(TMapPoint *)_point;
- (TMapPoint *)getCenterPoint;
- (void)setCircleRadius:(int)_radius;
- (int)getCircleRadius;
- (void)setCircleAreaColor:(CGColorRef)_color;
- (CGColorRef)getCircleAreaColor;
- (void)setCircleLineWidth:(float)_width;
- (float)getCircleLineWidth;
- (void)setCircleLineColor:(CGColorRef)_color;
- (CGColorRef)getCircleLineColor;

- (void)setCircleAreaAlpha:(int)alpha;
- (int)getCircleAreaAlpha;
- (void)setCircleLineAlpha:(int)alpha;
- (int)getCircleLineAlpha;
- (void)setRadiusVisible:(bool)flag;

@end
