//
//  TmapSearchPathViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TmapSearchPathViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate>
{
    IBOutlet UITableView *itemTableView; //목록 테이블뷰
    IBOutlet UILabel *descLabel; //설명 라벨
    IBOutlet UISearchBar *itemSearchBar; //검색 바
    //NSMutableArray *saveListArray; //저장된 경로 검색 목록
    NSMutableArray *itemListArray; //표시할 경로 검색 목록
    
    BOOL isSearchMode;
    int selectedRow;
}

-(IBAction)pressedHomeButtonItem:(id)sender;

@end
