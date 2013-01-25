//
//  NateonViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NateonViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate, UIAlertViewDelegate>{

    IBOutlet UITableView *buddyListTableView; //친구 목록 테이블뷰
    IBOutlet UISearchBar *buddySearchBar; //친구 검색 바
    NSMutableArray *buddyListArray; //조회해온 친구 목록
    NSMutableArray *displayBuddyListArray; //실제 표시되는 친구 목록
    NSMutableArray *groupListArray; //그룹 목록
    NSString *myBuddies;    //목록조회할 친구들 NateOn 아이디 모음
    int reqTag;
}
@property (nonatomic, assign) NSInteger reqTag;
-(IBAction)pressedHomeButtonItem:(id)sender;

@end
