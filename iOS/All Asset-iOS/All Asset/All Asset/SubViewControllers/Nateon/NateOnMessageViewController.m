//
//  NateOnMessageViewController.m
//  All Asset
//
//  Created by Jason Nam on 12. 12. 20..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "NateOnMessageViewController.h"
#import "CommonUtil.h"
#import "SKPOP_v1.4.h"
#import "ApiUtil.h"
#import "Defines.h"
#import "SBJson.h"

#define REQ_RECEIVE     0
#define REQ_SEND        1
#define ALERT_TAG       100

@interface NateOnMessageViewController ()

@end

@implementation NateOnMessageViewController

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
    [self setupMessageList];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)setupMessageList
{
    messageList = [NSMutableArray new];
    [self requestNateOnMessage];
}

#pragma mark UITableViewDataSource methods
/**
 * 구간 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(messageList!=nil)
    {
        return [messageList count];
    }
    return 0;
}

/**
 * 테이블에 구간 정보를 표시한다.
 * CustomCell 인 ElevenstItemListTableViewCell 을 사용한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:nil];
        cell.textLabel.font = [UIFont systemFontOfSize:16];
        cell.tag = indexPath.section*1000+indexPath.row;
        cell.accessoryType = UITableViewCellAccessoryNone;
        if(messageList!=nil)
        {
            NSDictionary *dic = [messageList objectAtIndex:indexPath.row];
            if(dic!=nil)
            {
                cell.textLabel.text = [dic valueForKey:STR_CONTENT];
                cell.detailTextLabel.text = [dic valueForKey:STR_NAME];
            }
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
/**
 * 특정 컬럼을 선택 할 시 해당 URL 을 기준으로 브라우저 호출.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"sender : %@", [[messageList objectAtIndex:indexPath.row] valueForKey:STR_NAME]);
    
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"쪽지전송!"
                                                        message:[[messageList objectAtIndex:indexPath.row] valueForKey:STR_NAME]
                                                       delegate:self
                                              cancelButtonTitle:@"전송"
                                              otherButtonTitles:@"취소", nil];
    alertView.alertViewStyle = UIAlertViewStylePlainTextInput;
    //tag 를 통하여 필요 시 수신자 정보 전달
    alertView.tag = indexPath.section*1000+indexPath.row;
    [alertView show];
    
    return nil;
}

#pragma mark UIAlertViewDelegate methods
/**
 * UIAlertView Delegation 함수. 실제 NateOn 쪽지 전송을 처리한다.
 */
-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    NSLog(@"buttonIndex : %d", buttonIndex);
    
    if (buttonIndex == 0) {
        if(messageList!=nil)
        {
            NSDictionary *dic = [messageList objectAtIndex:alertView.tag/1000];
            if(dic!=nil)
            {
                NSString *recevier = [[dic valueForKey:STR_NAME] substringToIndex:[[dic valueForKey:STR_NAME] length] - 1];
                NSRange range = [recevier rangeOfString:@"<"];
                NSLog(@"sender2 : %@", [recevier substringFromIndex:range.location+1]);
                
                [self sendNateOnMessage:recevier message:[alertView textFieldAtIndex:0].text];
            }
        }
    }
}

#pragma mark API Request
/**
 * 설명 : 네이트온 쪽지 목록 검색
 * RequestURI : https://apis.skplanetx.com/nateon/notes/{seq}?version={version}&direction={direction}&count={count}&boxType={boxType}
 * Request PathParam :
 * {seq} 쪽지의 고유 일련번호입니다.
 */
- (void)requestNateOnMessage
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];

    reqType = REQ_RECEIVE;
    
    //쪽지 수신 여부에 따라 호출 선택
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
    
}

-(BOOL)sendNateOnMessage:(NSString *)userid message:(NSString*)message
{
    reqType = REQ_SEND;
    
    //쪽지 보내기 URL
    NSString *url = [SERVER_SSL stringByAppendingString:@"/nateon/notes"];
    
    //Querystring Parameters
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setValue:@"1" forKey:@"version"];       //API의 버전 정보입니다.
    
    //Request Payload
    [params setValue:userid forKey:@"receivers"];   //쪽지를 수신할 사용자의 ID를 입력합니다.
    [params setValue:message forKey:@"message"];    //쪽지 내용입니다.(2000자를 초과할 수 없습니다)
    [params setValue:@"N" forKey:@"confirm"];       //쪽지 수신 확인 여부(Y: 수신 확인, N: 수신 확인 안함)
    
    //Bundle 설정
    RequestBundle *bundle = [ApiUtil initRequestBundle:nil
                                                   url:url
                                                params:params
                                               payload:nil
                                        uploadFilePath:nil
                                            httpMethod:SKPopHttpMethodPOST
                                           requestType:SKPopContentTypeFORM
                                          responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:bundle];
    
    return YES;
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
- (void)resultParseAndSave:(NSString *)result
{
    NSLog(@"result : %@", result);
    
    NSDictionary *dic = [result JSONValue];
    
    [ApiUtil errorAlert:dic];
    
    if (reqType == REQ_SEND) {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"알림"
                                                            message:@"쪽지전송 완료"
                                                           delegate:nil
                                                  cancelButtonTitle:@"확인"
                                                  otherButtonTitles:nil, nil];
        alertView.alertViewStyle = UIAlertViewStyleDefault;
        [alertView setTag:ALERT_TAG];
        [alertView show];
    } else if (reqType == REQ_RECEIVE) {
        NSMutableArray *notesList = [[dic objectForKey:@"notes"] objectForKey:@"note"];
        NSMutableArray *noteDicList = [NSMutableArray new];
        
        for (NSDictionary *noteDic in notesList) {
            NSString *sender    = [noteDic valueForKey:@"sender"];
            NSString *message   = [noteDic valueForKey:@"message"];
            
            [noteDicList addObject:[[NSDictionary alloc] initWithObjectsAndKeys:sender, STR_NAME, message, STR_CONTENT, nil]];
        }
        [messageList removeAllObjects];
        [messageList addObjectsFromArray:noteDicList];
        [messageTableView reloadData];
    }
}

/**
 * 메인 화면 복귀
 */
-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
