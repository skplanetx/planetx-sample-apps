//
//  ConfigurationViewController.m
//  All Asset
//
//  Created by fulstory on 12. 10. 11..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "ConfigurationViewController.h"
#import "AppDelegate.h"
#import "Defines.h"
#import <QuartzCore/QuartzCore.h>

@interface ConfigurationViewController ()

@end

@implementation ConfigurationViewController
@synthesize configElevenstViewController, configTmapViewController;

- (void)viewDidLoad
{
    [super viewDidLoad];
	
    displayCountArray = [NSMutableArray arrayWithObjects:@"5",@"10",@"15", nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (ConfigurationElevenstViewController *)configElevenstViewController
{
    // Instantiate the editin view controller if necessary.
    if (configElevenstViewController == nil) {
        configElevenstViewController = [[ConfigurationElevenstViewController alloc] initWithNibName:@"ConfigurationElevenstViewController" bundle:nil];
    }
    return configElevenstViewController;
}

- (ConfigurationTmapViewController *)configTmapViewController
{
    // Instantiate the editin view controller if necessary.
    if (configTmapViewController == nil) {
        configTmapViewController = [[ConfigurationTmapViewController alloc] initWithNibName:@"ConfigurationTmapViewController" bundle:nil];
    }
    return configTmapViewController;
}

#pragma mark PickerView Create
- (void)showPickerView
{
    UIActionSheet *menu = [[UIActionSheet alloc] initWithTitle:@"싸이월드 방명록 노출갯수" delegate:self cancelButtonTitle:@"완료" destructiveButtonTitle:nil otherButtonTitles:nil, nil];
    
    //Add picker
    UIPickerView *pickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0,90,0,0)];
    
    pickerView.delegate = self;
    pickerView.showsSelectionIndicator = YES;    // note this is default to NO
    
    [menu addSubview:pickerView];
    [menu showFromTabBar:self.tabBarController.tabBar];
    [menu setBounds:CGRectMake(0,0,320,500)];
}

#pragma mark UIPickerViewDelegate
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    if (displayCountArray != nil && [displayCountArray count] > 0) {
        //선택한 싸이월드 방명록 카운트 저장
        selectedDisplayCount = [[displayCountArray objectAtIndex:row] intValue];
        NSLog(@"selectedDisplayCount : %d", selectedDisplayCount);
    }
}

#pragma mark UIPickerViewDataSource
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if (displayCountArray != nil && [displayCountArray count] > 0) {
        return [displayCountArray objectAtIndex:row];
    }
	return nil;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    if (displayCountArray != nil && [displayCountArray count] > 0) {
        return [displayCountArray count];
    }
	return 1;
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
	return 1;
}

#pragma mark ActionSheet Delegate methods
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    //NSInteger section = ((UIControl*)sender).tag/10;
    [appDelegate setCyworldGuestbookCount:1 displayCount:selectedDisplayCount];
}

#pragma mark UITableViewDataSource methods
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 3;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    if(appDelegate.configurationArray!=nil)
       return [appDelegate.configurationArray count];
    return 0;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    if(appDelegate.configurationArray!=nil)
    {
        NSDictionary *item = [appDelegate.configurationArray objectAtIndex:section];
        return [item objectForKey:CONFIGURATION_KEY_NAME];
    }
    return nil;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
    
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithFrame:CGRectZero];
        cell.textLabel.font = [UIFont systemFontOfSize:16];
        cell.tag = indexPath.section*10+indexPath.row;
        AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
        if(appDelegate.configurationArray!=nil)
        {
            NSDictionary *service = [appDelegate.configurationArray objectAtIndex:indexPath.section];
            
            if(0 == indexPath.row)
            {
                UISwitch *swichview = [[UISwitch alloc] initWithFrame:CGRectZero];
                [swichview addTarget:self action:@selector(updateSwitchWithTag:) forControlEvents:UIControlEventTouchUpInside];
                [swichview setOn:[[service objectForKey:CONFIGURATION_KEY_IS_SHORTCUT] boolValue]];
                swichview.tag = indexPath.section*10+indexPath.row;
                cell.accessoryView = swichview;
                cell.textLabel.text = CONFIGURATION_TEXT_FIRST;
            }
            else if(1 == indexPath.row)
            {
                UISwitch *swichview = [[UISwitch alloc] initWithFrame:CGRectZero];
                [swichview addTarget:self action:@selector(updateSwitchWithTag:) forControlEvents:UIControlEventTouchUpInside];
                [swichview setOn:[[service objectForKey:CONFIGURATION_KEY_IS_LIVE] boolValue]];
                swichview.tag = indexPath.section*10+indexPath.row;
                NSString *descText = [service objectForKey:CONFIGURATION_KEY_TEXT];
                if(descText == nil || [descText length]==0)
                {
                    [swichview setOn:NO];
                    [swichview setEnabled:NO];
                }
                cell.accessoryView = swichview;
                cell.textLabel.text = CONFIGURATION_TEXT_SECOND;
            }
            else// 설정하기 가능할 시 설정 하기 버튼 표시
            {
                NSString *descText = [service objectForKey:CONFIGURATION_KEY_TEXT];
                
                if(descText!=nil && [descText length]>0)
                {
                    cell.textLabel.text = descText;
                    
                    if([[service objectForKey:CONFIGURATION_KEY_IS_EDIT] boolValue])
                    {
                        if ([[service objectForKey:CONFIGURATION_KEY_ID] isEqualToString:@"nateon"]) {
                            UISwitch *swichview = [[UISwitch alloc] initWithFrame:CGRectZero];
                            [swichview addTarget:self action:@selector(nateonSwitchWithTag:) forControlEvents:UIControlEventTouchUpInside];
                            [swichview setOn:[[service objectForKey:CONFIGURATION_KEY_ALLOW_MESSAGE] boolValue]];
                            swichview.tag = indexPath.section*10+indexPath.row;
                            cell.accessoryView = swichview;
                        } else {
                            UIButton *buttonview = [UIButton buttonWithType:UIButtonTypeRoundedRect];
                            buttonview.frame = CGRectMake(240, 6, 80, 30);
                            buttonview.titleLabel.font = [UIFont systemFontOfSize:14];
                            [buttonview setTitle:CONFIGURATION_TEXT_EDIT_BUTTON forState:UIControlStateNormal];
                            [buttonview addTarget:self action:@selector(pressConfigButtonWithTag:) forControlEvents:UIControlEventTouchUpInside];
                            buttonview.tag = indexPath.section*10+indexPath.row;
                            cell.accessoryView = buttonview;
                        }
                    }
                }
                else
                {
                    cell.textLabel.text = @"Live에 노출되지 않습니다.";
                }
            }
        }
    }
    return cell;
}

#pragma mark UITableViewDelegate methods
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 32.0;
}

-(NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

#pragma mark Custom delegation methods
-(void)updateSwitchWithTag:(id)sender
{
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    NSInteger section = ((UIControl*)sender).tag/10;
    NSInteger row = ((UIControl*)sender).tag%10;
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:row inSection:section];
    UITableViewCell *cell = [configurationTableView cellForRowAtIndexPath:indexPath];
    UISwitch *switchview = (UISwitch *)cell.accessoryView;
    if([switchview isOn])
    {
        if(0==row)
            [appDelegate setIsShortcut:section isOn:YES];
        else
            [appDelegate setIsLive:section isOn:YES];
    }
    else
    {
        if(0==row)
            [appDelegate setIsShortcut:section isOn:NO];
        else
            [appDelegate setIsLive:section isOn:NO];
    }
    [configurationTableView reloadData];
}

/**
 * nateon 쪽지 수신여부 설정
 */
-(void)nateonSwitchWithTag:(id)sender
{
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    NSInteger section = ((UIControl*)sender).tag/10;
    NSInteger row = ((UIControl*)sender).tag%10;
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:row inSection:section];
    UITableViewCell *cell = [configurationTableView cellForRowAtIndexPath:indexPath];
    UISwitch *switchview = (UISwitch *)cell.accessoryView;
    if([switchview isOn])
    {
        [appDelegate setIsAllowMessagge:section isOn:YES];
    }
    else
    {
        [appDelegate setIsAllowMessagge:section isOn:NO];
    }
    [configurationTableView reloadData];
}

-(void)pressConfigButtonWithTag:(id)sender
{
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    if(appDelegate.configurationArray==nil)
        return;
    NSInteger section = ((UIControl*)sender).tag/10;
    NSDictionary *dest = [[appDelegate.configurationArray objectAtIndex:section] mutableCopy];
    NSString *serviceId = [dest objectForKey:CONFIGURATION_KEY_ID];
    if(serviceId!=nil && [serviceId length] > 0)
    {
        if([serviceId isEqualToString:SERVICE_ELEVENST])
        {
            ConfigurationElevenstViewController *controller = self.configElevenstViewController;
            [self presentViewController:controller animated:YES completion:nil];
        }
        else if([serviceId isEqualToString:SERVICE_TMAP])
        {
            ConfigurationTmapViewController *controller = self.configTmapViewController;
            [self presentViewController:controller animated:YES completion:nil];
        }
        else if([serviceId isEqualToString:SERVICE_CYWORLD])
        {
            //PickerView 보여주기
            [self showPickerView];
        }
    }
}


@end
