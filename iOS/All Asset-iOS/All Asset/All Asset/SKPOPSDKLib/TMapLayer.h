//
//  TMapLayer.h
//  SKP-Sample
//
//  Created by Heung-Guk Kim on 12. 9. 25..
//  Copyright (c) 2012년 LBC. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <QuartzCore/QuartzCore.h>

#import "TMapType.h"

@class TMapAnnotation;

@interface TMapLayer : CAScrollLayer
{
    TMapAnnotation* _annotation;
    
    STMapPoint       _mapPoint;
}

@property (nonatomic, assign) TMapAnnotation *annotation;

@property (nonatomic, assign) STMapPoint mapPoint;

+ (UIFont *)defaultFont;

- (void)setPosition:(CGPoint)position animated:(BOOL)animated;

@end
