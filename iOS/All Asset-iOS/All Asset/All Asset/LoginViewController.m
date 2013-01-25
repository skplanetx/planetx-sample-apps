//
//  LoginViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "LoginViewController.h"
#import <QuartzCore/QuartzCore.h>

@interface LoginViewController ()

@end

@implementation LoginViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark UITableViewDataSource methods
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 2;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return 1;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return @"로그인 설정";
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithFrame:CGRectZero];
	}
    if(indexPath.row == 0)
        cell.textLabel.text = @"NateOn";
    else
        cell.textLabel.text = @"Cyworld";
    UIButton *buttonview = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    buttonview.frame = CGRectMake(240, 6, 80, 30);
    buttonview.titleLabel.font = [UIFont systemFontOfSize:14];
    [buttonview setTitle:@"로그인" forState:UIControlStateNormal];
    [buttonview addTarget:self action:@selector(pressButtonWithTag:) forControlEvents:UIControlEventTouchUpInside];
    buttonview.tag = indexPath.section*10+indexPath.row;
    cell.accessoryView = buttonview;
    return cell;
}

#pragma mark UITableViewDelegate methods
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 48.0;
}

-(NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

#pragma mark Custom delegation methods
-(void)pressButtonWithTag:(id)sender
{
    NSInteger section = ((UIControl*)sender).tag/10;
    NSInteger row = ((UIControl*)sender).tag%10;
    NSLog(@"Button Pressed Sec:%d Row:%d", section, row);
}

@end
