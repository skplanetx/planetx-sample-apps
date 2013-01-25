//
//  AsyncImageView.h
//  All Asset
//
//  Created by fulstory on 12. 10. 23..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

// ActivityIndicator를 활용한 Lazy Downloading URL based ImageView

#import <UIKit/UIKit.h>

@interface AsyncImageView : UIImageView
{
    NSURLConnection *connection;
    NSMutableData* data;
    UIActivityIndicatorView *ai;
}

-(void)initWithImageAtURL:(NSURL*)url;

@property (nonatomic, retain) UIActivityIndicatorView *ai;

@end
