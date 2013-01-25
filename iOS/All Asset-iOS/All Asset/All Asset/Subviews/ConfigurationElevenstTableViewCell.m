//
//  ConfigurationElevenstTableViewCell.m
//  SK Planet Service
//
//  Created by Jason Nam on 12. 10. 30..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ConfigurationElevenstTableViewCell.h"

@implementation ConfigurationElevenstTableViewCell
@synthesize labelCategoryName,buttonCategorySelect,itemDic, delegate;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)selectCategory:(id)sender
{
    [self.delegate selectItemCategory:itemDic];
}

@end
