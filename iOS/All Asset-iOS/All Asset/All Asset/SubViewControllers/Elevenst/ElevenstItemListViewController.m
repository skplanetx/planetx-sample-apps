//
//  ElevenstCommentViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ElevenstItemListViewController.h"
#import "ElevenstDetailViewController.h"
#import "ElevenstItemListTableViewCell.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

@interface ElevenstItemListViewController ()

@end

@implementation ElevenstItemListViewController
@synthesize itemInfoDictionary, itemListArray, tempListArray;

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
    [self setupSearchOption];
    itemSearchBar.text = [itemInfoDictionary valueForKey:STR_NAME];
    
    if (itemListArray != nil && [itemListArray count] > 0) {
        optionTextField.text = [[searchOptionArray objectAtIndex:0] valueForKey:STR_NAME];
        [itemTableView reloadData];
    }
    
    if(itemListArray!=nil)
    {
        NSString *result = @"검색결과";
        result = [result stringByAppendingFormat:@"(%d)", [itemListArray count]];
        resultLabel.text = result;
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark SKP OPR API
/**
 * PickerView에 사용할 상품 조회 옵션 Array
 */
-(void)setupSearchOption
{
    searchOptionArray = [[NSMutableArray alloc] init];
    [searchOptionArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:SORT_CP, STR_ID, TEXT_SORT_CP, STR_NAME, nil]];
    [searchOptionArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:SORT_A, STR_ID, TEXT_SORT_A, STR_NAME, nil]];
    [searchOptionArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:SORT_G, STR_ID, TEXT_SORT_G, STR_NAME, nil]];
    [searchOptionArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:SORT_I, STR_ID, TEXT_SORT_I, STR_NAME, nil]];
    [searchOptionArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:SORT_L, STR_ID, TEXT_SORT_L, STR_NAME, nil]];
    [searchOptionArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:SORT_H, STR_ID, TEXT_SORT_H, STR_NAME, nil]];
    [searchOptionArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:SORT_N, STR_ID, TEXT_SORT_N, STR_NAME, nil]];
}

-(void)setupItemList:(NSString *)searchOption
{
    itemListArray = [[NSMutableArray alloc] init];
    
    [self requestKeywordList:itemSearchBar.text sortCode:searchOption];
}

-(IBAction)showPicker:(id)sender
{
    UIActionSheet *menu = [[UIActionSheet alloc] initWithTitle:@"검색 조건 선택"
                                                      delegate:self
                                             cancelButtonTitle:@"완료"
                                        destructiveButtonTitle:nil
                                             otherButtonTitles:nil];
    // Add the picker
    UIPickerView *pickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0,90,0,0)];
    
    pickerView.delegate = self;
    pickerView.showsSelectionIndicator = YES;    // note this is default to NO
    
    [menu addSubview:pickerView];
    [menu showInView:self.view];
    [menu setBounds:CGRectMake(0,0,320,500)];
}

#pragma mark API request
/**
 * 설명 : 11번가 상품검색 
 * RequestURI : http://apis.skplanetx.com/11st/common/products
 */
-(void)requestKeywordList:(NSString *)searchKeyword sortCode:(NSString *)sortCode
{
    [CommonUtil commonStartActivityCenterIndicator:IndicatorStyle];
    
    if (searchKeyword == nil || sortCode == nil) {
        return;
    }
    
    NSString *url = [SERVER stringByAppendingFormat:ELEVENST_PRODUCT_PATH];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];               //API의 버전 정보입니다.
    [params setValue:@"1" forKey:@"page"];                  //조회할 목록의 페이지를 지정합니다.(유효값: 1~100)
    [params setValue:@"50" forKey:@"count"];                //페이지당 출력되는 상품 수를 지정합니다.(유효값: 1~50)
    [params setValue:sortCode forKey:@"sortCode"];          //상품의 정렬 방식을 지정합니다.
    [params setValue:searchKeyword forKey:@"searchKeyword"];//검색을 위한 키워드를 입력합니다.(UTF-8인코딩)
    
    //Bundle 설정
    RequestBundle *reqBundle = [ApiUtil initRequestBundle:nil url:url params:params payload:nil uploadFilePath:nil httpMethod:SKPopHttpMethodGET requestType:SKPopContentTypeFORM responseType:SKPopContentTypeJSON];
    
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
    [itemSearchBar resignFirstResponder];
    
    NSDictionary *dic = [resultData JSONValue];
    
    [ApiUtil errorAlert:dic];
    
    NSMutableArray *keywordResult = [[[dic objectForKey:@"ProductSearchResponse"] objectForKey:@"Products"] objectForKey:@"Product"];

    if (tempListArray != nil) {
        [tempListArray removeAllObjects];
        [tempListArray addObjectsFromArray:keywordResult];
    }
    
    if (itemListArray != nil && [itemListArray count] > 0)
    {
        [itemListArray removeAllObjects];
    }
    [itemListArray addObjectsFromArray:[ApiUtil arrangeTempList:keywordResult]];
    [itemTableView reloadData];
}

#pragma mark UIPickerViewDelegate
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    optionTextField.text = [[searchOptionArray objectAtIndex:row] valueForKey:STR_NAME];
    selectedPickerId = [[searchOptionArray objectAtIndex:row] valueForKey:STR_ID];
}

#pragma mark UIPickerViewDataSource
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
	return [[searchOptionArray objectAtIndex:row] valueForKey:STR_NAME];
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
	return [searchOptionArray count];
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
	return 1;
}

#pragma mark UITableViewDataSource methods
/**
 * 각 상품 그룹 별 상품 목록 개수 리턴
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
 * 상품 목록의 그룹 개수 리턴
 */
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if(itemListArray!=nil)
        return [itemListArray count];
    return 0;
}

/**
 * 테이블에 상품 정보를 표시한다.
 * CustomCell 인 ElevenstItemListTableViewCell 을 사용한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    ElevenstItemListTableViewCell *cell = (ElevenstItemListTableViewCell *)[tableView dequeueReusableCellWithIdentifier:nil];
    
    if (cell == nil)
	{
		NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"ElevenstItemListTableViewCell" owner:nil options:nil];
        cell = [array objectAtIndex:0];
        cell.tag = indexPath.section*1000+indexPath.row;
    }
    
    if(itemListArray!=nil)
    {
        NSDictionary *dic = [itemListArray objectAtIndex:indexPath.row];
            
        if(dic!=nil)
        {
            NSURL *url = [NSURL URLWithString:[dic valueForKey:STR_IMAGE_URL]];
            [cell.titleImage setContentMode:UIViewContentModeScaleAspectFill];
            [cell.titleImage initWithImageAtURL:url];
            cell.title.text = [dic valueForKey:STR_TITLE];
            cell.price.text = [NSString stringWithFormat:@"%@ 할인가 %@",[dic valueForKey:STR_PRICE],[dic valueForKey:STR_SALE_PRICE]];
            cell.seller.text =[NSString stringWithFormat:@"판매자 : %@",[dic valueForKey:STR_SELLER]];
            cell.comment.text =[dic valueForKey:STR_COMMENT];
            cell.condition.text = [NSString stringWithFormat:@"배송 : %@", [dic valueForKey:STR_CONDITION]];
        }
    }
    
    return cell;
}

#pragma mark UITableViewDelegate methods

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 120.0;
}

/**
 * 특정 컬럼을 선택 할 시 상세 상품 정보를 보여준다.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(itemListArray!=nil)
    {
        NSDictionary *dic = [itemListArray objectAtIndex:indexPath.row];

        if(dic!=nil)
        {
         
            ElevenstDetailViewController *controller = [[ElevenstDetailViewController alloc] initWithNibName:@"ElevenstDetailViewController" bundle:nil];
            controller.itemInfoDictionary =  dic;
            
            [self presentViewController:controller animated:YES completion:nil];
        }
    }
    return nil;
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
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
        optionTextField.text = [[searchOptionArray objectAtIndex:0] valueForKey:STR_NAME];
        //키워드 API 호출
        [self requestKeywordList:searchBar.text sortCode:[[searchOptionArray objectAtIndex:0] valueForKey:STR_ID]];
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

#pragma mark ActionSheet Delegate
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    [self performSelector:@selector(setupItemList:) withObject:selectedPickerId afterDelay:0.1f];
}
@end
