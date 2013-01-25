//
//  NSXMLDocument.h
//  LifeStyleProtocol
//
//  Created by Donghyun Kim on 10. 12. 20..
//  Copyright 2010 Three Wish Interactive. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSXMLElement.h"


@interface NSXMLDocument : NSObject <NSXMLParserDelegate> {
	NSXMLElement *rootElement;
	NSMutableArray *elementPath;
}

@property (nonatomic,retain) NSXMLElement *rootElement;
@property (nonatomic,retain) NSMutableArray *elementPath;

- (id)initWithData:(NSData *)data;
- (id)initWithContentsOfURL:(NSString *)url;

- (void)clearDocument;

@end

