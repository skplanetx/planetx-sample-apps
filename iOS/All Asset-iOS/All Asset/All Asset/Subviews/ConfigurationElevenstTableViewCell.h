//
//  ConfigurationElevenstTableViewCell.h
//  SK Planet Service
//
//  Created by Jason Nam on 12. 10. 30..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ConfigurationElevenstCellDelegate <NSObject>

- (void)selectItemCategory:(NSDictionary *)dic;

@end
@interface ConfigurationElevenstTableViewCell : UITableViewCell

@property (nonatomic, weak) IBOutlet UILabel *labelCategoryName;
@property (nonatomic, weak) IBOutlet UIButton *buttonCategorySelect;
@property (assign) id<ConfigurationElevenstCellDelegate> delegate;
@property (nonatomic, weak) NSDictionary *itemDic;

- (IBAction)selectCategory:(id)sender;
@end
