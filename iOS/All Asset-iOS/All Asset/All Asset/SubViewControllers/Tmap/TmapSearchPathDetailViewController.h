//
//  TmapSearchPathDetailViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "TMapView.h"

@interface TmapSearchPathDetailViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, CLLocationManagerDelegate>
{
    IBOutlet UITableView *itemTableView; //목록 테이블뷰
    IBOutlet UILabel *targetLabel; // 도착지 정보
    IBOutlet UILabel *arrivalTimeLabel; // 도착지 정보
    IBOutlet UISegmentedControl *viewModeSegment;   //모드변경
    
    NSMutableArray *sectionListArray; //구간 정보 목록
    NSMutableArray *pointListArray;
    NSMutableArray *lineListArray;
    NSDictionary *targetDictionary; //도착지 정보 딕셔너리
    NSArray *targetArray;           //시작지 도착지 정보 딕셔너리
    NSInteger arrivalTime;
    
    CLLocationManager *locationManager;
    
    BOOL isRequestReverseGeocoding;
    BOOL favoritePathMode;
    NSInteger reqType;
}

@property (nonatomic, retain) CLLocationManager *locationManager;
@property (nonatomic, retain) NSDictionary *targetDictionary;
@property (nonatomic, retain) NSArray *targetArray;
@property (assign) BOOL favoritePathMode;
@property (strong, nonatomic) TMapView *mapView;

-(IBAction)pressedHomeButtonItem:(id)sender;
-(IBAction)changeViewModeSegment:(id)sender;

@end
