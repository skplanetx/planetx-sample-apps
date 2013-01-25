//
//  CyworldFriendGuestBookViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CyworldFriendGuestBookViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>
{
    IBOutlet UILabel *nameLabel; //홈피 사용자
    IBOutlet UITableView *itemTableView; //목록 테이블뷰
    NSMutableArray *itemListArray; //방명록 목록
}

@property (nonatomic, retain) NSDictionary *userDictionary;

-(IBAction)pressedHomeButtonItem:(id)sender;

@end
