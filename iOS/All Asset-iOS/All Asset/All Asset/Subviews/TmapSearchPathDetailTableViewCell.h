//
//  TmapSearchPathDetailTableViewCell.h
//  All Asset
//
//  Created by fulstory on 12. 10. 23..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

//Tmap 구간 정보 표시 커스텀 뷰 셀 클래스


#import <UIKit/UIKit.h>

@interface TmapSearchPathDetailTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *section;
@property (weak, nonatomic) IBOutlet UILabel *direction;
@property (weak, nonatomic) IBOutlet UILabel *distance;
@property (weak, nonatomic) IBOutlet UILabel *description;
@end
