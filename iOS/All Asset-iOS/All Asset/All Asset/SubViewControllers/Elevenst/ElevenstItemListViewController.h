//
//  ElevenstItemListViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ElevenstItemListViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate, UIPickerViewDataSource, UIPickerViewDelegate, UIActionSheetDelegate>
{
    IBOutlet UITableView *itemTableView; //상품 목록 테이블뷰
    IBOutlet UISearchBar *itemSearchBar; //상품 검색 바
    IBOutlet UILabel *resultLabel; //상품 검색 결과
    IBOutlet UITextField *optionTextField; //폴더 선택
    
    NSDictionary *itemInfoDictionary; //상품 검색 정보
    NSMutableArray *itemListArray; //상품 검색 목록
    NSMutableArray *searchOptionArray; //검색 조건 목록
    NSMutableArray *tempListArray; //정리전 상품 검색 목록
    
    NSString *selectedPickerId;
}

@property (nonatomic, retain) NSMutableArray *itemListArray;
@property (nonatomic, retain) NSMutableArray *tempListArray;
@property (nonatomic, retain) NSDictionary *itemInfoDictionary;

-(IBAction)pressedHomeButtonItem:(id)sender;

-(IBAction)showPicker:(id)sender;
-(void)setupItemList:(NSString *)searchOption;

@end
