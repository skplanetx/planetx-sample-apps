//
//  TMapMarker.h
//  SKP-Sample
//
//  Created by Heung-Guk Kim on 12. 9. 25..
//  Copyright (c) 2012년 LBC. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "TMapLayer.h"

@interface TMapMarker : TMapLayer
{
    UIView  *label;
    UIColor *textForegroundColor;
    UIColor *textBackgroundColor;
}

@property (nonatomic, retain) UIView  *label;

@property (nonatomic, retain) UIColor *textForegroundColor;

@property (nonatomic, retain) UIColor *textBackgroundColor;

+ (UIFont *)defaultFont;

- (id)initWithUIImage:(UIImage *)image;
- (id)initWithUIImage:(UIImage *)image anchorPoint:(CGPoint)anchorPoint;

- (void)changeLabelUsingText:(NSString *)text;
- (void)changeLabelUsingText:(NSString *)text position:(CGPoint)position;
- (void)changeLabelUsingText:(NSString *)text font:(UIFont *)font foregroundColor:(UIColor *)textColor backgroundColor:(UIColor *)backgroundColor;
- (void)changeLabelUsingText:(NSString *)text position:(CGPoint)position font:(UIFont *)font foregroundColor:(UIColor *)textColor backgroundColor:(UIColor *)backgroundColor;

- (void)toggleLabel;
- (void)showLabel;
- (void)hideLabel;

- (void)replaceUIImage:(UIImage *)image;
- (void)replaceUIImage:(UIImage *)image anchorPoint:(CGPoint)anchorPoint;

@end
