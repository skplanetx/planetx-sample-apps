//
//  ElevenstViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ElevenstItemListViewController.h"

@interface ElevenstViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate, UIAlertViewDelegate>{
    IBOutlet UITableView *itemTableView; //상품 목록 테이블뷰
    IBOutlet UISearchBar *itemSearchBar; //상품 검색 바
    IBOutlet UIBarButtonItem *homeButton; //홈 버튼
    NSMutableArray *itemListArray; //카테고리 목록
    NSMutableArray *displayItemListArray; //실제 표시되는 카테고리 목록 - 세부 목록 선택 시
    BOOL isLeaf;
    NSInteger categoryType;

    ElevenstItemListViewController *elevenstItemListViewController;
}

@property (nonatomic, retain) NSMutableArray *itemListArray;
@property (nonatomic, retain) NSMutableArray *displayItemListArray;
@property (nonatomic, retain) ElevenstItemListViewController *elevenstItemListViewController;

-(IBAction)pressedHomeButtonItem:(id)sender;

@end
