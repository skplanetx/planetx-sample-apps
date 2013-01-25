//
//  TMapAnnotation.h
//  SKP-Sample
//
//  Created by Heung-Guk Kim on 12. 9. 25..
//  Copyright (c) 2012년 LBC. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "TMapType.h"

@class TMapBaseView, TMapLayer;

@interface TMapAnnotation : NSObject
{
    TMapLayer*      _layer;
    CLLocationCoordinate2D  _coordinate;
    STMapRect        _boundingRect;
    STMapPoint       _mapPoint;
    CGPoint         _position;
    
    UIImage*        _icon;
    CGPoint         _anchorPoint;
    TMapBaseView*   _mapView;
    NSString*       _title;
    
    //
    NSString*       _annotationType;
}

@property (nonatomic, retain)   TMapLayer* layer;

@property (nonatomic, assign)   CLLocationCoordinate2D  coordinate;

@property (nonatomic, assign)   STMapRect boundingRect;

@property (nonatomic, readonly) STMapPoint mapPoint;

@property (nonatomic, assign)   CGPoint position;

@property (nonatomic, retain)   UIImage* icon;

@property (nonatomic, assign)   CGPoint anchorPoint;

@property (nonatomic, retain)   TMapBaseView* mapView;

@property (nonatomic, retain)   NSString* annotationType;

@property (nonatomic, assign)   BOOL isUserLocationAnnotation;

@property (nonatomic, copy)     NSString* title;

+ (id)annotationWithMapView:(TMapBaseView *)mapView coordinate:(CLLocationCoordinate2D)coordinate andTitle:(NSString *)title;

- (id)initWithMapView:(TMapBaseView *)mapView coordinate:(CLLocationCoordinate2D)coordinate andTitle:(NSString *)title;

- (void)setPosition:(CGPoint)position animated:(BOOL)animated;
@end
