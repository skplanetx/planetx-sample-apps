//
//  TmapAreaDetailInfoViewController.h
//  All Asset
//
//  Created by Jason Nam on 12. 11. 7..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@interface TmapAreaDetailInfoViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, CLLocationManagerDelegate>
{
    IBOutlet UITableView *itemTableView;
    NSMutableArray *itemListArray;
    NSDictionary *requestInfoDictionary;
    
    CLLocationManager *locationManager;
    NSString *strStartLongitutde;
    NSString *strStartLatitude;
}
@property (nonatomic, retain) NSDictionary *requestInfoDictionary;
-(IBAction)pressedHomeButtonItem:(id)sender;

@end
