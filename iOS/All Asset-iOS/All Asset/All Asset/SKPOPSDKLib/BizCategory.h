//
//  BizCategory.h
//  skp
//
//  Created by Moon HanYong on 12. 8. 28..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//



@interface BizCategory : NSObject

@property (nonatomic, copy) NSString *upperBizCode;
@property (nonatomic, copy) NSString *upperBizName;
@property (nonatomic, copy) NSString *middleBizCode;
@property (nonatomic, copy) NSString *middleBizName;

- (NSString *)toString;

@end

