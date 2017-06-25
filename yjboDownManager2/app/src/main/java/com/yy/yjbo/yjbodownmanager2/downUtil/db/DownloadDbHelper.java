package com.yy.yjbo.yjbodownmanager2.downUtil.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Administrator on 2014/10/20 0020.
 */
public class DownloadDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "download_table";
    public static final String CREATE_TABLE = "CREATE TABLE 'download_list' ('_id'  INTEGER NOT NULL,'download_id'  INTEGER NOT NULL,'title'  TEXT NOT NULL,'state' INTEGER NOT NULL,'user_id' INTEGER NOT NULL,'date' TEXT,'img_uri' TEXT,'intro' TEXT,PRIMARY KEY ('_id' ASC));";

    public DownloadDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TABLE);
       if (DbUtil.tabIsExist(db, "download_list") && !DbUtil.checkColumnExist1(db, "download_list", "node_name")) {
			onUpgrade(db, 1, 2);
		}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	if (newVersion == 2) {
    		// 在下载记录表中添加一列“node_name”,版本号为2时更新
			if (DbUtil.tabIsExist(db, "download_list") && !DbUtil.checkColumnExist1(db, "download_list", "node_name")) {
				db.execSQL("ALTER TABLE download_list ADD COLUMN node_name TEXT");
			}
			// 在下载记录表中添加一列“catagory”,版本号为2时更新
			if (DbUtil.tabIsExist(db, "download_list") && !DbUtil.checkColumnExist1(db, "download_list", "catagory")) {
				db.execSQL("ALTER TABLE download_list ADD COLUMN catagory TEXT");
			}
			// 在下载记录表中添加一列“summary”,版本号为2时更新
			if (DbUtil.tabIsExist(db, "download_list") && !DbUtil.checkColumnExist1(db, "download_list", "summary")) {
				db.execSQL("ALTER TABLE download_list ADD COLUMN summary TEXT");
			}
		} 

    }
}
