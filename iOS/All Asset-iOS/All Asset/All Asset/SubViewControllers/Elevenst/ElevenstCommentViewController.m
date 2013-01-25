//
//  ElevenstCommentViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ElevenstCommentViewController.h"
#import "ElevenstCommentListTableViewCell.h"
#import "ElevenstReviewListTableViewCell.h"
#import "ApiUtil.h"
#import "SKPOP_v1.4.h"
#import "CommonUtil.h"
#import "SBJson.h"
#import "Defines.h"

#define QUERY_OPTION_COMMENT	0
#define QUERY_OPTION_REVIEW		1

#define STR_SATIS		@"SatisPercent"
#define STR_NAME		@"Name"
#define STR_ID			@"Id"
#define STR_TITLE		@"Title"
#define STR_DATE		@"Date"
#define STR_DESC_URL	@"DescURL"
#define STR_COMMENT		@"Comment"
#define STR_REVIEW_CNT  @"ReviewCnt"

#define OPTION_REVIEW   @"SemiReviews"  //리뷰
#define OPTION_COMMENT  @"PostScripts"  //후기

@interface ElevenstCommentViewController ()

@end

@implementation ElevenstCommentViewController
@synthesize itemInfoDictionary;

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
    commentCount.text = [NSString stringWithFormat:@"후기/리뷰 %@건", [[itemInfoDictionary valueForKey:STR_REVIEW_CNT] stringValue]];
    
    queryMode = classSegement.selectedSegmentIndex;
    [self setupCommentList];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark SKP OPR API
/**
 * 11번가 구매후기 API를 이용하여 후기 정보 데이터 조회
 */
-(void)setupCommentList
{
    commentListArray = [[NSMutableArray alloc] init];
    
    if (commentListArray != nil && [commentListArray count] > 0) {
        [commentListArray removeAllObjects];
    }
    
    [self requestComment];
}

#pragma mark UITableViewDataSource methods
/**
 * 리뷰 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(commentListArray!=nil)
    {
        return [commentListArray count];
    }
    return 0;
}

/**
 * 테이블에 리뷰 기본 정보를 표시한다.
 * CustomCell 인 ElevenstItemListTableViewCell 을 사용한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(queryMode == QUERY_OPTION_COMMENT)
    {
        ElevenstCommentListTableViewCell *cell = (ElevenstCommentListTableViewCell *)[tableView dequeueReusableCellWithIdentifier:nil];
        if (cell == nil)
        {
            NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"ElevenstCommentListTableViewCell" owner:nil options:nil];
            cell = [array objectAtIndex:0];
            cell.tag = indexPath.section*1000+indexPath.row;
            if(commentListArray!=nil)
            {
                cell.title.text = [[commentListArray objectAtIndex:indexPath.row] valueForKey:STR_TITLE];
                cell.name.text =[[commentListArray objectAtIndex:indexPath.row] valueForKey:STR_NAME];
                cell.date.text =[[commentListArray objectAtIndex:indexPath.row] valueForKey:STR_DATE];
                
                NSLog(@"title : %@, name : %@, date : %@", cell.title.text, cell.name.text, cell.date.text);
            }
        }
        return cell;
    }
    else
    {
        ElevenstReviewListTableViewCell *cell = (ElevenstReviewListTableViewCell *)[tableView dequeueReusableCellWithIdentifier:nil];
        if (cell == nil)
        {
            NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"ElevenstReviewListTableViewCell" owner:nil options:nil];
            cell = [array objectAtIndex:0];
            cell.tag = indexPath.section*1000+indexPath.row;
            if(commentListArray!=nil)
            {
                cell.title.text = [[commentListArray objectAtIndex:indexPath.row] valueForKey:STR_TITLE];
                cell.name.text =[[commentListArray objectAtIndex:indexPath.row] valueForKey:STR_NAME];
                cell.date.text =[[commentListArray objectAtIndex:indexPath.row] valueForKey:STR_DATE];
            }
        }
        return cell;
    }
}

#pragma mark UITableViewDelegate methods

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 74.0;
}

/**
 * 특정 컬럼을 선택 할 시 해당 URL 을 기준으로 브라우저 호출.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(commentListArray!=nil)
    {
        if (queryMode == QUERY_OPTION_REVIEW) {
            launchURL = [[commentListArray objectAtIndex:indexPath.row] valueForKey:STR_DESC_URL];
            
            [CommonUtil commonCustomAlertView:MSG_LAUNCH_BROWSER CancelMessage:CONFIRM OkMessage:CANCEL Tag:0 delegate:self];
        }
    }
    return nil;
}

-(IBAction)changedSegment:(id)sender
{
    queryMode = classSegement.selectedSegmentIndex;
    [self setupCommentList];
    [itemTableView reloadData];
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [CommonUtil commonStopActivityIndicator];
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark API request
/**
 * 설명 : 11번가 상품정보 조회
 * RequestURI : http://apis.skplanetx.com/11st/common/products/{productCode}
 * Request PathParam : {productCode} 상품 코드 입니다.
 */
- (void)requestComment
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *productCode = [itemInfoDictionary valueForKey:STR_ID];
    NSString *url = [SERVER stringByAppendingFormat:@"%@/%@",ELEVENST_PRODUCT_PATH,productCode];
    
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
    
    [params setValue:@"1" forKey:@"version"];               //API의 버전 정보입니다.
    
    //부가 정보를 지정합니다.
    if (queryMode == QUERY_OPTION_COMMENT) {
        [params setValue:@"PostScripts" forKey:@"option"];  //PostScripts: 사용 후기 조회 결과 요청
    } else {
        [params setValue:@"SemiReviews" forKey:@"option"];  //SemiReviews: 나도 전문가 조회 결과 요청
    }
    
    return params;
}

#pragma mark API response
-(void)apiRequestFinished:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    if (result != nil) {
        [self resultParseAndSave:[result valueForKey:SKPopASyncResultData]];
    } else {
        NSLog(@"API result have no data!!!!");
    }
}

-(void)apiRequestFailed:(NSDictionary *)result
{
    [CommonUtil commonStopActivityIndicator];
    
    if (result != nil) {
        [CommonUtil commonAlertView:[result valueForKey:SKPopASyncResultMessage]];
    }
}

#pragma mark Parsing JSON Result
- (void)resultParseAndSave:(NSString *)result
{
    NSMutableDictionary *dic = [result JSONValue];
    
    [ApiUtil errorAlert:dic];
    
    if (dic != nil) {
        if (queryMode == QUERY_OPTION_REVIEW) {
            NSMutableDictionary *semiReviews = [[dic objectForKey:@"ProductInfoResponse"] objectForKey:@"SemiReviews"];
            
            if (semiReviews != nil && [[semiReviews description] length] > 0) {
                NSMutableArray *reviewList = [semiReviews objectForKey:@"Review"];
                
                for (NSDictionary *review in reviewList) {
                    NSString *title     = [review valueForKey:@"Title"];
                    NSString *writer    = [review valueForKey:@"Writer"];
                    NSString *date      = [review valueForKey:@"Date"];
                    NSString *url       = [review valueForKey:@"Url"];
                    
                    NSDictionary *dic = [[NSDictionary alloc] initWithObjectsAndKeys:
                                         [itemInfoDictionary valueForKey:STR_ID], STR_ID,
                                         @"", STR_SATIS,
                                         title, STR_TITLE,
                                         writer, STR_NAME,
                                         date, STR_DATE,
                                         url, STR_DESC_URL,nil];
                    
                    [commentListArray addObject:dic];
                }
            }
        } else if (queryMode == QUERY_OPTION_COMMENT) {
            NSMutableDictionary *postscripts = [[dic objectForKey:@"ProductInfoResponse"] objectForKey:@"PostScripts"];

            //구매후기가 1건 이상인 경우
            if (postscripts != nil && [[postscripts description] length] > 0) {
                NSMutableArray *commentList = [postscripts objectForKey:@"PostScript"];
                
                for (NSDictionary *comment in commentList) {
                    NSString *title     = [comment valueForKey:@"Title"];
                    NSString *writer    = [comment valueForKey:@"Writer"];
                    NSString *date      = [comment valueForKey:@"Date"];
                    
                    NSDictionary *dic = [[NSDictionary alloc] initWithObjectsAndKeys:
                                         title, STR_TITLE,
                                         writer, STR_NAME,
                                         date, STR_DATE,
                                         [itemInfoDictionary valueForKey:STR_ID], STR_ID, nil];
                    
                    [commentListArray addObject:dic];
                }
            }
        }
    }
    
    [itemTableView reloadData];
}

#pragma mark AlertView Delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 0) {
        if(buttonIndex == NO){
            [CommonUtil launchBrowser:launchURL];
        }
    }
}

@end
