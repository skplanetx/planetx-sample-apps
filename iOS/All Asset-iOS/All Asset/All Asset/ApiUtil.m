//
//  ApiUtil.m
//  SK Planet Service
//
//  Created by Jason Nam on 12. 10. 18..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ApiUtil.h"
#import "Defines.h"
#import "CommonUtil.h"
#import "SKPOP_v1.4.h"
#import "SBJsonParser.h"

@implementation ApiUtil
+ (NSMutableDictionary *)getAuthParams
{
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    [dict setValue:MY_APP_KEY forKey:@"appKey"];
    [dict setValue:MY_CLIENT_ID forKey:@"clientId"];
    [dict setValue:MY_SECRET forKey:@"secret"];
    [dict setValue:MY_SCOPE forKey:@"scope"];
    
    return dict;
}

/** RequestBundle 생성 */
+ (RequestBundle *)initRequestBundle:(NSDictionary *)header
                                 url:(NSString *)url
                              params:(NSMutableDictionary *)param
                             payload:(NSString *)payload
                      uploadFilePath:(NSString *)uploadFilePath
                          httpMethod:(SKPopHttpMethod)httpMethod
                         requestType:(SKPopContentType)requestType
                        responseType:(SKPopContentType)responseType
{
    
    RequestBundle *bundle = [RequestBundle new];
    
    if (header) [bundle setHeader:header];
    if (url) [bundle setUrl:url];
    if (param) [bundle setParameters:param];
    if (payload) [bundle setPayload:payload];
    if (uploadFilePath) [bundle setUploadFilePath:uploadFilePath];
    [bundle setHttpMethod:httpMethod];
    [bundle setRequestType:requestType];
    [bundle setResponseType:responseType];
    
    return bundle;
}

/** API 호출 */
+ (void)requestAPI:(id)target
          finished:(SEL)finishSel
            failed:(SEL)failSel
            bundle:(RequestBundle *)bundle
{
    //API 비동기 호출
    APIRequest *apiRequest = [APIRequest new];
    [apiRequest setDelegate:target
                   finished:finishSel
                     failed:failSel];
    [apiRequest aSyncRequest:bundle];
}

+ (NSString *)makeReceivers:(NSMutableArray *)buddyArray
{
    NSString *result = @"";
    NSString *receivers = @"";
    NSDictionary *buddyDic = nil;
    
    if (buddyArray) {
        
        for (int i=0;i<[buddyArray count]; i++)
        {
            buddyDic = [[buddyArray objectAtIndex:i] objectForKey:@"nateId"];
            receivers = [receivers stringByAppendingFormat:@"%@;",buddyDic];
        }
        
        result = [result stringByAppendingString:[receivers substringToIndex:[receivers length] - 1]];
        
        NSLog(@"result : %@", result);
    }
    
    return result;
}

+ (NSMutableArray *)getBuddyGroupArray:(NSMutableArray *)buddyArray
{
    NSMutableArray *groupArray = [NSMutableArray new];
    NSString *groupId = @"";
    
    if (buddyArray) {
        for (int i=0; i<[buddyArray count]; i++) {
            NSMutableDictionary *group = [[[[buddyArray objectAtIndex:i] objectForKey:@"groups"] objectAtIndex:0] objectForKey:@"group"];
            NSString *curGroupId = [group objectForKey:@"groupId"];
            NSLog(@"groupId : %@, curGroupId : %@", groupId, curGroupId);
            
            if ([groupId isEqualToString:curGroupId] == NO)
            {
                groupId = curGroupId;
                if ([groupArray containsObject:group] == NO) {
                    
                    [groupArray addObject:group];
                } 
            }
        }
    //groupId를 작은것부터 위로가도록 정렬
    [groupArray sortUsingComparator:^ NSComparisonResult(NSDictionary *d1, NSDictionary *d2)
    {
        NSString *n1 = [d1 objectForKey:@"groupId"];
        NSString *n2 = [d2 objectForKey:@"groupId"];
        return [n1 localizedCompare:n2];
        
    } ];
    
        return groupArray;
    }
    return nil;
}

+ (NSString *)makeMessage:(const char*)message
{
    NSString *result = @"";
    
    if (message != NULL)
    {
        NSString *sendMessage = [NSString stringWithUTF8String:message];
        result = [result stringByAppendingString:sendMessage];
    }
    
    return result;
}

//멜론 요청 URL
+ (NSString *)getRequestURL:(NSString *)host index:(NSUInteger)reqType
{
    NSString *result = [[NSString alloc] initWithString:host];
    
    switch (reqType) {
        case REQ_NEW_SONG:
            result = [result stringByAppendingString:@"/melon/newreleases/songs"];
            break;
        case REQ_NEW_ALBUM:
            result = [result stringByAppendingString:@"/melon/newreleases/albums"];
            break;
        case REQ_CHART:
            result = [result stringByAppendingString:@"/melon/charts/realtime"];
            break;
        case REQ_SEARCH_SONG:
            result = [result stringByAppendingString:@"/melon/songs"];
            break;
        case REQ_SEARCH_ALBUM:
            result = [result stringByAppendingString:@"/melon/albums"];
            break;
        case REQ_SEARCH_ARTIST:
            result = [result stringByAppendingString:@"/melon/artists"];
            break;
    }
    return result;
}

#pragma mark MELON webpage URL
+ (NSString *)makeWebPageURL:(NSString *)serviceType contentId:(NSString *)contentId menuId:(NSString *)menuId
{
    NSString *webPageURL = [NSString stringWithFormat:MELON_WEB_PAGE,serviceType,contentId,menuId];
    
    return webPageURL;
}

#pragma mark 11st Utils
+ (BOOL)isElevenstCategorySaved
{
    NSDictionary *elevenstDic = [NSDictionary dictionaryWithContentsOfFile:[CommonUtil getPlistPath:ELEVEN_CATEGORY_PLIST_FILE_NAME]];
    return [[elevenstDic objectForKey:ELEVEN_KEY_ISSAVED] boolValue];
}

+ (NSString *)loadSelectedElevenstCategory
{
    NSDictionary *elevenstDic = [NSDictionary dictionaryWithContentsOfFile:[CommonUtil getPlistPath:ELEVEN_CATEGORY_PLIST_FILE_NAME]];
    if (elevenstDic != nil) {
        return [elevenstDic valueForKey:ELEVEN_KEY_CATEGORYNAME];
    }
    return nil;
}

+ (BOOL)saveElevenstCategory:(NSString *)categoryCode categoryName:(NSString *)categoryName;
{
    NSString *path = [CommonUtil getPlistPath:ELEVEN_CATEGORY_PLIST_FILE_NAME];
    NSMutableDictionary *elevenstDic = [NSMutableDictionary dictionaryWithContentsOfFile:path];
    @try {
        [elevenstDic setValue:[NSNumber numberWithBool:YES] forKey:ELEVEN_KEY_ISSAVED];
        [elevenstDic setValue:categoryCode forKey:ELEVEN_KEY_CATEGORYCODE];
        [elevenstDic setValue:categoryName forKey:ELEVEN_KEY_CATEGORYNAME];
    }
    @catch (NSException *exception) {
        NSLog(@"error occured : %@", exception);
        return NO;
    }
    @finally {
        return [elevenstDic writeToFile:path atomically:YES];
    }
}

+ (void)errorAlert:(NSDictionary *)dic
{
    if (dic != nil && [dic objectForKey:@"error"] != nil) {
        NSString *message = [[dic objectForKey:@"error"] valueForKey:@"message"];
        
        [CommonUtil commonAlertView:message];
        return;
    }
}

+(NSString *)makeTurnType:(int)turnType
{
    NSString *strTurnType = @"";
    
    switch (turnType) {
        case d11:
            strTurnType = D11;
            break;
        case d13:
            strTurnType = D13;
            break;
        case d14:
            strTurnType = D14;
            break;
        case d15:
            strTurnType = D15;
            break;
        case d16:
            strTurnType = D16;
            break;
        case d17:
            strTurnType = D17;
            break;
        case d18:
            strTurnType = D18;
            break;
        case d19:
            strTurnType = D19;
            break;
        case d101:
            strTurnType = D101;
            break;
        case d102:
            strTurnType = D102;
            break;
        case d103:
            strTurnType = D103;
            break;
        case d104:
            strTurnType = D104;
            break;
        case d105:
            strTurnType = D105;
            break;
        case d106:
            strTurnType = D106;
            break;
        case d111:
            strTurnType = D111;
            break;
        case d112:
            strTurnType = D112;
            break;
        case d113:
            strTurnType = D113;
            break;
        case d114:
            strTurnType = D114;
            break;
        case d115:
            strTurnType = D115;
            break;
        case d116:
            strTurnType = D116;
            break;
        case d117:
            strTurnType = D117;
            break;
        case d118:
            strTurnType = D118;
            break;
        case d119:
            strTurnType = D119;
            break;
        case d120:
            strTurnType = D120;
            break;
        case d122:
            strTurnType = D122;
            break;
        case d123:
            strTurnType = D123;
            break;
        case d124:
            strTurnType = D124;
            break;
        case d131:
            strTurnType = D131;
            break;
        case d132:
            strTurnType = D132;
            break;
        case d133:
            strTurnType = D133;
            break;
        case d134:
            strTurnType = D134;
            break;
        case d135:
            strTurnType = D135;
            break;
        case d136:
            strTurnType = D136;
            break;
        case d137:
            strTurnType = D137;
            break;
        case d138:
            strTurnType = D138;
            break;
        case d139:
            strTurnType = D139;
            break;
        case d140:
            strTurnType = D140;
            break;
        case d141:
            strTurnType = D141;
            break;
        case d142:
            strTurnType = D142;
            break;
        case d200:
            strTurnType = D200;
            break;
        case d201:
            strTurnType = D201;
            break;
    }
    return strTurnType;
}

/**
 * 상품리스트 Table Cell 형식에 맞게 데이터 포맷 변환
 */
+ (NSMutableArray *)arrangeTempList:(NSMutableArray *)tempListArray
{
    NSMutableArray *arrangedResult = [NSMutableArray new];
    
    if (tempListArray == nil) {
        return nil;
    }
    for (NSDictionary *dic in tempListArray) {
        NSString *str_id            = [dic objectForKey:@"ProductCode"];
        NSString *str_title         = [dic objectForKey:@"ProductName"];
        NSString *str_image_url     = [dic objectForKey:@"ProductImage"];
        NSString *str_image300_url  = [dic objectForKey:@"ProductImage300"];
        NSString *str_productPrice  = [NSString stringWithFormat:@"%@원",[[dic objectForKey:@"ProductPrice"] stringValue]];
        NSString *str_salePrice     = [NSString stringWithFormat:@"%@원",[[dic objectForKey:@"SalePrice"] stringValue]];
        NSString *str_mileage       = [NSString stringWithFormat:@"%@ 마일리지",[[[dic objectForKey:@"Benefit"] objectForKey:@"Mileage"] stringValue]];
        NSString *str_Infree        = [[dic objectForKey:@"Benefit"] objectForKey:@"InFree"];
        if (str_Infree == nil) {
            str_Infree = @"무이자 혜택 없음";
       }
        NSString *str_seller        = [dic objectForKey:@"Seller"];
        NSString *str_sellerNick    = [[dic objectForKey:@"SellerNick"] stringByAppendingFormat:@"(%@)",str_seller];
        NSString *str_reviewCount   = [dic objectForKey:@"ReviewCount"];
        NSString *str_satifsy       = [dic objectForKey:@"BuySatisfy"];
        NSString *str_comment       = [@"만족도" stringByAppendingFormat:@" %@%%, 후기/리뷰 %@건",str_satifsy, str_reviewCount];
        NSString *str_delivery      = [dic objectForKey:@"Delivery"];

        [arrangedResult addObject:[[NSDictionary alloc] initWithObjectsAndKeys:
                                   str_id, STR_ID,
                                   str_image_url, STR_IMAGE_URL,
                                   str_image300_url, STR_IMAGE300_URL,
                                   str_title, STR_TITLE,
                                   str_productPrice, STR_PRICE,
                                   str_salePrice, STR_SALE_PRICE,
                                   str_sellerNick, STR_SELLER,
                                   str_comment, STR_COMMENT,
                                   str_delivery, STR_CONDITION,
                                   str_reviewCount, STR_REVIEW_CNT,
                                   str_satifsy, STR_SATISFY,
                                   str_mileage, STR_MILEAGE,
                                   str_Infree, STR_INFREE,
                                   nil]];
    }
    return arrangedResult;
}

@end
