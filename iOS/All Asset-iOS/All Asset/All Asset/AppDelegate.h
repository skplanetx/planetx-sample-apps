//
//  AppDelegate.h
//  All Asset
//
//  Created by fulstory on 12. 10. 24..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate> {
    NSMutableArray *configurationArray;
}

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, retain) NSMutableArray *configurationArray;

-(void)loadConfiguration;
-(void)saveConfiguration:(NSMutableArray *)array;
-(NSMutableArray *)getLiveService;
-(NSMutableArray *)getShortcutService;

-(BOOL)isLive:(NSInteger)index;
-(BOOL)isShortcut:(NSInteger)index;
-(BOOL)isEdit:(NSInteger)index;
-(BOOL)isAllowMessage;
-(int)getDisplayCount;

-(void)setIsLive:(NSInteger)index isOn:(BOOL)isOn;
-(void)setIsShortcut:(NSInteger)index isOn:(BOOL)isOn;
- (void)setIsAllowMessagge:(NSInteger)index isOn:(BOOL)isOn;
- (void)setCyworldGuestbookCount:(NSInteger)index displayCount:(int)count;
@end
