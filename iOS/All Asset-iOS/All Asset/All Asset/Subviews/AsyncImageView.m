//
//  AsyncImageView.m
//  All Asset
//
//  Created by fulstory on 12. 10. 23..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "AsyncImageView.h"

@implementation AsyncImageView
@synthesize ai;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)initWithImageAtURL:(NSURL*)url
{
    [self setContentMode:UIViewContentModeScaleAspectFit];
    if (!ai){
        [self setAi:[[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray]];
        [ai startAnimating];
        [ai setFrame:CGRectMake(self.frame.size.width/4, self.frame.size.height/4, self.frame.size.width/2, self.frame.size.height/2)];
        [self addSubview:ai];
    }
	
    NSURLRequest* request = [NSURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60];
    connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    
}

- (void)connection:(NSURLConnection *)theConnection	didReceiveData:(NSData *)incrementalData {
    if (data==nil) data = [[NSMutableData alloc] initWithCapacity:2048];
    [data appendData:incrementalData];
}

- (void)connectionDidFinishLoading:(NSURLConnection*)theConnection
{
    [self setImage:[UIImage imageWithData: data]];
    [ai removeFromSuperview];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
