//
//  TmapAreaDetailInfoViewController.m
//  All Asset
//
//  Created by Jason Nam on 12. 11. 7..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "TmapAreaDetailInfoViewController.h"
#import "TmapAreaInfoTableViewCell.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

@interface TmapAreaDetailInfoViewController ()

@end

@implementation TmapAreaDetailInfoViewController
@synthesize requestInfoDictionary;

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

    [self initLocationManager];
    [self startUpdateMyLocation];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [CommonUtil commonStopActivityIndicator];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

/**
 * 주변 정보 API 호출
 */
- (void)setDetailAreaInfoList
{
    if (itemListArray == nil)
    {
        [NSMutableArray new];
    }
    [self requestNearArea:requestInfoDictionary];
}

#pragma mark LocationManager init & delegate
- (void)initLocationManager
{
    if (locationManager == nil) {
        locationManager = [CLLocationManager new];
    }
    locationManager.delegate = self;
    locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
}

- (void)startUpdateMyLocation
{
    if (locationManager != nil) {
        [locationManager startUpdatingLocation];
    }
}

- (void)stopUpdateMyLocation
{
    if (locationManager != nil) {
        [locationManager stopUpdatingLocation];
    }
}

- (void)setupNearPlaceDetailList
{
    if (itemListArray == nil) {
        itemListArray = [NSMutableArray new];
    }
    [self requestNearArea:requestInfoDictionary];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    if (strStartLongitutde == nil && strStartLatitude == nil) {
        NSLog(@"latitude : %f, longitude : %f", newLocation.coordinate.latitude, newLocation.coordinate.longitude);
        
        strStartLongitutde   = [NSString stringWithFormat:@"%f", newLocation.coordinate.longitude];
        strStartLatitude     = [NSString stringWithFormat:@"%f", newLocation.coordinate.latitude];
        
        NSLog(@"lat : %@, lon : %@", strStartLatitude, strStartLongitutde);
        
        //[self requestNearArea:requestInfoDictionary];
        [self setupNearPlaceDetailList];
    }
    
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
    NSLog(@"current CLAuthorizationStatus : %d", status);
}

- (void)currentStatusCheck
{
    int currentStatus = [CLLocationManager authorizationStatus];
    
    if (currentStatus != kCLAuthorizationStatusAuthorized) {
        [CommonUtil commonAlertView:@"환경설정 위치조회가 Off 상태입니다."];
        return;
    }
}

#pragma mark UITableViewDataSource methods
/**
 * 카테고리 목록의 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(itemListArray != nil)
        return [itemListArray count];
    return 0;
}

/**
 * 테이블에 카테고리 정보를 표시한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    TmapAreaInfoTableViewCell *cell = (TmapAreaInfoTableViewCell *)[tableView dequeueReusableCellWithIdentifier:nil];
    
    if (cell == nil)
	{
		NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"TmapAreaInfoTableViewCell" owner:nil options:nil];
        cell = [array objectAtIndex:0];
        cell.tag = indexPath.section*1000+indexPath.row;
    }
    
    if(itemListArray!=nil)
    {
        NSDictionary *dic = [itemListArray objectAtIndex:indexPath.row];
        
        if(dic!=nil)
        {
            cell.placename.text  = [dic valueForKey:STR_SHOPNAME];
            cell.placeContent.text = [dic valueForKey:STR_CONTENT];
            cell.placeTel.text = [dic valueForKey:STR_TEL];
            cell.placeAddress.text = [dic valueForKey:STR_ADDRESS];
        }
    }
    
    return cell;
}

#pragma mark UITableViewDelegate methods
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 160.0;
}

/**
 * 특정 컬럼을 선택 할 시 UIAlertView 를 Popup 하여 메시지 전송하게 한다.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

#pragma mark API request
/**
 * 설명 : T map POI 주변 검색
 * RequestURI : https://apis.skplanetx.com/tmap/pois/around
 */
- (void)requestNearArea:(NSDictionary *)requestInfo
{
    if (requestInfo == nil) {
        [CommonUtil commonAlertView:@"요청 파라메터 누락입니다."];
        return;
    }
    
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:TMAP_NEAR_PLACE];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    
    [params setValue:@"category" forKey:@"searchType"];                             //검색 종류입니다.(name: 명칭검색, category: 업종검색)
    [params setValue:[requestInfo valueForKey:@"BizCode"] forKey:@"classLCode"];    //업종 대분류 코드입니다.
    [params setValue:@"01" forKey:@"classMCode"];                                   //업종 중분류 코드입니다
    [params setValue:strStartLatitude forKey:@"noorLat"];                           //위도 좌표값입니다.
    [params setValue:strStartLongitutde forKey:@"noorLon"];                         //경도 좌표값입니다.
    [params setValue:@"WGS84GEO" forKey:@"reqCoordType"];                           //요청 좌표계 유형을 지정합니다.
    [params setValue:@"1" forKey:@"version"];                                       //API의 버전 정보입니다.
    
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
    
    if (itemListArray != nil) {
        itemListArray = [NSMutableArray new];
    }
    
    for (NSDictionary *poi in poiList) {
        NSString *telNo             = [poi valueForKey:@"telNo"];
        NSString *name              = [poi valueForKey:@"name"];
        NSString *desc              = [poi valueForKey:@"desc"];
        NSString *upperAddrName     = [poi valueForKey:@"upperAddrName"];
        NSString *middleAddrName    = [poi valueForKey:@"middleAddrName"];
        NSString *lowerAddrName     = [poi valueForKey:@"lowerAddrName"];
        NSString *firstNo           = [poi valueForKey:@"firstNo"];
        
        if ([desc isEqualToString:@""]) {
            desc = @"설명 없음";
        }
        
        NSString *combinedAddress = [NSString stringWithFormat:@"%@ %@ %@ %@", upperAddrName, middleAddrName, lowerAddrName, firstNo];
        
        if (itemListArray != nil) {
            [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:name, STR_SHOPNAME, desc, STR_CONTENT, telNo, STR_TEL, combinedAddress, STR_ADDRESS, nil]];
        }
    }
    
    [itemTableView reloadData];
}
@end
