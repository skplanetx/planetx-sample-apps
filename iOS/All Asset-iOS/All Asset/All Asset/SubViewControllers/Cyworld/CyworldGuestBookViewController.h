//
//  CyworldGuestBookViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CyworldGuestBookViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>
{
    IBOutlet UITableView *itemTableView; //목록 테이블뷰
    NSMutableArray *itemListArray; //방명록 목록
}

-(IBAction)pressedHomeButtonItem:(id)sender;

@end
