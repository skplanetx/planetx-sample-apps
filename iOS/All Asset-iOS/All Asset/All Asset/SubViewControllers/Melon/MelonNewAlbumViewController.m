//
//  MelonNewAlbumViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "MelonNewAlbumViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define STR_NAME	@"Name"
#define STR_ARTIST	@"Artist"
#define STR_URL		@"Url"

@interface MelonNewAlbumViewController ()

@end

@implementation MelonNewAlbumViewController

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
- (void)setupData
{
    itemListArray = [[NSMutableArray alloc] init];
    
    [self performSelector:@selector(requestNewAlbumList) withObject:nil afterDelay:0.1f];
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
        NSLog(@"[MelonNewAlbumViewController] exception occured : %@", [exception description]);
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
 * 설명 : 멜론 최신앨범
 * RequestURI : http://apis.skplanetx.com/melon/newreleases/albums
 */
- (void)requestNewAlbumList
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [ApiUtil getRequestURL:SERVER index:REQ_NEW_ALBUM];
    
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
    [params setValue:@"1" forKey:@"version"];  //API의 버전 정보입니다.
    [params setValue:@"1" forKey:@"page"];     //조회할 목록의 페이지를 지정합니다.(값이 0이면 1페이지의 100개 데이터를 조회)
    [params setValue:@"10" forKey:@"count"];   //페이지당 출력되는 앨범 수를 지정합니다.
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
    
    [ApiUtil errorAlert:dic];
    
    //menuId
    NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
    NSMutableArray *albumList = [[[dic objectForKey:@"melon"] objectForKey:@"albums"] objectForKey:@"album"];
    
    NSMutableArray *artistList = [NSMutableArray new];
    
    if (albumList != nil) {
        for (NSDictionary *album in albumList) {
            NSString *albumId        = [album objectForKey:@"albumId"];
            NSString *albumName      = [album objectForKey:@"albumName"];
            
            NSMutableArray *artistArray = [[album objectForKey:@"repArtists"] objectForKey:@"artist"];
            NSString *artistName = @"";
            
            //아티스트가 1명 이상인 경우
            if (artistArray.count > 1) {
                for (NSDictionary *dicArtist in artistArray) {
                    artistName = [artistName stringByAppendingFormat:@",%@", [dicArtist objectForKey:@"artistName"]];
                }
                artistName = [artistName substringFromIndex:1];
            } else {
                artistName    = [[[[album objectForKey:@"repArtists"] objectForKey:@"artist"] objectAtIndex:0] objectForKey:@"artistName"];
            }
            
            NSString *url           = [ApiUtil makeWebPageURL:@"album" contentId:albumId menuId:menuId];
            
            [artistList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:albumName, STR_NAME, artistName, STR_ARTIST, url, STR_URL, nil]];
        }
        
        [itemListArray removeAllObjects];
        [itemListArray addObjectsFromArray:artistList];
        [itemTableView reloadData];
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == NO){
        [CommonUtil launchBrowser:launchURL];
    }
}

@end
