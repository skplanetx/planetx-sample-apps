//
//  CyworldFriendPhotoAlbumViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "CyworldFriendPhotoAlbumViewController.h"
#import "CyworldPhotoAlbumTableViewCell.h"
#import "CyworldPhotoDetailViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define STR_NAME		@"Name"
#define STR_TITLE		@"Title"
#define STR_DATE		@"Date"
#define STR_IMAGE_URL	@"ImageURL"
#define STR_CLASSES		@"Classes"
#define STR_ID			@"Id"
#define STR_FOLDER_ID	@"FolderId"

@interface CyworldFriendPhotoAlbumViewController ()

@end

@implementation CyworldFriendPhotoAlbumViewController
@synthesize userDictionary;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

}

- (void)viewWillAppear:(BOOL)animated
{
    
    [self setupData];
    if(folderListArray!=nil && [folderListArray count] > 0)
    {
        folderTextField.text = [[folderListArray objectAtIndex:0] valueForKey:STR_NAME];
        [self filtered:[[folderListArray objectAtIndex:0] valueForKey:STR_ID]];
    }
    NSString *name = [userDictionary valueForKey:STR_NAME];
    name = [name stringByAppendingString:@"님의 미니홈피"];
    nameLabel.text = name;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark SKP OPR API
/**
 * Cyworld 폴더목록 API를 이용하여 해당 사용자의 사진첩 데이터 조회
 */
-(void)setupData
{
    folderListArray = [[NSMutableArray alloc] init];
    itemListArray = [[NSMutableArray alloc] init];

    //사진첩 폴더목록 API 요청
    [self performSelector:@selector(requestFolderList:) withObject:[userDictionary valueForKey:STR_ID] afterDelay:0.1f];
}

-(void)filtered:(NSString *)folderId
{
    if(itemListArray!=nil)
    {
        filteredItemListArray = [[NSMutableArray alloc] init];
        for(NSDictionary *dic in itemListArray)
        {
            NSString *value = [dic valueForKey:STR_FOLDER_ID];
            if([value isEqualToString:folderId])
            {
                [filteredItemListArray addObject:dic];
            }
        }
    }
}

-(IBAction)showPicker:(id)sender
{
    if (folderListArray == nil || (folderListArray != nil && folderListArray.count <= 0)) {
        [CommonUtil commonAlertView:@"폴더 목록이 없습니다."];
        return;
    }
    UIActionSheet *menu = [[UIActionSheet alloc] initWithTitle:@"폴더 선택"
                                                      delegate:self
                                             cancelButtonTitle:@"완료"
                                        destructiveButtonTitle:nil
                                             otherButtonTitles:nil];
    // Add the picker
    UIPickerView *pickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0,90,0,0)];
    
    pickerView.delegate = self;
    pickerView.showsSelectionIndicator = YES;    // note this is default to NO
    
    [menu addSubview:pickerView];
    [menu showFromTabBar:self.tabBarController.tabBar];
    [menu setBounds:CGRectMake(0,0,320,500)];
}

#pragma mark UIPickerViewDelegate
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    if (folderListArray != nil && [folderListArray count] > 0) {
        folderTextField.text = [[folderListArray objectAtIndex:row] valueForKey:STR_NAME];
        
        selectedFolderId = [[[folderListArray objectAtIndex:row] valueForKey:STR_ID] stringValue];
    }
}

#pragma mark UIPickerViewDataSource
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if (folderListArray != nil && [folderListArray count] > 0) {
        return [[folderListArray objectAtIndex:row] valueForKey:STR_NAME];
    }
    return nil;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    if (folderListArray != nil && [folderListArray count] > 0) {
        return [folderListArray count];
    }
	return 1;
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
	return 1;
}

#pragma mark UITableViewDataSource methods
/**
 * 일촌 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(filteredItemListArray!=nil)
        return [filteredItemListArray count];
    return 0;
}

/**
 * 테이블에 사진첩 정보를 표시한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CyworldPhotoAlbumTableViewCell *cell = (CyworldPhotoAlbumTableViewCell *)[tableView dequeueReusableCellWithIdentifier:nil];
    if (cell == nil)
	{
		NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"CyworldPhotoAlbumTableViewCell" owner:nil options:nil];
        cell = [array objectAtIndex:0];
        cell.tag = indexPath.section*1000+indexPath.row;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        if(filteredItemListArray!=nil)
        {
            NSURL *url = [NSURL URLWithString:[[filteredItemListArray objectAtIndex:indexPath.row] valueForKey:STR_IMAGE_URL]];
            [cell.titleImage setContentMode:UIViewContentModeScaleAspectFill];
            [cell.titleImage initWithImageAtURL:url];
            cell.title.text = [[filteredItemListArray objectAtIndex:indexPath.row] valueForKey:STR_TITLE];
            cell.date.text = [[filteredItemListArray objectAtIndex:indexPath.row] valueForKey:STR_DATE];
            cell.classes.text = [[filteredItemListArray objectAtIndex:indexPath.row] valueForKey:STR_CLASSES];
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
-(NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(itemListArray!=nil)
    {
        CyworldPhotoDetailViewController *controller = [[CyworldPhotoDetailViewController alloc] initWithNibName:@"CyworldPhotoDetailViewController" bundle:nil];;
        controller.itemDictionary = [filteredItemListArray objectAtIndex:indexPath.row];
        [self presentViewController:controller animated:YES completion:nil];
    }
    return nil;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 120.0;
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark API request
/**
 * 설명 : Cyworld 사진첩 게시물 목록보기
 * RequestURI : https://apis.skplanetx.com/cyworld/minihome/{cyId}/albums/{folderNo}/items
 * Request PathParam : 
 * {cyId} 조회할 대상의 싸이월드 ID입니다.
 * {folderNo} 폴더의 번호(전체보기의 경우: 0)입니다.
 */
- (void)requestPhotoAlbum:(NSString *)cyId folderNo:(NSString *)folderNo
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:CY_PHOTO_LIST_PATH, cyId, folderNo];
    
    //Querystring Parameters
    NSMutableDictionary *dict = [NSMutableDictionary new];
    [dict setValue:@"1" forKey:@"version"];    //API의 버전 정보 입니다.
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:dict payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeJSON responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

/**
 * 설명 : Cyworld 사진첩 폴더 목록 보기
 * RequestURI : https://apis.skplanetx.com/cyworld/minihome/{cyId}/albums
 * Request PathParam :
 * {cyId} 조회할 대상의 싸이월드 ID입니다.
 */
- (void)requestFolderList:(NSString *)cyId
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url       = [SERVER_SSL stringByAppendingFormat:CY_FOLDER_LIST_PATH,cyId];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];   //API의 버전 정보 입니다.
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:params payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeFORM responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

#pragma mark API response
- (void)apiRequestFinished:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [self resultParseAndSave:[result valueForKey:SKPopASyncResultData]];
}

- (void)apiRequestFailed:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [CommonUtil commonAlertView:[result valueForKey:SKPopASyncResultMessage]];
}

#pragma mark Parsing JSON Result
- (void)resultParseAndSave:(NSString *)result
{
    NSDictionary *dic = [result JSONValue];
    
    [ApiUtil errorAlert:dic];
    
    if ([dic objectForKey:@"folders"] != nil) {
        NSMutableArray *folderList = [[dic objectForKey:@"folders"] objectForKey:@"folder"];
        if(folderListArray!=nil)
        {
            if ([folderListArray count] > 0) {
                [folderListArray removeAllObjects];
            }
            
            for (NSDictionary *folder in folderList) {
                NSString *folderNo      = [folder valueForKey:@"folderNo"];
                NSString *folderName    = [folder valueForKey:@"folderName"];
                
                [folderListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:folderName, STR_NAME, folderNo, STR_ID, nil]];
                
            }
        }
        
        //첫번째 폴더명 노출
        folderTextField.text = [[folderListArray objectAtIndex:0] valueForKey:STR_NAME];
        NSString *folderNo = [[folderListArray objectAtIndex:0] valueForKey:STR_ID];
        
        [self requestPhotoAlbum:[userDictionary valueForKey:STR_ID] folderNo:folderNo];
    } else if ([dic objectForKey:@"items"] != nil) {
        NSMutableArray *itemList = [[dic objectForKey:@"items"] objectForKey:@"item"];
        
        if (itemListArray != nil) {
            if ([itemListArray count] > 0) {
                [itemListArray removeAllObjects];
            }
            
            for (NSDictionary *dic in itemList) {
                NSString *title         = [dic objectForKey:@"title"];
                NSString *writerId      = [dic objectForKey:@"writerId"];
                NSString *photoVmUrl    = [dic objectForKey:@"photoVmUrl"];
                NSString *writeDate     = [dic objectForKey:@"writeDate"];
                NSString *itemOpen      = [dic objectForKey:@"itemOpen"];
                NSString *folderNo      = [dic objectForKey:@"folderNo"];
                
                [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:title, STR_TITLE, writerId, STR_ID, photoVmUrl, STR_IMAGE_URL, writeDate, STR_DATE, [self makeOpenState:itemOpen], STR_CLASSES, folderNo, STR_FOLDER_ID, nil]];
            }
            
            [self showFolderList:itemListArray];
        }
    }
}

//테이블뷰에 넣어줄 Array 바인딩
-(void)showFolderList:(NSMutableArray *)array
{
    if(array!=nil)
    {
        filteredItemListArray = [[NSMutableArray alloc] init];
        [filteredItemListArray addObjectsFromArray:array];
    }
    [itemListTableView reloadData];
}

- (NSString *)makeOpenState:(NSString *)itemOpen
{
    if ([itemOpen isEqualToString:@"secret"] || [itemOpen isEqualToString:@"0"]) {
        return @"비공개";
    } else if ([itemOpen isEqualToString:@"cyOpen"] || [itemOpen isEqualToString:@"1"]) {
        return @"일촌공개";
    } else if ([itemOpen isEqualToString:@"allOpen"] || [itemOpen isEqualToString:@"4"]) {
        return @"전체공개";
    }
    return @"";
}

#pragma mark ActionSheet Delegate
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (selectedFolderId != nil && [selectedFolderId length] > 0) {
        [self requestPhotoAlbum:[userDictionary valueForKey:STR_ID] folderNo:selectedFolderId];
    }
}

@end
