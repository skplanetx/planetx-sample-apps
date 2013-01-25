//
//  TMapBaseView.h
//  Sample
//
//  Created by Heung-Guk Kim on 12. 9. 19..
//
//

#import <UIKit/UIKit.h>

#import "TMapType.h"

#define kRMUserLocationAnnotationTypeName   @"RMUserLocationAnnotation"
#define kRMTrackingHaloAnnotationTypeName   @"RMTrackingHaloAnnotation"
#define kRMAccuracyCircleAnnotationTypeName @"RMAccuracyCircleAnnotation"

@interface TMapBaseViewInternal : NSObject {
}

@end

@class TMapAnnotation, TMapUserLocation;
@protocol UserLocationProtectedMethods <NSObject>
- (void)setUserLocationImage:(UIImage*)iamge;
- (void)setUserLocationTraking:(BOOL)tracking;
- (void)changeUserLocation:(CLLocation*)location;
- (CLLocationCoordinate2D)coordinateAtPoint:(CGPoint)point;
@end

@protocol TMapTileSource;

@interface TMapBaseView : UIView <UserLocationProtectedMethods>
{
    BOOL    _compassMode;
    // Navi or Normal
    TMapViewPositionType _centerPointType;
    
@private
    //TMapBaseViewInternal *_internal;
}

@property (nonatomic, readonly) double metersPerPixel;

@property (nonatomic, assign) NSUInteger boundingMask;

@property (nonatomic, assign)  TMapUserTrackingMode userTrackingMode;

@property (nonatomic, assign)   BOOL showsUserLocation;

@property (nonatomic, retain) TMapUserLocation *userLocation;

@property (nonatomic, assign) NSInteger zoomLevel;

@property (nonatomic, assign) BOOL  trafficMode;

@property (nonatomic, assign) id<TMapTileSource> tileSource;


- (void)setZoomLevel:(NSInteger)level;

- (void)setZoomLevel:(NSInteger)level animated:(BOOL)animated;

- (void)setCenter:(CLLocationCoordinate2D)coord;

- (void)setCenterCoordinate:(CLLocationCoordinate2D)coordinate;

- (void)setCenterCoordinate:(CLLocationCoordinate2D)coordinate animated:(BOOL)animated;

- (CLLocationCoordinate2D)centerCoordinate;

//- (void)changeScrollPoint;

// annotations
- (void)addAnnotation:(TMapAnnotation*)annotation;

- (void)removeAnnotation:(TMapAnnotation *)annotation;

- (void)removeAnnotations:(NSArray *)annotations;

- (void)removeAllAnnotations;

- (void)removeAnotationWithTitle:(NSString*)title;

- (CGPoint)screenPointFromCoordinate:(CLLocationCoordinate2D)coordinate;

- (CGPoint)screenPointFromMapPoint:(STMapPoint)mapPoint;

- (STMapPoint)mapPointFromScreenPoint:(CGPoint)screenPoint;

// location
- (void)startUserLocation;

// gesture
- (void)onClick:(CLLocationCoordinate2D)coordinate;
- (void)onLongClick:(CLLocationCoordinate2D)coordinate;

- (void)onCustomObjectClick:(TMapAnnotation*)annotation;
- (void)onCustomObjectLongClick:(TMapAnnotation*)annotation;

- (void)onDidScroll:(CLLocationCoordinate2D)coordinate zoomLevel:(NSInteger)zoomLevel;
- (void)onDidEndScroll:(CLLocationCoordinate2D)coordinate zoomLevel:(NSInteger)zoomLevel;

- (void)onUserLocationClick:(CLLocationCoordinate2D)coordinate;
- (void)onUserLocationLongClick:(CLLocationCoordinate2D)coordinate;

// gps
- (void)gpsLocationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;
- (void)gpsLocationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading;

@end


