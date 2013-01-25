//
//  TmapAreaInfoViewController.m
//  All Asset
//
//  Created by Jason Nam on 12. 11. 7..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "TmapAreaInfoViewController.h"
#import "TmapAreaInfoTableViewCell.h"
#import "TmapAreaDetailInfoViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define REQ_JOBKIND     100
#define REQ_NEAR_AREA   101

@interface TmapAreaInfoViewController ()

@end

@implementation TmapAreaInfoViewController

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
    [self setJobList];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setJobList
{
    if (itemListArray == nil) {
        itemListArray = [NSMutableArray new];
    }
    
    [self requestJobKind];
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
    if (reqTag == REQ_JOBKIND) {
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
                    cell.textLabel.text = [dic valueForKey:STR_BIZ_NAME];
                }
            }
        }
        return cell;
    } else {
        return nil;
    }
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
            
            TmapAreaDetailInfoViewController *controller = [[TmapAreaDetailInfoViewController alloc] initWithNibName:@"TmapAreaDetailInfoViewController" bundle:nil];
            controller.requestInfoDictionary =  dic;
            
            [self presentViewController:controller animated:YES completion:nil];
        }
    }
    return nil;
}

- (IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark API request
/**
 * 설명 : T map 업종 코드 검색
 * RequestURI : https://apis.skplanetx.com/tmap/poi/categories
 */
- (void)requestJobKind
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    reqTag = REQ_JOBKIND;
    
    NSString *url = [SERVER_SSL stringByAppendingString:TMAP_JOBKIND];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];   //API의 버전 정보입니다.
    
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
    
    if (reqTag == REQ_JOBKIND) {
        NSMutableArray *bizCategoryList = [[dic objectForKey:@"bizCategories"] objectForKey:@"bizCategory"];
        
        if (itemListArray != nil) {
            [itemListArray removeAllObjects];
        }
        for (NSDictionary *bizCategoryDic in bizCategoryList) {
            NSString *upperBizCode  = [bizCategoryDic valueForKey:@"upperBizCode"];
            NSString *upperBizName  = [bizCategoryDic valueForKey:@"upperBizName"];
            NSString *middleBizCode = [bizCategoryDic valueForKey:@"middleBizCode"];
            
            if (itemListArray != nil) {
                if ([upperBizCode isEqualToString:@"05"] == NO && [middleBizCode isEqualToString:@"01"]) {
                    [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:upperBizName, STR_BIZ_NAME, upperBizCode, STR_BIZ_CODE, nil]];
                }
            }
        }
    } else if (reqTag == REQ_NEAR_AREA) {
        
        NSMutableArray *poiList =  [[[dic objectForKey:@"searchPoiInfo"] objectForKey:@"pois"] objectForKey:@"poi"];
        
        if (itemListArray != nil && [itemListArray count] > 0) {
            itemListArray = [NSMutableArray new];
        }
        
        for (NSDictionary *poi in poiList) {
            NSString *telNo     = [poi valueForKey:@"telNo"];
            NSString *name      = [poi valueForKey:@"name"];
            NSString *desc      = [poi valueForKey:@"desc"];
            
            if (itemListArray != nil) {
                [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:name, STR_SHOPNAME, desc, STR_CONTENT, telNo, STR_TEL, nil]];
            }
        }
    }
    
    [itemTableView reloadData];
}
@end
