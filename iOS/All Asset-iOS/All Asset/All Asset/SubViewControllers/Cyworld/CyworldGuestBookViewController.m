//
//  CyworldGuestBookViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "CyworldGuestBookViewController.h"
#import "CyworldGuestBookTableViewCell.h"
#import "Defines.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define STR_NAME		@"Name"
#define STR_ID			@"Id"
#define STR_DATE			@"Date"
#define STR_CONTENT		@"Content"

@interface CyworldGuestBookViewController ()

@end

@implementation CyworldGuestBookViewController

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
    [self setupItemList];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark SKP OPR API
-(void)setupItemList
{
    itemListArray = [[NSMutableArray alloc] init];

    [self requestGuestBook];
}

#pragma mark UITableViewDataSource methods
/**
 * 방명록 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(itemListArray!=nil)
        return [itemListArray count];
    return 0;
}

/**
 * 테이블에 방명록 글을 표시한다.
 * CustomCell 인 ElevenstItemListTableViewCell 을 사용한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CyworldGuestBookTableViewCell *cell = (CyworldGuestBookTableViewCell *)[tableView dequeueReusableCellWithIdentifier:nil];
    if (cell == nil)
	{
		NSArray *array = [[NSBundle mainBundle] loadNibNamed:@"CyworldGuestBookTableViewCell" owner:nil options:nil];
        cell = [array objectAtIndex:0];
        cell.tag = indexPath.section*1000+indexPath.row;
        cell.userInteractionEnabled = NO;
        if(itemListArray!=nil)
        {
            cell.name.text = [[itemListArray objectAtIndex:indexPath.row] valueForKey:STR_NAME];
            cell.date.text = [[itemListArray objectAtIndex:indexPath.row] valueForKey:STR_DATE];
            cell.content.text =[[itemListArray objectAtIndex:indexPath.row] valueForKey:STR_CONTENT];
            
            NSLog(@"content : %@", cell.content.text);
            [cell.content sizeToFit];
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
/**
 * 방명록의 내용에 따라 동적으로 셀의 높이를 계산하여 리턴.
 */
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *text = [[itemListArray objectAtIndex:indexPath.row] valueForKey:STR_CONTENT];
    CGSize constraintSize = CGSizeMake(280.0f, MAXFLOAT);
    CGSize labelSize = [text sizeWithFont:[UIFont systemFontOfSize:12]
                        constrainedToSize:constraintSize
                            lineBreakMode:NSLineBreakByWordWrapping];
    return labelSize.height + 56.0;
    //return 147.0;
}

-(NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark API request
/**
 * 설명 : 싸이월드 방명록 게시물 목록보기
 * RequestURI : https://apis.skplanetx.com/cyworld/minihome/{cyId}/visitbook/{year}/items
 * Request PathParam :
 * {cyId} 조회할 대상의 싸이월드 ID입니다.
 * {year} 대상 방명록의 연도입니다.
 */
- (void)requestGuestBook
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    NSString *url = [SERVER_SSL stringByAppendingFormat:CY_GUESTBOOK_PATH,@"me",THISYEAR];
    
    //Querystring Parameters
    NSMutableDictionary *params = [NSMutableDictionary new];
    [params setValue:@"1" forKey:@"version"];   //API의 버전 정보 입니다.
    
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
- (void)resultParseAndSave:(NSString *)resultData
{
    NSDictionary *dic = [resultData JSONValue];
    
    NSMutableArray *itemList = [[dic objectForKey:@"items"] objectForKey:@"item"];
    
    if (itemListArray != nil) {
        [itemListArray removeAllObjects];
    }
    
    for (NSDictionary *item in itemList) {
        NSString *writerId      = [item valueForKey:@"writerId"];
        NSString *writerName    = [item valueForKey:@"writerName"];
        NSString *writerDate    = [item valueForKey:@"writeDate"];
        NSString *content       = [item valueForKey:@"content"];
        
        if (itemListArray != nil) {
            [itemListArray addObject:[[NSDictionary alloc] initWithObjectsAndKeys:writerId, STR_ID, writerName, STR_NAME, writerDate, STR_DATE, content, STR_CONTENT, nil]];
        }
    }
    
    [itemTableView reloadData];
}

@end
