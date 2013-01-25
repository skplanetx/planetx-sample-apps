//
//  AllAssetDB.h
//  All Asset
//
//  Created by Jason Nam on 12. 10. 29..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface AllAssetDB : NSObject
{
    
}
@property (nonatomic) sqlite3 *allassetDB;

//데이터베이스 파일의 경로
@property(strong,nonatomic) NSString *databasePath;

-(BOOL)createEditableCopyOfDatabaseIfNeeded:(NSString *)dbfilename;
-(void)createDatabase;
-(NSMutableArray *)loadRecentSearchPath;
-(void)saveRecentSearchPath:(NSDictionary *)dic;

@end
