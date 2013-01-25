
//
//  MainViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MarqueeLabel.h"

@interface MainViewController : UIViewController <UIAlertViewDelegate>{
    
    NSDictionary *oAuthDic;
    NSInteger reqTag;       
}

@property (nonatomic, retain) NSDictionary *oAuthDic;

-(SEL)getSelectorLiveService:(NSString *)service;

@end
