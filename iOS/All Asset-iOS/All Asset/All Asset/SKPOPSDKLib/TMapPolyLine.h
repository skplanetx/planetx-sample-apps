//
//  TMapPolyLine.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 5..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import <CoreGraphics/CGColor.h>
#import "TMapPoint.h"
#import "TMapObject.h"

@interface TMapPolyLine : TMapObject 
{
    UIColor*    _lineColor;
    CGFloat     _lineWidth;
    
    NSMutableArray*     _points;
}

@property (nonatomic, retain) UIColor*  lineColor_;

@property (nonatomic, assign) CGFloat   lineWidth;

@property (nonatomic, readonly) NSMutableArray* points;

- (void)setLineColor:(CGColorRef)_color;
- (CGColorRef)getLineColor;
- (void)setLineWidth:(float)_width;
- (float)getLineWidth;
- (void)addLinePoint:(TMapPoint *)point;
- (NSArray *)getLinePoint;
- (double) getDistance;

 // Dash pattern
- (void)setLineDashPattern:(NSArray*)lineDashParttern;  // NSNumber의 배열
- (NSArray *)getLineDashPattern;


@end
