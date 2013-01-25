//
//  NateOnMessageViewController.h
//  All Asset
//
//  Created by Jason Nam on 12. 12. 20..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NateOnMessageViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate>
{
    NSMutableArray *messageList;
    IBOutlet UITableView *messageTableView;
    
    int reqType;
}

@end
