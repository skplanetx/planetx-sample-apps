//
//  LoginViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    IBOutlet UITableView *loginTableView;
}

@end
