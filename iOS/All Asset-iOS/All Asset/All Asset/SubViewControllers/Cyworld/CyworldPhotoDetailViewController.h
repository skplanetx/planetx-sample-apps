//
//  CyworldPhotoDetailViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AsyncImageView.h"

@interface CyworldPhotoDetailViewController : UIViewController
{
    IBOutlet AsyncImageView *photoImageView;
    IBOutlet UILabel *titleLabel;
    IBOutlet UILabel *dateLabel;
    NSDictionary *itemDictionary;
}

@property (nonatomic, retain) NSDictionary *itemDictionary;

-(IBAction)pressedHomeButtonItem:(id)sender;

@end
