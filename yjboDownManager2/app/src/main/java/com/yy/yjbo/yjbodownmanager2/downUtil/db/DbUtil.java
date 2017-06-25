package com.yy.yjbo.yjbodownmanager2.downUtil.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;

/**
 * afinal框架数据库管理工具（数据库升级，增加字段）
 *   主要包含上传记录和推送消息两个数据库，其他数据库没有使用afinal管理
 * @author lixiaofeng
 * @date 2014-8-12下午6:38:06
 */

/**
 * 添加了消息的类型
 * @author yjbo
 * @time 2016/12/11 21:29
 */

public class DbUtil {
	private static final String TAG = "finalDb";
	private static final String DB_NAME = "bag_db";
	private static FinalDb db;
	private static int VERSION = 9;//8 //7

	/**
	 * 获得数据库操作对象
	 * 
	 * @param context
	 * @return
	 */
	public static FinalDb getFinalDb(Context context) {
		return db = FinalDb.create(context, DB_NAME, true, VERSION, new DbUpdateListener() {
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				if (newVersion != oldVersion) {
					// 在下载记录表中添加一列"savePath",版本号为2时更新
					if (tabIsExist(db, "download")
							&& !checkColumnExist1(db, "download", "savePath")) {
						db.execSQL("ALTER TABLE download ADD COLUMN savePath TEXT");
					}
					// 在上传记录表中添加一列“content”,版本号为3时更新
					if (tabIsExist(db, "upload") && !checkColumnExist1(db, "upload", "content")) {
						db.execSQL("ALTER TABLE upload ADD COLUMN content TEXT");
					}
					//版本4更新
					//在上传记录表中添加一列"userId",在下载记录表中添加一列“userId”
					if (tabIsExist(db, "download")
							&& !checkColumnExist1(db, "download", "userId")) {
						db.execSQL("ALTER TABLE download ADD COLUMN userId TEXT");
					}
					if (tabIsExist(db, "upload") && !checkColumnExist1(db, "upload", "userId")) {
						db.execSQL("ALTER TABLE upload ADD COLUMN userId TEXT");
					}
					//版本5更新
					//在消息表中添加一列"userId"
					if (tabIsExist(db, "pushnotice") && !checkColumnExist1(db, "pushnotice", "userId")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN userId TEXT");
					}
					//版本6更新，在消息表中添加一列"pushContent"
					if (tabIsExist(db, "pushnotice") && !checkColumnExist1(db, "pushnotice", "pushContent")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN pushContent TEXT");
					}
					//版本7更新，在消息表中添加一列"noticeUrl"
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","noticeUrl")){
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN noticeUrl TEXT");
					}
					//yjbo 版本8更新，在消息表中添加一列"msgtype"-消息类型，
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","msgtype")){
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN msgtype TEXT");
					}
					//yjbo 版本9更新，在消息表中添加很多列-消息类型，添加了operationId等
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","operationId")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN operationId TEXT");
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","companyName")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN companyName TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","commentUserName")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN commentUserName TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","commentUserIcon")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN commentUserIcon TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","categoryId")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN categoryId TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","categoryTitle")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN categoryTitle TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","pushMessageTitle")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN pushMessageTitle TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","pushMessageContent")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN pushMessageContent TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","pushMessageDetailUrl")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN pushMessageDetailUrl TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","subTypeId")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN subTypeId TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","subTypeTitle")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN subTypeTitle TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","commentLevel")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN commentLevel TEXT" );
					}
					if (tabIsExist(db,"pushnotice") && !checkColumnExist1(db,"pushnotice","isHide")) {
						db.execSQL("ALTER TABLE pushnotice ADD COLUMN isHide TEXT" );
					}
				} else {

				}

			}
		});

	}

	/**
	 * 根据 cursor.getColumnIndex(String columnName) 的返回值判断，如果为-1表示表中无此字段
	 * 方法1：检查某表列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	public static boolean checkColumnExist1(SQLiteDatabase db, String tableName, String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			// 查询一行
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return result;
	}

	/**
	 * 通过查询sqlite的系统表 sqlite_master 来查找相应表里是否存在该字段，稍微换下语句也可以查找表是否存在
	 * 方法2：检查表中某列是否存在
	 * 
	 * @param db
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @return
	 */
	public static boolean checkColumnExists2(SQLiteDatabase db, String tableName, String columnName) {
		boolean result = false;
		Cursor cursor = null;

		try {
			cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?",
					new String[] { tableName, "%" + columnName + "%" });
			result = null != cursor && cursor.moveToFirst();
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return result;
	}

	/**
	 * 判断数据库中表是否存在
	 * 
	 * @param db
	 * @param tabName
	 * @return
	 */
	public static boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {

			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
		}finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return result;
	}

}
