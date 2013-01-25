//
//  ConfigurationViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ConfigurationElevenstViewController.h"
#import "ConfigurationTmapViewController.h"

@interface ConfigurationViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UIPickerViewDataSource, UIPickerViewDelegate, UIActionSheetDelegate> {
    IBOutlet UITableView *configurationTableView;
    ConfigurationElevenstViewController *configElevenstViewController;
    ConfigurationTmapViewController *configTmapViewController;
    
    NSMutableArray *displayCountArray;
    int selectedDisplayCount;
}

@property (nonatomic, retain) ConfigurationElevenstViewController *configElevenstViewController;
@property (nonatomic, retain) ConfigurationTmapViewController *configTmapViewController;

@end
