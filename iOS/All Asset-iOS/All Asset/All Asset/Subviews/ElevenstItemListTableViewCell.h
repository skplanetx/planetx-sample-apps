//
//  ElevenstItemListTableViewCell.h
//  All Asset
//
//  Created by fulstory on 12. 10. 23..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

//11번가 상품 정보 테이블 뷰에 대한 커스텀 뷰 셀 클래스


#import <UIKit/UIKit.h>
#import "AsyncImageView.h"

@interface ElevenstItemListTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet AsyncImageView *titleImage;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UILabel *price;
@property (weak, nonatomic) IBOutlet UILabel *seller;
@property (weak, nonatomic) IBOutlet UILabel *comment;
@property (weak, nonatomic) IBOutlet UILabel *condition;

@end
