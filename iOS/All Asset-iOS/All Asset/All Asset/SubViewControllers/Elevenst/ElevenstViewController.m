//
//  ElevenstViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ElevenstViewController.h"
#import "ElevenstItemListViewController.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "SBJson.h"
#import "CommonUtil.h"

@interface ElevenstViewController ()

@end

@implementation ElevenstViewController
@synthesize itemListArray,displayItemListArray,elevenstItemListViewController;

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
    isLeaf = NO;
    [self setupCategoryList];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
    categoryType = CATE_PARENT;
}

- (ElevenstItemListViewController *)elevenstItemListViewController
{
    // Instantiate the editin view controller if necessary.
    if (elevenstItemListViewController == nil) {
        elevenstItemListViewController = [[ElevenstItemListViewController alloc] initWithNibName:@"ElevenstItemListViewController" bundle:nil];
    }
    return elevenstItemListViewController;
}

#pragma mark SKP OPR API
-(void)setupCategoryList
{
    /* dummy data setup */
    itemListArray = [[NSMutableArray alloc] init];

    //[self requestCategoryAll];
    [self performSelector:@selector(requestCategoryAll) withObject:nil afterDelay:0.1f];
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
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
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
                
                cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            }
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
/**
 * 하위 항목이 있을 경우 하위 항목 표시, 그렇지 않다면 해당 항목으로 검색.
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
                [self requestKeywordList:[dic valueForKey:STR_NAME]];
                break;
        }
    }
    return nil;
}

/**
 * 악세서리 버튼도 동일 처리, 하위 항목이 있을 경우 하위 항목 표시, 그렇지 않다면 해당 항목으로 검색.
 */
- (void)tableView:(UITableView *)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath
{
    if(displayItemListArray!=nil)
    {
        NSDictionary *dic = [displayItemListArray objectAtIndex:indexPath.row];
        if(dic!=nil)
        {
            NSMutableArray *child = [dic valueForKey:STR_CHILD];
            if(child!=nil && [child count] >0)
            {
                isLeaf = YES;
                homeButton.title = @"이전";
                displayItemListArray = [[NSMutableArray alloc] initWithArray:child];
                [itemTableView reloadData];
            }
            else
            {
                ElevenstItemListViewController *controller = [[ElevenstItemListViewController alloc] initWithNibName:@"ElevenstItemListViewController" bundle:nil];;
                controller.itemInfoDictionary = dic;
                [self presentViewController:controller animated:YES completion:nil];
            }
        }
    }
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
        [self requestKeywordList:searchBar.text];
    }
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    if(isLeaf)
    {
        isLeaf = NO;
        homeButton.title = @"홈";
        [self setupCategoryList];
        displayItemListArray = [[NSMutableArray alloc] initWithArray:itemListArray];
        [itemTableView reloadData];
    }
    else
    {
        [self dismissViewControllerAnimated:YES completion:nil];
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

#pragma mark API Request
/**
 * 설명 : 11번가 카테고리 전체조회
 * RequestURI : http://apis.skplanetx.com/11st/common/categories
 */
- (void)requestCategoryAll
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    categoryType = CATE_PARENT;
    NSString *url = [SERVER stringByAppendingFormat:ELEVENST_CATEGORY_PATH];
    
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

/**
 * 설명 : 11번가 카테고리 조회
 * RequestURI : http://apis.skplanetx.com/11st/common/categories/{categoryCode}
 * Request PathParam : {categoryCode} 카테고리 코드 정보입니다.
 */
- (void)requestCategoryChild:(NSString *)categoryCode
{
    categoryType = CATE_CHILDREN;
    
    [CommonUtil commonStartActivityCenterIndicator:IndicatorStyle];
    
    NSString *url = [SERVER stringByAppendingFormat:ELEVENST_ONE_CATEGORY_PATH, categoryCode];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];       //API의 버전 정보입니다.
    [params setValue:@"Children" forKey:@"option"]; //부가 정보를 지정합니다(Children: 하위 카테고리 목록 요청)
    
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
-(void)requestKeywordList:(NSString *)searchKeyword
{
    categoryType = CATE_KEYWORD;
    
    [CommonUtil commonStartActivityCenterIndicator:IndicatorStyle];
    
    NSString *url = [SERVER stringByAppendingFormat:ELEVENST_PRODUCT_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];               //API의 버전 정보입니다.
    [params setValue:@"1" forKey:@"page"];                  //조회할 목록의 페이지를 지정합니다.(유효값: 1~100)
    [params setValue:@"50" forKey:@"count"];                //페이지당 출력되는 상품 수를 지정합니다.(유효값: 1~50)
    [params setValue:searchKeyword forKey:@"searchKeyword"];//검색을 위한 키워드를 입력합니다.(UTF-8인코딩)
    [params setValue:SORT_CP forKey:@"sortCode"];           //상품의 정렬 방식을 지정합니다.(CP: 인기 순)
    
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
-(void)apiRequestFinished:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [self resultParseAndSave:[result valueForKey:SKPopASyncResultData]];
    
}

-(void)apiRequestFailed:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    [CommonUtil commonAlertView:[result valueForKey:SKPopASyncResultMessage]];
}

#pragma mark Parsing JSON Result
-(void)resultParseAndSave:(NSString *)resultData
{
    NSDictionary *dic = [resultData JSONValue];
    
    [ApiUtil errorAlert:dic];
    
    
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
        homeButton.title = @"이전";
    } else if (categoryType == CATE_KEYWORD){
        categoryType = CATE_CHILDREN;
        
        NSMutableArray *keywordResult = [[[dic objectForKey:@"ProductSearchResponse"] objectForKey:@"Products"] objectForKey:@"Product"];
        NSString *keyword = [[[[dic objectForKey:@"ProductSearchResponse"] objectForKey:@"Request"] objectForKey:@"Arguments"] objectForKey:@"searchKeyword"];
        
        [self presentElevenstItemListViewController:keyword keywordResult:keywordResult];
        
        return;
    }
    
    displayItemListArray = [[NSMutableArray alloc] initWithArray:itemListArray];
    
    [displayItemListArray removeAllObjects];
    [displayItemListArray addObjectsFromArray:itemListArray];
    [itemTableView reloadData];
}

#pragma mark presentModalViewController
-(void)presentElevenstItemListViewController:(NSString *)keyword keywordResult:(NSMutableArray *)keywordResult
{
    ElevenstItemListViewController *controller = self.elevenstItemListViewController;
    controller.itemInfoDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:keyword, STR_NAME, nil];
    if (controller.itemListArray != nil) {
        [controller.itemListArray removeAllObjects];
    } else {
        controller.itemListArray = [NSMutableArray new];
    }
    [controller.itemListArray addObjectsFromArray:[ApiUtil arrangeTempList:keywordResult]];
    
    [self presentViewController:controller animated:YES completion:nil];
}

#pragma mark UIAlertViewDelegate methods
-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    [CommonUtil commonStopActivityIndicator];
}

@end
