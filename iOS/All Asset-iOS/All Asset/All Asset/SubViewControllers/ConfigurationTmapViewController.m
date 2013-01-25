//
//  ConfigurationTmapViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ConfigurationTmapViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"
#import "AllAssetDB.h"

#define STR_LAT     @"Latitude"
#define STR_LON     @"Longitude"
#define STR_RECENT  @"최근 목적지"
@interface ConfigurationTmapViewController ()
{
    AllAssetDB *db;
}
@end

@implementation ConfigurationTmapViewController

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
    db = [AllAssetDB new];
    [db createDatabase];
    itemListArray = [NSMutableArray new];
    
    //[self loadTmapRecent];
    
    [self loadTmapPlist];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark UITextField Delegate methods
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    NSLog(@"textFieldDidBeginEditing");
    if ([textField isEqual:startSearchField]) {
        isStartSearchMode = YES;
        isEndSearchMode = NO;
    } else {
        isStartSearchMode = NO;
        isEndSearchMode = YES;
    }
    [itemTableView setUserInteractionEnabled:NO];
}

- (BOOL)textFieldShouldClear:(UITextField *)textField
{
    NSLog(@"textFieldShouldClear");
    
    if ([textField isEqual:startSearchField]) {
        isStartSearchMode = NO;
    } else if ([textField isEqual:endSearchField]) {
        isEndSearchMode = NO;
    }
    [textField resignFirstResponder];
    
    return YES;
}

-(BOOL)textFieldShouldReturn:(UITextField*)textField
{
    NSLog(@"textFieldShouldReturn");
    
    if([startSearchField.text length] > 0)
    {
        //isStartSearchMode = YES;

        [self setupData:startSearchField.text];
    }
    else if ([endSearchField.text length] > 0)
    {
        //isEndSearchMode = YES;
        
        [self setupData:endSearchField.text];
    }
    
    if (isStartSearchMode && [startSearchField.text length] > 0) {
        [self setupData:startSearchField.text];
    } else if (isEndSearchMode && [endSearchField.text length] > 0) {
        [self setupData:endSearchField.text];
    }
    
    [textField resignFirstResponder];
    [itemListArray removeAllObjects];
    return YES;
}

#pragma mark UITableViewDataSource methods
/**
 * 카테고리 목록의 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(itemListArray!=nil)
        return [itemListArray count];
    return 0;
}

/**
 * 테이블에 카테고리 정보를 표시한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithFrame:CGRectZero];
        cell.textLabel.font = [UIFont systemFontOfSize:16];
        cell.tag = indexPath.section*1000+indexPath.row;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        if(itemListArray!=nil)
        {
            NSDictionary *dic = [itemListArray objectAtIndex:indexPath.row];
            if(dic!=nil)
            {
                cell.textLabel.text = [dic valueForKey:STR_NAME];
            }
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
/**
 * 특정 컬럼을 선택 할 시 UIAlertView 를 Popup 하여 메시지 전송하게 한다.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(itemListArray!=nil)
    {
        NSDictionary *dic = [itemListArray objectAtIndex:indexPath.row];
        
        if(dic!=nil)
        {
//            //DB 저장
//            [db saveRecentSearchPath:dic];
//            //마지막으로 검색한 경로를 저장
//            [CommonUtil savePlist:dic plist:TMAP_RECENT_PLIST_FILE_NAME];
//            //설정 Plist에도 최종 경로명을 저장
//            if ([self saveConfiguration:[dic valueForKey:STR_NAME]]) {
//                NSString *departure = [NSString stringWithFormat:@"%@를 목적지로 설정하였습니다.",[dic valueForKey:STR_NAME]];
//                [CommonUtil commonCustomAlertView:departure CancelMessage:nil OkMessage:CONFIRM Tag:100 delegate:self];
//            }
            int index = -1;
            NSString *locationName = @"";
            if (isStartSearchMode == YES && isEndSearchMode == NO) {
                index = 0;
                locationName = [locationName stringByAppendingFormat:@"%@를 출발지로 설정하였습니다.",[dic valueForKey:STR_NAME]];
            } else if (isStartSearchMode == NO && isEndSearchMode == YES) {
                index = 1;
                locationName = [locationName stringByAppendingFormat:@"%@를 목적지로 설정하였습니다.",[dic valueForKey:STR_NAME]];
            }
            
            if (index != -1) {
                [self saveTmapPlist:dic index:index];
                
                [CommonUtil commonCustomAlertView:locationName CancelMessage:nil OkMessage:CONFIRM Tag:100 delegate:self];
            }
        }
    }
    return nil;
}

/**
 * SearchBar resign 처리. SearchBar의 다른 영역 클릭 시 SearchBar를 사라지게 함.
 */
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [[event allTouches] anyObject];
    if ([startSearchField isFirstResponder] && [touch view] != startSearchField)
    {
        [startSearchField resignFirstResponder];
    }
    
    if ([endSearchField isFirstResponder] && [touch view] != endSearchField) {
        [endSearchField resignFirstResponder];
    }
    
    [itemTableView setUserInteractionEnabled:YES];
    [super touchesBegan:touches withEvent:event];
}

#pragma mark Property List Persistence
/**
 * 지정된 Property List에서 최근 경로 로딩
 */
-(void)loadTmapRecent
{
    [self initView];
    if (itemListArray != nil)
    {
        [itemListArray removeAllObjects];
    }
    [itemListArray addObjectsFromArray:[db loadRecentSearchPath]];
    [itemTableView reloadData];
}

#pragma mark SKP OPR API
/**
 *  Tmap POI 통합검색 API를 이용하여 검색 결과 조회
 */
-(void)setupData:(NSString *)searchText
{
    NSLog(@"searchText : %@", searchText);
    [self requestPOI:searchText];
}

- (void)initView
{
    descLabel.text = STR_RECENT;
    //itemSearchBar.text = @"";
}

#pragma mark API request
/**
 * 설명 : T map POI 통합검색
 * RequestURI : https://apis.skplanetx.com/tmap/pois
 */
- (void)requestPOI:(NSString *)searchKeyword
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:TMAP_POI_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    
    [params setValue:searchKeyword forKey:@"searchKeyword"];
    [params setValue:@"1" forKey:@"version"];
    [params setValue:@"WGS84GEO" forKey:@"resCoordType"];
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil
                                                      url:url
                                                   params:params
                                                  payload:nil
                                           uploadFilePath:nil
                                               httpMethod:SKPopHttpMethodGET
                                              requestType:SKPopContentTypeFORM
                                             responseType:SKPopContentTypeJSON];
    
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
    NSLog(@"result : %@", result);
    
    NSDictionary *dic       = [result JSONValue];
    
    //에러코드 리턴시 Alert 처리
    [ApiUtil errorAlert:dic];
    
    NSMutableArray *poiList =  [[[dic objectForKey:@"searchPoiInfo"] objectForKey:@"pois"] objectForKey:@"poi"];
    
    if (itemListArray != nil && [itemListArray count] > 0) {
        itemListArray = [NSMutableArray new];
    }
    
    for (NSDictionary *poi in poiList) {
        NSString *poiId     = [poi valueForKey:@"id"];
        NSString *name      = [poi valueForKey:@"name"];
        NSString *frontLat  = [poi valueForKey:@"frontLat"];
        NSString *frontLon  = [poi valueForKey:@"frontLon"];
        
        if (itemListArray != nil) {
            [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:name, STR_NAME, poiId, STR_ID, frontLat, STR_LAT, frontLon, STR_LON, nil]];
        }
    }
    
    [itemTableView reloadData];
    NSString *text = @"검색결과";
    if(itemListArray!=nil)
    {
        text = [text stringByAppendingFormat:@"(%d)", [itemListArray count]];
    }
    else
    {
        text = [text stringByAppendingString:@"(없음)"];
    }
    descLabel.text = text;
}

/** 최근 검색 목적지 저장 */
- (BOOL)saveConfiguration:(NSString *)departure
{
    NSString *path = [CommonUtil getPlistPath:CONFIGURATION_PLIST_FILE_NAME];
    NSMutableArray *configArr = [NSMutableArray arrayWithContentsOfFile:path];
    NSMutableDictionary *productDic = [configArr objectAtIndex:4];
    [productDic setValue:departure forKey:CONFIGURATION_KEY_DEFAULT_TEXT];
    
    return [configArr writeToFile:path atomically:YES];
}

/**
 * 출발지 목적지 설정 저장
 */
- (void)saveFavoritePath:(NSMutableArray *)favoriteArray
{
    if (favoriteArray != nil) {
        NSString *path = [CommonUtil getPlistPath:TMAP_RECENT_PLIST_FILE_NAME];
        [favoriteArray writeToFile:path atomically:YES];
    }
}

#pragma mark IBAction methods
- (IBAction)pressedBackButtonItem:(id)sender
{
    isStartSearchMode = NO;
    isEndSearchMode = NO;
    [itemListArray removeAllObjects];
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)pressedSettingButtonItem:(id)sender
{
    isStartSearchMode = NO;
    isEndSearchMode = NO;
    
    //설정 plist에 저장
    
}

#pragma mark UIAlertView Delegate
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
//    if (alertView.tag == 100) {
//        isEndSearchMode = NO;
//        itemListArray = nil;
//        //[self dismissViewControllerAnimated:YES completion:nil];
//    }
}

- (void)saveTmapPlist:(NSDictionary *)dic index:(int)index
{
    [tmapListArray replaceObjectAtIndex:index withObject:dic];
    [CommonUtil saveTmapPlist:tmapListArray];
}

- (void)loadTmapPlist
{
    tmapListArray = [NSMutableArray arrayWithContentsOfFile:[CommonUtil getPlistPath:TMAP_RECENT_PLIST_FILE_NAME]];
}
@end
