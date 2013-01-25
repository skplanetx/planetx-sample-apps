//
//  ElevenstCommentListTableViewCell.h
//  All Asset
//
//  Created by fulstory on 12. 10. 23..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

//11번가 후기 테이블 뷰에 대한 커스텀 뷰 셀 클래스


#import <UIKit/UIKit.h>

@interface ElevenstCommentListTableViewCell : UITableViewCell

//@property (weak, nonatomic) IBOutlet UILabel *satis;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *date;

@end
