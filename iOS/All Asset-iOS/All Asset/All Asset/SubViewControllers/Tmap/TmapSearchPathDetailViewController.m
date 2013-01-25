//
//  TmapSearchPathDetailViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "TmapSearchPathDetailViewController.h"
#import "TmapSearchPathDetailTableViewCell.h"
#import <CoreLocation/CoreLocation.h>
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"
#import "NSXMLDocument.h"
#import "NSXMLElement.h"
#import "TMapType.h"

#define STR_NAME            @"Name"
#define STR_SECTIONNAME		@"SectionName"
#define STR_DESCRIPTION     @"Description"
#define STR_DIRECTION		@"Direction"
#define STR_DISTANCE		@"Distance"

#define STR_LAT             @"Latitude"
#define STR_LON             @"Longitude"

#define REQ_POI             100
#define REQ_REVERSE_GEOCODE 101
#define REQ_DETAIL_PATH     103

#define TYPE_POINT          @"Point"
#define TYPE_LINE           @"LineString"

#define NO_DISTANCE         @"-999"

@interface TmapSearchPathDetailViewController ()
{
    NSString *strStartLongitude;
    NSString *strStartLatitude;
    NSString *strDepartureLongitude;
    NSString *strDepartureLatitude;
    NSString *currentMyAddress;
    
    NSXMLParser *parser;                //파서
    NSMutableArray *placemarkArray;     //파싱된 데이터가 들어갈 배열
    NSString *parsedString;             //elementArray에 object로 들어갈 dictionary
    NSMutableDictionary *placemarkDic;
}
@end

@implementation TmapSearchPathDetailViewController
@synthesize targetDictionary, locationManager, mapView, favoritePathMode, targetArray;

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
    // Do any additional setup after loading the view from its nib.
    
    [self initLocationManager];
    [self startUpdateMyLocation];
    
    if (favoritePathMode) {
        targetLabel.text = [[targetArray objectAtIndex:1] valueForKey:STR_NAME];
    } else {
        targetLabel.text = [targetDictionary valueForKey:STR_NAME];
    }
    
    [self setArrivalTime:0];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
    isRequestReverseGeocoding = YES;
    
    if (locationManager != nil) {
        [locationManager startUpdatingLocation];
    }
}

- (void)stopUpdateMyLocation
{
    isRequestReverseGeocoding = NO;
    
    if (locationManager != nil) {
        [locationManager stopUpdatingLocation];
    }
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    if (strStartLongitude == nil && strStartLatitude == nil) {
        
        strStartLongitude   = [NSString stringWithFormat:@"%f", newLocation.coordinate.longitude];
        strStartLatitude     = [NSString stringWithFormat:@"%f", newLocation.coordinate.latitude];
        
        [self requestReverseGeocoding];
    }
    
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{

}

- (void)currentStatusCheck
{
    int currentStatus = [CLLocationManager authorizationStatus];
    
    if (currentStatus != kCLAuthorizationStatusAuthorized) {
        [CommonUtil commonAlertView:@"환경설정 위치조회가 Off 상태입니다."];
        return;
    }
}

#pragma mark SKP OPR API
-(void)setupData
{
    sectionListArray = [[NSMutableArray alloc] init];
    lineListArray = [[NSMutableArray alloc] init];
    
    [self requestPathDetail];
}

- (void)setupTmapView
{
    if (mapView == nil) {
        mapView = [[TMapView alloc] initWithFrame:CGRectMake(0, 147, 320, 313)];
        
        //API Set
        [mapView setSKPMapApiKey:MY_APP_KEY];
        [mapView setLanguage:KOREAN];
        [mapView setIconVisibility:YES];
        [mapView setZoomLevel:15];
        //[mapView setCompassMode:YES];
        if (favoritePathMode) {
            [mapView setCenterPoint:[[TMapPoint alloc] initWithLon:[[[targetArray objectAtIndex:0] valueForKey:STR_LON] doubleValue] Lat:[[[targetArray objectAtIndex:0] valueForKey:STR_LAT] doubleValue]]];
        } else {
            [mapView setCenterPoint:[[TMapPoint alloc] initWithLon:[strStartLongitude doubleValue] Lat:[strStartLatitude doubleValue]]];
            
            [mapView setTrackingMode:YES];
        }
    }
    
    [self.view addSubview:mapView];

    [self findPath];

}

- (void)findPath
{
    if (mapView != nil) {
        TMapPoint *startPoint = nil;
        TMapPoint *endpoint = nil;
        
        if (favoritePathMode) {
            startPoint = [[TMapPoint alloc] initWithLon:[[[targetArray objectAtIndex:0] valueForKey:STR_LON] doubleValue] Lat:[[[targetArray objectAtIndex:0] valueForKey:STR_LAT] doubleValue]];
            endpoint = [[TMapPoint alloc] initWithLon:[[[targetArray objectAtIndex:1] valueForKey:STR_LON] doubleValue] Lat:[[[targetArray objectAtIndex:1] valueForKey:STR_LAT] doubleValue]];
        } else {
            startPoint = [[TMapPoint alloc] initWithLon:[strStartLongitude doubleValue] Lat:[strStartLatitude doubleValue]];
            endpoint = [[TMapPoint alloc] initWithLon:[[targetDictionary valueForKey:STR_LON] doubleValue] Lat:[[targetDictionary valueForKey:STR_LAT] doubleValue]];
        }

        TMapPathData *path = [TMapPathData new];
        TMapPolyLine *polyLine = [path findPathDataFrom:startPoint to:endpoint];
        
        if (polyLine == nil) {
            return;
        }
        
        if ([[polyLine getLinePoint] count] == 0)
            return;
        
        // 출발, 도착 아이콘 설정
        TMapPoint* start = [[polyLine getLinePoint] objectAtIndex:0];
        TMapPoint* end = [[polyLine getLinePoint] lastObject];
        TMapMarkerItem* startMarkerItem = [[TMapMarkerItem alloc]initWithTMapPoint:start];
        [startMarkerItem setIcon:[UIImage imageNamed:@"TrackingDot.png"] anchorPoint:CGPointMake(0.4, 1.0)];
        TMapMarkerItem* endMarkerItem = [[TMapMarkerItem alloc]initWithTMapPoint:end];
        [endMarkerItem setIcon:[UIImage imageNamed:@"TrackingDot.png"]anchorPoint:CGPointMake(0.5, 1.0)];
        //시작점은 별도로 표시하지 않고 nil 처리
        if (favoritePathMode) {
            [mapView setTMapPathIconStart:startMarkerItem End:endMarkerItem];
        } else {
            [mapView setTMapPathIconStart:nil End:endMarkerItem];
        }

        // 라인
        if(polyLine)
        {
            [mapView addTMapPath:polyLine];
        }
    }
}

#pragma mark UITableViewDataSource methods
/**
 * 구간 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(sectionListArray!=nil)
    {
        NSString *distance = NO_DISTANCE;
        NSMutableDictionary *lineDic = [NSMutableDictionary new];
        
        [lineDic setValue:distance forKey:STR_DISTANCE];
        [lineListArray addObject:lineDic];
        
        NSLog(@"sectionListArray count : %d, lineListArray count : %d", [sectionListArray count], [lineListArray count]);
        return [sectionListArray count];
    }
    return 0;
}

/**
 * 테이블에 구간 정보를 표시한다.
 * CustomCell 인 ElevenstItemListTableViewCell 을 사용한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    TmapSearchPathDetailTableViewCell *cell = (TmapSearchPathDetailTableViewCell *)[tableView dequeueReusableCellWithIdentifier:nil];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"TmapSearchPathDetailTableViewCell" owner:nil options:nil];
        cell = [array objectAtIndex:0];
        cell.textLabel.font = [UIFont systemFontOfSize:16];
        cell.tag = indexPath.section*1000+indexPath.row;
        if(sectionListArray!=nil)
        {
            NSString *section = [[sectionListArray objectAtIndex:indexPath.row] valueForKey:STR_SECTIONNAME];
            if([section length]>0)
                cell.section.text = [[sectionListArray objectAtIndex:indexPath.row] valueForKey:STR_SECTIONNAME];
            else
                cell.section.text = @"구간 정보 없음";
            cell.direction.text = [[sectionListArray objectAtIndex:indexPath.row] valueForKey:STR_DIRECTION];
            NSString *distance = [[lineListArray objectAtIndex:indexPath.row] valueForKey:STR_DISTANCE];
            if ([distance isEqualToString:NO_DISTANCE] == NO) {
                distance = [distance stringByAppendingString:@"m"];
            } else {
                distance = @"";
            }
            cell.distance.text = distance;
            cell.description.text = [[sectionListArray objectAtIndex:indexPath.row] valueForKey:STR_DESCRIPTION];
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 58.0;
}
/**
 * 특정 컬럼을 선택 할 시 해당 URL 을 기준으로 브라우저 호출.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

#pragma mark IBAction methods
-(IBAction)pressedHomeButtonItem:(id)sender
{
    if (mapView != nil) {
        [mapView removeFromSuperview];
        mapView = nil;
    }
    favoritePathMode = NO;
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(IBAction)changeViewModeSegment:(id)sender
{
    if (viewModeSegment.selectedSegmentIndex == 0)
    {
        [self setupTmapView];
    } else {
        [mapView removeFromSuperview];
        [self setupData];
    }
}

#pragma mark API request
/**
 * 설명 : T map POI 통합 검색
 * RequestURI : https://apis.skplanetx.com/tmap/pois
 */
- (void)requestPOI:(NSString *)searchKeyword
{
    reqType = REQ_POI;
    
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:TMAP_POI_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    
    [params setValue:searchKeyword forKey:@"searchKeyword"];    //검색어입니다.
    [params setValue:@"1" forKey:@"version"];                   //API의 버전 정보입니다.
    [params setValue:@"WGS84GEO" forKey:@"resCoordType"];       //받고자 하는 응답 좌표계 유형을 지정합니다.(WGS84GEO: 경위도)
    
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

/**
 * 설명 : T map Reverse Geocoding
 * RequestURI : https://apis.skplanetx.com/tmap/geo/reversegeocoding
 */
- (void)requestReverseGeocoding
{
    [self currentStatusCheck];
    
    reqType = REQ_REVERSE_GEOCODE;
    
    isRequestReverseGeocoding = true;
    
    NSString *url = [SERVER_SSL stringByAppendingString:TMAP_REVERSE_GEO_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];
    [params setValue:strStartLatitude forKey:@"lat"];
    [params setValue:strStartLongitude forKey:@"lon"];
    [params setValue:@"WGS84GEO" forKey:@"coordType"];
    
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

/**
 * 설명 : T map 자동차 경로안내
 * RequestURI : https://apis.skplanetx.com/tmap/routes
 */
- (void)requestPathDetail
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    reqType = REQ_DETAIL_PATH;
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:TMAP_ROUTES_PATH];
    
    if (favoritePathMode) {
        strStartLatitude        = [[targetArray objectAtIndex:0] valueForKey:STR_LAT];
        strStartLongitude       = [[targetArray objectAtIndex:0] valueForKey:STR_LON];
        strDepartureLatitude    = [[targetArray objectAtIndex:1] valueForKey:STR_LAT];
        strDepartureLongitude   = [[targetArray objectAtIndex:1] valueForKey:STR_LON];
    } else {
        strDepartureLatitude    = [targetDictionary valueForKey:STR_LAT];
        strDepartureLongitude   = [targetDictionary valueForKey:STR_LON];
    }
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];
    [params setValue:strStartLatitude forKey:@"startY"];
    [params setValue:strStartLongitude forKey:@"startX"];
    [params setValue:strDepartureLatitude forKey:@"endY"];
    [params setValue:strDepartureLongitude forKey:@"endX"];
    [params setValue:@"WGS84GEO" forKey:@"reqCoordType"];
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil
                                                      url:url
                                                   params:params
                                                  payload:nil
                                           uploadFilePath:nil
                                               httpMethod:SKPopHttpMethodPOST
                                              requestType:SKPopContentTypeFORM
                                             responseType:SKPopContentTypeKML];
    
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
- (void)resultParseAndSave:(NSString *)resultData
{
    if (reqType == REQ_REVERSE_GEOCODE) {
        
        NSDictionary *dic       = [resultData JSONValue];
        
        //에러코드 리턴시 Alert 처리
        [ApiUtil errorAlert:dic];
        
        NSMutableDictionary *addressInfo = [dic objectForKey:@"addressInfo"];
        
        currentMyAddress = [addressInfo valueForKey:@"fullAddress"];
        
        [self setupData];
    } else if (reqType == REQ_DETAIL_PATH) {
        NSData* data = [resultData dataUsingEncoding: NSUTF8StringEncoding];
        NSXMLDocument *document = [[NSXMLDocument alloc] initWithData:data];
        
        //Error rootElement
        NSXMLElement *rootElement = document.rootElement;
        
        if ([[rootElement Name] isEqualToString:@"error"]) {
            NSXMLElement *message = [rootElement objectForKey:@"message"];
            [CommonUtil commonAlertView:[message Value]];
            return;
        }
        
        NSXMLElement *doc = [rootElement objectForKey:@"Document"];
        NSXMLElement *rsdTotDtm = [doc objectForKey:@"tmap:totalTime"];
        arrivalTime = [[rsdTotDtm Value] intValue];
        [self setArrivalTime:arrivalTime];
        
        //document Element의 Child정보를 받는다.
        NSArray *documentList = doc.Childs;
        
        for (NSXMLElement *e in documentList) {
            if([e.Name isEqualToString:@"Placemark"]) {
                //포인토 및 라인 정보(실제 상세정보)
                NSXMLElement *placemark = e;
                
                NSMutableArray *placemarkList = placemark.getChildsToDictionary;
                
                for (NSDictionary *placemark in placemarkList) {
                    if ([self containsKey:placemark key:@"tmap:pointIndex"]) {
                        NSString *name = [[NSString alloc] initWithData:[[placemark valueForKey:@"name"] dataUsingEncoding: NSUTF8StringEncoding] encoding:NSUTF8StringEncoding];
                        NSString *description = ([name isEqualToString:@"도착지"] == YES ? @"" :[placemark valueForKey:@"description"]);
                        NSString *turnType = ([name isEqualToString:@"도착지"] == YES ? @"" :[ApiUtil makeTurnType:[[placemark valueForKey:@"tmap:turnType"] intValue]]);
                        
                        NSMutableDictionary *pathDic = [NSMutableDictionary new];
                        
                        [pathDic setValue:name forKey:STR_SECTIONNAME];
                        [pathDic setValue:description forKey:STR_DESCRIPTION];
                        [pathDic setValue:turnType forKey:STR_DIRECTION];
                        [sectionListArray addObject:pathDic];
                    } else if ([self containsKey:placemark key:@"tmap:lineIndex"]) {
                        NSString *distance = [placemark valueForKey:@"tmap:distance"];
                        
                        NSMutableDictionary *lineDic = [NSMutableDictionary new];
                        [lineDic setValue:distance forKey:STR_DISTANCE];
                        [lineListArray addObject:lineDic];
                    }
                }
            }
        }
        
        if (viewModeSegment.selectedSegmentIndex == 0) {
            [self setupTmapView];
        } else {
            [itemTableView reloadData];
        }
    }
    
    if (isRequestReverseGeocoding == NO) {
        [self stopUpdateMyLocation];
    }
}

//도착시간 환산값 label 세팅
- (void)setArrivalTime:(int)totaltime
{
    NSInteger hour = totaltime/3600;
    NSInteger min = (totaltime%3600)/60;
    NSInteger sec = (totaltime%3600)%60;
    NSString *humanTime = @"도착시간:";
    if(hour>0)
        humanTime = [humanTime stringByAppendingFormat:@"%d시 %d분 %d초", hour, min, sec];
    else
        humanTime = [humanTime stringByAppendingFormat:@"%d분 %d초", min, sec];
    arrivalTimeLabel.text = humanTime;
}

//pointIndex, lineIndex 가지고 있는지 확인
- (BOOL)containsKey:(NSDictionary *)dic key:(NSString *)key {
    BOOL retVal = 0;
    NSArray *allKeys = [dic allKeys];
    retVal = [allKeys containsObject:key];
    return retVal;
}
@end
