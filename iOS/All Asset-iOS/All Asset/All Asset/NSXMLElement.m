//
//  NSXMLElement.m
//  LifeStyleProtocol
//
//  Created by Donghyun Kim on 10. 12. 20..
//  Copyright 2010 Three Wish Interactive. All rights reserved.
//

#import "NSXMLElement.h"


@implementation NSXMLElement

@synthesize Name;
@synthesize Value;
@synthesize Attribute;
@synthesize Parent;
@synthesize Childs;


- (id)init {
	self = [super init];
	if (self) {
		Childs = [[NSMutableArray alloc] init];
	}
	return self;
}

- (void)dealloc {
	[Name release];
	[Value release];
	[Attribute release];
	[Parent release];
	[Childs release];
	[super dealloc];
}

- (void)clearElement {
	while ([self.Childs count] > 0) {
		NSXMLElement *el = [self.Childs lastObject];
		[el clearElement];
		[self.Childs removeLastObject];
	}
}

#pragma mark -
#pragma mark get childs

- (NSMutableArray *)getChildsToDictionary {
	myEntries = [[NSMutableArray alloc]init];
	
	NSMutableDictionary *dic = [[NSMutableDictionary alloc]init];
	for (NSXMLElement *el in Childs) {
		[dic setObject:[el Value] == nil ? @"" : [el Value] forKey:[el Name]];
	}
	[myEntries addObject:dic];
	[dic release];
	return myEntries;
}

- (NSMutableArray *)getListItemToDictionary {
	myEntries = [[NSMutableArray alloc]init];
	
	for (NSXMLElement *el in Childs) {
		/*if (([[el Name] isEqualToString:@"count"] || [[el Name] isEqualToString:@"total_count"]) 
         && [el Value] != nil) {
         NSMutableDictionary *dic = [[NSMutableDictionary alloc]init];
         [dic setObject:[el Value] forKey:[el Name]];
         [myEntries addObject:dic];
         [dic release];
         continue;
         } else */if (![[el Name] isEqualToString:@"item"] || [el Value] == nil) 
             continue;
		
		NSMutableDictionary *dic = [[NSMutableDictionary alloc]init];
		for (NSXMLElement *elChild in [el Childs]) {
			//NSLog(@"%@=%@", [elChild Name], [elChild Value]);
			if ([elChild Name] != nil && [elChild Value] != nil)
				[dic setObject:[elChild Value] forKey:[elChild Name]];
		}
		[myEntries addObject:dic];
		[dic release];
	}
	
	return myEntries;
}

- (NSMutableArray *)getChildrens:(NSString *)nodeName {
	myChildrens = [[NSMutableArray alloc]init];
	
	for(NSXMLElement *child in Childs)
	{
		if ([nodeName isEqualToString:[child Name]]) {      
			[myChildrens addObject:child];
		}
	}
	
	return myChildrens;
}

- (NSMutableArray *)getChildrensToDictionary:(NSString *)nodeName {
	myEntries = [[NSMutableArray alloc]init];
	
	for(NSXMLElement *el in Childs)
	{
		if ([nodeName isEqualToString:[el Name]]) {
			NSMutableDictionary *dic = [[NSMutableDictionary alloc]init];
			for (NSXMLElement *ch in el.Childs)
				[dic setObject:[ch Value] == nil ? @"" : [ch Value] forKey:[ch Name]];
			
			[myEntries addObject:dic];
			[dic release];
		}
	}
	
    //	return [myEntries copy];
	return myEntries;
}

#pragma mark -
#pragma mark search elements

- (NSXMLElement *)objectAtIndex:(NSInteger)index {
	if ([Childs count] <= index) {
		return nil;
	}
	NSXMLElement *resultElement = (NSXMLElement *)[Childs objectAtIndex:index];
	return resultElement;
}

- (NSXMLElement *)objectForKey:(NSString *)key {
	for (NSXMLElement *element in Childs) {
		//NSLog(@"name = %@, key = %@", [element Name], key);
		if ([[element Name] isEqualToString:key]) {
			return element;
		}
	}
	return nil;
}

- (NSMutableArray *)objectsForKey:(NSString *)key {
	myEntries = [[NSMutableArray alloc]init];
	
	for (NSXMLElement *element in Childs) {
		//NSLog(@"name = %@, key = %@", [element Name], key);
		if ([[element Name] isEqualToString:key]) {
			[myEntries addObject:element];
		}
	}
	
	return myEntries;
}

@end
