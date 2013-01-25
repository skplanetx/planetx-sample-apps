//
//  MainViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "MainViewController.h"
#import "AppDelegate.h"
#import "ElevenstItemListViewController.h"
#import "TmapSearchPathDetailViewController.h"
#import "ConfigurationViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SKPOP_v1.4.h"
#import "SBJsonParser.h"
#import "SBJson.h"
#import "AllAssetDB.h"
#import "NSXMLDocument.h"
#import <QuartzCore/QuartzCore.h>

#define STR_NAME            @"Name"
#define STR_ID              @"Id"
#define STR_ARTIST          @"Artist"
#define STR_URL             @"Url"
#define STR_CONTENT         @"Content"

#define TAG_OAUTH_FAILED    10
#define TAG_REQUEST_FAILED  100

@interface MainViewController ()

@property(nonatomic, weak) IBOutlet UIView *liveView;
@property(nonatomic, weak) IBOutlet UIView *shortcutView;

-(void)setLiveLabel;
-(void)setLiveTextLabel;
-(void)setShortcutButton;
-(void)setShortcutButton2;  //neep305
-(void)addLiveLabel:(UIView*)superView frame:(CGRect)frame text:(NSString*)text;
-(void)addMarqueeLabel:(UIView*)superView frame:(CGRect)frame text:(NSString*)text action:(SEL)action;
-(void)addShortcutButton:(UIView*)superView frame:(CGRect)frame text:(NSString*)text isTop:(BOOL)isTop action:(SEL)action;

@end

@implementation MainViewController
@synthesize liveView, shortcutView;
@synthesize oAuthDic;   //neep305

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
	AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
	[appDelegate loadConfiguration];
    /*[[UITabBarItem appearance] setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:[UIFont fontWithName:@"System Bold" size:32.0f], UITextAttributeFont, nil] forState:UIControlStateNormal];*/
    [[UITabBarItem appearance] setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                            [UIFont fontWithName:@"Helvetica-Bold" size:14.0], UITextAttributeFont, nil]
                                  forState:UIControlStateNormal];
    [[UITabBarItem appearance] setTitlePositionAdjustment:UIOffsetMake(0.0, -16.0)];
    
    // API 호출 (performSelector로 안하면 Device에서 죽음)
    [self performSelector:@selector(setOAuth:) withObject:[ApiUtil getAuthParams] afterDelay:0.1f];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

    [self performSelector:@selector(requestNateOnMessage) withObject:nil afterDelay:0.1f];
    //[self performSelector:@selector(requestElevenst) withObject:nil afterDelay:0.1f];
    
    // 설정 Property List 읽는다.
    //[self updateAllView];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [CommonUtil commonStopActivityIndicator];
}

- (void)updateAllView
{
    // View를 동적으로 생성하기 위하여 Subview 들을 삭제하고 새로 생성한다.
    [self removeSubview];
    // 라이브 라벨을 표시한다.
    [self setLiveLabel];
    // 라이브 텍스트 데이터를 표시한다.
    [self setLiveTextLabel];
    // 단축 버튼 텍스트를 표시한다.
    [self setShortcutButton2];
}
     
-(void) removeSubview
{
    for(UIView *subview in [self.liveView subviews])
    {
        [subview removeFromSuperview];
    }
    for(UIView *subview in [self.shortcutView subviews])
    {
        [subview removeFromSuperview];
    }
}

#pragma setLiveLabel
/**
 * 라이브 영역 라벨 생성
 */
- (void)setLiveLabel
{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 0, 152, 21)];
    label.backgroundColor = [UIColor colorWithRed:240/255.0 green:240/255.0 blue:240/255.0 alpha:1];
    label.text = TITLE_TEXT;
    label.font = [UIFont fontWithName:@"Helvetica-Bold" size:16.000];
    label.textAlignment =  NSTextAlignmentLeft;
    [liveView addSubview:label];
    
    NSInteger startX= 20, startY =30, spaceY = 27, width=67, height = 24;

    // 라이브 노출 라벨 데이터를 가져 온다.
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    NSMutableArray *liveService = [appDelegate getLiveService];

    if(liveService!=nil)
    {
        for(int index=0;index<[liveService count];index++)
        {
            [self addLiveLabel:liveView frame:CGRectMake(startX, startY+(spaceY*index), width, height) text:[[liveService objectAtIndex:index] objectForKey:CONFIGURATION_KEY_NAME]];
        }
    }
}

#pragma setLiveTextLabel
/**
 * 라이브 영역 데이터 라벨 생성
 */
- (void)setLiveTextLabel
{
    //TODO: 라이브 데이터를 가져오는 루틴 추가 하여야 함. Wait Progress Status를 표시하여야 함. 데이터가 없으면 Property List에 설정된 기본값 표시.
    NSInteger startX= 95, startY =30, spaceY = 27, width=205, height = 24;

    // 라이브 서비스 정보를 가져 온다.
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    NSMutableArray *liveService = [appDelegate getLiveService];
    if(liveService!=nil)
    {
        for(int index=0;index<[liveService count];index++)
        {
            [self addMarqueeLabel:liveView frame:CGRectMake(startX, startY+(spaceY*index), width, height) text:[[liveService objectAtIndex:index] objectForKey:CONFIGURATION_KEY_DEFAULT_TEXT] action:[self getSelectorLiveService:[[liveService objectAtIndex:index]  objectForKey:CONFIGURATION_KEY_ID]]];
        }
    }
}

#pragma setShortcutButton
/**
 * 단축버튼 영역 버튼 생성
 */
-(void)setShortcutButton
{
    NSInteger topStartX= 20, topSpaceX = 144, topStartY =20, topWidth=136, topHeight = 90; // 상단 버튼 영역값
    NSInteger botStartX= 20, botSpaceX = 98, botStartY =118, botWidth=84, botHeight = 64; // 하단 버튼 영역값

    // 단축 버튼 라벨 데이터를 가져 온다.
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    NSMutableArray *shortcutService = [appDelegate getShortcutService];

    if(shortcutService!=nil)
    {
        for(int index=0;index<[shortcutService count];index++)
        {
            if(2>index) // 상단 버튼 - 최대 2개
                [self addShortcutButton:shortcutView frame:CGRectMake(topStartX+(topSpaceX*index), topStartY, topWidth, topHeight) text:[[shortcutService objectAtIndex:index]  objectForKey:CONFIGURATION_KEY_NAME] isTop:YES
                                 action:[self getSelectorShortcutService:[[shortcutService objectAtIndex:index]  objectForKey:CONFIGURATION_KEY_ID]]];
            else // 하단 버튼 -최대 3개
                [self addShortcutButton:shortcutView frame:CGRectMake(botStartX+(botSpaceX*(index-2)), botStartY, botWidth, botHeight) text:[[shortcutService objectAtIndex:index] objectForKey:CONFIGURATION_KEY_NAME] isTop:NO
                                 action:[self getSelectorShortcutService:[[shortcutService objectAtIndex:index]  objectForKey:CONFIGURATION_KEY_ID]]];
        }
    }
}

/**
 * 단축 버튼 일렬로 생성
 */
-(void)setShortcutButton2
{
    NSInteger topStartX = 20, topSpacingY = 5, topStartY = 20, topWidth = 280, topHeight = 30; // 상단 버튼 영역값
    
    // 단축 버튼 라벨 데이터를 가져 온다.
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    NSMutableArray *shortcutService = [appDelegate getShortcutService];
    
    if(shortcutService!=nil)
    {
        for(int index=0;index<[shortcutService count];index++)
        {
            NSInteger measuredStartY = (index <= 0 ? topStartY + (topHeight * index) :
                                        topStartY + (topHeight * index) + (topSpacingY * index));
            
            [self addShortcutButton:shortcutView
                              frame:CGRectMake(topStartX, measuredStartY, topWidth, topHeight)
                               text:[[shortcutService objectAtIndex:index] objectForKey:CONFIGURATION_KEY_NAME]
                              isTop:NO
                             action:[self getSelectorShortcutService:[[shortcutService objectAtIndex:index] objectForKey:CONFIGURATION_KEY_ID]]];
        }
    }
}

#pragma addLiveLabel
/**
 * frame 영역에 text 라벨을 추가
 */
-(void)addLiveLabel:(UIView*)superView frame:(CGRect)frame text:(NSString*)text
{
    UILabel *label = [[UILabel alloc] initWithFrame:frame];
    label.text = text;
    label.font = [UIFont systemFontOfSize:14];
    label.textAlignment =  NSTextAlignmentCenter;
    label.layer.borderColor = [UIColor blackColor].CGColor;
    label.layer.borderWidth = 1.0;
    label.layer.cornerRadius = 2.5;
    [superView addSubview:label];
}

/**
 * frame 영역에 text Marquee 라벨을 추가
 */
#pragma addMarqueeLabel
-(void)addMarqueeLabel:(UIView*)superView frame:(CGRect)frame text:(NSString*)text action:(SEL)action
{
    MarqueeLabel *marqueeLabel = [[MarqueeLabel alloc] initWithFrame:frame rate:50.0f andFadeLength:10.0f];
    marqueeLabel.marqueeType = MLContinuous;
    marqueeLabel.animationCurve = UIViewAnimationOptionCurveLinear;
    marqueeLabel.numberOfLines = 1;
    marqueeLabel.opaque = NO;
    marqueeLabel.enabled = YES;
    marqueeLabel.shadowOffset = CGSizeMake(0.0, -1.0);
    marqueeLabel.textAlignment = NSTextAlignmentLeft;
    marqueeLabel.textColor = [UIColor colorWithRed:0.234 green:0.234 blue:0.234 alpha:1.000];
    marqueeLabel.backgroundColor = [UIColor clearColor];
    marqueeLabel.font = [UIFont fontWithName:@"Helvetica-Bold" size:14.000];
    marqueeLabel.text = text;
    marqueeLabel.labelize = NO;
    marqueeLabel.userInteractionEnabled = YES;
    // UIView 이벤트 처리를 위하여 TapGesture 추가
    UITapGestureRecognizer *singleFingerTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:action];
    [marqueeLabel addGestureRecognizer:singleFingerTap];
    [superView addSubview:marqueeLabel];
}

/**
 * frame 영역에 text 버튼을 추가
 */
#pragma addShortcutButton
-(void)addShortcutButton:(UIView*)superView frame:(CGRect)frame text:(NSString*)text isTop:(BOOL)isTop action:(SEL)action
{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    button.frame = frame;
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:text forState:UIControlStateNormal];
    button.titleLabel.textColor = [UIColor colorWithRed:0.334 green:0.334 blue:0.334 alpha:1.000];
    button.titleLabel.textAlignment = NSTextAlignmentCenter;
    if(isTop)
        button.titleLabel.font = [UIFont systemFontOfSize:24];
    else
        button.titleLabel.font = [UIFont systemFontOfSize:16];
    button.layer.borderWidth = 2.0;
    button.layer.borderColor = [UIColor blackColor].CGColor;
    button.layer.cornerRadius = 6;
    [button addTarget:self action:action forControlEvents:UIControlEventTouchUpInside];

    [superView addSubview:button];
}

-(SEL)getSelectorLiveService:(NSString *)service
{
    if(service==nil)
        return nil;
    if ([service isEqualToString:SERVICE_NATEON])
        return @selector(presentNateonShortcutModal:);
    else if([service isEqualToString:SERVICE_CYWORLD])
        return @selector(presentCyworldShortcutModal:);
    else if([service isEqualToString:SERVICE_ELEVENST])
        return @selector(presentElevenstLiveModal:);
    else if([service isEqualToString:SERVICE_MELON])
        return @selector(presentMelonLiveModal:);
    else if([service isEqualToString:SERVICE_TMAP])
        return @selector(presentTmapLiveModal:);
    return nil;
}

-(void)presentConfigurationLiveModal:(id)sender
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UITabBarController *controller = [storyboard instantiateInitialViewController];
    controller.selectedIndex = 1;
    [self presentViewController:controller animated:YES completion:nil];
}

-(void)presentMelonLiveModal:(id)sender
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MelonStoryboard" bundle:nil];
    UITabBarController *controller = [storyboard instantiateInitialViewController];
    controller.selectedIndex = 2;
    [self presentViewController:controller animated:YES completion:nil];
}

-(void)presentTmapLiveModal:(id)sender
{
    //if ([self loadTmapRecentArray] == nil) {
    NSArray *array = [self loadTmapRecentArray];
    
    NSString *strStart  = [[array objectAtIndex:0] valueForKey:STR_NAME];
    NSString *strEnd    = [[array objectAtIndex:1] valueForKey:STR_NAME];
        //[CommonUtil commonAlertView:@"설정된 목적지가 없습니다."];
    if ([strStart length] == 0 || [strEnd length] == 0) {
        [self presentConfigurationLiveModal:nil];
        return;
    }
    //}
    
    TmapSearchPathDetailViewController *controller = [[TmapSearchPathDetailViewController alloc] initWithNibName:@"TmapSearchPathDetailViewController" bundle:nil];
    //최근 검색지를 가져와 이동
    //controller.targetDictionary = [self loadTmapRecent];
    controller.targetArray = [self loadTmapRecentArray];
    controller.favoritePathMode = YES;
    [self presentViewController:controller animated:YES completion:nil];
}

-(void)presentElevenstLiveModal:(id)sender
{
    //11번가 카테고리 설정이 되어있지 않으면 API 요청 안함
    if ([self isCategorySaved] == NO) {
        [self presentConfigurationLiveModal:nil];
        
        return;
    }
    
    [self performSelector:@selector(requestKeywordList:) withObject:[[self loadElevenstRecent] valueForKey:STR_NAME] afterDelay:0.1f];
}

- (void)presentNateonLiveModal:(id)sender
{
    
}

- (void)presentCyworldLiveModal:(id)sender
{

}

-(SEL)getSelectorShortcutService:(NSString *)service
{
    if(service==nil)
        return nil;
    if([service isEqualToString:SERVICE_NATEON])
        return @selector(presentNateonShortcutModal:);
    else if([service isEqualToString:SERVICE_CYWORLD])
        return @selector(presentCyworldShortcutModal:);
    else if([service isEqualToString:SERVICE_ELEVENST])
        return @selector(presentElevenstShortcutModal:);
    else if([service isEqualToString:SERVICE_MELON])
        return @selector(presentMelonShortcutModal:);
    else if([service isEqualToString:SERVICE_TMAP])
        return @selector(presentTmapShortcutModal:);
    return nil;
}

-(void)presentMelonShortcutModal:(id)sender
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MelonStoryboard" bundle:nil];
    UITabBarController *controller = [storyboard instantiateInitialViewController];
    [self presentViewController:controller animated:YES completion:nil];
}

-(void)presentTmapShortcutModal:(id)sender
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"TmapStoryboard" bundle:nil];
    UITabBarController *controller = [storyboard instantiateInitialViewController];
    [self presentViewController:controller animated:YES completion:nil];
}

-(void)presentElevenstShortcutModal:(id)sender
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"ElevenstStoryboard" bundle:nil];
    UITabBarController *controller = [storyboard instantiateInitialViewController];
    [self presentViewController:controller animated:YES completion:nil];
}

-(void)presentCyworldShortcutModal:(id)sender
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"CyworldStoryboard" bundle:nil];
    UITabBarController *controller = [storyboard instantiateInitialViewController];
    [self presentViewController:controller animated:YES completion:nil];
}

-(void)presentNateonShortcutModal:(id)sender
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"NateonStoryboard" bundle:nil];
    UITabBarController *controller = [storyboard instantiateInitialViewController];
    [self presentViewController:controller animated:YES completion:nil];
}

#pragma mark API request
/**
 * 설명 : 네이트온 쪽지 목록 검색
 * RequestURI : https://apis.skplanetx.com/nateon/notes/{seq}?version={version}&direction={direction}&count={count}&boxType={boxType}
 * Request PathParam : 
 * {seq} 쪽지의 고유 일련번호입니다.
 */
- (void)requestNateOnMessage
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    reqTag = REQ_NATEON;
    
    //쪽지 수신 여부에 따라 호출 선택
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    BOOL isAllowMessage = [appDelegate isAllowMessage];
    
    if (isAllowMessage) {
        NSString *url = [SERVER_SSL stringByAppendingFormat:NATEON_MESSAGE_PATH,@"0"];
        
        //Querystring Parameters
        NSMutableDictionary *params = [NSMutableDictionary new];
        [params setValue:@"1" forKey:@"version"];   //API의 버전 정보 입니다.
        [params setValue:@"10" forKey:@"count"];    //쪽지 갯수
        [params setValue:@"R" forKey:@"boxType"];   //쪽지함 구분코드
        
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
    } else {
        [self requestGuestBook];
    }
}

/**
 * 설명 : 싸이월드 방명록 게시물 목록보기
 * RequestURI : https://apis.skplanetx.com/cyworld/minihome/{cyId}/visitbook/{year}/items
 * Request PathParam :
 * {cyId} 조회할 대상의 싸이월드 ID입니다.
 * {year} 대상 방명록의 연도입니다.
 */
- (void)requestGuestBook
{
    //[CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    reqTag = REQ_CYWORLD;
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:CY_GUESTBOOK_PATH,@"me",THISYEAR];
    
    //configuration에서 카운트값 가져오기
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    int displayCount = [appDelegate getDisplayCount];
    NSString *strDisplayCount = [[NSNumber numberWithInt:displayCount] stringValue];
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];   //API의 버전 정보 입니다.
    [params setValue:@"1" forKey:@"page"];
    [params setValue:strDisplayCount forKey:@"count"];
    
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
 * 설명 : 11번가 상품검색
 * RequestURI : http://apis.skplanetx.com/11st/common/products
 */
- (void)requestElevenst
{
    //[CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    reqTag = REQ_ELEVENST;
    
    if ([ApiUtil isElevenstCategorySaved] == YES) {
        NSString *categoryName = [ApiUtil loadSelectedElevenstCategory];
        
        NSString *url = [SERVER stringByAppendingString:ELEVENST_PRODUCT_PATH];

        //Querystring Parameters
        NSMutableDictionary *params = [NSMutableDictionary new];
        [params setValue:@"1" forKey:KEY_VER];              //API의 버전 정보입니다.
        [params setValue:categoryName forKey:KEY_KEYWORD];  //검색을 위한 키워드를 입력합니다.(UTF-8인코딩)
        [params setValue:@"1" forKey:KEY_PAGE];             //조회할 목록의 페이지를 지정합니다.(유효값: 1~100)
        [params setValue:@"10" forKey:KEY_COUNT];           //페이지당 출력되는 상품 수를 지정합니다.(유효값: 1~50)
        [params setValue:@"Categories" forKey:KEY_OPTION];  //부가 정보를 지정합니다.(Categories: 카테고리 검색 결과 요청)
        
        //Bundle 설정
        RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:params payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeFORM responseType:SKPopContentTypeJSON];
        
        //API 호출
        [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
    } else {
        [self performSelector:@selector(requestMelon) withObject:nil afterDelay:0.1f];
    }
}

/**
 * 설명 : Melon 최신곡
 * RequestURI : http://apis.skplanetx.com/melon/newreleases/songs
 */
-(void)requestMelon
{
    reqTag = REQ_MELON;
    
    NSString *url = [ApiUtil getRequestURL:SERVER index:REQ_CHART];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:KEY_VER];      //API의 버전 정보입니다.
    [params setValue:@"1" forKey:KEY_PAGE];     //조회할 목록의 페이지를 지정합니다.(값이 0이면 1페이지의 100개 데이터 조회)
    [params setValue:@"10" forKey:KEY_COUNT];   //페이지당 출력되는 곡 수를 지정합니다.
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:params payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeFORM responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

/**
 * 설명 : 11번가 상품검색
 * 호출URL : http://apis.skplanetx.com/11st/common/products
 */
-(void)requestKeywordList:(NSString *)searchKeyword
{
    reqTag = CATE_KEYWORD;
    
    [CommonUtil commonStartActivityCenterIndicator:IndicatorStyle];
    
    NSString *url = [SERVER stringByAppendingFormat:ELEVENST_PRODUCT_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:KEY_VER];              //API의 버전 정보입니다.
    [params setValue:@"1" forKey:KEY_PAGE];             //조회할 목록의 페이지를 지정합니다.(유효값: 1~100)
    [params setValue:@"50" forKey:KEY_COUNT];           //페이지당 출력되는 상품 수를 지정합니다.(유효값: 1~50)
    [params setValue:searchKeyword forKey:KEY_KEYWORD]; //검색을 위한 키워드를 입력합니다.(UTF-8인코딩)
    [params setValue:SORT_CP forKey:KEY_SORTCODE];      //상품의 정렬 방식을 지정합니다.
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:params payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeFORM responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

/**
 * 설명 : T map 자동차 경로안내
 * RequestURI : https://apis.skplanetx.com/tmap/routes
 */
- (void)requestPathDetail
{
    //[CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    reqTag = REQ_TMAP;
    
    NSArray *tmapRecentArray = [self loadTmapRecentArray];
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:TMAP_ROUTES_PATH];
    
    if (tmapRecentArray != nil) {
        NSDictionary *startDic      = [tmapRecentArray objectAtIndex:0];
        NSDictionary *endDic        = [tmapRecentArray objectAtIndex:1];
        NSString *strStartLatitude  = [startDic valueForKey:STR_LAT];
        NSString *strStartLongitude = [startDic valueForKey:STR_LON];
        NSString *strEndLatitude    = [endDic valueForKey:STR_LAT];
        NSString *strEndLongitude   = [endDic valueForKey:STR_LON];
        
        if ([strStartLatitude length] == 0 || [strStartLongitude length] == 0 || [strEndLatitude length] == 0 || [strEndLongitude length] == 0)
        {
            return;
        }
        //Querystring Parameters
        NSMutableDictionary *params = [NSMutableDictionary new];
        [params setValue:@"1" forKey:@"version"];
        [params setValue:strStartLatitude forKey:@"startY"];
        [params setValue:strStartLongitude forKey:@"startX"];
        [params setValue:strEndLatitude forKey:@"endY"];
        [params setValue:strEndLongitude forKey:@"endX"];
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
}

- (NSArray *)loadTmapRecentArray
{
    NSArray *tmapRecentArray = [NSArray arrayWithContentsOfFile:[CommonUtil getPlistPath:TMAP_RECENT_PLIST_FILE_NAME]];
    NSLog(@"tmapRecentArray : %@", [tmapRecentArray description]);
    return tmapRecentArray;
}

- (NSDictionary *)loadElevenstRecent
{
    NSDictionary *elevenstRecentDic = [NSDictionary dictionaryWithContentsOfFile:[CommonUtil getPlistPath:ELEVEN_CATEGORY_PLIST_FILE_NAME]];
    
    NSString *categoryName = [elevenstRecentDic valueForKey:@"categoryName"];
    NSString *categoryId = [elevenstRecentDic valueForKey:@"categoryId"];
    
    NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:categoryName, STR_NAME, categoryId, STR_ID, nil];
    
    return dic;
}

- (BOOL)isCategorySaved
{
    //11번가 카테고리 설정이 되어있지 않으면 API 요청 안함.
    NSDictionary *elevenstRecentDic = [NSDictionary dictionaryWithContentsOfFile:[CommonUtil getPlistPath:ELEVEN_CATEGORY_PLIST_FILE_NAME]];
    
    if ([[elevenstRecentDic valueForKey:@"isCategorySaved"] boolValue]) {
        return YES;
    }
    return NO;
}

#pragma mark API Response
-(void)apiRequestFinished:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    if (result == nil) {
        [CommonUtil commonAlertView:@"정상적으로 결과값이 수신되지 않았습니다."];
        return;
    }
    
    [self resultParseAndSave:[result valueForKey:SKPopASyncResultData]];
    
}

-(void)apiRequestFailed:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [CommonUtil commonCustomAlertView:[result valueForKey:SKPopASyncResultMessage] CancelMessage:nil OkMessage:CONFIRM Tag:TAG_REQUEST_FAILED delegate:self];
}

#pragma mark Parsing JSON Result
-(void)resultParseAndSave:(NSString *)result
{
    NSLog(@"result : %@", result);
    if (reqTag == REQ_TMAP) {
        NSData* data = [result dataUsingEncoding: NSUTF8StringEncoding];
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
        
        NSString *arrivalTime       = [self setArrivalTime:[[rsdTotDtm Value] intValue]];
        
        NSArray *tmapRecentArray = [self loadTmapRecentArray];
        
        NSDictionary *startDic      = [tmapRecentArray objectAtIndex:0];
        NSDictionary *endDic        = [tmapRecentArray objectAtIndex:1];
        NSString *startPlace        = [startDic valueForKey:STR_NAME];
        NSString *endPlace          = [endDic valueForKey:STR_NAME];
        
        [self saveTmapConfiguration:[[NSDictionary alloc] initWithObjectsAndKeys:startPlace,@"start",endPlace,@"end", arrivalTime, @"time", nil]];
        
        return;
    }
    
    NSDictionary *dic = [result JSONValue];
    
    if (dic != nil) {
        if ([dic valueForKey:@"error"] != nil) {
            //NSString *message = [[dic valueForKey:@"error"] valueForKey:@"message"];
            
            //[CommonUtil commonAlertView:message];
            
            return;
        } else if ([dic valueForKey:@"mock"] != nil) {
            [CommonUtil commonAlertView:[dic valueForKey:@"mock"]];
            
            return;
        }
        
        if (reqTag == REQ_NATEON) {
            NSMutableArray *notesList = [[dic objectForKey:@"notes"] objectForKey:@"note"];
            NSMutableArray *noteDicList = [NSMutableArray new];
            
            for (NSDictionary *noteDic in notesList) {
                NSString *sender    = [noteDic valueForKey:@"sender"];
                NSString *message   = [noteDic valueForKey:@"message"];
                
                [noteDicList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:sender, STR_NAME, message, STR_CONTENT, nil]];
            }
            [self saveNateOnConfiguration:noteDicList];
            [self performSelector:@selector(requestGuestBook) withObject:nil afterDelay:0.1f];
        } else if (reqTag == REQ_CYWORLD) {
            NSMutableArray *itemList = [[dic objectForKey:@"items"] objectForKey:@"item"];
            NSMutableArray *guestbookList = [NSMutableArray new];
            
            for (NSDictionary *item in itemList) {
                NSString *writerName    = [item valueForKey:@"writerName"];
                NSString *content       = [item valueForKey:@"content"];
                
                NSLog(@"writeName : %@, writerContent : %@", writerName, content);
                [guestbookList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:writerName, STR_NAME, content, STR_CONTENT, nil]];
            }
            [self saveCyworldConfiguration:guestbookList];
            [self performSelector:@selector(requestElevenst) withObject:nil afterDelay:0.1f];
        } else if (reqTag == REQ_MELON) {
            //menuId
            NSString *menuId = [[dic objectForKey:@"melon"] objectForKey:@"menuId"];
            NSMutableArray *songList = [[[dic objectForKey:@"melon"] objectForKey:@"songs"] objectForKey:@"song"];
            
            NSMutableArray *artistList = [NSMutableArray new];
            
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
                
                [artistList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:songName, STR_NAME, artistName, STR_ARTIST, url, STR_URL, nil]];
            }
            
            [self saveMelonConfiguration:artistList];
            [self performSelector:@selector(requestPathDetail) withObject:nil afterDelay:0.1f];
        } else if (reqTag == REQ_ELEVENST) {
            NSMutableArray *keywordResult = [[[dic objectForKey:@"ProductSearchResponse"] objectForKey:@"Products"] objectForKey:@"Product"];
            NSMutableArray *productList = [NSMutableArray new];
            for (NSDictionary *dic in keywordResult) {
                
                NSString *productName   = [dic valueForKey:@"ProductName"];
                NSString *productPrice  = [dic valueForKey:@"ProductPrice"];
                NSString *product = [NSString stringWithFormat:@"%@ %@원",productName,productPrice];
                
                [productList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:product, STR_NAME, nil]];
            }
            [self saveElevenstConfiguration:productList];
            
            [self performSelector:@selector(requestMelon) withObject:nil afterDelay:0.1f];
        } else if (reqTag == CATE_KEYWORD) {
            NSMutableArray *keywordResult = [[[dic objectForKey:@"ProductSearchResponse"] objectForKey:@"Products"] objectForKey:@"Product"];
            NSString *keyword = [[[[dic objectForKey:@"ProductSearchResponse"] objectForKey:@"Request"] objectForKey:@"Arguments"] objectForKey:@"searchKeyword"];
            
            [self presentElevenstItemListViewController:keyword keywordResult:keywordResult];
        } 
    }
}

#pragma mark PresentModalViewController
-(void)presentElevenstItemListViewController:(NSString *)keyword keywordResult:(NSMutableArray *)keywordResult
{
    ElevenstItemListViewController *controller = [[ElevenstItemListViewController alloc]initWithNibName:@"ElevenstItemListViewController" bundle:nil];
    controller.itemInfoDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:keyword, STR_NAME, nil];
    if (controller.itemListArray != nil) {
        [controller.itemListArray removeAllObjects];
    } else {
        controller.itemListArray = [NSMutableArray new];
    }
    [controller.itemListArray addObjectsFromArray:[ApiUtil arrangeTempList:keywordResult]];
    
    [self presentViewController:controller animated:YES completion:nil];
}

#pragma mark NateOn String for marquee
- (void)saveNateOnConfiguration:(NSMutableArray *)array
{
    NSString *result = @"";
    
    if (array != nil) {
        for (NSDictionary *dic in array) {
            
            NSString *writer    = [dic valueForKey:STR_NAME];
            NSString *content   = [dic valueForKey:STR_CONTENT];
            result = [result stringByAppendingFormat:@"%@-%@/", writer, content];
        }
        
        if ([result length] > 0) {
            result = [result substringToIndex:[result length] - 1];
        } else {
            result = @"새 쪽지가 없습니다.";
        }
        
        NSString *path = [CommonUtil getPlistPath:CONFIGURATION_PLIST_FILE_NAME];
        NSMutableArray *configArr = [NSMutableArray arrayWithContentsOfFile:path];
        NSMutableDictionary *productDic = [configArr objectAtIndex:CONFIG_INDEX_NATEON];
        [productDic setValue:result forKey:CONFIGURATION_KEY_DEFAULT_TEXT];
        
        [configArr writeToFile:path atomically:YES];
    }
}

#pragma mark CyWorld String for marquee
- (void)saveCyworldConfiguration:(NSMutableArray *)array
{
    NSString *result = @"";
    
    if (array != nil) {
        for (NSDictionary *dic in array) {
            
            NSString *writer    = [dic valueForKey:STR_NAME];
            NSString *content   = [dic valueForKey:STR_CONTENT];
            result = [result stringByAppendingFormat:@"%@-%@/", writer, content];
        }
        
        if ([result length] > 0) {
            result = [result substringToIndex:[result length] - 1];
        } else {
            result = @"일촌 새글이 없습니다.";
        }
        
        NSString *path = [CommonUtil getPlistPath:CONFIGURATION_PLIST_FILE_NAME];
        NSMutableArray *configArr = [NSMutableArray arrayWithContentsOfFile:path];
        NSMutableDictionary *productDic = [configArr objectAtIndex:CONFIG_INDEX_CYWORLD];
        [productDic setValue:result forKey:CONFIGURATION_KEY_DEFAULT_TEXT];
        
        [configArr writeToFile:path atomically:YES];
    }
}

#pragma mark 11st String for marquee
- (void)saveElevenstConfiguration:(NSMutableArray *)array
{
    NSString *result = @"";
    
    if (array != nil) {
        for (NSDictionary *dic in array) {
            
            NSString *product = [dic valueForKey:STR_NAME];
            
            result = [result stringByAppendingFormat:@"%@/", product];
        }
        
        if ([result length] > 0) {
            result = [result substringToIndex:[result length] - 1];
        } else {
            result = @"설정된 카테고리 상품이 없습니다.";
        }
        
        NSString *path = [CommonUtil getPlistPath:CONFIGURATION_PLIST_FILE_NAME];
        NSMutableArray *configArr = [NSMutableArray arrayWithContentsOfFile:path];
        NSMutableDictionary *productDic = [configArr objectAtIndex:CONFIG_INDEX_11ST];
        [productDic setValue:result forKey:CONFIGURATION_KEY_DEFAULT_TEXT];
        
        [configArr writeToFile:path atomically:YES];
    }
}

#pragma mark MelonSong String for marquee
- (void)saveMelonConfiguration:(NSMutableArray *)array
{
    NSString *result = @"";
    int ranking = 0;
    
    if (array != nil) {
        for (NSDictionary *dic in array) {
            ranking++;
            
            NSString *song = [dic valueForKey:STR_NAME];
            NSString *artist = [dic valueForKey:STR_ARTIST];
            NSString *songWithArtist = [NSString stringWithFormat:@"%d위 %@-%@", ranking, song, artist];
            
            result = [result stringByAppendingFormat:@"%@/", songWithArtist];
        }
        
        if (result.length > 0) {
            result = [result substringToIndex:[result length] - 1];
        }
        
        NSString *path = [CommonUtil getPlistPath:CONFIGURATION_PLIST_FILE_NAME];
        NSMutableArray *configArr = [NSMutableArray arrayWithContentsOfFile:path];
        NSMutableDictionary *melonDic = [configArr objectAtIndex:CONFIG_INDEX_MELON];
        [melonDic setValue:result forKey:CONFIGURATION_KEY_DEFAULT_TEXT];
        
        [configArr writeToFile:path atomically:YES];
    }
    
    [self refreshAllViews];
}

#pragma mark Tmap String for marquee
- (void)saveTmapConfiguration:(NSDictionary *)dic
{
    NSString *result = @"";
    
    if (dic != nil) {
        
        NSString *start = [dic valueForKey:@"start"];
        NSString *end   = [dic valueForKey:@"end"];
        NSString *time  = [dic valueForKey:@"time"];
        
        result = [result stringByAppendingFormat:@"출발 : %@, 도착 : %@, %@", start, end, time];
        
        if ([result length] == 0) {
            result = @"설정된 목적지가 없습니다.";
        }
        
        NSString *path = [CommonUtil getPlistPath:CONFIGURATION_PLIST_FILE_NAME];
        NSMutableArray *configArr = [NSMutableArray arrayWithContentsOfFile:path];
        NSMutableDictionary *productDic = [configArr objectAtIndex:CONFIG_INDEX_TMAP];
        [productDic setValue:result forKey:CONFIGURATION_KEY_DEFAULT_TEXT];
        
        [configArr writeToFile:path atomically:YES];
    }
    
    [self refreshAllViews];
}

#pragma mark OAuth
/**
 * OAuth pList 파일 로딩
 */
- (void)loadOAuth
{
    oAuthDic = [NSDictionary dictionaryWithContentsOfFile:[CommonUtil getPlistPath:OAUTH_PLIST_FILE_NAME]];
}

#pragma mark SKP_OAuth_Result
/**
 * APP 최초 실행시 OAuth 실행
 */
- (void)setOAuth:(NSMutableDictionary *)params
{
    OAuthInfoManager *oaim = [OAuthInfoManager sharedInstance];
    [oaim setAppKey:[params objectForKey:@"appKey"]];
    [oaim setClientId:[params objectForKey:@"clientId"]];
    [oaim setClientSecret:[params objectForKey:@"secret"]];
    [oaim setScope:[params objectForKey:@"scope"]];
    
    [oaim login:self
       finished:@selector(oAuthLoginFinished:)
         failed:@selector(oAuthLoginFailed:)];
    
    [oaim saveOAuthInfo];
    [oaim restoreOAuthInfo];
}

/**
 * OAuth 완료시 콜백처리
 */
- (void)oAuthLoginFinished:(NSDictionary *)result
{
    NSString *resultData = [result objectForKey:SKPopASyncResultData];
    
    [self parsingResultSave:resultData];
    
    if ([self isCategorySaved]) {
        //[self performSelector:@selector(requestElevenst) withObject:nil afterDelay:0.1f];
        [self performSelector:@selector(requestNateOnMessage) withObject:nil afterDelay:0.1f];
    } else {
        [self performSelector:@selector(requestNateOnMessage) withObject:nil afterDelay:0.1f];
    }
}

/**
 * OAuth 실패시 콜백처리
 */
- (void)oAuthLoginFailed:(NSDictionary *)result
{
    [CommonUtil commonCustomAlertView:@"OAuth 인증실패"
                        CancelMessage:nil
                            OkMessage:CONFIRM
                                  Tag:TAG_OAUTH_FAILED
                             delegate:self];
}

#pragma mark-SKP_APIs_Request
/**
 * API 호출
 * bundle : url parameters
 */
- (void)requestAPI:(RequestBundle *)bundle target:(id)target finished:(SEL)finished failed:(SEL)failed
{
    APIRequest *apiRequest = [APIRequest new];
    [apiRequest setDelegate:self
                   finished:finished
                     failed:failed];
    [apiRequest aSyncRequest:bundle];
}

#pragma mark OAuth Plist Save
/**
 * JSON 파싱
 */
- (void)parsingResultSave:(NSString *)resultData
{
    //JSON Parser
    SBJsonParser *parser = [[SBJsonParser alloc] init];
    oAuthDic = [parser objectWithString:resultData error:nil];
    NSString *access_token = [oAuthDic objectForKey:OAUTH_KEY_ACCESS_TOKEN];
    NSString *refresh_token = [oAuthDic objectForKey:OAUTH_KEY_REFRESH_TOKEN];
    
    NSLog(@"parsed access_token : %@", access_token);
    NSLog(@"parsed refresh_token : %@", refresh_token);
    [oAuthDic setValue:[NSNumber numberWithBool:YES] forKey:OAUTH_KEY_IS_SAVED];
    
    [CommonUtil savePlist:oAuthDic plist:OAUTH_PLIST_FILE_NAME];
}

/**
 * OAuth 인증 여부
 */
- (BOOL)isOAuthSaved
{
    if (oAuthDic != nil) {
        return [[oAuthDic objectForKey:OAUTH_KEY_IS_SAVED] boolValue];
    }
    return NO;
}

#pragma mark Refresh all views
- (void)refreshAllViews
{
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
	[appDelegate loadConfiguration];
    [self updateAllView];
}

#pragma mark AlertView Delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == TAG_REQUEST_FAILED)
    {
        [self refreshAllViews];
    } else if (alertView.tag == TAG_OAUTH_FAILED) {
        [self performSelector:@selector(requestElevenst) withObject:nil afterDelay:0.1f];
    }
}

- (NSString *)setArrivalTime:(int)totaltime
{
    NSInteger hour = totaltime/3600;
    NSInteger min = (totaltime%3600)/60;
    NSInteger sec = (totaltime%3600)%60;
    NSString *humanTime = @"도착시간:";
    if(hour>0)
        humanTime = [humanTime stringByAppendingFormat:@"%d시 %d분 %d초", hour, min, sec];
    else
        humanTime = [humanTime stringByAppendingFormat:@"%d분 %d초", min, sec];
    
    return humanTime;
}
@end
