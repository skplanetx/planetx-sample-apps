//
//  CyworldPhotoDetailViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "CyworldPhotoDetailViewController.h"
#define STR_TITLE		@"Title"
#define STR_DATE		@"Date"
#define STR_IMAGE_URL	@"ImageURL"

@interface CyworldPhotoDetailViewController ()

@end

@implementation CyworldPhotoDetailViewController
@synthesize itemDictionary;

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
    // Do any additional setup after loading the view from its nib.
    NSURL *url = [NSURL URLWithString:[itemDictionary valueForKey:STR_IMAGE_URL]];
    [photoImageView setContentMode:UIViewContentModeScaleAspectFill];
    [photoImageView initWithImageAtURL:url];
    titleLabel.text = [itemDictionary valueForKey:STR_TITLE];
    dateLabel.text = [itemDictionary valueForKey:STR_DATE];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
