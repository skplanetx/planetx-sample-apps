//
//  MelonSearchViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "MelonSearchViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define STR_NAME	@"Name"
#define STR_ARTIST	@"Artist"
#define STR_URL		@"Url"

@interface MelonSearchViewController ()
{
    BOOL isSearchEdit;
}

@end

@implementation MelonSearchViewController

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
    [self setupData:nil];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [CommonUtil commonStopActivityIndicator];
    //[self initData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark SKP OPR API
/**
 * 멜론 검색 API를 이용하여 최신 곡 데이터 조회
 */
- (void)setupData:(NSString *)keyword
{
    if(keyword!=nil)
    {
        NSLog(@"select search option %d", categorySegement.selectedSegmentIndex);
        itemListArray = [[NSMutableArray alloc] init];
        
        [self requestSearchKeyword:[self getCurrentReqType:categorySegement.selectedSegmentIndex]];
    }
}

- (void)initData
{
    itemSearchBar.text = @"";
    [itemListArray removeAllObjects];
    [itemTableView reloadData];
}

#pragma mark UITableViewDataSource methods
/**
 * 데이터 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(itemListArray!=nil)
    {
        return [itemListArray count];
    }
    return 0;
}

/**
 * 테이블에 데이터를 표시한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
    
    @try {
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:nil];
            cell.textLabel.font = [UIFont systemFontOfSize:16];
            cell.tag = indexPath.section*1000+indexPath.row;
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            if(itemListArray!=nil)
            {
                cell.textLabel.text = [[itemListArray objectAtIndex:indexPath.row] valueForKey:STR_NAME];
                cell.detailTextLabel.text = [[itemListArray objectAtIndex:indexPath.row] valueForKey:STR_ARTIST];
            }
        }
    }
    @catch (NSException *exception) {
        NSLog(@"[MelonSearchViewController] exception occured : %@", [exception description]);
    }
    @finally {
        
    }
	
    return cell;
}

#pragma mark UITableViewDelegate methods
/**
 * 특정 컬럼을 선택 할 시 상세 상품 정보를 보여준다.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(itemListArray!=nil)
    {
        launchURL = [[itemListArray objectAtIndex:indexPath.row] valueForKey:STR_URL];

        [CommonUtil commonCustomAlertView:MSG_LAUNCH_BROWSER CancelMessage:CONFIRM OkMessage:CANCEL Tag:0 delegate:self];
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
    if([searchBar.text length] > 0)
    {
        [self setupData:searchBar.text];
        [itemTableView reloadData];
        [searchBar resignFirstResponder];
        
        [self setupData:searchBar.text];
    }
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(IBAction)pressedSegement:(id)sender
{
    if([itemSearchBar.text length] > 0)
    {
        [self setupData:itemSearchBar.text];
        [itemTableView reloadData];
    }
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

#pragma mark SKP API
/**
 * 설명 : Melon 곡검색, 앨범검색, 아티스트검색
 * 곡검색 RequestURI : http://apis.skplanetx.com/melon/songs
 * 아티스트검색 RequestURI : http://apis.skplanetx.com/melon/artists
 * 앨범검색 RequestURI : http://apis.skplanetx.com/melon/albums
 */
- (void)requestSearchKeyword:(NSInteger)reqType
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [ApiUtil getRequestURL:SERVER index:reqType];
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil
                                                      url:url
                                                   params:[self makeParams]
                                                  payload:nil
                                           uploadFilePath:nil
                                               httpMethod:SKPopHttpMethodGET
                                              requestType:SKPopContentTypeFORM
                                             responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

//Querystring Parameters
- (NSMutableDictionary *)makeParams
{
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];                       //API의 버전 정보입니다.
    [params setValue:@"1" forKey:@"page"];                          //조회할 목록의 페이지를 지정합니다.
    [params setValue:@"10" forKey:@"count"];                        //페이지당 출력되는 곡 수를 지정합니다.
    [params setValue:itemSearchBar.text forKey:@"searchKeyword"];   //검색 키워드입니다(UTF-8 인코딩)
    return params;
}

#pragma mark API response
- (void)apiRequestFinished:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [itemSearchBar resignFirstResponder];
    
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
    
    NSMutableArray *parseResult = [NSMutableArray new];
    
    switch (categorySegement.selectedSegmentIndex) {
        case 0:
            parseResult = [self parseSongResult:dic];
            break;
        case 1:
            parseResult = [self parseAlbumResult:dic];
            break;
        case 2:
            parseResult = [self parseArtistResult:dic];
            break;
    }
    
    [itemListArray removeAllObjects];
    [itemListArray addObjectsFromArray:parseResult];
    [itemTableView reloadData];
}

/**
 * 곡 검색 결과 JSON 파싱
 */
-(NSMutableArray *)parseSongResult:(NSDictionary *)dic
{
    NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
    NSMutableArray *songList = [[[dic objectForKey:@"melon"] objectForKey:@"songs"] objectForKey:@"song"];
    
    NSMutableArray *resultList = [NSMutableArray new];
    
    for (NSDictionary *song in songList) {
        NSString *songId        = [song objectForKey:@"songId"];
        NSString *songName      = [song objectForKey:@"songName"];
        
        NSMutableArray *artistArray = [[song objectForKey:@"artists"] objectForKey:@"artist"];
        NSString *artistName = @"";
        
        //아티스트가 1명 이상인 경우
        if (artistArray.count > 1) {
            for (NSDictionary *dicArtist in artistArray) {
                artistName = [artistName stringByAppendingFormat:@",%@", [dicArtist objectForKey:@"artistName"]];
            }
            artistName = [artistName substringFromIndex:1];
        } else {
            artistName    = [[[[song objectForKey:@"artists"] objectForKey:@"artist"] objectAtIndex:0] objectForKey:@"artistName"];
        }
        
        NSString *url           = [ApiUtil makeWebPageURL:@"song" contentId:songId menuId:menuId];
        
        [resultList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:songName, STR_NAME, artistName, STR_ARTIST, url, STR_URL, nil]];
    }
    
    return resultList;
}

/**
 * 앨범검색 결과 JSON 파싱
 */
-(NSMutableArray *)parseAlbumResult:(NSDictionary *)dic
{
    NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
    
    NSMutableArray *albumList = [[[dic objectForKey:@"melon"] objectForKey:@"albums"] objectForKey:@"album"];
    
    NSMutableArray *resultList = [NSMutableArray new];
    
    for (NSDictionary *song in albumList) {
        NSString *albumId        = [song objectForKey:@"albumId"];
        NSString *albumName      = [song objectForKey:@"albumName"];
        
        NSMutableArray *artistArray = [[song objectForKey:@"artists"] objectForKey:@"artist"];
        NSString *artistName = @"";
        
        //아티스트가 1명 이상인 경우
        if (artistArray.count > 1) {
            for (NSDictionary *dicArtist in artistArray) {
                artistName = [artistName stringByAppendingFormat:@",%@", [dicArtist objectForKey:@"artistName"]];
            }
            artistName = [artistName substringFromIndex:1];
        } else {
            artistName    = [[[[song objectForKey:@"artists"] objectForKey:@"artist"] objectAtIndex:0] objectForKey:@"artistName"];
        }
        
        NSString *url = [ApiUtil makeWebPageURL:@"album" contentId:albumId menuId:menuId];
        
        [resultList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:albumName, STR_NAME, artistName, STR_ARTIST, url, STR_URL, nil]];
    }
    return resultList;
}

/**
 * 아티스트 검색결과 JSON 파싱
 */
-(NSMutableArray *)parseArtistResult:(NSDictionary *)dic
{
    NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
    
    NSMutableArray *artistList = [[[dic objectForKey:@"melon"] objectForKey:@"artists"] objectForKey:@"artist"];
    
    NSMutableArray *resultList = [NSMutableArray new];
    
    for (NSDictionary *artist in artistList) {
        NSString *artistId        = [artist objectForKey:@"artistId"];
        NSString *artistName      = [artist objectForKey:@"artistName"];
        NSString *genreNames      = [artist objectForKey:@"genreNames"];
        
        NSString *url           = [ApiUtil makeWebPageURL:@"artist" contentId:artistId menuId:menuId];
        
        [resultList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:artistName, STR_NAME, genreNames, STR_ARTIST, url, STR_URL, nil]];
    }
    return resultList;
}

/**
 * 현재 세그먼트 인덱스 -  Define 요청값 연결
 */
-(NSInteger)getCurrentReqType:(NSInteger)segmentIndex
{
    switch (segmentIndex) {
        case 0:
            return REQ_SEARCH_SONG;
        case 1:
            return REQ_SEARCH_ALBUM;
        case 2:
            return REQ_SEARCH_ARTIST;
    }
    return 0;
}

#pragma mark AlertView Delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 0) {
        if (buttonIndex == NO) {
            [CommonUtil launchBrowser:launchURL];
        }
    }
}

@end
