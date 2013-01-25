//
//  TmapAreaInfoViewController.h
//  All Asset
//
//  Created by Jason Nam on 12. 11. 7..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TmapAreaInfoViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
{
    IBOutlet UITableView *itemTableView; //목록 테이블뷰
    NSMutableArray *itemListArray; //표시할 경로 검색 목록
    NSInteger reqTag;
}
-(IBAction)pressedHomeButtonItem:(id)sender;
@end
