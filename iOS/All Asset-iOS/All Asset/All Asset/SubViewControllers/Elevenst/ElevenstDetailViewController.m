//
//  ElevenstDetailViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ElevenstDetailViewController.h"
#import "ElevenstCommentViewController.h"
#import "Defines.h"
#import "ApiUtil.h"

@interface ElevenstDetailViewController ()

@end

@implementation ElevenstDetailViewController
@synthesize itemInfoDictionary;
@synthesize titleLabel, satisLabel, priceLabel, disPriceLabel, mileageLabel, extraRewardLabel;
@synthesize titleImage, commentButton;

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

    titleLabel.text = [itemInfoDictionary valueForKey:STR_TITLE];
    NSURL *url = [NSURL URLWithString:[itemInfoDictionary valueForKey:STR_IMAGE300_URL]];
    [titleImage setContentMode:UIViewContentModeCenter];
    if (url != nil) {
        [titleImage initWithImageAtURL:url];
    }
    priceLabel.text = [itemInfoDictionary valueForKey:STR_PRICE];
    //만족도 표시
    satisLabel.text = [NSString stringWithFormat:@"상품만족도 %@%%",[[itemInfoDictionary valueForKey:STR_SATISFY] stringValue]];
    //후기/리뷰 카운트
    //commentButton.titleLabel.text
    NSString *buttonTitle = [NSString stringWithFormat:@"후기/리뷰 %@건",[[itemInfoDictionary valueForKey:STR_REVIEW_CNT] stringValue]];
    [self setButtonTitle:buttonTitle];
    //할인모음가
    disPriceLabel.text = [itemInfoDictionary valueForKey:STR_SALE_PRICE];
    //마일리지
    mileageLabel.text = [itemInfoDictionary valueForKey:STR_MILEAGE];
    //카드 무이자
    extraRewardLabel.text = [itemInfoDictionary valueForKey:STR_INFREE];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)pressedCommentButton:(id)sender
{
    ElevenstCommentViewController *controller = [[ElevenstCommentViewController alloc] initWithNibName:@"ElevenstCommentViewController" bundle:nil];
    controller.itemInfoDictionary = [itemInfoDictionary copy];
    [self presentViewController:controller animated:YES completion:nil];
}

-(IBAction)pressedHomeButtonItem:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
    
    
}

-(void)setButtonTitle:(NSString *)title
{
    [commentButton setTitle:title forState:UIControlStateNormal];
}

@end
