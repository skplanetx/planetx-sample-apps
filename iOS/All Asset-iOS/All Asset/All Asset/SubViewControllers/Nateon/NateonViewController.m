//
//  NateonViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 15..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "NateonViewController.h"
#import "Defines.h"
#import "AppDelegate.h"
#import "ApiUtil.h"
#import "CommonUtil.h"
#import "SBJson.h"

#define STR_GROUP @"Group"
#define STR_ARRAY @"Array"
#define STR_NAME @"Name"
#define STR_ALIAS @"Alias"
#define STR_ID @"Id"
#define REQ_FRIEND_LIST     1
#define REQ_FRIEND_INFO     2
#define REQ_NATEON_MESSAGE  3
#define ALERT_TAG           -1

@interface NateonViewController ()

@end

@implementation NateonViewController
@synthesize reqTag;

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
    //친구 목록 기본 정보를 설정한다.
    [self setupBuddyList];
    //검색 필터링을 위한 별도의 배열을 설정한다.
    displayBuddyListArray = [[NSMutableArray alloc] initWithArray:buddyListArray];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark SKP OPR API
-(void)setupBuddyList
{
    [self requestNateOnFriend:REQ_FRIEND_LIST nateIds:nil];
}

#pragma mark UITableViewDataSource methods
/**
 * 각 그룹 별 친구 목록 개수 리턴
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(displayBuddyListArray!=nil)
    {
        NSDictionary *dic = [displayBuddyListArray objectAtIndex:section];
        if(dic!=nil)
        {
            NSMutableArray *buddy = [dic valueForKey:STR_ARRAY];
            if(buddy!=nil)
                return [buddy count];
        }
    }
    return 0;
}

/**
 * 친구 목록의 그룹 개수 리턴
 */
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if(displayBuddyListArray!=nil)
        return [displayBuddyListArray count];
    return 0;
}

/**
 * 그룹명을 리턴
 */
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    if(displayBuddyListArray!=nil)
    {
        NSDictionary *dic = [displayBuddyListArray objectAtIndex:section];
        if(dic!=nil)
        {
            return [dic valueForKey:STR_GROUP];
        }
    }
    return nil;
}

/**
 * 테이블에 친구 정보를 표시한다.
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:nil];
        cell.textLabel.font = [UIFont systemFontOfSize:16];
        cell.tag = indexPath.section*1000+indexPath.row;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        if(displayBuddyListArray!=nil)
        {
            NSDictionary *dic = [displayBuddyListArray objectAtIndex:indexPath.section];
            if(dic!=nil)
            {
                NSMutableArray *buddy = [dic valueForKey:STR_ARRAY];
                if(buddy!=nil)
                {
                    cell.textLabel.text = [[buddy objectAtIndex:indexPath.row] valueForKey:STR_NAME];
                    cell.detailTextLabel.text = [[buddy objectAtIndex:indexPath.row] valueForKey:STR_ALIAS];
                }
            }
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
/**
 * 특정 컬럼을 선택 할 시 UIAlertView 를 Popup 하여 메시지 전송하게 한다.
 */
- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(displayBuddyListArray!=nil)
    {
        NSDictionary *dic = [displayBuddyListArray objectAtIndex:indexPath.section];
        if(dic!=nil)
        {
            NSMutableArray *buddy = [dic valueForKey:STR_ARRAY];
            if(buddy!=nil)
            {
                NSString *recevier = [[buddy objectAtIndex:indexPath.row] valueForKey:STR_NAME];
                recevier = [recevier stringByAppendingFormat:@"(%@)", [[buddy objectAtIndex:indexPath.row] valueForKey:STR_ALIAS]];
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"쪽지전송!"
                                                                    message:recevier
                                                                   delegate:self
                                                          cancelButtonTitle:@"전송"
                                                          otherButtonTitles:@"취소", nil];
                alertView.alertViewStyle = UIAlertViewStylePlainTextInput;
                //tag 를 통하여 필요 시 수신자 정보 전달
                alertView.tag = indexPath.section*1000+indexPath.row;
                [alertView show];
            }
        }
    }
    return nil;
}

#pragma mark UISearchBarDelegate methods
/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    if([searchBar.text length] == 0)
    {
        [displayBuddyListArray removeAllObjects];
        [displayBuddyListArray addObjectsFromArray:buddyListArray];
        [buddyListTableView reloadData];
    }
}

/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar
{
    [buddyListTableView setUserInteractionEnabled:NO];
    return YES;
}

/**
 * UISearchBar 입력 시 Delegation 함수.
 */
- (void)searchBarSearchButtonClicked:(UISearchBar*)searchBar
{
    if([searchBar.text length] == 0)
    {
        [displayBuddyListArray removeAllObjects];
        [displayBuddyListArray addObjectsFromArray:buddyListArray];
    }
    else
    {
        [displayBuddyListArray removeAllObjects];
        for(NSDictionary *dic in buddyListArray)
        {
            NSMutableDictionary *filterDic = [[NSMutableDictionary alloc] init];
            [filterDic setObject:[dic valueForKey:STR_GROUP] forKey:STR_GROUP];
            NSMutableArray *filterArray = [[NSMutableArray alloc] init];
            for(NSDictionary *buddy in [dic valueForKey:STR_ARRAY])
            {
                NSString *name =  [buddy valueForKey:STR_NAME];
                NSRange r = [name rangeOfString:searchBar.text options:NSCaseInsensitiveSearch];
                if (r.location != NSNotFound)
                {
                    [filterArray addObject:buddy];
                }
            }
            if([filterArray count] > 0)
            {
                [filterDic setObject:filterArray forKey:STR_ARRAY];
                [displayBuddyListArray addObject:filterDic];
            }
        }
    }
    [searchBar resignFirstResponder];
    [buddyListTableView reloadData];
}

#pragma mark UIAlertViewDelegate methods
/**
 * UIAlertView Delegation 함수. 실제 NateOn 쪽지 전송을 처리한다.
 */
-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    NSLog(@"buttonIndex : %d", buttonIndex);
    
    if (buttonIndex == 0) {
        if(displayBuddyListArray!=nil)
        {
            NSDictionary *dic = [displayBuddyListArray objectAtIndex:alertView.tag/1000];
            if(dic!=nil)
            {
                NSMutableArray *buddy = [dic valueForKey:STR_ARRAY];
                if(buddy!=nil)
                {
                    NSString *receiverId =[[buddy objectAtIndex:alertView.tag%1000] valueForKey:STR_ID];
                    [self sendNateOnMessage:receiverId message:[alertView textFieldAtIndex:0].text];
                }
            }
        }
    }
}

/**
 * 메인 화면 복귀
 */
-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

/**
 * SearchBar Dismiss
 */
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [[event allTouches] anyObject];
    if ([buddySearchBar isFirstResponder] && [touch view] != buddySearchBar)
    {
        [buddySearchBar resignFirstResponder];
    }
    [buddyListTableView setUserInteractionEnabled:YES];
    [super touchesBegan:touches withEvent:event];
}

#pragma mark API request
/**
 * 설명 : NateOn 쪽지 발송
 * RequestURI : https://apis.skplanetx.com/nateon/notes
 */
-(BOOL)sendNateOnMessage:(NSString *)userid message:(NSString*)message
{
    reqTag = REQ_NATEON_MESSAGE;
    
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

/**
 * 설명 : NateOn 친구목록 조회, 친구정보 조회 
 * 친구목록 조회 RequestURI : https://apis.skplanetx.com/nateon/buddies
 * 친구정보 조회 RequestURI : https://apis.skplanetx.com/nateon/buddies/profiles
 */
- (void)requestNateOnFriend:(NSInteger)requestType nateIds:(NSString *)nateIds
{
    [CommonUtil commonStartActivityCenterIndicator:UIActivityIndicatorViewStyleWhiteLarge];
    
    reqTag = requestType;
    
    NSString *path = (requestType == REQ_FRIEND_LIST ? @"/nateon/buddies" : @"/nateon/buddies/profiles");
    
    NSString *url = [SERVER_SSL stringByAppendingString:path] ;
    
    //Querystring Parameters
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setValue:@"1" forKey:@"version"];         //API의 버전 정보입니다.
    if (reqTag == REQ_FRIEND_INFO) {
        [params setValue:nateIds forKey:@"nateIds"];  //조회할 친구들의 ID를 입력합니다.(ex)test@nate.com;abcdef@nate.com
    } else {
        [params setValue:@"1" forKey:@"page"];
        [params setValue:@"10" forKey:@"count"];
    }
    [params setValue:@"id.asc" forKey:@"order"];      //친구 목록의 정렬 방식을 지정합니다.(id.asc / id.desc: 아이디 순)
    
    //Bundle 설정
    RequestBundle *bundle = [ApiUtil initRequestBundle:nil
                                                   url:url
                                                params:params
                                               payload:nil
                                        uploadFilePath:nil
                                            httpMethod:SKPopHttpMethodGET
                                           requestType:SKPopContentTypeFORM
                                          responseType:SKPopContentTypeJSON];
    
    //API 호출
    [ApiUtil requestAPI:self finished:@selector(apiRequestFinished:) failed:@selector(apiRequestFailed:) bundle:bundle];
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
    
    if (reqTag == REQ_NATEON_MESSAGE) {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"알림"
                                                            message:@"쪽지전송 완료"
                                                           delegate:nil
                                                  cancelButtonTitle:@"확인"
                                                  otherButtonTitles:nil, nil];
        alertView.alertViewStyle = UIAlertViewStyleDefault;
        [alertView setTag:ALERT_TAG];
        [alertView show];
        
    } else if (reqTag == REQ_FRIEND_LIST) {
        
        NSDictionary *nateon = [dic objectForKey:@"nateon"];
        
        //buddies 정보
        NSDictionary *buddies = [nateon objectForKey:@"buddies"];
        
        //buddy정보 가져오기
        NSMutableArray *buddy = [buddies objectForKey:@"buddy"];
        
        myBuddies = [ApiUtil makeReceivers:buddy];
        
        //그룹 정보만 분리
        groupListArray = [ApiUtil getBuddyGroupArray:buddy];
        
        //별명이 포함된 네이트온 친구정보 조회
        [self requestNateOnFriend:REQ_FRIEND_INFO nateIds:myBuddies];
    } else if (reqTag == REQ_FRIEND_INFO) {
        NSMutableArray *buddy = [[dic objectForKey:@"buddies"] objectForKey:@"buddy"];
        
        buddyListArray = [self getBuddyListByGroup:buddy groupArray:groupListArray];
        
        [displayBuddyListArray removeAllObjects];
        [displayBuddyListArray addObjectsFromArray:buddyListArray];
        [buddyListTableView reloadData];
    }
}

- (NSMutableArray *)getBuddyListByGroup:(NSMutableArray *)buddy groupArray:(NSMutableArray *)groupArray
{
    if (buddy == nil || groupArray == nil) {
        return nil;
    }
    //그룹을 담을 Array
    NSMutableArray *array = [NSMutableArray new];
    
    for (int i=0; i<groupArray.count; i++)
    {
        //현재 그룹의 값을 담을 Array
        NSMutableArray *curGroup = [NSMutableArray new];
        
        for (NSMutableDictionary *dic in buddy) {
            //현재 행의 그룹 딕셔너리
            NSDictionary *dicGroup = [[[dic objectForKey:@"groups"] objectAtIndex:0] objectForKey:@"group"];
            
            //비교할 그룹 딕셔너리
            NSDictionary *standardDic = [groupArray objectAtIndex:i];
            
            if ([dicGroup isEqualToDictionary:standardDic] == YES) {
                
                NSString *nateId    = [dic objectForKey:@"nateId"];
                NSString *name      = [dic objectForKey:@"name"];
                NSString *nickname  = [dic objectForKey:@"nickname"];
                
                NSDictionary *curRowDic = [[NSDictionary alloc] initWithObjectsAndKeys:name, STR_NAME, nickname , STR_ALIAS, nateId, STR_ID, nil];
                
                [curGroup addObject:curRowDic];
                
                NSString *groupName = [dicGroup objectForKey:@"groupName"];
                
                NSDictionary *curGroupDic = [[NSDictionary alloc] initWithObjectsAndKeys:groupName, STR_GROUP, curGroup, STR_ARRAY, nil];
                
                if ([array containsObject:curGroupDic] == YES) {
                    break;
                }
                [array addObject:curGroupDic];
            }
        }
    }
    
    return array;
}

@end
