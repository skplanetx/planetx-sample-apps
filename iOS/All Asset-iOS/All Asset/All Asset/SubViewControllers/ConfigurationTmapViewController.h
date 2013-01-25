//
//  ConfigurationTmapViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ConfigurationTmapViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, UISearchBarDelegate, UIAlertViewDelegate, UITextFieldDelegate>
{
    IBOutlet UITableView *itemTableView;    //목록 테이블뷰
    IBOutlet UILabel *descLabel;            //설명 라벨
    IBOutlet UITextField *startSearchField; //출발지 검색 필드
    IBOutlet UITextField *endSearchField;   //도착지 검색 필드
    
    NSMutableArray *itemListArray;          //표시할 경로 검색 목록
    NSMutableArray *tmapListArray;
    
    BOOL isStartSearchMode;
    BOOL isEndSearchMode;
    
    int selectedRow;
}
- (IBAction)pressedBackButtonItem:(id)sender;
- (IBAction)pressedSettingButtonItem:(id)sender;

@end
