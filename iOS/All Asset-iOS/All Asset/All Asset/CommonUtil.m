//
//  CommonUtil.m
//  SK Planet Service
//
//  Created by Jason Nam on 12. 10. 25..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "CommonUtil.h"
#import "Defines.h"
#import "NSXMLDocument.h"
#import "NSXMLElement.h"

@implementation CommonUtil

+(float)systemVersion{
    return [[[UIDevice currentDevice]systemVersion]floatValue];
}

+ (void)commonAlertView:(NSString*)alertMessage
{   
    UIAlertView *globalAlert = [[UIAlertView alloc] initWithTitle:@"알림"
                                                          message:alertMessage
                                                         delegate:nil
                                                cancelButtonTitle:nil
                                                otherButtonTitles:@"확인", nil];

    [globalAlert setTag:-1];
    [globalAlert show];
}

+ (void)commonCustomAlertView:(NSString*)alertMessage
                CancelMessage:(NSString*)cancelTitle
                    OkMessage:(NSString*)okTitle
                          Tag:(NSInteger)tag
                     delegate:(id)delegate{
    
    
    UIAlertView *globalAlert = [[UIAlertView alloc] initWithTitle:@"알림"
                                                          message:alertMessage
                                                         delegate:delegate
                                                cancelButtonTitle:cancelTitle
                                                otherButtonTitles:okTitle, nil];
    
    [globalAlert setTag:tag];
    [globalAlert show];
}

UIActivityIndicatorView * activity;
UIView *loadingBGView;
+ (void)commonStartActivityCenterIndicator:(UIActivityIndicatorViewStyle)style{
    
    if(activity){
        [activity stopAnimating];
        activity = nil;
    }
    
    if(loadingBGView){
        [loadingBGView removeFromSuperview];
        loadingBGView = nil;
    }
    
    CGRect mainBounds = [[UIScreen mainScreen]bounds];
    CGFloat pointX = mainBounds.size.width / 2.0 - 19;
    CGFloat pointY = mainBounds.size.height / 2.0;
    
    loadingBGView = [[UIView alloc]initWithFrame:mainBounds];
    [loadingBGView setAlpha:0.5];
    [loadingBGView setBackgroundColor:[UIColor darkGrayColor]];
    
    activity= [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:style];
    [activity setColor:[UIColor whiteColor]];
    
    [activity setFrame:CGRectMake(pointX, pointY, 39.0f, 39.0f)];
    [loadingBGView addSubview:activity];
    
    [[[UIApplication sharedApplication]keyWindow] addSubview:loadingBGView];
    
    [activity startAnimating];
    
    //StatusBar Indicator
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

+ (void)commonStopActivityIndicator{
    
    if( activity  && [activity isAnimating]){
        [activity stopAnimating];
        [loadingBGView removeFromSuperview];
        activity = nil;
        loadingBGView = nil;
        
        //StatusBar Indicator
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    }
}

/**
 * pList 경로 얻어오기
 */
+ (NSString *)getPlistPath:(NSString *)plistName
{
    NSError *error;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:plistName];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if(![fileManager fileExistsAtPath:path])
    {
        NSString *bundle = [[NSBundle mainBundle] pathForResource:plistName ofType:@"plist"];
        [fileManager copyItemAtPath:bundle toPath:path error:&error];
    }
    
    return path;
}

/**
 * Property List 저장 공통
 */
+ (void)savePlist:(NSDictionary *)dic plist:(NSString *)plistName
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:plistName];
    
    [dic writeToFile:path atomically:YES];
}

+ (void)saveTmapPlist:(NSMutableArray *)array
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:TMAP_RECENT_PLIST_FILE_NAME];
    [array writeToFile:path atomically:YES];
}

+ (NSString*)getXMLElement:(NSString*)name document:(NSXMLElement*)document
{
    NSArray *listArray = document.Childs;
    
    for (NSXMLElement *e in listArray) {
        if([e.Name isEqualToString:name])
            return e.Value;
	}
    
    return nil;
}

/**
 * 웹 브라우저 런칭
 */
+ (void)launchBrowser:(NSString *)url
{
    NSLog(@"launchBrowser url : %@", url);
    BOOL result = [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
    
    if (result) {
        NSLog(@"launchBrowser result : YES");
    } else {
        NSLog(@"launchBrowser result : NO");
    }
}
@end
