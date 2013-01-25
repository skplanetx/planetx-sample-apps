//
//  ConfigurationElevenstViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ConfigurationElevenstViewController.h"
#import "ConfigurationViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "SBJson.h"
#import "CommonUtil.h"

#define STR_NAME	@"Name"
#define STR_ID		@"Id"
#define STR_CHILD	@"Child"
#define ALERT_TAG_GO_MAINSETTING    100

@interface ConfigurationElevenstViewController ()

@end

@implementation ConfigurationElevenstViewController
@synthesize itemListArray, displayItemListArray;

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
    
    [self setupCategoryList];
}

- (void)viewWillAppear:(BOOL)animated
{

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark UITableViewDataSource methods
/**
 * 카테고리 목록의 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(displayItemListArray!=nil)
        return [displayItemListArray count];
    return 0;
}

/**
 * 테이블에 카테고리 정보를 표시한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (categoryType == CATE_PARENT) {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:TV_ID_ELEVENST_CATEGOTY];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc] initWithFrame:CGRectZero];
            cell.textLabel.font = [UIFont systemFontOfSize:16];
            cell.tag = indexPath.section*1000+indexPath.row;
            if(displayItemListArray!=nil)
            {
                NSDictionary *dic = [displayItemListArray objectAtIndex:indexPath.row];
                if(dic!=nil)
                {
                    cell.textLabel.text = [dic valueForKey:STR_NAME];
                    
                    if (isLeaf == YES) {
                        cell.accessoryType = UITableViewCellAccessoryDetailDisclosureButton;
                    } else {
                        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                    }
                }
            }
        }
        return cell;
    } else if (categoryType == CATE_CHILDREN) {
        ConfigurationElevenstTableViewCell *cell = (ConfigurationElevenstTableViewCell *)[tableView dequeueReusableCellWithIdentifier:TV_ID_CONFIGURATION_ELEVENST];
        if (cell == nil) {
            NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"ConfigurationElevenstTableViewCell" owner:nil options:nil];
            cell = [array objectAtIndex:0];
            cell.tag = indexPath.section*1000+indexPath.row;
            
            if(displayItemListArray!=nil)
            {
                cell.labelCategoryName.text = [[displayItemListArray objectAtIndex:indexPath.row] valueForKey:STR_NAME];
            }
        }
        cell.delegate = self;
        cell.itemDic = [displayItemListArray objectAtIndex:indexPath.row];
        return cell;
    }
    return nil;
}

#pragma mark UITableViewDelegate methods
/**
 * 특정 컬럼을 선택 할 시 UIAlertView 를 Popup 하여 메시지 전송하게 한다.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(displayItemListArray !=nil)
    {
        NSDictionary *dic = [displayItemListArray objectAtIndex:indexPath.row];
        
        //API 호출
        switch(categoryType)
        {
            case CATE_PARENT:   //상위카테고리인 경우 서브카테고리 검색 API 호출
                [self requestCategoryChild:[dic valueForKey:STR_ID]];
                break;
            case CATE_CHILDREN: //서브카테고리인 경우 키워드 검색 API 호출
                //선택한 카테고리명, 카테고리코드 저장
                [ApiUtil saveElevenstCategory:[dic valueForKey:STR_ID] categoryName:[dic valueForKey:STR_NAME]];
                break;
        }
    }
    return nil;
}

-(IBAction)pressedBackButtonItem:(id)sender
{   
    if (isLeaf) {
        isLeaf = NO;
        //homeButton.title = @"홈";
        [self setupCategoryList];
        displayItemListArray = [[NSMutableArray alloc] initWithArray:itemListArray];
        [itemTableView reloadData];
    } else {
        //[self dismissModalViewControllerAnimated:YES];
        [CommonUtil commonCustomAlertView:@"카테고리가 설정되었습니다."
                            CancelMessage:nil
                                OkMessage:@"확인"
                                      Tag:ALERT_TAG_GO_MAINSETTING
                                 delegate:self];
    }
}

#pragma mark API request
/**
 * 설명 : 11번가 카테고리 전체조회
 * RequestURI : http://apis.skplanetx.com/11st/common/categories
 */
- (void)requestElevenstCategory
{
    categoryType = CATE_PARENT;
    
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER stringByAppendingString:ELEVENST_CATEGORY_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];   //API의 버전 정보입니다.
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:params payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeFORM responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

/**
 * 설명 : 11번가 카테고리 조회
 * RequestURI : http://apis.skplanetx.com/11st/common/categories/{categoryCode}
 * Request PathParam : {categoryCode} 카테고리 코드 정보입니다.
 */
- (void)requestCategoryChild:(NSString *)selectedCategory
{
    categoryType = CATE_CHILDREN;
    
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    if (selectedCategory == nil) {
        return;
    }
    
    NSString *url = [SERVER stringByAppendingFormat:@"%@/%@", ELEVENST_CATEGORY_PATH, selectedCategory];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];
    [params setValue:@"Children" forKey:@"option"];
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:params payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeFORM responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:reqBundle];
}

#pragma mark API response
-(void)apiRequestFinished:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [self resultParse:[result valueForKey:SKPopASyncResultData]];
    
}

-(void)apiRequestFailed:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [CommonUtil commonAlertView:[result valueForKey:SKPopASyncResultMessage]];
}

#pragma mark Parsing JSON Result
-(void)resultParse:(NSString *)resultData
{
    NSDictionary *dic = [resultData JSONValue];
    
    if ([dic objectForKey:@"error"] != nil) {
        
        NSString *errorMsg = [[dic objectForKey:@"error"] objectForKey:@"message"];
        
        [CommonUtil commonCustomAlertView:errorMsg CancelMessage:nil OkMessage:@"확인" Tag:0 delegate:self];
        
        return;
    }
    
    if (categoryType == CATE_PARENT) {
        //전체 카테고리 리스트
        NSMutableArray *categories = [[[dic objectForKey:@"CategoryResponse"]objectForKey:@"Children"] objectForKey:@"Category"];
        
        //개별 카테고리 Dictionary
        for (NSDictionary *category in categories) {
            NSString *categoryName  = [category objectForKey:@"CategoryName"];
            NSString *categoryCode  = [category objectForKey:@"CategoryCode"];
            
            [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:categoryName, STR_NAME, categoryCode, STR_ID, nil, STR_CHILD, nil]];
        }
    } else if (categoryType == CATE_CHILDREN) {
        if (itemListArray.count > 0) {
            [itemListArray removeAllObjects];
        }
        //서브 카테고리 리스트
        NSMutableArray *subCategories = [[[dic objectForKey:@"CategoryResponse"] objectForKey:@"Children"] objectForKey:@"Category"];
        
        for (NSDictionary *subCategory in subCategories) {
            NSString *subCategoryName = [subCategory objectForKey:@"CategoryName"];
            NSString *subCategoryCode = [subCategory objectForKey:@"CategoryCode"];
            
            [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:
                                      subCategoryName,STR_NAME,
                                      subCategoryCode,STR_ID,
                                      nil,STR_CHILD,
                                      nil]];
        }
        isLeaf = YES;
    }
    
    if (displayItemListArray == nil)
    {
        displayItemListArray = [[NSMutableArray alloc] initWithArray:itemListArray];
    } else {
        [displayItemListArray removeAllObjects];
    }
    
    [displayItemListArray addObjectsFromArray:itemListArray];
    
    [itemTableView reloadData];
}

#pragma mark init data
-(void)setupCategoryList
{
    itemListArray = [[NSMutableArray alloc] init];
    
    [self requestElevenstCategory];
}

#pragma mark Cell Delegate
- (void)selectItemCategory:(NSDictionary *)dic
{
    NSString *categoryName = [dic valueForKey:STR_NAME];
    NSString *categoryCode = [dic valueForKey:STR_ID];
    
    //설정값 저장
    BOOL saveSuccess = [ApiUtil saveElevenstCategory:categoryCode categoryName:categoryName];
    
    NSString *resultMessage = @"";
    
    if (saveSuccess) {
        resultMessage = CONFIG_SAVE_SUCCESS;
    } else {
        resultMessage = CONFIG_SAVE_FAIL;
    }

    [CommonUtil commonCustomAlertView:resultMessage CancelMessage:nil OkMessage:@"확인" Tag:ALERT_TAG_GO_MAINSETTING delegate:self];
}

#pragma mark UIAlertView Delegate
/**
 * UIAlertView Delegation 함수. 카테고리 선택시 메뉴 설정 메인화면으로 이동한다.
 */

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag == ALERT_TAG_GO_MAINSETTING){
        if(buttonIndex == NO){
            [self pressedBackButtonItem:nil];
            [self dismissViewControllerAnimated:YES completion:nil];
        }
    }
}

-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    //ConfigurationViewController를 호출한다.
    
}

@end
