//
//  TmapSearchPathViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "TmapSearchPathViewController.h"
#import "TmapSearchPathDetailViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"
#import "AllAssetDB.h"

#define STR_NAME	@"Name"
#define STR_ID		@"Id"
#define STR_LAT     @"Latitude"
#define STR_LON     @"Longitude"

@interface TmapSearchPathViewController ()
{
    AllAssetDB *db;
}
@end

@implementation TmapSearchPathViewController

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
	// Do any additional setup after loading the view.
    db = [AllAssetDB new];
    [db createDatabase];
    itemListArray = [NSMutableArray new];
    
    [self loadTmapRecent];
}

-(void)viewWillAppear:(BOOL)animated
{

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark Property List Persistence
/**
 * 지정된 Property List에서 최근 경로 로딩
 */
-(void)loadTmapRecent
{
    if (itemListArray != nil)
    {
        [itemListArray removeAllObjects];
        [itemListArray addObjectsFromArray:[db loadRecentSearchPath]];
    }
}

#pragma mark SKP OPR API
/**
 *  Tmap POI 통합검색 API를 이용하여 검색 결과 조회
 */
-(void)setupData:(NSString *)searchText
{
    [self requestPOI:searchText];
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
            //마지막으로 검색한 경로를 저장
            [CommonUtil savePlist:dic plist:TMAP_RECENT_PLIST_FILE_NAME];
            //설정 Plist에도 최종 경로명을 저장
            [self saveConfiguration:[dic valueForKey:STR_NAME]];
            //상세경로 화면으로 이동
            [self showTmapSearchPathDetailViewController:dic];
        }
    }
    return nil;
}

#pragma mark UISearchBarDelegate methods
/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar
{
    [itemTableView setUserInteractionEnabled:NO];
    return YES;
}

/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (void)searchBarSearchButtonClicked:(UISearchBar*)searchBar
{
    isSearchMode = YES;
    
    if([searchBar.text length] > 0)
    {
        [self setupData:searchBar.text];
    }
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    if([searchBar.text length] == 0)
    {
        isSearchMode = NO;
        [self loadTmapRecent];
        [itemTableView reloadData];
        descLabel.text = @"최근 경로 보기";
    }
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    isSearchMode = NO;
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

/**
 * SearchBar resign 처리. SearchBar의 다른 영역 클릭 시 SearchBar를 사라지게 함.
 */
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [[event allTouches] anyObject];
    if ([itemSearchBar isFirstResponder] && [touch view] != itemSearchBar)
    {
        [itemSearchBar resignFirstResponder];
    }
    [itemTableView setUserInteractionEnabled:YES];
    [super touchesBegan:touches withEvent:event];
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

    [params setValue:searchKeyword forKey:@"searchKeyword"];    //검색어입니다.
    [params setValue:@"1" forKey:@"version"];                   //API의 버전 정보입니다.
    [params setValue:@"WGS84GEO" forKey:@"resCoordType"];       //받고자 하는 응답 좌표계 유형을 지정합니다.
    
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
    
    if (isSearchMode == NO) {
        [self modalDetailPathView:selectedRow];
        return;
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

#pragma mark modal
- (void)modalDetailPathView:(int)row
{
    if(itemListArray!=nil)
    {
        NSDictionary *dic = [itemListArray objectAtIndex:row];
        [self showTmapSearchPathDetailViewController:dic];
    }
}

- (void)showTmapSearchPathDetailViewController:(NSDictionary *)dic
{
    if(dic!=nil)
    {
        if (db != nil && isSearchMode == YES) {
            [db saveRecentSearchPath:dic];
        }

        TmapSearchPathDetailViewController *controller = [[TmapSearchPathDetailViewController alloc] initWithNibName:@"TmapSearchPathDetailViewController" bundle:nil];;
        controller.targetDictionary = dic;
        controller.favoritePathMode = NO;
        [self presentViewController:controller animated:YES completion:nil];
    }
}

- (BOOL)saveConfiguration:(NSString *)departure
{
    NSString *path = [CommonUtil getPlistPath:CONFIGURATION_PLIST_FILE_NAME];
    NSMutableArray *configArr = [NSMutableArray arrayWithContentsOfFile:path];
    NSMutableDictionary *productDic = [configArr objectAtIndex:4];
    [productDic setValue:departure forKey:CONFIGURATION_KEY_DEFAULT_TEXT];
    
    return [configArr writeToFile:path atomically:YES];
}
@end
