//
//  MelonSearchViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "MelonTopChartViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "SBJson.h"
#import "CommonUtil.h"

#define STR_NAME	@"Name"
#define STR_ARTIST	@"Artist"
#define STR_URL		@"Url"

@interface MelonTopChartViewController ()

@end

@implementation MelonTopChartViewController

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
 * 멜론 음악차트 API를 이용하여 최신 곡 데이터 조회
 */
- (void)setupData
{
    itemListArray = [[NSMutableArray alloc] init];

    [self performSelector:@selector(requestRealtimeChart) withObject:nil afterDelay:0.1f];
    //[self requestChartList];
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
        NSLog(@"[MelonTopChartViewController] exception occured : %@", [exception description]);
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

- (IBAction)pressedSegement:(id)sender
{
    NSInteger index = categorySegement.selectedSegmentIndex;
    
    SEL currentSelector = nil;
    switch (index) {
        case 0:
            currentSelector = @selector(requestRealtimeChart);
            break;
        case 1:
            currentSelector = @selector(requestWeeklyTopChart);
            break;
        case 2:
            currentSelector = @selector(requestAlbumTopChart);
            break;
    }
    [self performSelector:currentSelector withObject:nil afterDelay:0.1f];
}

#pragma mark API request
/**
 * 설명 : 멜론 실시간 차트
 * RequestURI : http://apis.skplanetx.com/melon/charts/realtime
 */
- (void)requestRealtimeChart
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER stringByAppendingString:MELON_REALTIME_CHART];
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil
                                                      url:url
                                                   params:[self makeParams:@"100"]
                                                  payload:nil
                                           uploadFilePath:nil
                                               httpMethod:SKPopHttpMethodGET
                                              requestType:SKPopContentTypeFORM
                                             responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

/**
 * 멜론 [장르차트(가요)] 조회 API 호출
 * 호출URL : http://apis.skplanetx.com/melon/charts/topgenres
 */
- (void)requestWeeklyTopChart
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER stringByAppendingString:MELON_WEEKLY_TOP_CHART];
    
    //Bundle 생성
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil
                                                      url:url
                                                   params:[self makeParams:@"100"]
                                                  payload:nil
                                           uploadFilePath:nil
                                               httpMethod:SKPopHttpMethodGET
                                              requestType:SKPopContentTypeFORM
                                             responseType:SKPopContentTypeJSON];
    
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

/**
 * 멜론 [앨범 차트] 조회 API 호출
 * 호출URL : http://apis.skplanetx.com/melon/charts/topalbums
 */
- (void)requestAlbumTopChart
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER stringByAppendingString:MELON_ALBUM_TOP_CHART];
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil
                                                      url:url
                                                   params:[self makeParams:@"20"]
                                                  payload:nil uploadFilePath:nil
                                               httpMethod:SKPopHttpMethodGET
                                              requestType:SKPopContentTypeFORM
                                             responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

//Querystring Parameters
- (NSMutableDictionary *)makeParams:(NSString *)count
{
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];   //API의 버전 정보입니다.
    [params setValue:@"1" forKey:@"page"];      //조회할 목록의 페이지를 지정합니다.
    [params setValue:count forKey:@"count"];    //페이지당 출력되는 곡(or 앨범 or 아티스트) 수를 지정합니다.
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
    
    //에러 메시지 발생시 경고창 띄움
    [ApiUtil errorAlert:dic];
    
    if (itemListArray != nil) {
        [itemListArray removeAllObjects];
    }
    
    NSMutableArray *artistList = [NSMutableArray new];
    
    if (categorySegement.selectedSegmentIndex != 2) {
        //menuId
        NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
        NSMutableArray *songList = [[[dic objectForKey:@"melon"] objectForKey:@"songs"] objectForKey:@"song"];
        
        if (songList != nil) {
            for (NSDictionary *song in songList) {
                NSString *songId        = [song objectForKey:@"songId"];
                NSString *songName      = [song objectForKey:@"songName"];
                NSString *currentRank   = [song valueForKey:@"currentRank"];
                
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
                
                NSString *combinedSongName = [NSString stringWithFormat:@"%@. %@", currentRank, songName];
                
                NSString *url           = [ApiUtil makeWebPageURL:@"song" contentId:songId menuId:menuId];
                
                [artistList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:combinedSongName, STR_NAME, artistName, STR_ARTIST, url, STR_URL, nil]];
            }
        }
    } else {
        //menuId
        NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
        NSMutableArray *albumList = [[[dic objectForKey:@"melon"] objectForKey:@"albums"] objectForKey:@"album"];
        
        if (albumList != nil) {
            for (NSDictionary *album in albumList) {
                NSString *albumId           = [album objectForKey:@"albumId"];
                NSString *albumName         = [album objectForKey:@"albumName"];
                NSString *currentRank       = [album valueForKey:@"currentRank"];
                
                NSMutableArray *artistArray = [[album objectForKey:@"artists"] objectForKey:@"artist"];
                NSString *artistName = @"";
                
                //아티스트가 1명 이상인 경우
                if (artistArray.count > 1) {
                    for (NSDictionary *dicArtist in artistArray) {
                        artistName = [artistName stringByAppendingFormat:@",%@", [dicArtist objectForKey:@"artistName"]];
                    }
                    artistName = [artistName substringFromIndex:1];
                } else {
                    artistName    = [[[[album objectForKey:@"artists"] objectForKey:@"artist"] objectAtIndex:0] objectForKey:@"artistName"];
                }
                
                NSString *url           = [ApiUtil makeWebPageURL:@"album" contentId:albumId menuId:menuId];
                
                NSString *combinedAlbumName = [NSString stringWithFormat:@"%@. %@", currentRank, albumName];
                
                [artistList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:combinedAlbumName, STR_NAME, artistName, STR_ARTIST, url, STR_URL, nil]];
            }
        }
    }

    if (itemListArray != nil) {
        [itemListArray addObjectsFromArray:artistList];
    }
    //테이블 데이터 갱신
    [itemTableView reloadData];
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
