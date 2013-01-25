//
//  CyworldFriendListViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "CyworldFriendListViewController.h"
#import "CyworldFriendPhotoAlbumViewController.h"
#import "CyworldFriendGuestBookViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define STR_NAME @"Name"
#define STR_ALIAS @"Alias"
#define STR_ID @"Id"


@interface CyworldFriendListViewController ()

@end

@implementation CyworldFriendListViewController

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
    
}

- (void)viewWillAppear:(BOOL)animated
{
    //친구 목록 기본 정보를 설정한다.
    [self setupBuddyList];
    //검색 필터링을 위한 별도의 배열을 설정한다.
    displayBuddyListArray = [[NSMutableArray alloc] initWithArray:buddyListArray];
}

- (void)viewWillDisappear:(BOOL)animated
{
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark SKP OPR API
-(void)setupBuddyList
{
    buddyListArray = [[NSMutableArray alloc] init];
    
    [self performSelector:@selector(requestFriendList) withObject:nil afterDelay:0.1f];
}

#pragma mark UITableViewDataSource methods
/**
 * 일촌 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(displayBuddyListArray!=nil)
        return [displayBuddyListArray count];
    return 0;
}

/**
 * 테이블에 친구 정보를 표시한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:nil];
        cell.textLabel.font = [UIFont systemFontOfSize:16];
        cell.tag = indexPath.section*1000+indexPath.row;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        if(displayBuddyListArray!=nil)
        {
            cell.textLabel.text =[[displayBuddyListArray objectAtIndex:indexPath.row] valueForKey:STR_NAME];
            cell.detailTextLabel.text = [[displayBuddyListArray objectAtIndex:indexPath.row] valueForKey:STR_ALIAS];
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
/**
 * 특정 컬럼을 선택 할 시 해당 친구의 미니 홈피를 보여준다.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(displayBuddyListArray!=nil)
    {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"CyworldFriendMiniHompStoryboard" bundle:nil];
        UITabBarController *controller = [storyboard instantiateInitialViewController];
        CyworldFriendPhotoAlbumViewController *photoAlbumChildContoller = (CyworldFriendPhotoAlbumViewController *)[[controller viewControllers] objectAtIndex:0];
        photoAlbumChildContoller.userDictionary = [displayBuddyListArray objectAtIndex:indexPath.row];
        CyworldFriendGuestBookViewController *guestBookChildContoller = (CyworldFriendGuestBookViewController *)[[controller viewControllers] objectAtIndex:1];
        guestBookChildContoller.userDictionary = [displayBuddyListArray objectAtIndex:indexPath.row];
        [self presentViewController:controller animated:YES completion:nil];
    }
    return nil;
}

#pragma mark UISearchBarDelegate methods
/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    if([searchBar.text length] == 0)
    {
        [displayBuddyListArray removeAllObjects];
        [displayBuddyListArray addObjectsFromArray:buddyListArray];
        [buddyListTableView reloadData];
    }
}

/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar
{
    [buddyListTableView setUserInteractionEnabled:NO];
    return YES;
}

/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (void)searchBarSearchButtonClicked:(UISearchBar*)searchBar
{
    if([searchBar.text length] == 0)
    {
        [displayBuddyListArray removeAllObjects];
        [displayBuddyListArray addObjectsFromArray:buddyListArray];
    }
    else
    {
        [displayBuddyListArray removeAllObjects];
        for(NSDictionary *dic in buddyListArray)
        {
            NSString *name = [dic valueForKey:STR_NAME];
            NSRange r = [name rangeOfString:searchBar.text options:NSCaseInsensitiveSearch];
            if (r.location != NSNotFound)
            {
                [displayBuddyListArray addObject:dic];
            }
        }
    }
    [buddyListTableView reloadData];
    [searchBar resignFirstResponder];
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

/**
 * SearchBar Dismiss
 */
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [[event allTouches] anyObject];
    if ([buddySearchBar isFirstResponder] && [touch view] != buddySearchBar)
    {
        [buddySearchBar resignFirstResponder];
    }
    [buddyListTableView setUserInteractionEnabled:YES];
    [super touchesBegan:touches withEvent:event];
}

#pragma mark API request
/**
 * 설명 : Cyworld 관심일촌 목록
 * RequestURI : https://apis.skplanetx.com/cyworld/cys/besties
 */
- (void)requestFriendList
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER_SSL stringByAppendingString:CY_BESTIES_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];    //API의 버전 정보입니다.
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil
                                                      url:url
                                                   params:params
                                                  payload:nil
                                           uploadFilePath:nil
                                               httpMethod:SKPopHttpMethodGET
                                              requestType:SKPopContentTypeJSON
                                             responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

/**
 * 설명 : Cyworld 전체 홈 정보 보기
 * RequestURI : https://apis.skplanetx.com/cyworld/minihome/{cyId}
 * Request PathParam : {cyId} 조회할 대상의 싸이월드 ID입니다.
 */
- (void)requestHomeInfo:(NSString *)cyId
{
    NSString *url = @"https://apis.skplanetx.com/cyworld/minihome/%@";
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];    //API의 버전 정보입니다.
    
    //Bundle 설정
    RequestBundle *bundle = [RequestBundle new];
    [bundle setUrl:[NSString stringWithFormat:url, cyId]];
    [bundle setParameters:params];
    [bundle setHttpMethod:SKPopHttpMethodGET];
    [bundle setRequestType:SKPopContentTypeFORM];
    [bundle setResponseType:SKPopContentTypeJSON];
    
    //API 호출
    APIRequest *apiRequest = [APIRequest new];
    [apiRequest setDelegate:self
                   finished:@selector(apiRequestFinished:)
                     failed:@selector(apiRequestFailed:)];
    [apiRequest aSyncRequest:bundle];
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
    
    if (dic != nil) {
        NSMutableArray *cyList = [[dic objectForKey:@"cys"] objectForKey:@"cy"];
        
        if (buddyListArray != nil && [buddyListArray count] > 0) {
            [buddyListArray removeAllObjects];
        }
        [buddyListArray addObjectsFromArray:[self makeCyDictionary:cyList]];
        [displayBuddyListArray addObjectsFromArray:buddyListArray];
        [buddyListTableView reloadData];
    }
}

- (NSMutableArray *)makeCyDictionary:(NSMutableArray *)cyList
{
    NSMutableArray *arrangedCyList = [NSMutableArray new];
    
    for (NSDictionary *cy in cyList) {
        NSString *cyId          = [cy valueForKey:@"cyId"];
        NSString *cyName        = [cy valueForKey:@"cyName"];
        NSString *relationName  = [cy valueForKey:@"relationName"];
        
        [arrangedCyList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:cyName, STR_NAME, relationName, STR_ALIAS, cyId, STR_ID, nil]];
    }

    return arrangedCyList;
}
@end
