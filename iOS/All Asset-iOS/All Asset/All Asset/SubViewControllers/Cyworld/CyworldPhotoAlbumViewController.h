//
//  CyworldPhotoAlbumViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CyworldPhotoAlbumViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIPickerViewDataSource, UIPickerViewDelegate, UIActionSheetDelegate>
{
    IBOutlet UITextField *folderTextField; //폴더 선택
    IBOutlet UITableView *itemListTableView; //게시물 목록 테이블뷰
    
    NSMutableArray *folderListArray; //폴더 목록
    NSMutableArray *itemListArray; //전체 게시물 목록
    NSMutableArray *filteredItemListArray; //선택 폴더 게시물 목록
    
    NSString *selectedFolderId;
}

-(IBAction)pressedHomeButtonItem:(id)sender;

-(IBAction)showPicker:(id)sender;

@end
