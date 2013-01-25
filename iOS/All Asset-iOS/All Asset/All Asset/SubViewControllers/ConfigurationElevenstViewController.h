//
//  ConfigurationElevenstViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ConfigurationElevenstTableViewCell.h"

@interface ConfigurationElevenstViewController : UIViewController<UITableViewDelegate, UITableViewDelegate, ConfigurationElevenstCellDelegate, UIAlertViewDelegate>
{
    IBOutlet UITableView *itemTableView;    //목록 테이블뷰
    IBOutlet UIBarButtonItem *homeButton;   //홈 버튼
    NSMutableArray *itemListArray; //카테고리 목록
    NSMutableArray *displayItemListArray; //실제 표시되는 카테고리 목록 - 세부 목록 선택 시
    BOOL isLeaf;
    NSInteger categoryType;
}

@property (nonatomic, retain) NSMutableArray *itemListArray;
@property (nonatomic, retain) NSMutableArray *displayItemListArray;

-(IBAction)pressedBackButtonItem:(id)sender;
@end
