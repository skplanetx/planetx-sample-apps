//
//  MelonNewSongViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "MelonNewSongViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define STR_NAME	@"Name"
#define STR_ARTIST	@"Artist"
#define STR_URL		@"Url"

@interface MelonNewSongViewController ()

@end

@implementation MelonNewSongViewController

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
    [self setupData];
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
 * 멜론 최신곡 API를 이용하여 최신 곡 데이터 조회
 */
- (void)setupData
{
    itemListArray = [[NSMutableArray alloc] init];
    
    [self performSelector:@selector(requestNewSongList) withObject:nil afterDelay:0.1f];
    //[self requestNewSongList];
}

- (void)initData
{
    if (itemListArray != nil) {
        [itemListArray removeAllObjects];
    }
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
        NSLog(@"[MelonNewSongViewController] exception occured : %@", [exception description]);
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

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark API request
/**
 * 설명 : 멜론 최신곡
 * RequestURI : http://apis.skplanetx.com/melon/newreleases/songs
 */
- (void)requestNewSongList
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [ApiUtil getRequestURL:SERVER index:REQ_NEW_SONG];
    
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
    [params setValue:@"1" forKey:@"version"];   //API의 버전 정보입니다.
    [params setValue:@"1" forKey:@"page"];      //조회할 목록의 페이지를 지정합니다.(값이 0이면 1페이지의 100개 데이터를 조회)
    [params setValue:@"10" forKey:@"count"];    //페이지당 출력되는 곡 수를 지정합니다.
    return params;
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
    
    //에러 리턴시 Alert 처리
    [ApiUtil errorAlert:dic];
    
    //데이터 파싱
    NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
    NSMutableArray *songList = [[[dic objectForKey:@"melon"] objectForKey:@"songs"] objectForKey:@"song"];
    
    NSMutableArray *artistList = [NSMutableArray new];
    
    if (songList != nil) {
        for (NSDictionary *song in songList) {
            NSString *songId        = [song objectForKey:@"songId"];
            NSString *songName      = [song objectForKey:@"songName"];
            NSString *artistName    = [[[[song objectForKey:@"artists"] objectForKey:@"artist"] objectAtIndex:0] objectForKey:@"artistName"];
            NSString *url           = [ApiUtil makeWebPageURL:@"song" contentId:songId menuId:menuId];
            
            [artistList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:songName, STR_NAME, artistName, STR_ARTIST, url, STR_URL, nil]];
        }
        
        [itemListArray removeAllObjects];
        [itemListArray addObjectsFromArray:artistList];
        [itemTableView reloadData];
    }
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
