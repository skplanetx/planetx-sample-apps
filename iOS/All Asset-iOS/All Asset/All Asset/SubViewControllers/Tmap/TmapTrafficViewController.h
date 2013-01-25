//
//  TmapTrafficViewController.h
//  All Asset
//
//  Created by Jason Nam on 12. 12. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TMapView.h"

@interface TmapTrafficViewController : UIViewController <TMapViewDelegate, TMapGpsManagerDelegate>

@property (strong, nonatomic) TMapView *mapView;

- (IBAction)pressedHomeButtonItem:(id)sender;
@end
