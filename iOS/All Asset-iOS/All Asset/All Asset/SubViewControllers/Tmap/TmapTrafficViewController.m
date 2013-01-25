//
//  TmapTrafficViewController.m
//  All Asset
//
//  Created by Jason Nam on 12. 12. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "TmapTrafficViewController.h"
#import "Defines.h"

@interface TmapTrafficViewController ()

@end

@implementation TmapTrafficViewController
@synthesize mapView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)viewWillAppear:(BOOL)animated
{
    [self initTMap];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - TMapViewDelegate
- (void)onClick:(TMapPoint *)TMP
{
    NSLog(@"onClick: %@", TMP);
}

- (void)onLongClick:(TMapPoint*)TMP
{
    NSLog(@"onLongClick: %@", TMP);
}

- (void)onCustomObjectClick:(TMapObject*)obj
{
    if([obj isMemberOfClass:[TMapMarkerItem class]])
    {
        NSLog(@"TMapMarkerItem clicked:%@", obj);
        
        TMapMarkerItem* markerItem = (TMapMarkerItem*)obj;
        [markerItem setIcon:[UIImage imageNamed:@"end.png"]];
    }
}

- (void)onCustomObjectLongClick:(TMapObject*)obj
{
    if([obj isMemberOfClass:[TMapMarkerItem class]])
    {
        NSLog(@"TMapMarkerItem clicked:%@", obj);
        
    }
}
- (void)onCustomObjectClick:(TMapObject*)obj screenPoint:(CGPoint)point
{
    if([obj isMemberOfClass:[TMapMarkerItem class]])
    {
        NSLog(@"onCustomObjectClick :%@ screenPoint:{%f, %f}", obj, point.x, point.y);
    }
}

- (void)onCustomObjectLongClick:(TMapObject*)obj screenPoint:(CGPoint)point
{
    if([obj isMemberOfClass:[TMapMarkerItem class]])
    {
        NSLog(@"onCustomObjectLongClick :%@ screenPoint:{%f, %f}", obj, point.x, point.y);
    }
}

- (void)onDidScrollWithZoomLevel:(NSInteger)zoomLevel centerPoint:(TMapPoint*)mapPoint
{
    //NSLog(@"zoomLevel: %d point: %@", zoomLevel, mapPoint);
}

- (void)onDidEndScrollWithZoomLevel:(NSInteger)zoomLevel centerPoint:(TMapPoint*)mapPoint
{
    //    NSLog(@"zoomLevel: %d point: %@", zoomLevel, mapPoint);
    //    NSLog(@"trackingMode %d", [_mapView getIsTracking]);
}

#pragma mark - TMapGpsManagerDelegate
- (void)locationChanged:(TMapPoint*)newTmp
{
//    NSLog(@"%@", newTmp);
}

- (void)headingChanged:(double)heading
{
    //NSLog(@"%f", heading);
}

#pragma mark TMapView methods
- (void)initTMap
{
    mapView = [[TMapView alloc] initWithFrame:CGRectMake(0, 44, 320, 416)];
    TMapPoint *point = [[TMapPoint alloc] initWithLon:127.55963904390588 Lat:36.411576147592186];
    //API Set
    [mapView setSKPMapApiKey:MY_APP_KEY];
    [mapView setLanguage:KOREAN];
    [mapView setZoomLevel:6];
    [mapView setMapType:TRAFFIC];
    [mapView setTrafficMode:YES];
    [mapView setCenterPoint:point];
    [mapView setDelegate:self];
    [mapView setGpsManagersDelegate:self];
    
    [self.view addSubview:mapView];
}
@end
