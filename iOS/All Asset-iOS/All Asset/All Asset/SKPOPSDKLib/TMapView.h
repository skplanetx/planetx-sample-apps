//
//  TMapView.h
//  SKP-Sample
//
//  Created by Heung-Guk Kim on 12. 10. 6..
//  Copyright (c) 2012년 LBC. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "TMapBaseView.h"
#import "TMap.h"

@protocol TMapViewDelegate <NSObject>
@optional
- (void)onClick:(TMapPoint*)TMP;
- (void)onCustomObjectClick:(TMapObject*)obj;
- (void)onCustomObjectClick:(TMapObject*)obj screenPoint:(CGPoint)point;
- (void)onLongClick:(TMapPoint*)TMP;
- (void)onCustomObjectLongClick:(TMapObject*)obj;
- (void)onCustomObjectLongClick:(TMapObject*)obj screenPoint:(CGPoint)point;
- (void)onDidScrollWithZoomLevel:(NSInteger)zoomLevel centerPoint:(TMapPoint*)mapPoint;
- (void)onDidEndScrollWithZoomLevel:(NSInteger)zoomLevel centerPoint:(TMapPoint*)mapPoint;
- (void)onUserLocationClick:(TMapPoint*)TMP;
- (void)onUserLocationLongClick:(TMapPoint*)TMP;
@end


@interface TMapView : TMapBaseView

@property (nonatomic, assign) id <TMapViewDelegate> delegate;
@property (nonatomic, assign) id <TMapGpsManagerDelegate> gpsManagersDelegate;
@property (nonatomic, readonly) NSString* version;

// ApiKey
- (void)setSKPMapApiKey:(NSString*)key;

// Map Type
- (void)setLanguage:(TMapViewLanguage)language;
- (void)setMapType:(TMapViewType)type;
- (TMapViewType)getMapType;

// Map Coordinate
- (void)setCenterPoint:(TMapPoint*)tmp;
- (TMapPoint*)getCenterPoint;
- (void)setLocationPoint:(TMapPoint*)tmp;
- (TMapPoint*)getLocationPoint;

// UserLocation 이미지 설정
- (void)setIcon:(UIImage *)icon;
- (void)setIconVisibility:(BOOL)visible;

// Zoom
- (void)setZoomLevel:(NSInteger)level;
- (NSInteger)getZoomLevel;

- (void)zoomOut;
- (void)zoomIn;
- (bool)zoomEnable;

//
- (void)setCompassMode:(BOOL)compassMode;
- (BOOL)getIsCompass;

- (void)setSightVisible:(BOOL)flag;
- (BOOL)getSightVisible;

- (void)setTrackingMode:(BOOL)trackingMode;
- (BOOL)getIsTracking;

// Overlays
- (void)addTMapCircleID:(NSString *)circleID Circle:(TMapCircle *)circle;
- (void)removeTMapCircleID:(NSString *)circleID;

- (void)addTMapPolygonID:(NSString *)polygonID Polygon:(TMapPolygon *)polygon;
- (void)removeTMapPolygonID:(NSString *)polygonID;

- (void)addTMapPolyLineID:(NSString *)polyLineID Line:(TMapPolyLine *)line;
- (void)removeTMapPolyLineID:(NSString *)polyLineID;

- (void)addTMapMarkerItemID:(NSString *)markerID Marker:(TMapMarkerItem *)marker;
- (void)removeTMapMarkerItemID:(NSString *)markerID;

- (void)addTMapPOIItemID:(NSString *)poiID Poi:(TMapPOIItem *)poiitem;
- (void)removeTMapPOIItemID:(NSString *)poiID;

- (TMapCircle *)getCircleFromID:(NSString *)circleID;
- (TMapPolyLine *)getPolyLineFromID:(NSString *)polyLineID;
- (TMapPolygon *)getPolygonFromID:(NSString *)polygonID;
- (TMapMarkerItem *)getMarketItemFromID:(NSString *)markerID;

//
- (void)setMapPositionType:(TMapViewPositionType)type;
- (TMapViewPositionType)getMapPositionType;

- (TMapPoint *)convertPointToGpsX:(float)x andY:(float)y;

// 경로 찾기 RP
- (void)addTMapPath:(TMapPolyLine *)polyline;
- (void)removeTMapPath;
- (void)setTMapPathIconStart:(TMapMarkerItem *)start End:(TMapMarkerItem *)end;

- (void)setTrafficInfo:(BOOL)flag;
- (BOOL)isTrafficInfo;

- (void)addCustomObject:(TMapObject*)customObject ID:(NSString*)_id;
- (void)clearCustomObjects;
- (void)removeCustomObject:(NSString*)_id;

- (void)showFullPath:(NSArray*)objs;
- (void)resizeWidthFrame:(CGRect)frame;

- (BOOL)isValidTMapPoint:(TMapPoint*)tmp;

//NORMALTILE,		// 일반타일 (256x256)
//EXTENSIONTILE,	// 일반타일 확대
//HDTILE          // HD타일 (512x512)
- (void)setTMapTileType:(TMapTileType)tileType;
- (TMapTileType)tmapTileType;

@end
