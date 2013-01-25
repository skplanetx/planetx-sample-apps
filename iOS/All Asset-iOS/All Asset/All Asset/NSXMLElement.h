//
//  NSXMLElement.h
//  LifeStyleProtocol
//
//  Created by Donghyun Kim on 10. 12. 20..
//  Copyright 2010 Three Wish Interactive. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface NSXMLElement : NSObject {
	NSString *Name;
	NSString *Value;
	NSDictionary *Attribute;
	NSXMLElement *Parent;
	NSMutableArray *Childs;
	
	NSMutableArray *myEntries;
	NSMutableArray *myChildrens;
}

@property (nonatomic,retain) NSString *Name;
@property (nonatomic,retain) NSString *Value;
@property (nonatomic,retain) NSDictionary *Attribute;
@property (nonatomic,retain) NSXMLElement *Parent;
@property (nonatomic,retain) NSMutableArray *Childs;

- (NSMutableArray *)getChildsToDictionary;
- (NSMutableArray *)getListItemToDictionary;
- (NSMutableArray *)getChildrens:(NSString *)nodeName;
- (NSMutableArray *)getChildrensToDictionary:(NSString *)nodeName;

- (NSXMLElement *)objectAtIndex:(NSInteger)index;
- (NSXMLElement *)objectForKey:(NSString *)key;
- (NSMutableArray *)objectsForKey:(NSString *)key;

- (void)clearElement;

@end
