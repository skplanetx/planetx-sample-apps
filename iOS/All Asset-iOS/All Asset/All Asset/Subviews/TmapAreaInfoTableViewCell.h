//
//  TmapAreaInfoTableViewCell.h
//  All Asset
//
//  Created by Jason Nam on 12. 11. 7..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TmapAreaInfoTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *placename;
@property (weak, nonatomic) IBOutlet UILabel *placeContent;
@property (weak, nonatomic) IBOutlet UILabel *placeTel;
@property (weak, nonatomic) IBOutlet UILabel *placeAddress;
@end
