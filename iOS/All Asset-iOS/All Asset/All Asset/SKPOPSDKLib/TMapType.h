//
//  TMapType.h
//  SKP-Sample
//
//  Created by Heung-Guk Kim on 12. 9. 21..
//  Copyright (c) 2012년 LBC. All rights reserved.
//
#import <CoreLocation/CoreLocation.h>
typedef enum {
    UNKNOWNTILE,
    NORMALTILE,		// 일반타일 (256x256)
    EXTENSIONTILE,	// 일반타일 확대
    HDTILE          // HD타일 (512x512 이미지)
} TMapTileType;


typedef enum {
    KOREAN,
    ENGLISH,
    CHINESS
} TMapViewLanguage;

typedef enum {
    STANDARD,
    SATELLITE,
    HYBRID,
    TRAFFIC
} TMapViewType;

typedef enum {
    POSITION_DEFAULT,
    POSITION_NAVI
} TMapViewPositionType;
/////////////////////////////////////////////////

typedef enum  {
    TMapUserTrackingModeNone              = 0,
    TMapUserTrackingModeFollow            = 1,
    TMapUserTrackingModeFollowWithHeading = 2
} TMapUserTrackingMode;


typedef struct {
	double latitude;
	double longitude;
} STMapCoordinate;

typedef struct {
    double x;
    double y;
} STMapPoint;

typedef struct {
    double width;
    double height;
} STMapSize;

typedef struct {
    STMapPoint origin;
    STMapSize  size;
} STMapRect;

typedef double TMapDistance;


extern const STMapSize STMapSizeWorld;

extern const STMapRect STMapRectWorld;

/////

STMapCoordinate STMapCoordinateMake (double latitude, double longitude);

STMapSize STMapSizeMake (double width, double height);

STMapPoint STMapPointMake (double  x, double y);

BOOL TMapCordinateEqualToCoordinate (STMapCoordinate coordinate1, STMapCoordinate coordinate2);

BOOL CordinateEqualToCoordinate (CLLocationCoordinate2D coordinate1, CLLocationCoordinate2D coordinate2);

#pragma mark -
STMapPoint mapPointFromCoordinate (CLLocationCoordinate2D coordinate);

CLLocationCoordinate2D coordinateFromMapPoint (STMapPoint point);

// Scale <---> ZoomLevel
NSUInteger TMapZoomLevelFromScale (NSUInteger scale);
NSUInteger TMapScaleFromZoomLevel (NSUInteger zoomLevel);


