//
//  ElevenstCommentViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ElevenstCommentViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate>
{
    IBOutlet UITableView *itemTableView; //목록 테이블뷰
    IBOutlet UILabel *commentCount;
    IBOutlet UISegmentedControl *classSegement; // 후기 or 리뷰
    NSInteger queryMode;
    NSDictionary *itemInfoDictionary; //상품 정보
    NSMutableArray *commentListArray;
    NSString *launchURL;
}

@property (nonatomic, retain) NSDictionary *itemInfoDictionary;

-(IBAction)pressedHomeButtonItem:(id)sender;
-(IBAction)changedSegment:(id)sender;

@end
