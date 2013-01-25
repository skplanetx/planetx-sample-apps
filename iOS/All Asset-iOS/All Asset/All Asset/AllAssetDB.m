//
//  AllAssetDB.m
//  All Asset
//
//  Created by Jason Nam on 12. 10. 29..
//  Copyright (c) 2012년 fulstory. All rights reserved.
//

#import "AllAssetDB.h"
#import <sqlite3.h>

#define STR_NAME	@"Name"
#define STR_ID		@"Id"
#define STR_LAT     @"Latitude"
#define STR_LON     @"Longitude"

@implementation AllAssetDB
@synthesize allassetDB;
@synthesize databasePath;

- (BOOL)createEditableCopyOfDatabaseIfNeeded:(NSString *)dbfilename
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentationDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *myPath = [documentsDirectory stringByAppendingPathComponent:dbfilename];
    
    //파일이 Documents에 존재하는지 확인.
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL exist = [fileManager fileExistsAtPath:myPath];
    
    if (exist) {
        return true;
    }
    
    //없으면 Bundle에서 복사
    NSString *defaultDBPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:dbfilename];
    databasePath = defaultDBPath;
    
    NSError *error;
    
    return [fileManager copyItemAtPath:defaultDBPath toPath:myPath error:&error];
}

- (void)createDatabase
{
    // Do any additional setup after loading the view, typically from a nib.
    NSString *docsDir;
    NSArray *dirPaths;
    
    // Get the documents directory
    dirPaths = NSSearchPathForDirectoriesInDomains(
                                                   NSDocumentDirectory, NSUserDomainMask, YES);
    
    docsDir = [dirPaths objectAtIndex:0];
    
    // Build the path to the database file
    databasePath = [[NSString alloc]
                    initWithString: [docsDir stringByAppendingPathComponent:
                                     @"allasset.db"]];
    
    NSFileManager *filemgr = [NSFileManager defaultManager];
    
    if ([filemgr fileExistsAtPath: databasePath] == NO)
    {
        const char *dbpath = [databasePath UTF8String];
        
        if (sqlite3_open(dbpath, &allassetDB) == SQLITE_OK)
        {
            char *errMsg;
            const char *sql_stmt = "CREATE TABLE IF NOT EXISTS TMAPRECENT (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, POI_ID TEXT, LATITUDE TEXT, LONGITUDE TEXT)";
            
            if (sqlite3_exec(allassetDB, sql_stmt, NULL, NULL, &errMsg) != SQLITE_OK)
            {
                NSLog(@"Failed to create table");
            }
            
            sqlite3_close(allassetDB);
            
        } else {
            NSLog(@"Failed to open/create database");
        }
    }
}

/**
 * 최근 경로를 DB에 저장
 */
-(void)saveRecentSearchPath:(NSDictionary *)dic
{    
    if (dic == nil) {
        return;
    }
    
    NSString *poi_id        = [dic valueForKey:STR_ID];
    NSString *poi_name      = [dic valueForKey:STR_NAME];
    NSString *latitude      = [dic valueForKey:STR_LAT];
    NSString *longitude     = [dic valueForKey:STR_LON];
    
    sqlite3_stmt *statement;
    const char *dbpath = [databasePath UTF8String];
    
    if (sqlite3_open(dbpath, &allassetDB) == SQLITE_OK) {
        
        NSString *insertSQL = [NSString stringWithFormat:@"INSERT INTO TMAPRECENT (NAME, POI_ID, LATITUDE, LONGITUDE) VALUES (\"%@\",\"%@\",\"%@\",\"%@\")", poi_name, poi_id, latitude, longitude];
        
        const char *insert_stmt = [insertSQL UTF8String];
        
        //SQL 문장 준비
        sqlite3_prepare_v2(allassetDB, insert_stmt, -1, &statement, NULL);
        
        //SQL 문장 실행 완료
        if (sqlite3_step(statement) == SQLITE_DONE) {
            //Do something in UI
            NSLog(@"SQLITE_DONE");
        } else {
            NSLog(@"SQLITE_FAILED");
        }
        
        //메모리에서 SQL 문장 제거
        sqlite3_finalize(statement);
        
        //데이터 베이스 닫음
        sqlite3_close(allassetDB);
    }

}

-(NSMutableArray *)loadRecentSearchPath
{
    NSMutableArray *pathArray = [NSMutableArray new];
    @try {
        const char *dbpath=[databasePath UTF8String];
        sqlite3_stmt *statement;
        
        
        NSString *name      = @"";
        NSString *poi_Id    = @"";
        NSString *latitude  = @"";
        NSString *longitude = @"";
        
        //데이터 베이스를 열고
        if(sqlite3_open(dbpath,&allassetDB)==SQLITE_OK)
        {
            //쿼리문장 생성
            NSString *querySQL = @"SELECT NAME, POI_ID, LATITUDE, LONGITUDE FROM TMAPRECENT";
            
            //UTF8String으로 변환
            const char *query_stmt = [querySQL UTF8String];
            
            //쿼리문장 실행 준비
            if(sqlite3_prepare_v2(allassetDB,query_stmt,-1,&statement,NULL)==SQLITE_OK)
            {
                
                //쿼리문장 실행, 데이터에 대해 쿼리문의 조건이 맞을 때
                while(sqlite3_step(statement)==SQLITE_ROW)
                {
                    //0번 열 : address 에 있는 값을 불러와 address Textfield에 표시
                    name        = [[NSString alloc]initWithUTF8String:(const char *)sqlite3_column_text(statement,0)];
                    
                    //1번 열 : phone 에 있는 값을 불러와 phone Textfield에 표시
                    poi_Id      = [[NSString alloc]initWithUTF8String:(const char *)sqlite3_column_text(statement,1)];
                    
                    //2번 열 : 위도
                    latitude    = [[NSString alloc]initWithUTF8String:(const char *)sqlite3_column_text(statement, 2)];
                    
                    //3번 열 : 경도
                    longitude   = [[NSString alloc]initWithUTF8String:(const char *)sqlite3_column_text(statement, 3)];
                    
                    NSLog(@"db name : %@, poi_id : %@, latitude : %@, longitude : %@", name, poi_Id, latitude, longitude);
                    
                    NSDictionary *pathDic = [[NSDictionary alloc] initWithObjectsAndKeys:name, STR_NAME, poi_Id, STR_ID, latitude, STR_LAT, longitude, STR_LON, nil];
                    
                    [pathArray addObject:pathDic];
                }
                
                //쿼리문 삭제
                sqlite3_finalize(statement);
            }
            
            //데이터베이스 close
            sqlite3_close(allassetDB);
        }
    }
    @catch (NSException *exception) {
        NSLog(@"An exception occured: %@", [exception reason]);
    }
    @finally {
        return pathArray;
    }
}

-(NSString *)loadMelonSongs:(NSInteger)recordID sql:(const char *)sql;
{
    const char *dbpath=[databasePath UTF8String];
    sqlite3_stmt *statement;
    
    NSString *songName = @"";
    NSString *artistName = @"";
    
    //데이터 베이스를 열고
    if(sqlite3_open(dbpath,&allassetDB)==SQLITE_OK)
    {
        //쿼리문장 생성
        NSString *querySQL = @"SELECT songName, artistName FROM melonSongChart";
        
        //UTF8String으로 변환
        const char *query_stmt = [querySQL UTF8String];
        
        //쿼리문장 실행 준비
        if(sqlite3_prepare_v2(allassetDB,query_stmt,-1,&statement,NULL)==SQLITE_OK)
        {
            
            //쿼리문장 실행, 데이터에 대해 쿼리문의 조건이 맞을 때
            if(sqlite3_step(statement)==SQLITE_ROW)
            {                
                //0번 열 : address 에 있는 값을 불러와 address Textfield에 표시
                songName = [[NSString alloc]initWithUTF8String:(const char *) sqlite3_column_text(statement,1)];
                
                //1번 열 : phone 에 있는 값을 불러와 phone Textfield에 표시
                artistName = [[NSString alloc]initWithUTF8String:(const char *)sqlite3_column_text(statement,2)];
            }
            
            //쿼리문의 조건에 대한 데이터가 없을 때
            else
            {
                
            }
            
            //쿼리문 삭제
            sqlite3_finalize(statement);
        }
        
        //데이터베이스 close
        sqlite3_close(allassetDB);
    }
    return [songName stringByAppendingFormat:@"-%@", artistName];
}

-(void)insertMelonSongs:(NSDictionary *)songData
{
    NSDictionary *curSongDic = songData;
    
    if (curSongDic == nil) {
        return;
    }
    
    NSString *songId        = [curSongDic valueForKey:@"songId"];
    NSString *songName      = [curSongDic valueForKey:@"songName"];
    NSString *artistId      = [curSongDic valueForKey:@"artistId"];
    NSString *artistName    = [curSongDic valueForKey:@"artistName"];
    
    sqlite3_stmt *statement;
    const char *dbpath = [databasePath UTF8String];
    
    if (sqlite3_open(dbpath, &allassetDB) == SQLITE_OK) {
        
        NSString *insertSQL = [NSString stringWithFormat:@"INSERT INTO melonSongChart (songId,songName, artistId, artistName) VALUES (\"%@\",\"%@\",\"%@\",\"%@\")", songId, songName, artistId, artistName];
        
        const char *insert_stmt = [insertSQL UTF8String];
        
        //SQL 문장 준비
        sqlite3_prepare_v2(allassetDB, insert_stmt, -1, &statement, NULL);
        
        //SQL 문장 실행 완료
        if (sqlite3_step(statement) == SQLITE_DONE) {
            //Do something in UI
            NSLog(@"SQLITE_DONE");
        } else {
            NSLog(@"SQLITE_FAILED");
        }
        
        //메모리에서 SQL 문장 제거
        sqlite3_finalize(statement);
        
        //데이터 베이스 닫음
        sqlite3_close(allassetDB);
    }
}
@end
