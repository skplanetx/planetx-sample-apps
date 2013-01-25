//
//  MelonSearchViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MelonTopChartViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate>
{
    IBOutlet UITableView *itemTableView; //목록 테이블뷰
    IBOutlet UISegmentedControl *categorySegement; // 검색 조건
    NSMutableArray *itemListArray; //데이터 목록
    NSString *launchURL;
}

-(IBAction)pressedHomeButtonItem:(id)sender;
-(IBAction)pressedSegement:(id)sender;
@end
