//
//  ElevenstDetailViewController.h
//  All Asset
//
//  Created by fulstory on 12. 10. 16..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AsyncImageView.h"

@interface ElevenstDetailViewController : UIViewController
{
    NSDictionary *itemInfoDictionary; //상품 정보
}

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet AsyncImageView *titleImage;
@property (weak, nonatomic) IBOutlet UILabel *satisLabel;
@property (weak, nonatomic) IBOutlet UIButton *commentButton;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;
@property (weak, nonatomic) IBOutlet UILabel *disPriceLabel;
@property (weak, nonatomic) IBOutlet UILabel *mileageLabel;
@property (weak, nonatomic) IBOutlet UILabel *extraRewardLabel;

@property (nonatomic, retain) NSDictionary *itemInfoDictionary;

-(IBAction)pressedHomeButtonItem:(id)sender;
-(IBAction)pressedCommentButton:(id)sender;

@end
