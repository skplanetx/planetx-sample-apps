//
//  AppDelegate.m
//  All Asset
//
//  Created by fulstory on 12. 10. 24..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "AppDelegate.h"
#import "Defines.h"

@implementation AppDelegate
@synthesize configurationArray;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
    [self loadConfiguration];
    //NSLog(@"Load %@", configurationArray);
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


#pragma mark Property List Persistence
/**
 * 설정 Property List 파일 로딩
 */
-(void)loadConfiguration
{
    NSError *error;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:CONFIGURATION_PLIST_FILE_NAME];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if(![fileManager fileExistsAtPath:path])
    {
        NSString *bundle = [[NSBundle mainBundle] pathForResource:CONFIGURATION_PLIST_FILE_NAME ofType:@"plist"];
        [fileManager copyItemAtPath:bundle toPath:path error:&error];
    }
    
    configurationArray = [NSMutableArray arrayWithContentsOfFile:path];
}

/**
 * 설정 Property List 파일 저장
 */
-(void)saveConfiguration:(NSMutableArray *)array;
{
    configurationArray = array;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:CONFIGURATION_PLIST_FILE_NAME];
    [array writeToFile:path atomically:YES];
    [self loadConfiguration];
}

/**
 * 라이브 서비스 리턴
 */
-(NSMutableArray *)getLiveService
{
    if(configurationArray!=nil)
    {
        NSMutableArray *value = [[NSMutableArray alloc] init];
        
        for(NSDictionary *dic in configurationArray)
        {
            BOOL isLived = [[dic objectForKey:CONFIGURATION_KEY_IS_LIVE] boolValue];
            if(isLived)
            {
                [value addObject:dic];
            }
        }
        return value;
    }
    return nil;
}

/**
 * 단축 버튼 서비스 리턴
 */
-(NSMutableArray *)getShortcutService
{
    if(configurationArray!=nil)
    {
        NSMutableArray *value = [[NSMutableArray alloc] init];
        
        for(NSDictionary *dic in configurationArray)
        {
            BOOL isLived = [[dic objectForKey:CONFIGURATION_KEY_IS_SHORTCUT] boolValue];
            if(isLived)
            {
                [value addObject:dic];
            }
        }
        return value;
    }
    return nil;
}

/**
 * 라이브 노출 설정 여부
 */
-(BOOL)isLive:(NSInteger)index
{
    if(configurationArray!=nil)
    {
        NSDictionary *item = [configurationArray objectAtIndex:index];
        return [[item objectForKey:CONFIGURATION_KEY_IS_LIVE] boolValue];
    }
    return NO;
}

/**
 * 단축 영역 노출 설정 여부
 */
-(BOOL)isShortcut:(NSInteger)index
{
    if(configurationArray!=nil)
    {
        NSDictionary *item = [configurationArray objectAtIndex:index];
        return [[item objectForKey:CONFIGURATION_KEY_IS_SHORTCUT] boolValue];
    }
    return NO;
}

/**
 * 설정하기 여부
 */
-(BOOL)isEdit:(NSInteger)index
{
    if(configurationArray!=nil)
    {
        NSDictionary *item = [configurationArray objectAtIndex:index];
        return [[item objectForKey:CONFIGURATION_KEY_IS_EDIT] boolValue];
    }
    return NO;
}

/**
 * 네이트온 쪽지 수신 여부
 */
- (BOOL)isAllowMessage
{
    if (configurationArray != nil) {
        NSDictionary *item = [configurationArray objectAtIndex:0];
        return [[item objectForKey:CONFIGURATION_KEY_ALLOW_MESSAGE] boolValue];
    }
    return NO;
}

- (int)getDisplayCount
{
    if (configurationArray != nil) {
        NSDictionary *item = [configurationArray objectAtIndex:1];
        return [[item objectForKey:CONFIGURATION_KEY_ALLOW_MESSAGE] intValue];
    }
    return 5;
}

/**
 * 라이브 노출 설정
 */
-(void)setIsLive:(NSInteger)index isOn:(BOOL)isOn
{
    NSDictionary *item = [configurationArray objectAtIndex:index];
    [item setValue:[NSNumber numberWithBool:isOn] forKey:CONFIGURATION_KEY_IS_LIVE];
    [configurationArray replaceObjectAtIndex:index withObject:item];
    [self saveConfiguration:configurationArray];
}

/**
 * 단축 영역 노출 설정
 */
-(void)setIsShortcut:(NSInteger)index isOn:(BOOL)isOn
{
    NSDictionary *item = [configurationArray objectAtIndex:index];
    [item setValue:[NSNumber numberWithBool:isOn] forKey:CONFIGURATION_KEY_IS_SHORTCUT];
    [configurationArray replaceObjectAtIndex:index withObject:item];
    [self saveConfiguration:configurationArray];
}

/**
 * 네이트온 쪽지 수신 설정
 */
- (void)setIsAllowMessagge:(NSInteger)index isOn:(BOOL)isOn
{
    NSDictionary *item = [configurationArray objectAtIndex:index];
    [item setValue:[NSNumber numberWithBool:isOn] forKey:CONFIGURATION_KEY_ALLOW_MESSAGE];
    [configurationArray replaceObjectAtIndex:index withObject:item];
    [self saveConfiguration:configurationArray];
}

/**
 * 싸이월드 방명록 노출 갯수 설정
 */
- (void)setCyworldGuestbookCount:(NSInteger)index displayCount:(int)count
{
    NSDictionary *item = [configurationArray objectAtIndex:index];
    [item setValue:[NSNumber numberWithInt:count] forKey:CONFIGURATION_KEY_DISPLAY_COUNT];
    [configurationArray replaceObjectAtIndex:index withObject:item];
    [self saveConfiguration:configurationArray];
}
@end
