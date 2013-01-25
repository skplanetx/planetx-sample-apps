//
//  CyworldFriendListViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CyworldFriendListViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate>
{
    IBOutlet UITableView *buddyListTableView; //친구 목록 테이블뷰
    IBOutlet UISearchBar *buddySearchBar; //친구 검색 바
    NSMutableArray *buddyListArray; //조회해온 친구 목록
    NSMutableArray *displayBuddyListArray; //실제 표시되는 친구 목록
}

-(IBAction)pressedHomeButtonItem:(id)sender;

@end
